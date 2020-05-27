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
import org.xdat.data.Cluster;
import org.xdat.data.DataSheet;
import org.xdat.gui.dialogs.ClusterDialog;
import org.xdat.gui.tables.ClusterTableModel;

import java.awt.event.ActionEvent;
import java.util.List;

public class ClusterDialogActionListener {
	private final Main mainWindow;
	private final ClusterDialog dialog;
	private final ClusterTableModel tableModel;

	public ClusterDialogActionListener(Main mainWindow, ClusterDialog dialog, ClusterTableModel tableModel) {
		this.mainWindow = mainWindow;
		this.dialog = dialog;
		this.tableModel = tableModel;
	}

	public void onOk(ActionEvent e) {
		// make sure the cell is not in editing mode anymore
		if (this.dialog.getClusterTable().isEditing() && this.dialog.getClusterTable().getCellEditor() != null) {
			this.dialog.getClusterTable().getCellEditor().stopCellEditing();
		}

		List<Cluster> clustersBuffer = this.tableModel.getClustersBuffer();
		DataSheet dataSheet = this.mainWindow.getDataSheet();

		mainWindow.getCurrentClusterSet().applyClustersBuffer(clustersBuffer);

		this.tableModel.applyBuffer(this.mainWindow.getCurrentClusterSet().getClusters(), dataSheet);
		for (int i = 0; i < this.mainWindow.getChartFrameCount(); i++) {
			this.mainWindow.getChartFrame(i).validate();
			this.mainWindow.getChartFrame(i).repaint();
		}
		dialog.setVisible(false);
		dialog.dispose();
	}

	public void onCancel(ActionEvent e) {
		dialog.setVisible(false);
		dialog.dispose();
	}

	public void onRemove(ActionEvent e) {
		int[] selectedRows = this.dialog.getClusterTable().getSelectedRows();
		for (int i = selectedRows.length - 1; i >= 0; i--) {
			this.tableModel.removeCluster(selectedRows[i]);
		}
	}

	public void onAdd(ActionEvent e) {
		this.tableModel.addCluster();
	}
}
