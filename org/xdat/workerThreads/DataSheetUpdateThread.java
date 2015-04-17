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

import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;
import org.xdat.exceptions.InconsistentDataException;
import org.xdat.gui.frames.ChartFrame;

/**
 * A thread that runs in the background to create a new datasheet. This takes
 * away this potentially long-running task from the EDT. <br>
 * At the same time a ProgressMonitor is used to show progress, if required.
 * 
 */
public class DataSheetUpdateThread extends SwingWorker {
	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The path to the input file to be imported. */
	private String pathToInputFile;

	/** Specifies, whether the data to be imported has headers. */
	private boolean dataHasHeaders;

	/** The main window. */
	private Main mainWindow;

	/** The progress monitor. */
	private ProgressMonitor progressMonitor;

	/**
	 * Instantiates a new data sheet creation thread.
	 * 
	 * @param pathToInputFile
	 *            The path to the input file to be imported
	 * @param dataHasHeaders
	 *            Specifies, whether the data to be imported has headers
	 * @param mainWindow
	 *            The main window
	 * @param progressMonitor
	 *            The progress monitor
	 */
	public DataSheetUpdateThread(String pathToInputFile, boolean dataHasHeaders, Main mainWindow, ProgressMonitor progressMonitor) {
		log("constructor called");
		this.pathToInputFile = pathToInputFile;
		this.dataHasHeaders = dataHasHeaders;
		this.mainWindow = mainWindow;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public Object doInBackground() {
		// log("do in background invoked from Thread "+Thread.currentThread().getId());
		try {
			Hashtable<ChartFrame, double[]> upperFilterValues = new Hashtable<ChartFrame, double[]>();
			Hashtable<ChartFrame, double[]> lowerFilterValues = new Hashtable<ChartFrame, double[]>();
			for (int i = 0; i < mainWindow.getChartFrameCount(); i++) {
				ChartFrame f = mainWindow.getChartFrame(i);
				if (f.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
					ParallelCoordinatesChart c = (ParallelCoordinatesChart) f.getChart();
					double[] ufValues = new double[c.getAxisCount()];
					double[] lfValues = new double[c.getAxisCount()];
					for (int a = 0; a < c.getAxisCount(); a++) {
						ufValues[a] = c.getAxis(a).getUpperFilter().getValue();
						log("doInBackground: " + c.getAxis(a).getName() + ": filterGetValue = " + ufValues[a]);
						lfValues[a] = c.getAxis(a).getLowerFilter().getValue();
					}
					upperFilterValues.put(f, ufValues);
					lowerFilterValues.put(f, lfValues);
				}
			}

			DataSheet dataSheet = mainWindow.getDataSheet();
			dataSheet.updateData(pathToInputFile, dataHasHeaders, progressMonitor);

			for (int i = 0; i < mainWindow.getChartFrameCount(); i++) {
				ChartFrame f = mainWindow.getChartFrame(i);
				if (f.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
					ParallelCoordinatesChart c = (ParallelCoordinatesChart) f.getChart();

					double[] ufValues = upperFilterValues.get(f);
					double[] lfValues = lowerFilterValues.get(f);
					for (int a = 0; a < c.getAxisCount(); a++) {
						log("doInBackground: " + c.getAxis(a).getName() + ": filterSetValue = " + ufValues[a]);
						c.getAxis(a).getUpperFilter().setValue(ufValues[a]);
						c.getAxis(a).getLowerFilter().setValue(lfValues[a]);
					}
				}
				f.repaint();
			}

		} catch (IOException e) {

			JOptionPane.showMessageDialog(this.mainWindow, "Error on updating data from file:\n " + e.getMessage(), "Update Data", JOptionPane.ERROR_MESSAGE);
		} catch (InconsistentDataException e1) {
			JOptionPane.showMessageDialog(this.mainWindow, e1.getMessage() + "\nThe previous state will be restored.", "Update Data", JOptionPane.ERROR_MESSAGE);
		}

		this.progressMonitor.close();
		return null;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (DataSheetUpdateThread.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
