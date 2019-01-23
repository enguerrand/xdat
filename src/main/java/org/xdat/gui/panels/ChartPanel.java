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

package org.xdat.gui.panels;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import org.xdat.Main;
import org.xdat.chart.Chart;
import org.xdat.data.DataSheet;

/**
 * Panel that is used to display a {@link org.xdat.chart.Chart}.
 * <p>
 * This class should be extended to model specific chart panels.
 */
public abstract class ChartPanel extends JPanel {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The chart. */
	private Chart chart;

	/** The data sheet. */
	private DataSheet dataSheet;

	/** The top margin. */
	private int marginTop = 20;

	/** The bottom margin. */
	private int marginBottom = 80;

	/** The left margin. */
	private int marginLeft = 80;

	/** The right margin. */
	private int marginRight = 20;

	/**
	 * Instantiates a new chart panel.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 * @param chart
	 *            the chart
	 */
	public ChartPanel(DataSheet dataSheet, Chart chart) {
		log("constructor called");
		this.dataSheet = dataSheet;
		this.chart = chart;
	}

	/**
	 * Overridden to implement the painting of the chart.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(chart.getBackGroundColor());
		this.drawPlotFieldBackground(g);
	}

	/**
	 * Draws the plot field background.
	 * 
	 * @param g
	 *            the graphics object
	 */
	public void drawPlotFieldBackground(Graphics g) {
		g.setColor(this.chart.getBackGroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		int width = marginLeft + marginRight + chart.getWidth();
		int height = marginTop + marginBottom + chart.getHeight();
		Dimension preferredSize = new Dimension(width, height);

		return preferredSize;
	}

	/**
	 * Gets the chart's top margin .
	 * 
	 * @return the top margin
	 */
	public int getMarginTop() {
		return marginTop;
	}

	/**
	 * Gets the chart's bottom margin .
	 * 
	 * @return the bottom margin
	 */
	public int getMarginBottom() {
		return marginBottom;
	}

	/**
	 * Gets the chart's left margin .
	 * 
	 * @return the left margin
	 */
	public int getMarginLeft() {
		return marginLeft;
	}

	/**
	 * Gets the chart's right margin .
	 * 
	 * @return the right margin
	 */
	public int getMarginRight() {
		return marginRight;
	}

	/**
	 * Gets the panel's chart.
	 * 
	 * @return the chart
	 */
	public Chart getChart() {
		return this.chart;
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
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartPanel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
