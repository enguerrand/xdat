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

import org.xdat.Main;
import org.xdat.customEvents.DataTableModelEvent;
import org.xdat.data.DataSheet;

import javax.swing.JTable;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumnModel;

public class DataTable extends JTable {

	static final long serialVersionUID = 1;
	private Main mainWindow;

	public DataTable(DataSheet dataSheet, TableColumnModel cm, Main mainWindow) {
		super(dataSheet, cm);
		this.mainWindow = mainWindow;
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		super.tableChanged(e);
		if (e.getType() == DataTableModelEvent.CUSTOM_TABLE_MODEL_TYPE) {
			DataTableModelEvent ec = (DataTableModelEvent) e;
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
					this.mainWindow.autofitAxisAllChartFrames(i);
				}
				if (resetFiltersRequired[i]) {
					this.mainWindow.resetFiltersOnAxisAllChartFrames(i);
				}
				if (applyFiltersRequired[i]) {
					this.mainWindow.refilterAllChartFrames(i);
				}
			}
			if (ec.isChartRebuildRequired()) {
				this.mainWindow.rebuildAllChartFrames();
			}
			if (ec.isChartRepaintRequired()) {
				this.mainWindow.repaintAllChartFrames();
			}
			if (ec.isDataPanelUpdateRequired()) {
				this.mainWindow.updateDataPanel();
			}

			progressMonitor.close();
		}
	}
}
