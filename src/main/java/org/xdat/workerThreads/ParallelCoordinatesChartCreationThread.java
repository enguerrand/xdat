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

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.frames.ChartFrame;

/**
 * A thread that runs in the background to create a new parallel coordinates
 * chart. This takes away this potentially long-running task from the EDT. <br>
 * At the same time a ProgressMonitor is used to show progress, if required.
 * 
 */
public class ParallelCoordinatesChartCreationThread extends SwingWorker {
	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The progress monitor. */
	private ProgressMonitor progressMonitor;

	/**
	 * Instantiates a new chart creation thread.
	 * 
	 * @param mainWindow
	 *            The main window
	 * @param progressMonitor
	 *            The progress monitor
	 */
	public ParallelCoordinatesChartCreationThread(Main mainWindow, ProgressMonitor progressMonitor) {
		this.mainWindow = mainWindow;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public Object doInBackground() {
		// log("do in background invoked from Thread "+Thread.currentThread().getId());
		// log("Create: Creation invoked at "+(new Date()).toString());
		ParallelCoordinatesChart chart = new ParallelCoordinatesChart(mainWindow.getDataSheet(), progressMonitor, mainWindow.getUniqueChartId(ParallelCoordinatesChart.class));

		// log("doInBackground: progressMonitor isCanceled = "+progressMonitor.isCanceled());
		if (progressMonitor.isCanceled()) {
			// log("doInBackground: progressMonitor is Canceled ");
			return null;
		} else {
			try {
				new ChartFrame(mainWindow, chart);
				this.mainWindow.getCurrentSession().addChart(chart);
			} catch (NoParametersDefinedException e) {
				JOptionPane.showMessageDialog(mainWindow, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
			}
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SwingWorker#done()
	 */
	@Override
	protected void done() {
		super.done();
	}
}
