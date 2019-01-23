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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ProgressMonitor;
import javax.swing.event.ListDataListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.customEvents.DataTableModelEvent;
import org.xdat.exceptions.InconsistentDataException;

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

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 8;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The cluster set. */
	private ClusterSet clusterSet;

	/** The Vector containing all Designs. */
	private Vector<Design> data = new Vector<Design>(0, 1);
	
	/** Maps the design ids to the design. */
	private Map<Integer, Design> designIdsMap = new Hashtable<Integer, Design>();

	/** The parameters. */
	private Vector<Parameter> parameters = new Vector<Parameter>(0, 1);

	/** Table Model Listeners to enable updating the GUI. */
	private transient Vector<TableModelListener> listeners = new Vector<TableModelListener>();

	/** List Model Listeners to enable updating the GUI. */
	private transient Vector<ListDataListener> listDataListener = new Vector<ListDataListener>();

	/** The delimiter used for the data import. */
	private String delimiter;

	/**
	 * Instantiates a new data sheet and fills it with data from a given file.
	 * <p>
	 * Uses {@link #importData(String, boolean, ProgressMonitor) } for the step
	 * of reading the data.
	 * 
	 * @param pathToInputFile
	 *            the path to the input file
	 * @param dataHasHeaders
	 *            specifies whether the data has headers to read the Parameter
	 *            names from.
	 * @param mainWindow 
	 * 				the main window
	 * @param progressMonitor
	 * 				the progress monitor 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public DataSheet(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) throws IOException {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.clusterSet = new ClusterSet(this);
		this.delimiter = userPreferences.getDelimiter();
		if (userPreferences.isTreatConsecutiveAsOne())
			this.delimiter = this.delimiter + "+";
		importData(pathToInputFile, dataHasHeaders, progressMonitor);
		boolean continueChecking = true;
		for (int i = 0; i < this.parameters.size(); i++) {
			if (this.parameters.get(i).isMixed() && continueChecking) {
				int userAction = JOptionPane.showConfirmDialog(mainWindow, "Parameter " + this.parameters.get(i).getName() + " has numeric values in some designs\n" + "and non-numerical values in others. \nThis will result in the parameter being treated as a \n" + "non-numeric parameter. \n" + "If this is incorrect it is recommended to find the design(s)\n" + "with non-numeric values and correct or remove them.\n\n" + "Press Ok to continue checking parameters or Cancel to\n" + "suppress further warnings.", "Mixed Parameter Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				continueChecking = (userAction == JOptionPane.OK_OPTION);

			}
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
		Map<Integer, Design> idbuffer = (Map<Integer, Design>) ((Hashtable<Integer, Design>) this.designIdsMap).clone();
		this.data.clear();
		this.designIdsMap.clear();
		log("updateData: bufferlength: " + buffer.size());
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
		log("update data pm canceled state: " + progressMonitor.isCanceled());
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

	/**
	 * @param progressMonitor
	 *            the Progress Monitor to be updated during import
	 * @param f
	 *            a BufferedReader
	 * @param idCounter
	 *            The current value of the design id incremental counter
	 * @throws IOException
	 */
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
				boolean newDesignContainsValues = false; // flag to prevent
															// reading in empty
															// lines
				for (int i = 0; i < lineElements.length; i++) {
					if (lineElements[i].length() > 0 && (!lineElements[i].equals(new String("\\s")))) {
						newDesignContainsValues = true; // found non-empty empty
														// field on the line
					}

				}
				if (newDesignContainsValues) // only continue if at least one
												// non-empty field was found on
												// the line
				{
					for (int i = 0; i < this.parameters.size(); i++) {
						if (lineElements.length <= i || lineElements[i].length() <= 0 || lineElements[i].equals(new String("\\s"))) {
							newDesign.setValue(this.parameters.get(i), "-");
						} else {
							newDesignContainsValues = true; // found a non-empty
															// field on the line
							newDesign.setValue(this.parameters.get(i), lineElements[i]);
						}
						// log("importData: design ID "+newDesign.getId()+": setting value of parameter "+this.parameters.get(i).getName()+" to "+newDesign.getStringValue(this.parameters.get(i)));

					}
					this.data.add(newDesign);
					this.designIdsMap.put(newDesign.getId(), newDesign);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnClass(int)
	 */
	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return parameters.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int columnIndex) {
		return parameters.get(columnIndex).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return this.data.get(rowIndex).getId();
		} else {
			return this.data.get(rowIndex).getStringValue(parameters.get(columnIndex - 1));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
	 */
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
			log("setValueAt: value " + arg0.toString() + " of parameter " + this.parameters.get(columnIndex - 1).getName() + " is not numeric.");
			this.parameters.get(columnIndex - 1).setNumeric(false);
		}
		axisApplyFiltersRequired[columnIndex - 1] = true;
		fireTableChanged(rowIndex, rowIndex, columnIndex - 1, false, true, false, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
	 * TableModelListener)
	 */
	public void addTableModelListener(TableModelListener l) {
		if (listeners == null)
			listeners = new Vector<TableModelListener>();
		listeners.add(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event
	 * .TableModelListener)
	 */
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	/**
	 * Updates listeners when the table model changed
	 * @param firstRow	the first changed row
	 * @param lastRow	the last changed row
	 * @param column	the changed column
	 * @param chartRebuildRequired	specifies, whether charts will be rebuild
	 * @param chartRepaintRequired	specifies, whether charts should be repainted
	 * @param dataPanelUpdateRequired	specifies, whether the data panel needs to be updated
	 * @param axisAutofitRequired	specifies, whether autofit should be called on axes
	 * @param axisResetFilterRequired	specifies, whether filters should be reset
	 * @param axisApplyFiltersRequired	specifies, whether filters should be reapplied
	 */
	public void fireTableChanged(int firstRow, int lastRow, int column, boolean chartRebuildRequired, boolean chartRepaintRequired, boolean dataPanelUpdateRequired, boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired) {
		DataTableModelEvent e = new DataTableModelEvent(this, firstRow, lastRow, column, chartRebuildRequired, chartRepaintRequired, dataPanelUpdateRequired, axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			((TableModelListener) listeners.get(i)).tableChanged(e);
		}
	}

	/**
	 * Opens a text file and counts the number of lines.
	 * <p>
	 * 
	 * @param pathToInputFile
	 *            the path to the input file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException
	 *             Signals that the the file was not found under the given path
	 *             of columns as the current DataSheet.
	 */
	private int getLineCount(String pathToInputFile) throws FileNotFoundException, IOException {
		BufferedReader f;
		f = new BufferedReader(new FileReader(pathToInputFile));
		int lineCount = 0;
		while ((f.readLine()) != null)
			lineCount++;
		f.close();
		return lineCount;
	}

	/**
	 * Gets the Design with index i.
	 * 
	 * @param i
	 *            the index
	 * @return the Design
	 */
	public Design getDesign(int i) {
		return this.data.get(i);
	}

	/**
	 * Gets the Design with ID id.
	 * 
	 * @param id
	 *            the index
	 * @return the Design
	 */
	public Design getDesignByID(int id) {
		return this.designIdsMap.get(id);
	}

	/**
	 * Adds a Design to the DataSheet.
	 * 
	 * @param design
	 *            the Design
	 */
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

	/**
	 * Removes a Design from the DataSheet.
	 * 
	 * @param designsToRemove
	 *            an array of indeces that indicate the designs to remove
	 */
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

	/**
	 * Gets the Parameter count.
	 * 
	 * @return the Parameter count
	 */
	public int getParameterCount() {
		return this.parameters.size();
	}

	/**
	 * Gets the Parameter name of the Parameter with index index.
	 * 
	 * @param index
	 *            the index
	 * @return the parameter name
	 */
	public String getParameterName(int index) {
		if (index >= this.parameters.size() || index < 0)
			throw new IllegalArgumentException("Invalid Index " + index);
		return this.parameters.get(index).getName();
	}

	/**
	 * Gets the Parameter with the index index.
	 * 
	 * @param index
	 *            the Parameter index
	 * @return the Parameter
	 */
	public Parameter getParameter(int index) {
		if (index >= this.parameters.size() || index < 0)
			throw new IllegalArgumentException("Invalid Index " + index);
		return this.parameters.get(index);
	}

	/**
	 * Gets the Parameter with the name parameterName.
	 * 
	 * @param parameterName
	 *            the Parameter name
	 * @return the Parameter
	 */
	public Parameter getParameter(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				return this.parameters.get(i);
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	/**
	 * Gets the index of the Parameter with the name parameterName.
	 * 
	 * @param parameterName
	 *            the Parameter name
	 * @return the index
	 */
	public int getParameterIndex(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				return i;
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	/**
	 * Removes the Parameter with the name parameterName.
	 * 
	 * @param parameterName
	 *            the Parameter name
	 * @return the Parameter
	 */
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

	/**
	 * Checks if the given Parameter exists.
	 * 
	 * @param param
	 *            the Parameter
	 * @return true, if the parameter exists
	 */
	public boolean parameterExists(Parameter param) {
		return this.parameters.contains(param);
	}

	/**
	 * Gets the maximum value of a given Parameter in the DataSheet.
	 * 
	 * @param param
	 *            the Parameter
	 * @return the maximum value of the given Parameter.
	 */
	public double getMaxValueOf(Parameter param) {
		if (param.isNumeric()) {
			double max = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < this.data.size(); i++) {
				log("getMaxValueOf: max = " + max);
				if (max < this.data.get(i).getDoubleValue(param)) {

					log("getMaxValueOf: Higher value found. New max = " + max);
					max = this.data.get(i).getDoubleValue(param);
				}
			}

			log("getMaxValueOf: Final max = " + max);
			return max;
		} else {
			return param.getDiscreteLevelCount() - 1;
		}
	}

	/**
	 * Gets the minimum value of a given Parameter.
	 * 
	 * @param param
	 *            the parameter
	 * @return the minimum value of the given Parameter.
	 */
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

	/**
	 * Gets the design count.
	 * 
	 * @return the design count
	 */
	public int getDesignCount() {
		return this.data.size();
	}

	/**
	 * Checks whether a given String is a unique parameter name using
	 * {@link #isNameUnique(String)}. Returns a unique name if the provided
	 * string is not unique and the unchanged String otherwise.
	 * 
	 * @param nameSuggestion
	 *            the name that should be checked
	 * @return the a unique name if nameSuggestion was not a unique name and
	 *         nameSuggestion otherwise
	 */
	private String getUniqueParameterName(String nameSuggestion) {
		String name = nameSuggestion;
		int id = 2;
		while (!isNameUnique(name))
			name = nameSuggestion + " (" + (id++) + ")";
		// log("getUniqueParameterName: returning name "+name);
		return name;
	}

	/**
	 * Checks if a give String is a unique Parameter name.
	 * 
	 * @param name
	 *            the name to check
	 * @return true, if the name is unique
	 */
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

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (DataSheet.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the cluster set.
	 * 
	 * @return the cluster set
	 */
	public ClusterSet getClusterSet() {
		return clusterSet;
	}

	/**
	 * Sets the cluster set.
	 * 
	 * @param clusterSet
	 *            the new cluster set
	 */
	public void setClusterSet(ClusterSet clusterSet) {
		this.clusterSet = clusterSet;
	}

	/**
	 * Evaluate each Design to check whether it is within all axis bounds.
	 * 
	 * @param chart
	 *            the chart
	 * @see Design
	 */
	public void evaluateBoundsForAllDesigns(ParallelCoordinatesChart chart) {
		for (int i = 0; i < this.getDesignCount(); i++) {
			this.data.get(i).evaluateBounds(chart);
		}
	}

	/**
	 * Function to reorder the parameters in the datasheet
	 * 
	 * @param oldIndex
	 *            the index of the parameter to be moved
	 * @param newIndex
	 *            the target index for the parameter to be moved
	 */
	public void moveParameter(int oldIndex, int newIndex) {
		log("moveParameter called with arguments " + oldIndex + " and " + newIndex);
		Parameter param = this.parameters.remove(oldIndex);
		this.parameters.insertElementAt(param, newIndex);
		log("new order is:");
		for (int i = 0; i < this.parameters.size(); i++) {
			log(i + " :  " + this.parameters.get(i).getName());
		}
	}

	/**
	 * Function to create an array of booleans set to a userdefined initial
	 * value with the same length as the number of parameters. <br>
	 * This is used to build DataTableModelEvents
	 * 
	 * @param value
	 *            the initial value for all element
	 * @return the cluster set
	 */
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
			listDataListener = new Vector<ListDataListener>();
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
