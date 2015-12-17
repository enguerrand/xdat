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
package org.xdat.actionListeners.parallelCoordinatesChartFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.Cluster;
import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinatesChartFrameAddDesignToClusterMenu;

/**
 * ActionListener for the
 * {@link ParallelCoordinatesChartFrameAddDesignToClusterMenu} to add a
 * {@link Design} to a {@link Cluster}.
 */
public class ChartFrameAddDesignToClusterMenuActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates this class.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 */
	public ChartFrameAddDesignToClusterMenuActionListener(Main mainWindow, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
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
		DataSheet dataSheet = chartFrame.getChart().getDataSheet();
		Cluster cluster = dataSheet.getClusterSet().getCluster(actionCommand);
		ParallelCoordinatesChart chart = (ParallelCoordinatesChart) chartFrame.getChart();
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			if (dataSheet.getDesign(i).isActive(chart)) {
				dataSheet.getDesign(i).setCluster(cluster);
			}
		}
		this.chartFrame.getChartPanel().setPreferredSize(this.chartFrame.getChartPanel().getPreferredSize());

		for (int i = 0; i < this.mainWindow.getChartFrameCount(); i++) {
			this.mainWindow.getChartFrame(i).validate();
			this.mainWindow.getChartFrame(i).repaint();
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartFrameAddDesignToClusterMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
