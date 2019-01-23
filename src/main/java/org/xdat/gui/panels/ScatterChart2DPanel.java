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

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.chart.ScatterChart2D;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.data.Parameter;

/**
 * Panel that is used to display a {@link org.xdat.chart.ScatterChart2D}.
 */
public class ScatterChart2DPanel extends ChartPanel {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The Main Window */
	private Main mainWindow;

	/** The y-axis offset */
	private int yAxisOffset = 0;
	
	/**
	 * Instantiates a new scatter chart 2D panel.
	 * 
	 * @param mainWindow
	 *            the main Window
	 * @param chart
	 *            the chart
	 */
	public ScatterChart2DPanel(Main mainWindow, ScatterChart2D chart) {
		super(mainWindow.getDataSheet(), chart);
		this.mainWindow = mainWindow;
		log("constructor called");
	}

	/**
	 * Overridden to implement the painting of the chart.
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		log("paintComponent called ");
		ScatterChart2D chart = (ScatterChart2D) this.getChart();
		ScatterPlot2D plot = chart.getScatterPlot2D();

		if (chart.getScatterPlot2D().isShowDecorations()) {
			// first, check what offset is needed to account for the tic labels
			// on the left
			g.setFont(new Font("SansSerif", Font.PLAIN, plot.getTicLabelFontSizeY()));
			this.yAxisOffset = plot.getParameterForYAxis().getLongestTicLabelStringLength(g.getFontMetrics(), plot.TIC_LABEL_FORMAT);
		}

		if (this.mainWindow.getDataSheet().getParameterCount() > 0) {
			if (!this.mainWindow.getDataSheet().parameterExists(chart.getScatterPlot2D().getParameterForXAxis()))
				chart.getScatterPlot2D().setParameterForXAxis(this.mainWindow.getDataSheet().getParameter(0));
			if (!this.mainWindow.getDataSheet().parameterExists(chart.getScatterPlot2D().getParameterForYAxis()))
				chart.getScatterPlot2D().setParameterForYAxis(this.mainWindow.getDataSheet().getParameter(0));
			this.drawDesigns(g, chart);
		}

		if (chart.getScatterPlot2D().isShowDecorations()) {
			this.drawAxes(g, chart, plot);
		}
	}

	/**
	 * Draws the lines representing the designs.
	 * 
	 * @param g
	 *            the graphics object
	 * @param chart 
	 * 			the chart
	 */
	public void drawDesigns(Graphics g, ScatterChart2D chart) {
		ScatterPlot2D plot = chart.getScatterPlot2D();
		Parameter paramX = plot.getParameterForXAxis();
		Parameter paramY = plot.getParameterForYAxis();
		if (paramX == null || paramY == null) {
			return;
		}
		DataSheet dataSheet = chart.getDataSheet();
		double xValues[] = new double[dataSheet.getDesignCount()];
		double yValues[] = new double[dataSheet.getDesignCount()];
		double minX = chart.getScatterPlot2D().getMinX();
		double maxX = chart.getScatterPlot2D().getMaxX();
		double minY = chart.getScatterPlot2D().getMinY();
		double maxY = chart.getScatterPlot2D().getMaxY();

		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			xValues[i] = dataSheet.getDesign(i).getDoubleValue(paramX);
			yValues[i] = dataSheet.getDesign(i).getDoubleValue(paramY);
		}

