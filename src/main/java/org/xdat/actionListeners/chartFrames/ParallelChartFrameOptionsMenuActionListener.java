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

package org.xdat.actionListeners.chartFrames;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;

import java.awt.event.ActionEvent;

public class ParallelChartFrameOptionsMenuActionListener extends ChartFrameOptionsMenuActionListener {

	private ParallelCoordinatesChart chart;
	public ParallelChartFrameOptionsMenuActionListener(Main mainWindow, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		super(mainWindow, chartFrame);
		this.chart = chart;
	}

	public void displaySettings(ActionEvent e) {
		new ParallelCoordinatesDisplaySettingsDialog(this.getMainWindow(), this.chart, this.getChartFrame());
	}

	public void resetToDefault(ActionEvent e) {
		this.chart.resetDisplaySettingsToDefault(this.getMainWindow().getDataSheet());
		this.getChartFrame().repaint();
	}

}
