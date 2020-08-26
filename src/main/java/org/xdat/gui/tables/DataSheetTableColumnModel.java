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
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.Collection;

public class DataSheetTableColumnModel extends DefaultTableColumnModel {

	private Main mainWindow;

	public DataSheetTableColumnModel(Main mainWindow) {
		super();
		this.mainWindow = mainWindow;
		this.setColumnMargin(1);
	}

	public boolean getColumnSelectionAllowed() {
		return false;
	}

	public void moveColumn(int src, int tar) {
		if (src != 0 && tar != 0) {
			super.moveColumn(src, tar);
			if (src != tar) {
				this.mainWindow.getDataSheet().moveParameter(src - 1, tar - 1);
				for (int i = 0; i < this.getColumnCount(); i++) {
					this.getColumn(i).setModelIndex(i);
				}

				Collection<Chart> charts = this.mainWindow.getCurrentSession().getCharts();
				for (Chart chart : charts) {
					if (chart instanceof ParallelCoordinatesChart) {
						((ParallelCoordinatesChart) charts).moveAxis(src - 1, tar - 1);
					}
				}
				this.mainWindow.repaintAllChartFrames();
			}
		}
	}

	public void removeColumn(TableColumn arg0) {
	}
}