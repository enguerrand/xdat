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
import org.xdat.exceptions.InconsistentDataException;

import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ProgressMonitor;
import javax.swing.event.ListDataListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class DataSheet implements Serializable, ListModel {

	static final long serialVersionUID = 8;
	private List<Design> data = new ArrayList<>();
	private Map<Integer, Design> designIdsMap = new HashMap<>();
	private final List<Parameter> parameters = new LinkedList<>();
	private transient List<ListDataListener> listDataListener;
	private transient List<DatasheetListener> listeners;
	private String delimiter;

	public void initTransientData() {
		this.listDataListener = new ArrayList<>();
		this.listeners = new ArrayList<>();
	}

	public DataSheet(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) throws IOException {
		initTransientData();
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.delimiter = userPreferences.getDelimiter();
		if (userPreferences.isTreatConsecutiveAsOne())
			this.delimiter = this.delimiter + "+";
		importData(pathToInputFile, dataHasHeaders, progressMonitor);
		boolean continueChecking = true;
		for (Parameter parameter : this.parameters) {
			if (parameter.isMixed(this) && continueChecking) {
				int userAction = JOptionPane.showConfirmDialog(mainWindow, "Parameter " + parameter.getName() + " has numeric values in some designs\n" + "and non-numerical values in others. \nThis will result in the parameter being treated as a \n" + "non-numeric parameter. \n" + "If this is incorrect it is recommended to find the design(s)\n" + "with non-numeric values and correct or remove them.\n\n" + "Press Ok to continue checking parameters or Cancel to\n" + "suppress further warnings.", "Mixed Parameter Warning", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				continueChecking = (userAction == JOptionPane.OK_OPTION);
			}
			parameter.setTicLabelDigitCount(userPreferences.getParallelCoordinatesAxisTicLabelDigitCount());
		}
	}

	private void importData(String pathToInputFile, boolean dataHasHeaders, ProgressMonitor progressMonitor) throws IOException {
		List<Design> buffer = new ArrayList<>();
		if (this.data != null) {
			buffer = new ArrayList<>(this.data);
		}
		int lineCount = getLineCount(pathToInputFile);
		progressMonitor.setMaximum(lineCount);

		BufferedReader f;
		String line;
		int idCounter = 1;
		f = new BufferedReader(new FileReader(pathToInputFile));
		line = (f.readLine()).trim();

		String[] lineElements = line.split(this.delimiter);
		if (dataHasHeaders) {
			// if data has headers read the parameter names from the first line
			for (String lineElement : lineElements) {
				this.parameters.add(new Parameter(this.getUniqueParameterName(lineElement), this));
			}
		} else {
			// if data does not have headers read the first Design from the first line and create default Parameter names
			Design newDesign = new Design(idCounter++);
			for (int i = 0; i < lineElements.length; i++) {
				this.parameters.add(new Parameter("Parameter " + (i + 1), this));
				newDesign.setValue(this.parameters.get(i), lineElements[i], this);

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

		for (Parameter parameter : this.parameters) {
			parameter.updateDiscreteLevels(this);
		}
	}

	public void updateData(String pathToInputFile, boolean dataHasHeaders, ProgressMonitor progressMonitor, ClusterSet clusterSet) throws IOException, InconsistentDataException {
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

		List<Design> buffer = new ArrayList<>(this.data);
		Map<Integer, Design> idbuffer = new HashMap<>(this.designIdsMap);
		// clusterId -> designHashes
		Map<Integer, Set<Integer>> clustersToDesignHashes = computeClusterDesignHashes(this.data);
		this.data.clear();
		this.designIdsMap.clear();

		// if data has headers read the parameter names from the first line
		if (dataHasHeaders){
			for (Parameter parameter : this.parameters) {
				parameter.setName(null);
			}
			for (int i = 0; i < lineElements.length; i++) {
				this.parameters.get(i).setName(this.getUniqueParameterName(lineElements[i]));
			}
			// if data does not have headers read the first Design from the first line and create default Parameter names
		} else {
			Design newDesign = new Design(idCounter++);
			for (int i = 0; i < this.parameters.size(); i++) {
				if (lineElements.length <= i) {
					newDesign.setValue(this.parameters.get(i), "-", this);
				} else {
					this.parameters.get(i).setName("Parameter " + (i + 1));
					newDesign.setValue(this.parameters.get(i), lineElements[i], this);
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

		for (Parameter parameter : this.parameters) {
			parameter.updateNumeric(this);
			parameter.updateDiscreteLevels(this);
		}

		restoreClustersFromHashes(clustersToDesignHashes, this.data, clusterSet);

		fireOnDataChanged(initialiseBooleanArray(true), initialiseBooleanArray(true), initialiseBooleanArray(true), false);
		fireDataPanelUpdateRequired();

	}

	private void restoreClustersFromHashes(Map<Integer, Set<Integer>> clustersToDesignHashes, List<Design> designs, ClusterSet clusterSet) {
		for (Map.Entry<Integer, Set<Integer>> entry : clustersToDesignHashes.entrySet()) {
			Integer clusterId = entry.getKey();
			Set<Integer> designHashes = entry.getValue();
			clusterSet.findClusterById(clusterId).ifPresent(cluster -> {
				for (Design design : designs) {
					if (designHashes.contains(design.computeValuesHash(parameters))) {
						design.setCluster(cluster);
					}
				}
			});
		}
	}

	private Map<Integer, Set<Integer>> computeClusterDesignHashes(List<Design> designs) {
		Map<Integer, Set<Integer>> clustersToDesignHashes = new HashMap<>();
		for (Design design : designs) {
			Cluster cluster = design.getCluster();
			if(cluster != null) {
				clustersToDesignHashes
						.computeIfAbsent(cluster.getUniqueId(), k -> new HashSet<>())
						.add(design.computeValuesHash(parameters));
			}
		}
		return clustersToDesignHashes;
	}

	private void readDesignsFromFile(ProgressMonitor progressMonitor, BufferedReader f, int idCounter) throws IOException {
		String line;
		String[] lineElements;
		while ((line = f.readLine()) != null && !progressMonitor.isCanceled()) // read all  subsequent lines  into Designs
		{
			progressMonitor.setProgress(idCounter - 1);
			Design newDesign;
			lineElements = line.split(this.delimiter);
			if (lineElements.length > 0) {
				newDesign = new Design(idCounter++);
				boolean newDesignContainsValues = false;
				for (String lineElement : lineElements) {
					if (lineElement.length() > 0 && (!lineElement.equals("\\s"))) {
						newDesignContainsValues = true; // found non-empty empty
						break;
					}
				}
				if (newDesignContainsValues) {
					for (int i = 0; i < this.parameters.size(); i++) {
						if (lineElements.length <= i || lineElements[i].length() <= 0 || lineElements[i].equals("\\s")) {
							newDesign.setValue(this.parameters.get(i), "-", this);
						} else {
							newDesign.setValue(this.parameters.get(i), lineElements[i], this);
						}
					}
					this.data.add(newDesign);
					this.designIdsMap.put(newDesign.getId(), newDesign);
				}
			}
		}
	}

	public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
		Parameter parameter = this.parameters.get(columnIndex - 1);
		boolean previousNumeric = parameter.isNumeric();
		boolean[] axisAutofitRequired = initialiseBooleanArray(false);
		boolean[] axisResetFilterRequired = initialiseBooleanArray(false);
		boolean[] axisApplyFiltersRequired = initialiseBooleanArray(false);
		this.data.get(rowIndex).setValue(parameters.get(columnIndex - 1), newValue.toString(), this);

		if (!previousNumeric) {
			parameter.updateDiscreteLevels(this);
		}

		Optional<Float> parsed = NumberParser.parseNumber(newValue.toString());
		boolean parsable = parsed.isPresent();

		if(previousNumeric && !parsable) {
			parameter.setNumeric(false, this);
		} else if (!previousNumeric && parsable) {
			parameter.updateNumeric(this);
		}
		if (previousNumeric != parameter.isNumeric()) {
			axisAutofitRequired[columnIndex - 1] = true;
			axisResetFilterRequired[columnIndex - 1] = true;
		}

		axisApplyFiltersRequired[columnIndex - 1] = true;
		fireOnDataChanged(axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired, false);
	}

	private int getLineCount(String pathToInputFile) throws IOException {
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
			// check if that makes any non-numeric parameter numeric
			for (Parameter parameter : this.parameters) {
				if (!parameter.isNumeric()) {
					String string = removedDesign.getStringValue(parameter);
					if (!NumberParser.parseNumber(string).isPresent()) {
						parameter.updateNumeric(this);
						parameter.updateDiscreteLevels(this);
					}
				}
			}
		}

		for (int i = 0; i < this.parameters.size(); i++) {
			axisAutofitRequired[i] = discrete[i] && this.parameters.get(i).isNumeric();
			axisApplyFiltersRequired[i] = true;
		}

		fireOnDataChanged(axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired, false);
		fireDataPanelUpdateRequired();
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
		for (Parameter parameter : this.parameters) {
			if (parameterName.equals(parameter.getName())) {
				return parameter;
			}
		}
		throw new IllegalArgumentException("Parameter " + parameterName + " not found");
	}

	public void removeParameter(String parameterName){
		boolean removed = this.parameters.removeIf(p -> p.getName().equals(parameterName));
		if (removed) {
			fireDataPanelUpdateRequired();
			fireOnDataChanged(false, false, false, true);
		}
	}

	public int getParameterIndex(String parameterName) {
		for (int i = 0; i < this.parameters.size(); i++) {
			if (parameterName.equals(this.parameters.get(i).getName())) {
				return i;
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
			for (Design datum : this.data) {
				if (max < datum.getDoubleValue(param)) {
					max = datum.getDoubleValue(param);
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
			for (Design datum : this.data) {
				if (min > datum.getDoubleValue(param)) {
					min = datum.getDoubleValue(param);
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
		while (!isNameUnique(name)) {
			name = nameSuggestion + " (" + (id++) + ")";
		}
		return name;
	}

	private boolean isNameUnique(String name) {
		boolean unique = true;
		for (Parameter parameter : this.parameters) {
			if (name.equals(parameter.getName())) {
				unique = false;
				break;
			}
		}
		return unique;
	}

	public void evaluateBoundsForAllDesigns(ParallelCoordinatesChart chart) {
		for (int i = 0; i < this.getDesignCount(); i++) {
			this.data.get(i).evaluateBounds(chart, this);
		}
	}

	public void moveParameter(int oldIndex, int newIndex) {
		Parameter param = this.parameters.remove(oldIndex);
		this.parameters.add(newIndex, param);
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

	public void addListener(DatasheetListener l) {
		this.listeners.add(l);
	}

	public void removeListener(DatasheetListener l) {
		this.listeners.remove(l);
	}

	private void fireListeners(Consumer<DatasheetListener> action) {
		for (DatasheetListener listener : this.listeners) {
			action.accept(listener);
		}
	}

	void onClustersUpdated(List<Cluster> changed, List<Cluster> added, List<Cluster> removed) {
		for (Cluster removedCluster : removed) {
			for (int j = 0; j < getDesignCount(); j++) {
				if (removedCluster.equals(getDesign(j).getCluster())) {
					getDesign(j).setCluster(null);
				}
			}
		}
		if (changed.isEmpty() && added.isEmpty() && removed.isEmpty()) {
			return;
		}
		fireClustersChanged();
	}

	public Collection<Design> getDesigns() {
		return Collections.unmodifiableList(this.data);
	}

	public List<Parameter> getParameters() {
		return Collections.unmodifiableList(this.parameters);
	}

	public void fireClustersChanged() {
		fireListeners(DatasheetListener::onClustersChanged);
	}

	public void fireDataPanelUpdateRequired() {
		fireListeners(DatasheetListener::onDataPanelUpdateRequired);
	}

	public void fireOnDataChanged(boolean axisAutofitRequired, boolean axisResetFilterRequired, boolean axisApplyFiltersRequired, boolean parametersChanged) {
		boolean[] autofit = new boolean[parameters.size()];
		boolean[] resetFilter = new boolean[parameters.size()];
		boolean[] applyFilters = new boolean[parameters.size()];
		Arrays.fill(autofit, axisAutofitRequired);
		Arrays.fill(resetFilter, axisResetFilterRequired);
		Arrays.fill(applyFilters, axisApplyFiltersRequired);
		fireOnDataChanged(autofit, resetFilter, applyFilters, parametersChanged);
	}

	public void fireOnDataChanged(boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired, boolean parametersChanged) {
		fireListeners(l -> l.onDataChanged(axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired, parametersChanged));
	}
}