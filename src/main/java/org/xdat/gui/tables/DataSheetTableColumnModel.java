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

package org.xdat.gui.tables;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

import org.xdat.Main;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;

/**
 * A column model for the JTable that displays the
 * {@link org.xdat.data.DataSheet}.
 */
public class DataSheetTableColumnModel extends DefaultTableColumnModel {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0002;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** A reference to the mainWindow. */
	private Main mainWindow;

	/** The data sheet. */
	private DataSheet dataSheet;

	/**
	 * Instantiates a new generic table column model.
	 * @param mainWindow the main window
	 * @param dataSheet the data sheet
	 */
	public DataSheetTableColumnModel(Main mainWindow, DataSheet dataSheet) {
		super();
		this.mainWindow = mainWindow;
		this.dataSheet = dataSheet;
		this.setColumnMargin(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableColumnModel#getColumnSelectionAllowed()
	 */
	public boolean getColumnSelectionAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableColumnModel#moveColumn(int, int)
	 */
	public void moveColumn(int src, int tar) {
		if (src != 0 && tar != 0) {
			super.moveColumn(src, tar);
			if (src != tar) {
				this.dataSheet.moveParameter(src - 1, tar - 1);
				for (int i = 0; i < this.getColumnCount(); i++) {
					this.getColumn(i).setModelIndex(i);
				}
				Chart[] charts = new Chart[this.mainWindow.getCurrentSession().getChartCount()];
				for (int i = 0; i < charts.length; i++) {
					charts[i] = (this.mainWindow.getCurrentSession().getChart(i));
					if (charts[i].getClass().equals(ParallelCoordinatesChart.class)) {
						((ParallelCoordinatesChart) charts[i]).moveAxis(src - 1, tar - 1);
					}
				}
				this.mainWindow.repaintAllChartFrames();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableColumnModel#removeColumn(javax.swing.table
	 * .TableColumn)
	 */
	public void removeColumn(TableColumn arg0) {

	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (DataSheetTableColumnModel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}