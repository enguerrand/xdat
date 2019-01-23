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

package org.xdat.gui.menus;

import javax.swing.JMenuBar;

import org.xdat.chart.Chart;
import org.xdat.gui.frames.ChartFrame;

/**
 * Menubar for a {@link org.xdat.gui.frames.ChartFrame}
 */
public abstract class ChartFrameMenuBar extends JMenuBar {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/** The chart. */
	private Chart chart;

	/**
	 * Instantiates a new chart frame menu bar.
	 * 
	 * @param chartFrame
	 *            the chart frame
	 * @param chart
	 *            the chart
	 */
	public ChartFrameMenuBar(ChartFrame chartFrame, Chart chart) {
		this.chartFrame = chartFrame;
		this.chart = chart;
	}

	/**
	 * Gets the chart frame.
	 * 
	 * @return the chart frame
	 */
	public ChartFrame getChartFrame() {
		return chartFrame;
	}

	/**
	 * Gets the chart
	 * 
	 * @return the chart
	 */
	public Chart getChart() {
		return chart;
	}

}
