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

import org.xdat.Main;
import org.xdat.gui.dialogs.ClusterDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinatesChartFrameClusteringMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener for the {@link ParallelCoordinatesChartFrameClusteringMenu}.
 */
public class ChartFrameClusteringMenuActionListener implements ActionListener {

	/** The main window. */
	private Main mainWindow;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates a new chart frame clustering menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 */
	public ChartFrameClusteringMenuActionListener(Main mainWindow, ChartFrame chartFrame) {
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
		if (actionCommand.equals("Edit Clusters")) {
			new ClusterDialog(this.chartFrame, this.mainWindow);
		}

	}
}
