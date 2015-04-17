/*
 *  Copyright 2010, Enguerrand de Rochefort
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
package org.xdat.customEvents;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.xdat.Main;

/**
 * The Class DataTableModelEvent. This class is used to send information from
 * the {@link org.xdat.data.DataSheet} to the GUI. <br>
 * Whenever the data in the datasheet is modified a DataTableModelEvent should
 * be constructed. The constructor arguments are a combination of the standard
 * TableModelEvent constructors and xdat specific boolean flags that provide
 * information how the change impacts the GUI. <br>
 * Based on this information the {@link org.xdat.gui.tables.DataTable} will call
 * the appropriate functions to update the GUI when receiving the
 * DataTableModelEvent.
 * 
 * @see org.xdat.gui.tables.DataTable
 */
public class DataTableModelEvent extends TableModelEvent {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0003;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** True, when the event requires rebuilding all charts */
	private boolean chartRebuildRequired;

	/** True, when the event requires repainting all charts */
	private boolean chartRepaintRequired;

	/** True, when the event requires updating the data panel */
	private boolean dataPanelUpdateRequired;

	/** True for the index of each axis that needs to be autofitted. */
	private boolean[] axisAutofitRequired;

	/** True for the index of each axis where the filters need to be reset. */
	private boolean[] axisResetFilterRequired;

	/** True for the index of each axis where the filters need to be applied. */
	private boolean[] axisApplyFiltersRequired;

	/**
	 * Used to identify table model events that require application specific
	 * code to run.
	 */
	public static final int CUSTOM_TABLE_MODEL_TYPE = 999;

	/**
	 * Instantiates a new data table model event.
	 * 
	 * @param source
	 *            the source
	 * @param firstRow
	 *            the first row
	 * @param lastRow
	 *            the last row
	 * @param column
	 *            the column
	 * @param chartRebuildRequired
	 *            specifies whether the event requires rebuilding all charts
	 * @param chartRepaintRequired
	 *            specifies whether the event requires repainting all charts
	 * @param dataPanelUpdateRequired 
	 * 			  specified whether the event requires updating the data panel
	 * @param axisAutofitRequired
	 *            specifies which axes should be autofitted as a result of the
	 *            event
	 * @param axisResetFilterRequired
	 *            specifies for which axes the filters should be reset as a
	 *            result of the event
	 * @param axisApplyFiltersRequired
	 *            specifies for which axes the filters should be applied as a
	 *            result of the event
	 */
	public DataTableModelEvent(TableModel source, int firstRow, int lastRow, int column, boolean chartRebuildRequired, boolean chartRepaintRequired, boolean dataPanelUpdateRequired, boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired) {
		super(source, firstRow, lastRow, column, CUSTOM_TABLE_MODEL_TYPE);
		log("constructor called ");
		this.chartRebuildRequired = chartRebuildRequired;
		this.chartRepaintRequired = chartRepaintRequired;
		this.dataPanelUpdateRequired = dataPanelUpdateRequired;
		this.axisAutofitRequired = axisAutofitRequired;
		this.axisResetFilterRequired = axisResetFilterRequired;
		this.axisApplyFiltersRequired = axisApplyFiltersRequired;
	}

	/**
	 * Checks if chart rebuild required is true.
	 * 
	 * @return the chartRebuildRequired state
	 */
	public boolean isChartRebuildRequired() {
		return chartRebuildRequired;
	}

	/**
	 * Checks if chart repaint required is true.
	 * 
	 * @return the chartRepaintRequired state
	 */
	public boolean isChartRepaintRequired() {
		return chartRepaintRequired;
	}

	/**
	 * Checks if data Panel update required is true.
	 * 
	 * @return the dataPanelUpdateRequired
	 */
	public boolean isDataPanelUpdateRequired() {
		return dataPanelUpdateRequired;
	}

	/**
	 * Gets the axis autofit required.
	 * 
	 * @return an array of booleans where each boolean specifies, whether the
	 *         corresponding axis should be autofitted.
	 */
	public boolean[] getAxisAutofitRequired() {
		return axisAutofitRequired;
	}

	/**
	 * Gets the axis reset filter required.
	 * 
	 * @return an array of booleans where each boolean specifies, whether for
	 *         the corresponding axis the filters should be reset.
	 */
	public boolean[] getAxisResetFilterRequired() {
		return axisResetFilterRequired;
	}

	/**
	 * Gets the axis apply filters required.
	 * 
	 * @return an array of booleans where each boolean specifies, whether for
	 *         the corresponding axis the filters should be reapplied.
	 */
	public boolean[] getAxisApplyFiltersRequired() {
		return axisApplyFiltersRequired;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (DataTableModelEvent.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
