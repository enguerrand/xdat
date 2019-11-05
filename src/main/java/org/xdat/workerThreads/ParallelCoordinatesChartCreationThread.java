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
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import javax.swing.SwingWorker;

public class ParallelCoordinatesChartCreationThread extends SwingWorker {

	private Main mainWindow;
	private ProgressMonitor progressMonitor;

	public ParallelCoordinatesChartCreationThread(Main mainWindow, ProgressMonitor progressMonitor) {
		this.mainWindow = mainWindow;
		this.progressMonitor = progressMonitor;
	}

	@Override
	public Object doInBackground() {
		ParallelCoordinatesChart chart = new ParallelCoordinatesChart(mainWindow.getDataSheet(), progressMonitor, mainWindow.getUniqueChartId(ParallelCoordinatesChart.class));
		if (progressMonitor.isCanceled()) {
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
}
