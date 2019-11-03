/*
 *  Copyright 2014, Enguerrand de Rochefort
 * 
 * This file is part of xdat.
 *
 * xdat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xdat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xdat.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package org.xdat.data;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.customEvents.DataTableModelEvent;
import org.xdat.exceptions.InconsistentDataException;

import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ProgressMonitor;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A representation of the data imported from a text file.
 * <p>
 * Everytime the user imports data from a text file the data is stored in a
 * DataSheet. The data sheet is a kind of wrapper class for the collection of
 * all rows in the text file. Each row represents one
 * {@link org.xdat.data.Design}.
 * <p>
 * In addition to storing the Designs and providing the possibility to display
 * them in a JTable by implementing TableModel, the DataSheet class also keeps
 * track of the Parameters in the data set. Each column represents one
 * {@link org.xdat.data.Parameter}.
 * <p>
 * The third main function of this class is to actually read the data from a
 * text file. While doing this, the DataSheet also collects some additional
 * information, such as
 * <ul>
 * <li>the parameter types (numeric/discrete, see the Parameter class for
 * further info)
 * <li>the Parameter names. These are obtained from a header line in the data or
 * are given default names such as Parameter 1, Parameter 2 and so on.
 * </ul>
 * <p>
 * A ListModel is implemented for other functions of the program to be able to
 * display parameters in a JList.
 * <p>
 * Finally, the DataSheet also keeps track of all {@link org.xdat.data.Cluster}
 * s. However, it does not store the information to which Cluster each Design
 * belongs. This information is stored in the Designs themselves. It is
 * important to understand this because it means that whenever
 * {@link DataSheet#updateData(String, boolean, ProgressMonitor) } is called this
 * information is lost.
 */
public class DataSheet implements TableModel, Serializable, ListModel {

