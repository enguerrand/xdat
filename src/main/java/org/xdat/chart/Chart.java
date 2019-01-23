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

package org.xdat.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;

/**
 * A serializable representation of all relevant settings for a chart which is
 * displayed on a ChartFrame.
 * <p>
 * This class should be extended to model actual charts.
 * 
 * @see org.xdat.chart.ParallelCoordinatesChart
 */
public abstract class Chart implements Serializable {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 1;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/**
	 * The location of the {@link org.xdat.gui.frames.ChartFrame} on the screen.
	 */
	private Point location;

	/** The chart's id to form the title. */
	private int id;

	/** The size of this Chart. */
	private Dimension frameSize;

	/**
	 * The data sheet that is displayed in this Chart.
	 * 
	 * @see org.xdat.data.DataSheet
	 */
	private DataSheet dataSheet;

	/**
	 * Stores whether the graphics of the chart should use anti-aliasing
	 */
	private boolean antiAliasing;
	
	/**
	 * Stores whether the graphics of the chart should use transparency
	 */
	private boolean useAlpha;
	
	/**
	 * Instantiates a new chart.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 * @param id 
	 * 				the id
	 */
	public Chart(DataSheet dataSheet, int id) {
		this.dataSheet = dataSheet;
		this.id = id;
		this.location = new Point(100, 100);
		this.frameSize = new Dimension(800, 600);
		this.antiAliasing = UserPreferences.getInstance().isAntiAliasing();
		this.useAlpha = UserPreferences.getInstance().isUseAlpha();
		log("constructor called. Read Base settings.");
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public abstract String getTitle();

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Determines the width of this Chart.
	 * 
	 * @return the width of this Chart
	 */
	public abstract int getWidth();

	/**
	 * Determines the height of this Chart.
	 * 
	 * @return the height of this Chart
	 */
	public abstract int getHeight();

	/**
	 * Gets the data sheet.
	 * 
	 * @return the data sheet
	 */
	public DataSheet getDataSheet() {
		return dataSheet;
	}

	/**
	 * Sets the data sheet.
	 * 
	 * @param dataSheet
	 *            the new data sheet
	 */
	public void setDataSheet(DataSheet dataSheet) {
		this.dataSheet = dataSheet;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (Chart.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the back ground color.
	 * 
	 * @return the back ground color
	 */
	public abstract Color getBackGroundColor();

	/**
	 * Sets the back ground color.
	 * 
	 * @param backGroundColor
	 *            the new back ground color
	 */
	public abstract void setBackGroundColor(Color backGroundColor);

	/**
	 * Gets the size that this chart's frame takes up on screen.
	 * 
	 * @return the size of this Chart.
	 */
	public Dimension getFrameSize() {
		return frameSize;
	}

	/**
	 * Sets the size that this chart's frame takes up on screen.chart.getWidth()
	 * - chart.getScatterPlot2D().getMargin();
	 * 
	 * @param size
	 *            the new size of this Chart.
	 */
	public void setFrameSize(Dimension size) {
		log("setFrameSize: New height = " + size.height);
		log("setFrameSize: New width = " + size.width);
		this.frameSize = size;
	}

	/**
	 * Gets the location of this Chart on the Screen.
	 * 
	 * @return the location of this Chart on the Screen.
	 */
	public Point getLocation() {
		return location;
	}

	/**
	 * Sets the location of this Chart on the Screen..
	 * 
	 * @param location
	 *            the new location of this Chart on the Screen.
	 */
	public void setLocation(Point location) {
		this.location = location;
	}

	/**
	 * Reset display settings to default.
	 */
	public abstract void resetDisplaySettingsToDefault();
	
	/**
	 * Anti Aliasing Flag for Graphics
	 * @return true, if the graphics should use anti-aliasing
	 */
	public boolean isAntiAliasing() {
		return antiAliasing;
	}
	
	/**
	 * Sets whether the chart should use anti-aliasing.
	 * @param antiAliasing
	 * 				the new anti-aliasing state
	 */
	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}
	
	/**
	 * Alpha for Graphics
	 * @return true, if alpha should be used
	 */
	public boolean isUseAlpha() {
		return useAlpha;
	}
	
	/**
	 * Sets whether the chart should use alpha.
	 * @param useAlpha
	 * 				the new alpha state
	 */
	public void setUseAlpha(boolean useAlpha) {
		this.useAlpha = useAlpha;
	}
}
