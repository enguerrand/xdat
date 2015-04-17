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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;

/**
 * A serializable representation of all relevant settings for a two-dimensional
 * scatter chart which is displayed on a ChartFrame.
 * 
 * @see org.xdat.gui.frames.ChartFrame
 */
public class ScatterChart2D extends Chart {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 1;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The ScatterPlot2D belonging to this chart */
	private ScatterPlot2D scatterPlot2D;

	/**
	 * Instantiates a new 2D scatter chart.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 * @param showDecorations
	 *            show decorations yes / no
	 * @param frameSize
	 *            size of the plot excluding margins
	 * @param id 
	 * 				the id
	 */
	public ScatterChart2D(DataSheet dataSheet, boolean showDecorations, Dimension frameSize, int id) {
		super(dataSheet, id);
		this.scatterPlot2D = new ScatterPlot2D(dataSheet, showDecorations);
		this.setFrameSize(frameSize);

	}

	/**
	 * Set current settings as default
	 */
	public void setCurrentSettingsAsDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		userPreferences.setScatterChart2DDisplayMode(this.scatterPlot2D.getDisplayedDesignSelectionMode());
		userPreferences.setScatterChart2DAutofitX(this.scatterPlot2D.isAutofitX());
		userPreferences.setScatterChart2DAutofitY(this.scatterPlot2D.isAutofitY());
		userPreferences.setScatterChart2DAxisTitleFontsizeX(this.scatterPlot2D.getAxisLabelFontSizeX());
		userPreferences.setScatterChart2DAxisTitleFontsizeY(this.scatterPlot2D.getAxisLabelFontSizeY());
		userPreferences.setScatterChart2DTicCountX(this.scatterPlot2D.getTicCountX());
		userPreferences.setScatterChart2DTicCountY(this.scatterPlot2D.getTicCountY());
		userPreferences.setScatterChart2DTicLabelFontsizeX(this.scatterPlot2D.getTicLabelFontSizeX());
		userPreferences.setScatterChart2DTicLabelFontsizeY(this.scatterPlot2D.getTicLabelFontSizeY());
		userPreferences.setScatterChart2DDataPointSize(this.scatterPlot2D.getDotRadius());
		userPreferences.setScatterChart2DForegroundColor(this.scatterPlot2D.getDecorationsColor());
		userPreferences.setScatterChart2DBackgroundColor(this.scatterPlot2D.getBackGroundColor());
		userPreferences.setScatterChart2DActiveDesignColor(this.scatterPlot2D.getActiveDesignColor());
		userPreferences.setScatterChart2DSelectedDesignColor(this.scatterPlot2D.getSelectedDesignColor());

	}

	/**
	 * Determines the width of this Chart.
	 * 
	 * @return the width of this chart
	 */
	public int getWidth() {
		return this.getFrameSize().width;
	}

	/**
	 * Determines the height of this Chart.
	 * 
	 * @return the height of this chart
	 */
	public int getHeight() {
		return this.getFrameSize().height;
	}

	/**
	 * Gets the title.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return "2D Scatter Chart " + this.getID();
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ScatterChart2D.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the back ground color.
	 * 
	 * @return the back ground color
	 */
	public ScatterPlot2D getScatterPlot2D() {
		return this.scatterPlot2D;
	}

	/**
	 * Gets the back ground color.
	 * 
	 * @return the back ground color
	 */
	public Color getBackGroundColor() {
		return this.scatterPlot2D.getBackGroundColor();
	}

	/**
	 * Sets the back ground color.
	 * 
	 * @param backGroundColor
	 *            the new back ground color
	 */
	public void setBackGroundColor(Color backGroundColor) {
		this.scatterPlot2D.setBackGroundColor(backGroundColor);
	}

	/**
	 * Reset display settings to default.
	 */
	public void resetDisplaySettingsToDefault() {
		this.scatterPlot2D.resetDisplaySettingsToDefault();
	}

}