	static final long serialVersionUID = 8;
	private ClusterSet clusterSet;
	private Vector<Design> data = new Vector<Design>(0, 1);
	private Map<Integer, Design> designIdsMap = new HashMap<Integer, Design>();
	private Vector<Parameter> parameters = new Vector<Parameter>(0, 1);
	private transient Vector<TableModelListener> listeners = new Vector<TableModelListener>();
	private transient Vector<ListDataListener> listDataListener = new Vector<ListDataListener>();
	private String delimiter;
	public DataSheet(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) throws IOException {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.clusterSet = new ClusterSet(this);
		this.delimiter = userPreferences.getDelimiter();
		if (userPreferences.isTreatConsecutiveAsOne())
			this.delimiter = this.delimiter + "+";
		importData(pathToInputFile, dataHasHeaders, progressMonitor);
		boolean continueChecking = true;
		for (Parameter parameter : this.parameters) {
			if (parameter.isMixed() && continueChecking) {
				int userAction = JOptionPane.showConfirmDialog(mainWindow, "Parameter " + parameter.getName() + " has numeric values in some designs\n" + "and non-numerical values in others. \nThis will result in the parameter being treated as a \n" + "non-numeric parameter. \n" + "If this is incorrect it is recommended to find the design(s)\n" + "with non-numeric values and correct or remove them.\n\n" + "Press Ok to continue checking parameters or Cancel to\n" + "suppress further warnings.", "Mixed Parameter Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				continueChecking = (userAction == JOptionPane.OK_OPTION);
			}
			parameter.setTicLabelDigitCount(userPreferences.getParallelCoordinatesAxisTicLabelDigitCount());
		}
	}

	/**
	 * Fills the DataSheet with data from a given file and assigns Parameter
	 * names.r
	 * <p>
	 * If dataHasHeaders is true, the Parameter names are read from the first
	 * line. Otherwise the parameter names are created automatically.
	 * 
	 * @param pathToInputFile
	 *            the path to the input file
	 * @param dataHasHeaders
	 *            specifies whether the data has headers to read the Parameter
	 *            names from.
	 * @param progressMonitor
	 *            the progress monitor.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void importData(String pathToInputFile, boolean dataHasHeaders, ProgressMonitor progressMonitor) throws IOException {
		Vector<Design> buffer = new Vector<Design>(0, 1);
		if (this.data != null) {
			buffer = (Vector<Design>) this.data.clone();
		}
		int lineCount = getLineCount(pathToInputFile);
		progressMonitor.setMaximum(lineCount);

		BufferedReader f;
		String line;
		int idCounter = 1;
		f = new BufferedReader(new FileReader(pathToInputFile));
		line = (f.readLine()).trim();

		String[] lineElements = line.split(this.delimiter);
		if (dataHasHeaders) // if data has headers read the parameter names from
							// the first line
		{
			for (int i = 0; i < lineElements.length; i++) {
				this.parameters.add(new Parameter(this.getUniqueParameterName(lineElements[i]), this));
			}
		} else // if data does not have headers read the first Design from the first line and create default Parameter names
		{
			Design newDesign = new Design(idCounter++);
			for (int i = 0; i < lineElements.length; i++) {
				this.parameters.add(new Parameter("Parameter " + (i + 1), this));
				newDesign.setValue(this.parameters.get(i), lineElements[i]);

			}
			this.data.add(newDesign);
			this.designIdsMap.put(newDesign.getId(), newDesign);
			progressMonitor.setProgress(idCounter - 1);
		}
		try {
			readDesignsFromFile(progressMonitor, f, idCounter);
		} catch (IOException e) {

			this.data = buffer;
			throw e;
		}
		f.close();
		if (progressMonitor.isCanceled()) {
			this.data = buffer;
		}

		// this loop ensures that all discrete levels are known to the parameter
		// so it returns the right double values
		for (int i = 0; i < this.parameters.size(); i++) {
			if (!this.parameters.get(i).isNumeric()) {
				this.parameters.get(i).getMaxValue();
			}
		}
	}

	/**
	 * Updates the DataSheet with Data from a given file and assigns Parameter
	 * names.
	 * <p>
	 * If dataHasHeaders is true, the Parameter names are read from the first
	 * line. Otherwise the parameter names are created automatically.
	 * <p>
	 * The difference of updating vs. importing is that all Charts are kept.
	 * This requires the new data to have the same number of parameters as the
	 * previous one. Otherwise the InconsistentDataException is thrown.
	 * <p>
	 * 
	 * @param pathToInputFile
	 *            the path to the input file
	 * @param dataHasHeaders
	 *            specifies whether the data has headers to read the Parameter
	 *            names from.
	 * @param progressMonitor
	 *            the progress monitor.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws InconsistentDataException
	 *             if the user tries to import data that does not have the same
	 *             number of columns as the current DataSheet.
	 */
	public void updateData(String pathToInputFile, boolean dataHasHeaders, ProgressMonitor progressMonitor) throws IOException, InconsistentDataException {
		int lineCount = getLineCount(pathToInputFile);
		progressMonitor.setMaximum(lineCount);

		BufferedReader f;
		String line;
		int idCounter = 1;
		f = new BufferedReader(new FileReader(pathToInputFile));

		// check datasheet to be read for consistency
		line = (f.readLine()).trim();
		String[] lineElements = line.split(this.delimiter);
		if (lineElements.length != this.getParameterCount()) {
			f.close();
			throw new InconsistentDataException(pathToInputFile);
		}

		Vector<Design> buffer = (Vector<Design>) this.data.clone();
		Map<Integer, Design> idbuffer = (Map<Integer, Design>) ((HashMap<Integer, Design>) this.designIdsMap).clone();
		this.data.clear();
		this.designIdsMap.clear();
		for (int i = 0; i < this.parameters.size(); i++) {
			this.parameters.get(i).resetDiscreteLevelsAndState();

		}

		if (dataHasHeaders) // if data has headers read the parameter names from
							// the first line
		{
			for (int i = 0; i < this.parameters.size(); i++) {
				this.parameters.get(i).setName(null);

			}
			for (int i = 0; i < lineElements.length; i++) {
				this.parameters.get(i).setName(this.getUniqueParameterName(lineElements[i]));
			}
		} else // if data does not have headers read the first Design from the
				// first line and create default Parameter names
		{
			Design newDesign = new Design(idCounter++);
			for (int i = 0; i < this.parameters.size(); i++) {
				if (lineElements.length <= i) {
					newDesign.setValue(this.parameters.get(i), "-");
				} else {
					this.parameters.get(i).setName("Parameter " + (i + 1));
					newDesign.setValue(this.parameters.get(i), lineElements[i]);
				}

			}
			this.data.add(newDesign);
			this.designIdsMap.put(newDesign.getId(), newDesign);
		}

		try {
			readDesignsFromFile(progressMonitor, f, idCounter);
		} catch (IOException e) {
			this.data = buffer;
			this.designIdsMap = idbuffer;
			throw e;
		}
		f.close();
		if (progressMonitor.isCanceled()) {
			this.data = buffer;
			this.designIdsMap = idbuffer;
		}

		// this loop ensures that all discrete levels are known to the parameter
		// so it returns the right double values
		for (int i = 0; i < this.parameters.size(); i++) {
			if (!this.parameters.get(i).isNumeric()) {
				this.parameters.get(i).getMaxValue();
			}
		}
		fireTableChanged(0, this.data.size(), -1, false, true, true, initialiseBooleanArray(true), initialiseBooleanArray(true), initialiseBooleanArray(true));

	}

	private void readDesignsFromFile(ProgressMonitor progressMonitor, BufferedReader f, int idCounter) throws IOException {
		String line;
		String[] lineElements;
		while ((line = f.readLine()) != null && !progressMonitor.isCanceled()) // read all  subsequent lines  into Designs
		{
			progressMonitor.setProgress(idCounter - 1);
			Design newDesign;
			lineElements = line.split(this.delimiter);
			if (lineElements.length < 1) {
				// skip line
			} else {
				newDesign = new Design(idCounter++);
				boolean newDesignContainsValues = false;
				for (int i = 0; i < lineElements.length; i++) {
					if (lineElements[i].length() > 0 && (!lineElements[i].equals(new String("\\s")))) {
						newDesignContainsValues = true; // found non-empty empty
					}
				}
				if (newDesignContainsValues) {
					for (int i = 0; i < this.parameters.size(); i++) {
						if (lineElements.length <= i || lineElements[i].length() <= 0 || lineElements[i].equals(new String("\\s"))) {
							newDesign.setValue(this.parameters.get(i), "-");
						} else {
							newDesign.setValue(this.parameters.get(i), lineElements[i]);
						}
					}
					this.data.add(newDesign);
					this.designIdsMap.put(newDesign.getId(), newDesign);
				}
			}
		}
	}

	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}

