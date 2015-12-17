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

import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

import org.xdat.Main;
import org.xdat.customEvents.DataTableModelEvent;
import org.xdat.data.DataSheet;

/**
 * The Class DataTable. <br>
 * Used to extend the JTable standard implementation of the tableChanged
 * function in combination with the
 * {@link org.xdat.customEvents.DataTableModelEvent}. Based on the boolean
 * switches set in the DataTableModelEvent appropriate functions of the class
 * {@link org.xdat.Main} are called to update the GUI.
 * 
 * @see org.xdat.customEvents.DataTableModelEvent
 */
public class DataTable extends JTable {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 1;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** A reference to the mainWindow. */
	private Main mainWindow;

	/**
	 * Instantiates a new data table.
	 * 
	 * @param dataSheet
	 *            the dataSheet
	 * @param cm
	 *            the column model
	 * @param mainWindow 
	 * 				the main window
	 */
	public DataTable(DataSheet dataSheet, TableColumnModel cm, Main mainWindow) {
		super(dataSheet, cm);
		this.mainWindow = mainWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JTable#tableChanged(javax.swing.event.TableModelEvent)
	 */
	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		if (e.getType() == DataTableModelEvent.CUSTOM_TABLE_MODEL_TYPE) {
			DataTableModelEvent ec = (DataTableModelEvent) e;
			// log("tableChanged: columnIndex: "+columnIndex+", firstRow = "+e.getFirstRow()+", lastRow = "+e.getLastRow()+", Type = "+e.getType()+", source = "+e.getSource());
			// log("tableChange: this.mainWindow: "+this.mainWindow);
			// log("TableModelEventIDs: Allcols:"+TableModelEvent.ALL_COLUMNS+", Delete:"+TableModelEvent.DELETE+", Insert:"+TableModelEvent.INSERT+", Header:"+TableModelEvent.HEADER_ROW+", Update:"+TableModelEvent.UPDATE);
			boolean[] autofitRequired = ec.getAxisAutofitRequired();
			boolean[] resetFiltersRequired = ec.getAxisResetFilterRequired();
			boolean[] applyFiltersRequired = ec.getAxisApplyFiltersRequired();

			final ProgressMonitor progressMonitor = new ProgressMonitor(this.getParent(), "", "Rebuilding charts", 0, this.mainWindow.getDataSheet().getParameterCount() - 1);
			progressMonitor.setMillisToPopup(0);

			for (int i = 0; i < this.mainWindow.getDataSheet().getParameterCount() && !progressMonitor.isCanceled(); i++) {
				final int progress = i;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						progressMonitor.setProgress(progress);
					}
				});
				if (autofitRequired[i]) {
					// log("tableChanged:  autofitRequired");
					this.mainWindow.autofitAxisAllChartFrames(i);
				}
				if (resetFiltersRequired[i]) {
					// log("tableChanged:  resetFiltersRequired");
					this.mainWindow.resetFiltersOnAxisAllChartFrames(i);
				}
				if (applyFiltersRequired[i]) {
					// log("tableChanged:  applyFiltersRequired");
					this.mainWindow.refilterAllChartFrames(i);
				}
			}
			if (ec.isChartRebuildRequired()) {
				log("tableChanged: isChartRebuildRequired ");
				this.mainWindow.rebuildAllChartFrames();
			}
			if (ec.isChartRepaintRequired()) {
				log("tableChanged:isChartRepaintRequired  ");
				this.mainWindow.repaintAllChartFrames();
			}
			if (ec.isDataPanelUpdateRequired()) {
				log("tableChanged:isDataPanelUpdateRequired  ");
				this.mainWindow.updateDataPanel();
			}

			progressMonitor.close();
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (DataTable.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
