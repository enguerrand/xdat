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
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.Cluster;
import org.xdat.data.DataSheet;
import org.xdat.gui.frames.ChartFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartFrameAddDesignToClusterMenuActionListener implements ActionListener {

	private Main mainWindow;
	private ChartFrame chartFrame;
	public ChartFrameAddDesignToClusterMenuActionListener(Main mainWindow, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
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
}