		if (chart.getScatterPlot2D().isAutofitX()) {
			minX = Double.POSITIVE_INFINITY;
			maxX = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < dataSheet.getDesignCount(); i++) {
				log("drawDesigns find xmin: xValues[i]=" + xValues[i]);
				xValues[i] = dataSheet.getDesign(i).getDoubleValue(paramX);
				if (xValues[i] > maxX)
					maxX = xValues[i];
				if (xValues[i] < minX)
					minX = xValues[i];
			}
			chart.getScatterPlot2D().setMaxX(maxX);
			chart.getScatterPlot2D().setMinX(minX);
		}

		if (chart.getScatterPlot2D().isAutofitY()) {
			minY = Double.POSITIVE_INFINITY;
			maxY = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < dataSheet.getDesignCount(); i++) {
				yValues[i] = dataSheet.getDesign(i).getDoubleValue(paramY);
				if (yValues[i] > maxY)
					maxY = yValues[i];
				if (yValues[i] < minY)
					minY = yValues[i];
			}
			chart.getScatterPlot2D().setMaxY(maxY);
			chart.getScatterPlot2D().setMinY(minY);
		}

		double xRange = maxX - minX;
		double yRange = maxY - minY;
		log("drawDesigns: maxX=" + maxX);
		log("drawDesigns: minX=" + minX);
		log("drawDesigns: xRange=" + xRange);
		int plotWidth = chart.getWidth() - chart.getScatterPlot2D().getPlotAreaDistanceToLeft(this.yAxisOffset) - chart.getScatterPlot2D().getPlotAreaDistanceToRight();
		int plotHeight = chart.getHeight() - chart.getScatterPlot2D().getPlotAreaDistanceToTop() - chart.getScatterPlot2D().getPlotAreaDistanceToBottom();
		int xOrig = chart.getScatterPlot2D().getPlotAreaDistanceToLeft(this.yAxisOffset) - chart.getScatterPlot2D().getDotRadius();
		int yOrig = chart.getHeight() - chart.getScatterPlot2D().getPlotAreaDistanceToBottom() - chart.getScatterPlot2D().getDotRadius();

		if (!paramX.isNumeric()) {
			int delta = (int) (0.5 * plotWidth / (paramX.getDiscreteLevelCount() + 1));
			plotWidth = plotWidth - 2 * delta;
			xOrig = xOrig + delta;
		}

		if (!paramY.isNumeric()) {
			int delta = (int) (0.5 * plotHeight / (paramY.getDiscreteLevelCount() + 1));
			plotHeight = plotHeight - 2 * delta;
			yOrig = yOrig - delta;
		}

		int ovalDiameter = 2 * chart.getScatterPlot2D().getDotRadius();
		boolean isXConstant = xRange == 0;
		int constantX = (int) (xOrig + 0.5 * plotWidth);
		boolean isYConstant = yRange == 0;
		int constantY = (int) (yOrig - 0.5 * plotHeight);

		switch (chart.getScatterPlot2D().getDisplayedDesignSelectionMode()) {
			case (ScatterPlot2D.SHOW_DESIGNS_ACTIVE_IN_PARALLEL_CHART): {
				ParallelCoordinatesChart parallelChart = chart.getScatterPlot2D().getParallelCoordinatesChartForFiltering();
				if (chart.getScatterPlot2D().getParallelCoordinatesChartForFiltering() != null) {
					for (int i = 0; i < dataSheet.getDesignCount(); i++) {
						Design design = dataSheet.getDesign(i);
						if (dataSheet.getDesign(i).isActive(parallelChart)) {
							int x = xOrig + (int) ((xValues[i] - minX) * plotWidth / xRange);
							int y = yOrig - (int) ((yValues[i] - minY) * plotHeight / yRange);
							if (isXConstant)
								x = constantX;
							if (isYConstant)
								y = constantY;
							if (design.isSelected()) {
								g.setColor(chart.getScatterPlot2D().getSelectedDesignColor());
							} else if (design.hasGradientColor()){
								g.setColor(parallelChart.getDesignColor(design, true, chart.isUseAlpha()));
							} else {
								g.setColor(chart.getScatterPlot2D().getDesignColor(design));
							}
							g.fillOval(x, y, ovalDiameter, ovalDiameter);
						}
					}
					break;
				}
				// deliberate fall-through!
			}
			case (ScatterPlot2D.SHOW_ALL_DESIGNS): {
				for (int i = 0; i < dataSheet.getDesignCount(); i++) {
					int x = xOrig + (int) ((xValues[i] - minX) * plotWidth / xRange);
					int y = yOrig - (int) ((yValues[i] - minY) * plotHeight / yRange);
					if (isXConstant)
						x = constantX;
					if (isYConstant)
						y = constantY;
					g.setColor(chart.getScatterPlot2D().getDesignColor(dataSheet.getDesign(i)));
					if (dataSheet.getDesign(i).isSelected()) {
						g.setColor(chart.getScatterPlot2D().getSelectedDesignColor());
					} else {
						g.setColor(chart.getScatterPlot2D().getDesignColor(dataSheet.getDesign(i)));
					}
					g.fillOval(x, y, ovalDiameter, ovalDiameter);
				}
				break;
			}
			case (ScatterPlot2D.SHOW_SELECTED_DESIGNS): {
				for (int i = 0; i < dataSheet.getDesignCount(); i++) {
					if (dataSheet.getDesign(i).isSelected()) {
						int x = xOrig + (int) ((xValues[i] - minX) * plotWidth / xRange);
						int y = yOrig - (int) ((yValues[i] - minY) * plotHeight / yRange);
						if (isXConstant)
							x = constantX;
						if (isYConstant)
							y = constantY;
						g.setColor(chart.getScatterPlot2D().getDesignColor(dataSheet.getDesign(i)));
						g.fillOval(x, y, ovalDiameter, ovalDiameter);
					}
				}
				break;
			}
			default: {
			}
		}

	}

	/**
	 * Draws the axes.
	 * 
	 * @param g
	 *            the graphics object
	 * @param chart 
	 * 				the chart
	 * @param plot 
	 * 				the plot
	 */
	public void drawAxes(Graphics g, ScatterChart2D chart, ScatterPlot2D plot) {

		log("drawAxes: chart height: " + chart.getHeight());
		log("drawAxes: chart width: " + chart.getWidth());

		// Frame
		g.setColor(chart.getBackGroundColor());
		g.fillRect(0, 0, chart.getWidth(), plot.getPlotAreaDistanceToTop());
		g.fillRect(0, 0, plot.getPlotAreaDistanceToLeft(this.yAxisOffset), chart.getHeight());
		g.fillRect(0, chart.getHeight() - plot.getPlotAreaDistanceToBottom(), chart.getWidth(), chart.getHeight());
		g.fillRect(chart.getWidth() - plot.getPlotAreaDistanceToRight(), 0, plot.getPlotAreaDistanceToRight(), chart.getHeight());

		// Y - Axis
		g.setColor(plot.getDecorationsColor());
		int x1 = plot.getPlotAreaDistanceToLeft(this.yAxisOffset);
		int y1 = chart.getHeight() - plot.getPlotAreaDistanceToBottom();
		int x2 = plot.getPlotAreaDistanceToLeft(this.yAxisOffset);
		int y2 = plot.getPlotAreaDistanceToTop();
		g.drawLine(x1, y1, x2, y2);

		// Y - Axis tics

		if (!plot.getParameterForYAxis().isNumeric()) {
			int delta = (int) (0.5 * (y1 - y2) / (plot.getParameterForYAxis().getDiscreteLevelCount() + 1));
			y1 = y1 - delta;
			y2 = y2 + delta;
		}

		int ticCountY;

		if (plot.getParameterForYAxis().isNumeric()) {
			if (plot.getMinY() == plot.getMaxY()) {
				ticCountY = 1;
			} else {
				ticCountY = plot.getTicCountY();
			}
		} else {
			ticCountY = plot.getParameterForYAxis().getDiscreteLevelCount();
		}

		g.setFont(new Font("SansSerif", Font.PLAIN, plot.getTicLabelFontSizeY()));
		if (ticCountY == 1) {
			int y = (int) (0.5 * (y2 + y1));
			g.drawLine(x2, (int) (0.5 * (y2 + y1)), x2 + plot.getTicSize(), y);

			// label
			String label = this.getTicLabelY(plot, 0);
			int xOffset = g.getFontMetrics().stringWidth(label);
			g.drawString(label, x2 - xOffset - ScatterPlot2D.AXIS_LABEL_PADDING, y + (int) (0.5 * plot.getTicLabelFontSizeY()));
		} else {
			int ticSpacingY = (int) (((float) (y1 - y2)) / ((float) (ticCountY - 1)));
			for (int i = 0; i < ticCountY; i++) {
				int ticY = y1 - i * ticSpacingY;
				g.drawLine(x2, ticY, x2 + plot.getTicSize(), ticY);

				// label
				String label = this.getTicLabelY(plot, i);
				int xOffset = g.getFontMetrics().stringWidth(label);
				g.drawString(label, x2 - xOffset - ScatterPlot2D.AXIS_LABEL_PADDING, ticY + (int) (0.5 * plot.getTicLabelFontSizeY()));
			}
		}

		// X - Axis
		x1 = plot.getPlotAreaDistanceToLeft(this.yAxisOffset);
		y1 = chart.getHeight() - plot.getPlotAreaDistanceToBottom();
		x2 = chart.getWidth() - plot.getPlotAreaDistanceToRight();
		y2 = chart.getHeight() - plot.getPlotAreaDistanceToBottom();
		g.drawLine(x1, y1, x2, y2);

		// X - Axis tics

		if (!plot.getParameterForXAxis().isNumeric()) {
			int delta = (int) (0.5 * (x2 - x1) / (plot.getParameterForXAxis().getDiscreteLevelCount() + 1));
			x1 = x1 + delta;
			x2 = x2 - delta;
		}

		int ticCountX;
		if (plot.getParameterForXAxis().isNumeric()) {
			if (plot.getMinX() == plot.getMaxX()) {
				ticCountX = 1;
			} else {
				ticCountX = plot.getTicCountX();
			}
		} else {
			ticCountX = plot.getParameterForXAxis().getDiscreteLevelCount();
		}

		g.setFont(new Font("SansSerif", Font.PLAIN, plot.getTicLabelFontSizeX()));
		if (ticCountX == 1) {
			int x = (int) (0.5 * (x1 + x2));
			g.drawLine(x, y2, x, y2 - plot.getTicSize());

			// label
			String label = this.getTicLabelX(plot, 0);
			int xOffset = (int) (0.5 * g.getFontMetrics().stringWidth(label));
			g.drawString(label, x - xOffset, y2 + ScatterPlot2D.AXIS_LABEL_PADDING + plot.getTicLabelFontSizeX());
		} else {
			int ticSpacingX = (int) ((float) ((x2 - x1)) / (float) ((ticCountX - 1)));

			for (int i = 0; i < ticCountX; i++) {
				int ticX = x1 + i * ticSpacingX;
				g.drawLine(ticX, y2, ticX, y2 - plot.getTicSize());

				// label
				String label = this.getTicLabelX(plot, i);
				int xOffset = (int) (0.5 * g.getFontMetrics().stringWidth(label));
				g.drawString(label, ticX - xOffset, y2 + ScatterPlot2D.AXIS_LABEL_PADDING + plot.getTicLabelFontSizeX());
			}
		}

		// X - Axis Label
		String xParameterName = "";
		if (plot.getParameterForXAxis() != null) {
			xParameterName = plot.getParameterForXAxis().getName();
		}

		g.setFont(new Font("SansSerif", Font.PLAIN, plot.getAxisLabelFontSizeX()));
		int slenX = g.getFontMetrics().stringWidth(xParameterName);
		g.drawString(xParameterName, (int) ((chart.getWidth() - slenX) / 2), chart.getHeight() - plot.getMargin() - ScatterPlot2D.AXIS_LABEL_PADDING);

		// Y - Axis Label
		String yParameterName = "";
		if (plot.getParameterForYAxis() != null) {
			yParameterName = plot.getParameterForYAxis().getName();
		}

		g.setFont(new Font("SansSerif", Font.PLAIN, plot.getAxisLabelFontSizeY()));
		int slenY = g.getFontMetrics().stringWidth(yParameterName);

		// code for rotated font taken from
		// http://greybeardedgeek.net/2009/05/15/rotated-text-in-java-swing-2d/
		Graphics2D g2D = (Graphics2D) g;
		AffineTransform fontAT = new AffineTransform();
		Font origFont = g2D.getFont();
		fontAT.rotate(-Math.PI / 2);
		Font theDerivedFont = origFont.deriveFont(fontAT);
		g2D.setFont(theDerivedFont);
		g2D.drawString(yParameterName, plot.getMargin() + ScatterPlot2D.AXIS_LABEL_PADDING + plot.getAxisLabelFontSizeY(), (int) ((chart.getHeight() + slenY) / 2));
		g2D.setFont(origFont);

	}

	/**
	 * Finds the string value for a tic label on the x axis.
	 * 
	 * @param plot
	 *            the plot
	 * @param ticIndex
	 *            the tic index
	 * @return the tic label
	 */
	private String getTicLabelX(ScatterPlot2D plot, int ticIndex) {
		double value = plot.getMinX() + ticIndex * (plot.getMaxX() - plot.getMinX()) / (plot.getTicCountX() - 1);
		if (plot.getParameterForXAxis().isNumeric()) {
			return String.format("%4.3f", value);
		} else {
			return plot.getParameterForXAxis().getStringValueOf(ticIndex);
		}
	}

	/**
	 * Finds the string value for a tic label on the y axis.
	 * 
	 * @param plot
	 *            the plot
	 * @param ticIndex
	 *            the tic index
	 * @return the tic label
	 */
	private String getTicLabelY(ScatterPlot2D plot, int ticIndex) {
		double value = plot.getMinY() + ticIndex * (plot.getMaxY() - plot.getMinY()) / (plot.getTicCountY() - 1);
		if (plot.getParameterForYAxis().isNumeric()) {
			return String.format(ScatterPlot2D.TIC_LABEL_FORMAT, value);
		} else {
			return plot.getParameterForYAxis().getStringValueOf(ticIndex);
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ScatterChart2DPanel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
