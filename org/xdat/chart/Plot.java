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
import java.io.Serializable;

import org.xdat.Main;
import org.xdat.data.DataSheet;

/**
 * A serializable representation of all relevant settings for a Plot.
 */
public abstract class Plot implements Serializable {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 1;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The background color of this plot. */
	private Color backGroundColor = Color.WHITE;

	/**
	 * The plot margin.
	 * 
	 * specifies the distance of the plotted area to the plot's borders.
	 */
	private int margin = 10;

	/**
	 * The data sheet that is displayed in this Plot.
	 * 
	 * @see org.xdat.data.DataSheet
	 */
	private DataSheet dataSheet;

	/**
	 * Instantiates a new 2D scatter plot.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 */
	public Plot(DataSheet dataSheet) {
		this.dataSheet = dataSheet;
		log("constructor called. Read Base settings.");
	}

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
		if (Plot.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the margin.
	 * 
	 * @return the margin
	 */
	public int getMargin() {
		return margin;
	}

	/**
	 * Reset display settings to default.
	 */
	public abstract void resetDisplaySettingsToDefault();

	/**
	 * Gets the back ground color.
	 * 
	 * @return the back ground color
	 */
	public Color getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * Sets the back ground color.
	 * 
	 * @param backGroundColor
	 *            the new back ground color
	 */
	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

}
