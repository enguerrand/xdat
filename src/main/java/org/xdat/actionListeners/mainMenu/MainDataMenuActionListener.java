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

package org.xdat.actionListeners.mainMenu;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;
import org.xdat.gui.dialogs.ClusterDialog;
import org.xdat.workerThreads.DataSheetCreationThread;
import org.xdat.workerThreads.DataSheetUpdateThread;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import java.awt.event.ActionEvent;
import java.io.File;

public class MainDataMenuActionListener {

	private Main mainWindow;

	public MainDataMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void importData(boolean headers) {
		if (mainWindow.getChartFrameCount() == 0 || JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(mainWindow, "This operation will close all charts.\n Are you sure you want to continue?", "Import Data", JOptionPane.OK_CANCEL_OPTION)) {
			mainWindow.disposeAllChartFrames();
			String filepath;
			JFileChooser chooser = new JFileChooser();
			if (UserPreferences.getInstance().getCurrentDir() != null)
				chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
			int returnVal = chooser.showOpenDialog(mainWindow);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = chooser.getSelectedFile().getAbsolutePath();
				UserPreferences.getInstance().setLastFile(chooser.getSelectedFile().getAbsolutePath());

				ProgressMonitor progressMonitor = new ProgressMonitor(mainWindow, "", "Importing Data...", 0, 100);
				progressMonitor.setProgress(0);

				DataSheetCreationThread sw = new DataSheetCreationThread(filepath, headers, this.mainWindow, progressMonitor);
				sw.execute();
			}
		}
	}

	public void updateData(boolean headers) {
		String filepath;
		JFileChooser chooser = new JFileChooser();
		if (UserPreferences.getInstance().getCurrentDir() != null)
			chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
		int returnVal = chooser.showOpenDialog(mainWindow);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filepath = chooser.getSelectedFile().getAbsolutePath();
			UserPreferences.getInstance().setLastFile(chooser.getSelectedFile().getAbsolutePath());
			ProgressMonitor progressMonitor = new ProgressMonitor(mainWindow, "", "Updating Data...", 0, 100);
			progressMonitor.setProgress(0);
			DataSheetUpdateThread sw = new DataSheetUpdateThread(filepath, headers, mainWindow, progressMonitor);
			sw.execute();
		}
	}

	public void unselectAllDesigns(ActionEvent e) {
		DataSheet dataSheet = mainWindow.getDataSheet();
		if (dataSheet == null)
			JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Clustering", JOptionPane.INFORMATION_MESSAGE);
		else {
			mainWindow.getDataSheetTablePanel().getDataTable().getSelectionModel().clearSelection();
		}
	}

	public void removeSelectedDesigns(ActionEvent e) {
		DataSheet dataSheet = mainWindow.getDataSheet();
		if (dataSheet == null)
			JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Clustering", JOptionPane.INFORMATION_MESSAGE);
		else if (mainWindow.getDataSheetTablePanel().getDataTable().getSelectedRowCount() < 1)
			JOptionPane.showMessageDialog(mainWindow, "Please select at least one design first.", "Remove Designs", JOptionPane.INFORMATION_MESSAGE);
		else {
			// get selection
			int[] selection = mainWindow.getDataSheetTablePanel().getDataTable().getSelectedRows();
			mainWindow.getDataSheetTablePanel().getDataTable().getSelectionModel().clearSelection();
			mainWindow.getDataSheet().removeDesigns(selection);
		}
	}

	public void clustering(ActionEvent e) {
		if (mainWindow.getDataSheet() == null) {
			JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Clustering", JOptionPane.INFORMATION_MESSAGE);
		} else {
			new ClusterDialog(mainWindow, mainWindow);
		}
	}
}
