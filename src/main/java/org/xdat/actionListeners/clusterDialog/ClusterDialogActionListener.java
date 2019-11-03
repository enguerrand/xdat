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

package org.xdat.actionListeners.clusterDialog;

import org.xdat.Main;
import org.xdat.gui.dialogs.ClusterDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClusterDialogActionListener implements ActionListener {
	private Main mainWindow;
	private ClusterDialog dialog;

	public ClusterDialogActionListener(Main mainWindow, ClusterDialog dialog) {
		this.mainWindow = mainWindow;
		this.dialog = dialog;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Add")) {
			this.dialog.getDataSheet().getClusterSet().addClusterToBuffer();
		} else if (actionCommand.equals("Remove")) {
			int[] selectedRows = this.dialog.getClusterTable().getSelectedRows();
			for (int i = selectedRows.length - 1; i >= 0; i--) {
				this.dialog.getDataSheet().getClusterSet().removeClusterFromBuffer(selectedRows[i]);
			}
		} else if (actionCommand.equals("Cancel")) {
			dialog.setVisible(false);
			dialog.dispose();
		} else if (actionCommand.equals("Ok")) {
			// make sure the cell is not in editing mode anymore
			if (this.dialog.getClusterTable().isEditing() && this.dialog.getClusterTable().getCellEditor() != null) {
				this.dialog.getClusterTable().getCellEditor().stopCellEditing();
			}

			this.dialog.getDataSheet().getClusterSet().applyChanges();
			for (int i = 0; i < this.mainWindow.getChartFrameCount(); i++) {
				this.mainWindow.getChartFrame(i).validate();
				this.mainWindow.getChartFrame(i).repaint();
			}
			dialog.setVisible(false);
			dialog.dispose();
		}
	}
}