	public int getColumnCount() {
		return parameters.size();
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int columnIndex) {
		return parameters.get(columnIndex).getName();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return this.data.get(rowIndex).getId();
		} else {
			return this.data.get(rowIndex).getStringValue(parameters.get(columnIndex - 1));
		}
	}

	public void setValueAt(Object arg0, int rowIndex, int columnIndex) {
		boolean previousNumeric = this.parameters.get(columnIndex - 1).isNumeric();
		String previousValue = this.getValueAt(rowIndex, columnIndex).toString();
		boolean[] axisAutofitRequired = initialiseBooleanArray(false);
		boolean[] axisResetFilterRequired = initialiseBooleanArray(false);
		boolean[] axisApplyFiltersRequired = initialiseBooleanArray(false);
		try {
			this.data.get(rowIndex).setValue(parameters.get(columnIndex - 1), arg0.toString());
			if (!previousNumeric) {
				this.parameters.get(columnIndex - 1).checkOccurrenceInDiscreteLevel(previousValue); // make
																									// sure
																									// no
																									// unused
																									// levels
																									// remain
			}

			NumberParser.parseNumber(arg0.toString());

			if (!previousNumeric) {
				this.parameters.get(columnIndex - 1).checkIfNumeric();
				axisAutofitRequired[columnIndex - 1] = true;
				axisResetFilterRequired[columnIndex - 1] = true;
			}

		} catch (ParseException e1) {
			this.parameters.get(columnIndex - 1).setNumeric(false);
		}
		axisApplyFiltersRequired[columnIndex - 1] = true;
		fireTableChanged(rowIndex, rowIndex, columnIndex - 1, false, true, false, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return false;
		} else {
			return true;
		}
	}

	public void addTableModelListener(TableModelListener l) {
		if (listeners == null)
			listeners = new Vector<>();
		listeners.add(l);
	}

	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	public void fireTableChanged(int firstRow, int lastRow, int column, boolean chartRebuildRequired, boolean chartRepaintRequired, boolean dataPanelUpdateRequired, boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired) {
		DataTableModelEvent e = new DataTableModelEvent(this, firstRow, lastRow, column, chartRebuildRequired, chartRepaintRequired, dataPanelUpdateRequired, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			((TableModelListener) listeners.get(i)).tableChanged(e);
		}
	}

	private int getLineCount(String pathToInputFile) throws FileNotFoundException, IOException {
		BufferedReader f;
		f = new BufferedReader(new FileReader(pathToInputFile));
		int lineCount = 0;
		while ((f.readLine()) != null)
			lineCount++;
		f.close();
		return lineCount;
	}

	public Design getDesign(int i) {
		return this.data.get(i);
	}

	public Design getDesignByID(int id) {
		return this.designIdsMap.get(id);
	}

	public void addDesign(Design design) {
		this.data.add(design);
		this.designIdsMap.put(design.getId(), design);
		boolean[] axisAutofitRequired = initialiseBooleanArray(false);
		boolean[] axisResetFilterRequired = initialiseBooleanArray(false);
		boolean[] axisApplyFiltersRequired = initialiseBooleanArray(true);

		// TODO find out if the new design makes a numeric parameter discrete
		// and set booleans appropriately

		fireTableChanged(this.getDesignCount() - 1, this.getDesignCount() - 1, -1, false, true, false, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
	}

	public void removeDesigns(int[] designsToRemove) {
		boolean[] axisAutofitRequired = initialiseBooleanArray(false);
		boolean[] axisResetFilterRequired = initialiseBooleanArray(false);
		boolean[] axisApplyFiltersRequired = initialiseBooleanArray(false);

		boolean[] discrete = new boolean[this.parameters.size()]; // check which parameters are discrete
		for (int i = 0; i < this.parameters.size(); i++) {
			discrete[i] = !this.parameters.get(i).isNumeric();
		}

		for (int i = designsToRemove.length - 1; i >= 0; i--) {
			Design removedDesign = data.remove(designsToRemove[i]);
			this.designIdsMap.remove(removedDesign.getId());
			for (int j = 0; j < this.parameters.size(); j++) // check if that makes any non-numeric parameter numeric
			{
				if (!this.parameters.get(j).isNumeric()) {
					try {
						String string = removedDesign.getStringValue(this.parameters.get(j));
						NumberParser.parseNumber(string);
					} catch (ParseException e1) {
						this.parameters.get(j).checkOccurrenceInDiscreteLevel(removedDesign.getStringValue(this.parameters.get(j))); // make
																																		// sure
																																		// no
																																		// unused
																																		// levels
																																		// remain
						this.parameters.get(j).checkIfNumeric();
					}
				}
			}
		}

		for (int i = 0; i < this.parameters.size(); i++) // if previously
															// non-numeric
															// parameters are
															// now numeric
															// autofit the axis
		{
			axisAutofitRequired[i] = discrete[i] && this.parameters.get(i).isNumeric();
			axisApplyFiltersRequired[i] = true;

		}

		fireTableChanged(designsToRemove[0], designsToRemove[designsToRemove.length - 1], -1, false, true, true, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
	}

	public int getParameterCount() {
		return this.parameters.size();
	}

	public String getParameterName(int index) {
		if (index >= this.parameters.size() || index < 0)
			throw new IllegalArgumentException("Invalid Index " + index);
		return this.parameters.get(index).getName();
	}

	public Parameter getParameter(int index) {
		if (index >= this.parameters.size() || index < 0)
			throw new IllegalArgumentException("Invalid Index " + index);
		return this.parameters.get(index);
	}

	public Parameter getParameter(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				return this.parameters.get(i);
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	public int getParameterIndex(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				return i;
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	public Parameter removeParameter(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				Parameter removedParam = this.parameters.remove(i);
				for (int j = 0; j < this.data.size(); j++) {
					this.data.get(j).removeParameter(removedParam);
				}
				boolean[] axisAutofitRequired = initialiseBooleanArray(false);
				boolean[] axisResetFilterRequired = initialiseBooleanArray(false);
				boolean[] axisApplyFiltersRequired = initialiseBooleanArray(false);

				this.fireTableChanged(0, this.data.size() - 1, i, false, true, true, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
				return removedParam;
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	public boolean parameterExists(Parameter param) {
		return this.parameters.contains(param);
	}

	public double getMaxValueOf(Parameter param) {
		if (param.isNumeric()) {
			double max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < this.data.size(); i++) {
				if (max < this.data.get(i).getDoubleValue(param)) {
					max = this.data.get(i).getDoubleValue(param);
				}
			}
			return max;
		} else {
			return param.getDiscreteLevelCount() - 1;
		}
	}

	public double getMinValueOf(Parameter param) {
		if (param.isNumeric()) {
			double min = Double.POSITIVE_INFINITY;
			for (int i = 0; i < this.data.size(); i++) {
				if (min > this.data.get(i).getDoubleValue(param)) {
					min = this.data.get(i).getDoubleValue(param);
				}
			}
			return min;
		} else {
			return 0.0;
		}
	}

	public int getDesignCount() {
		return this.data.size();
	}

	private String getUniqueParameterName(String nameSuggestion) {
		String name = nameSuggestion;
		int id = 2;
		while (!isNameUnique(name))
			name = nameSuggestion + " (" + (id++) + ")";
		// log("getUniqueParameterName: returning name "+name);
		return name;
	}

	private boolean isNameUnique(String name) {
		boolean unique = true;
		for (int i = 0; i < this.parameters.size(); i++) {
			if (name.equals(this.parameters.get(i).getName())) {
				unique = false;
				break;
			}
		}
		return unique;
	}

	public ClusterSet getClusterSet() {
		return clusterSet;
	}

	public void setClusterSet(ClusterSet clusterSet) {
		this.clusterSet = clusterSet;
	}

	public void evaluateBoundsForAllDesigns(ParallelCoordinatesChart chart) {
		for (int i = 0; i < this.getDesignCount(); i++) {
			this.data.get(i).evaluateBounds(chart);
		}
	}

	public void moveParameter(int oldIndex, int newIndex) {
		Parameter param = this.parameters.remove(oldIndex);
		this.parameters.insertElementAt(param, newIndex);
	}

	private boolean[] initialiseBooleanArray(boolean value) {
		boolean[] array = new boolean[this.getParameterCount()];
		for (int i = 0; i < this.getParameterCount(); i++) {
			array[i] = value;
		}
		return array;
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		if (listDataListener == null)
			listDataListener = new Vector<>();
		listDataListener.add(l);

	}

	@Override
	public Object getElementAt(int index) {
		return this.parameters.get(index).getName();
	}

	@Override
	public int getSize() {
		return this.parameters.size();
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listDataListener.remove(l);

	}
}
