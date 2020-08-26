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
import org.xdat.chart.ScatterChart2D;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.workerThreads.ParallelCoordinatesChartCreationThread;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

public class MainChartMenuActionListener {

	private Main mainWindow;

	public MainChartMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void createScatterChart2D(ActionEvent e) {
		if (mainWindow.getDataSheet() == null) {
			JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Create Chart", JOptionPane.INFORMATION_MESSAGE);
		} else {
			ScatterChart2D chart = new ScatterChart2D(mainWindow.getDataSheet(), true, new Dimension(600, 600), mainWindow.getUniqueChartId(ScatterChart2D.class));
			try {
				new ChartFrame(mainWindow, chart);
				this.mainWindow.getCurrentSession().addChart(chart);
			} catch (NoParametersDefinedException e1) {
				JOptionPane.showMessageDialog(mainWindow, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void createParallelChart(ActionEvent e) {
		if (mainWindow.getDataSheet() == null) {
			JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Create Chart", JOptionPane.INFORMATION_MESSAGE);
		} else {
			ProgressMonitor progressMonitor = new ProgressMonitor(mainWindow, "", "Building Chart...", 0, 100);
			progressMonitor.setProgress(0);
			ParallelCoordinatesChartCreationThread sw = new ParallelCoordinatesChartCreationThread(mainWindow, progressMonitor);
			sw.execute();
		}
	}
}
