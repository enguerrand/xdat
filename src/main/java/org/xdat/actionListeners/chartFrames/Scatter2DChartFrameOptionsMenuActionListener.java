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
import org.xdat.chart.ScatterChart2D;
import org.xdat.gui.dialogs.ScatterChart2DSettingsDialog;
import org.xdat.gui.frames.ChartFrame;

import java.awt.event.ActionEvent;

public class Scatter2DChartFrameOptionsMenuActionListener extends ChartFrameOptionsMenuActionListener {
	private ScatterChart2D chart;

	public Scatter2DChartFrameOptionsMenuActionListener(Main mainWindow, ScatterChart2D chart, ChartFrame chartFrame) {
		super(mainWindow, chartFrame);
		this.chart = chart;
	}

	public void resetToDefault(ActionEvent e) {
		this.chart.resetDisplaySettingsToDefault(getMainWindow().getDataSheet());
		this.getChartFrame().repaint();
	}

	public void settings(ActionEvent e) {
		new ScatterChart2DSettingsDialog(getMainWindow(), getChartFrame(), chart);
	}
}
