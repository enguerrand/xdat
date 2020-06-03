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
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.ClusterSet;
import org.xdat.data.DataSheet;
import org.xdat.exceptions.InconsistentDataException;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DataSheetUpdateThread extends SwingWorker {
	private String pathToInputFile;
	private boolean dataHasHeaders;
	private Main mainWindow;
	private ProgressMonitor progressMonitor;

	public DataSheetUpdateThread(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) {
		this.pathToInputFile = pathToInputFile;
		this.dataHasHeaders = dataHasHeaders;
		this.mainWindow = mainWindow;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public Object doInBackground() {
		try {
			Map<ChartFrame, double[]> upperFilterValues = new HashMap<>();
			Map<ChartFrame, double[]> lowerFilterValues = new HashMap<>();
			for (int i = 0; i < mainWindow.getChartFrameCount(); i++) {
				ChartFrame f = mainWindow.getChartFrame(i);
				if (f.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
					ParallelCoordinatesChart c = (ParallelCoordinatesChart) f.getChart();
					double[] ufValues = new double[c.getAxisCount()];
					double[] lfValues = new double[c.getAxisCount()];
					for (int a = 0; a < c.getAxisCount(); a++) {
						ufValues[a] = c.getAxis(a).getUpperFilter().getValue();
						lfValues[a] = c.getAxis(a).getLowerFilter().getValue();
					}
					upperFilterValues.put(f, ufValues);
					lowerFilterValues.put(f, lfValues);
				}
			}

			DataSheet dataSheet = mainWindow.getDataSheet();
			ClusterSet clusterSet = mainWindow.getCurrentClusterSet();
			dataSheet.updateData(pathToInputFile, dataHasHeaders, progressMonitor, clusterSet);

			for (int i = 0; i < mainWindow.getChartFrameCount(); i++) {
				ChartFrame f = mainWindow.getChartFrame(i);
				if (f.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
					ParallelCoordinatesChart c = (ParallelCoordinatesChart) f.getChart();

					double[] ufValues = upperFilterValues.get(f);
					double[] lfValues = lowerFilterValues.get(f);
					for (int a = 0; a < c.getAxisCount(); a++) {
						c.getAxis(a).getUpperFilter().setValue(ufValues[a], dataSheet);
						c.getAxis(a).getLowerFilter().setValue(lfValues[a], dataSheet);
					}
				}
			}

		} catch (IOException e) {

			JOptionPane.showMessageDialog(this.mainWindow, "Error on updating data from file:\n " + e.getMessage(), "Update Data", JOptionPane.ERROR_MESSAGE);
		} catch (InconsistentDataException e1) {
			JOptionPane.showMessageDialog(this.mainWindow, e1.getMessage() + "\nThe previous state will be restored.", "Update Data", JOptionPane.ERROR_MESSAGE);
		}

		this.progressMonitor.close();
		return null;
	}
}
