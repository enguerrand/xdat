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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.gui.dialogs.ClusterDialog;

/**
 * ActionListener for a {@link ClusterDialog}.
 * 
 */
public class ClusterDialogActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The dialog. */
	private ClusterDialog dialog;

	/**
	 * Instantiates a new cluster dialog action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog
	 */
	public ClusterDialogActionListener(Main mainWindow, ClusterDialog dialog) {
		this.mainWindow = mainWindow;
		this.dialog = dialog;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		log("Action Command = " + actionCommand);
		if (actionCommand.equals("Add")) {
			log(" adding cluster to buffer.");
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

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ClusterDialogActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
