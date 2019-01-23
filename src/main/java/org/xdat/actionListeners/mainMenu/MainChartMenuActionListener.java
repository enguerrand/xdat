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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitor;

import org.xdat.Main;
import org.xdat.chart.ScatterChart2D;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.mainWIndow.MainChartMenu;
import org.xdat.workerThreads.ParallelCoordinatesChartCreationThread;

/**
 * ActionListener for a {@link MainChartMenu}.
 */
public class MainChartMenuActionListener implements ActionListener {

	/** The main window. */
	private Main mainWindow;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/**
	 * Instantiates a new main chart menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainChartMenuActionListener(Main mainWindow) {
		log("constructor called");
		this.mainWindow = mainWindow;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Create Parallel Coordinates Chart")) {
			// log("Create: discrete level count of last parameter is: "+mainWindow.getDataSheet().getParameter(mainWindow.getDataSheet().getParameterCount()-1).getDiscreteLevelCount());
			if (mainWindow.getDataSheet() == null) {
				JOptionPane.showMessageDialog(mainWindow, "Please create a data sheet first by selecting Data->Import.", "Create Chart", JOptionPane.INFORMATION_MESSAGE);
			} else {
				ProgressMonitor progressMonitor = new ProgressMonitor(mainWindow, "", "Building Chart...", 0, 100);
				progressMonitor.setProgress(0);
				ParallelCoordinatesChartCreationThread sw = new ParallelCoordinatesChartCreationThread(mainWindow, progressMonitor);
				sw.execute();
			}
		}

		else if (e.getActionCommand().equals("Create Scatter Chart 2D")) {
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
		} else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (MainChartMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
