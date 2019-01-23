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

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.xdat.Main;
import org.xdat.data.Design;

/**
 * The Class DataTableSelectionModel. <br>
 * Used to trigger a repaint of all charts when the selection on the data table
 * changes..
 */
public class DataTableSelectionModel extends DefaultListSelectionModel implements ListSelectionListener {
	/** the main Window */
	private Main mainWindow;

	/**
	 * Instantiates a new Data Table Selection Model
	 * 
	 * @param mainWindow
	 *            the main Window
	 */
	public DataTableSelectionModel(Main mainWindow) {
		this.mainWindow = mainWindow;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		for (int d = 0; d < this.mainWindow.getDataSheet().getDesignCount(); d++) {
			this.mainWindow.getDataSheet().getDesign(d).setSelected(false);
		}

		int[] selection = this.mainWindow.getDataSheetTablePanel().getDataTable().getSelectedRows();
		for (int s = 0; s < selection.length; s++) {
			Design d = this.mainWindow.getDataSheet().getDesign(selection[s]);
			if(d!= null)
				d.setSelected(true);
		}
		this.mainWindow.repaintAllChartFrames();
	}
}
