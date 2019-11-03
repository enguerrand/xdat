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

public class DataTableModelEvent extends TableModelEvent {
	static final long serialVersionUID = 3L;
	private boolean chartRebuildRequired;
	private boolean chartRepaintRequired;
	private boolean dataPanelUpdateRequired;
	private boolean[] axisAutofitRequired;
	private boolean[] axisResetFilterRequired;
	private boolean[] axisApplyFiltersRequired;
	public static final int CUSTOM_TABLE_MODEL_TYPE = 999;

	public DataTableModelEvent(TableModel source, int firstRow, int lastRow, int column, boolean chartRebuildRequired, boolean chartRepaintRequired, boolean dataPanelUpdateRequired, boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired) {
		super(source, firstRow, lastRow, column, CUSTOM_TABLE_MODEL_TYPE);
		this.chartRebuildRequired = chartRebuildRequired;
		this.chartRepaintRequired = chartRepaintRequired;
		this.dataPanelUpdateRequired = dataPanelUpdateRequired;
		this.axisAutofitRequired = axisAutofitRequired;
		this.axisResetFilterRequired = axisResetFilterRequired;
		this.axisApplyFiltersRequired = axisApplyFiltersRequired;
	}

	public boolean isChartRebuildRequired() {
		return chartRebuildRequired;
	}

	public boolean isChartRepaintRequired() {
		return chartRepaintRequired;
	}

	public boolean isDataPanelUpdateRequired() {
		return dataPanelUpdateRequired;
	}

	public boolean[] getAxisAutofitRequired() {
		return axisAutofitRequired;
	}

	public boolean[] getAxisResetFilterRequired() {
		return axisResetFilterRequired;
	}

	public boolean[] getAxisApplyFiltersRequired() {
		return axisApplyFiltersRequired;
	}

}
