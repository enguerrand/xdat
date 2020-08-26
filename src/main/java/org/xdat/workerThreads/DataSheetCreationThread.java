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

package org.xdat.workerThreads;

import org.xdat.Main;
import org.xdat.data.ClusterSet;
import org.xdat.data.DataSheet;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import java.io.IOException;

public class DataSheetCreationThread extends SwingWorker {

	private String pathToInputFile;
	private boolean dataHasHeaders;
	private Main mainWindow;
	private ProgressMonitor progressMonitor;

	public DataSheetCreationThread(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) {
		this.pathToInputFile = pathToInputFile;
		this.dataHasHeaders = dataHasHeaders;
		this.mainWindow = mainWindow;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public Object doInBackground() {
		try {
			DataSheet dataSheet = new DataSheet(this.pathToInputFile, this.dataHasHeaders, this.mainWindow, this.progressMonitor);
			ClusterSet clusterSet = new ClusterSet(dataSheet);

			if (this.progressMonitor.isCanceled()) {
				this.mainWindow.repaint();
			} else {
				this.mainWindow.setDataSheet(dataSheet);
				this.mainWindow.setCurrentClusterSet(clusterSet);
				this.mainWindow.getMainMenuBar().setItemsRequiringDataSheetEnabled(true);
			}
		} catch (IOException e) {

			JOptionPane.showMessageDialog(this.mainWindow, "Error on importing data from file:\n " + e.getMessage(), "Import Data", JOptionPane.ERROR_MESSAGE);
		}
		this.progressMonitor.close();
		return null;
	}
}
