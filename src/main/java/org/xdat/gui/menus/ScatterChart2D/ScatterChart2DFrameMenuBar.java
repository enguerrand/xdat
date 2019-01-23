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

package org.xdat.gui.menus.ScatterChart2D;

import org.xdat.Main;
import org.xdat.chart.ScatterChart2D;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.ChartFrameMenuBar;

/**
 * Menubar for a {@link org.xdat.gui.frames.ChartFrame}
 */
public class ScatterChart2DFrameMenuBar extends ChartFrameMenuBar {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/**
	 * Instantiates a new chart frame menu bar.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 * @param chart
	 *            the chart
	 */
	public ScatterChart2DFrameMenuBar(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart) {
		super(chartFrame, chart);
		this.add(new ScatterChart2DFrameOptionsMenu(mainWindow, chartFrame));

	}

}
