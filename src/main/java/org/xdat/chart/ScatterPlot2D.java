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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.data.Parameter;

/**
 * A serializable representation of all relevant settings for a two-dimensional
 * scatter chart which is displayed on a ChartFrame.
 * 
 * @see org.xdat.gui.frames.ChartFrame
 */
public class ScatterPlot2D extends Plot {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 3;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** Show all designs. */
	public static final int SHOW_ALL_DESIGNS = 0;

	/** Show all designs that are not selected in the data table. */
	public static final int SHOW_SELECTED_DESIGNS = 1;

	/**
	 * Show all designs that are not filtered on a given @link
	 * chart.ParallelCoordinatesChart.
	 */
	public static final int SHOW_DESIGNS_ACTIVE_IN_PARALLEL_CHART = 2;

	/** The padding for axis labels */
	public static final int AXIS_LABEL_PADDING = 10;

	/** The padding for tic labels */
	public static final int TIC_LABEL_PADDING = 5;

	/** the tic label format */
	public static final String TIC_LABEL_FORMAT = "%4.3f";

	/** Specifies which designs should be shown */
	private int displayedDesignSelectionMode = SHOW_ALL_DESIGNS;

	/** Chart to get filter settings from */
	private ParallelCoordinatesChart parallelCoordinatesChartForFiltering;

	/** Specifies the dot diameter for designs. */
	private int dotRadius = 4;

	/**
	 * The standard design color.
	 * <p>
	 * All Designs that are not filtered and do not belong to any Clusters are
	 * displayed in this Color. New Clusters are also given this Color by
	 * default.
	 * 
	 * @see org.xdat.data.Design
	 * @see org.xdat.data.Cluster
	 * */
	private Color activeDesignColor = new Color(0, 150, 0);

	/** The color in which selected designs are shown on this plot. */
	private Color selectedDesignColor = Color.BLUE;

	/** Parameter to plot on x ordinate */
	private Parameter parameterForXAxis;

	/** Parameter to plot on y ordinate */
	private Parameter parameterForYAxis;

	/** Specifies whether axes, labels etc. should be drawn */
	private boolean showDecorations;

	/** Color for decorations (axes, labels etc.) */
	private Color decorationsColor = Color.BLACK;

	/** Specifies whether x-axis should be autofit. */
	private boolean autofitX = true;

	/** Specifies whether y-axis should be autofit. */
	private boolean autofitY = true;

	/** X Axis min value */
	private double minX = 0;

	/** X Axis max value */
	private double maxX = 1;

	/** Y Axis min value */
	private double minY = 0;

	/** Y Axis max value */
	private double maxY = 1;

	/** Number of tics on x axis */
	private int ticCountX = 2;

	/** Number of tics on y axis */
	private int ticCountY = 2;

	/** Tic size */
	private int ticSize = 5;

	/** The x axis label font size. */
	private int axisLabelFontSizeX = 20;

	/** The y axis label font size. */
	private int axisLabelFontSizeY = 20;

	/** The x axis tic label font size. */
	private int ticLabelFontSizeX = 12;

	/** The y axis tic label font size. */
	private int ticLabelFontSizeY = 12;

	/**
	 * Instantiates a new 2D scatter plot.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 * @param showDecorations
	 *            show decorations yes / no
	 */
	public ScatterPlot2D(DataSheet dataSheet, boolean showDecorations) {
		super(dataSheet);
		this.showDecorations = showDecorations;
		log("constructor called. Read Base settings.");
		if (dataSheet.getParameterCount() > 1) {
			this.parameterForXAxis = dataSheet.getParameter(0);
			this.parameterForYAxis = dataSheet.getParameter(1);
		} else if (dataSheet.getParameterCount() > 0) {
			this.parameterForXAxis = dataSheet.getParameter(0);
			this.parameterForYAxis = dataSheet.getParameter(0);
		}

		log("constructor called. Read Base settings.");
		resetDisplaySettingsToDefault();
	}

	/**
	 * Gets the design color.
	 * 
	 * @param design
	 *            the design
	 * @return the design color
	 */
	public Color getDesignColor(Design design) {
		if (design.getCluster() != null) {
			return design.getCluster().getActiveDesignColor(false);
		} else {
			return activeDesignColor;
		}
	}

	/**
	 * Gets the active design color.
	 * 
	 * @return the default design color
	 */
	public Color getActiveDesignColor() {
		return activeDesignColor;
	}

	/**
	 * Sets the active design color.
	 * 
	 * @param activeDesignColor
	 *            the new active design color
	 */
	public void setActiveDesignColor(Color activeDesignColor) {
		this.activeDesignColor = activeDesignColor;
	}

	/**
	 * Gets the displayed design selection mode.
	 * 
	 * @return the displayed design selection mode.
	 */
	public int getDisplayedDesignSelectionMode() {
		return displayedDesignSelectionMode;
	}

	/**
	 * Sets the displayed design selection mode..
	 * 
	 * @param displayedDesignSelectionMode
	 *            the new displayed design selection mode.
	 */
	public void setDisplayedDesignSelectionMode(int displayedDesignSelectionMode) {
		this.displayedDesignSelectionMode = displayedDesignSelectionMode;
	}

	/**
	 * Gets the parallel coordinates chart for filtering.
	 * 
	 * @return the parallel coordinates chart for filtering
	 */
	public ParallelCoordinatesChart getParallelCoordinatesChartForFiltering() {
		return parallelCoordinatesChartForFiltering;
	}

	/**
	 * Sets the the parallel coordinates chart for filtering.
	 * 
	 * @param parallelCoordinatesChartForFiltering
	 *            the new the parallel coordinates chart for filtering
	 */
	public void setParallelCoordinatesChartForFiltering(ParallelCoordinatesChart parallelCoordinatesChartForFiltering) {
		this.parallelCoordinatesChartForFiltering = parallelCoordinatesChartForFiltering;
	}

	/**
	 * Gets the dot radius.
	 * 
	 * @return the dot radius
	 */
	public int getDotRadius() {
		return dotRadius;
	}

	/**
	 * Sets the dot radius.
	 * 
	 * @param dotRadius
	 *            the new dot radius
	 */
	public void setDotRadius(int dotRadius) {
		this.dotRadius = dotRadius;
	}

	/**
	 * Gets the selected design color.
	 * 
	 * @return the selected design color.
	 */
	public Color getSelectedDesignColor() {
		return selectedDesignColor;
	}

	/**
	 * Sets the selected design color..
	 * 
	 * @param selectedDesignColor
	 *            the new selected design color.
	 */
	public void setSelectedDesignColor(Color selectedDesignColor) {
		this.selectedDesignColor = selectedDesignColor;
	}

	/**
	 * Gets the parameter for the x axis.
	 * 
	 * @return the parameter for the x axis.
	 */
	public Parameter getParameterForXAxis() {
		return parameterForXAxis;
	}

	/**
	 * Sets the parameter for the x axis.
	 * 
	 * @param parameterForXAxis
	 *            the new parameter for the x axis.
	 */
	public void setParameterForXAxis(Parameter parameterForXAxis) {
		this.parameterForXAxis = parameterForXAxis;
	}

	/**
	 * Gets the parameter for the y axis.
	 * 
	 * @return the parameter for the y axis.
	 */
	public Parameter getParameterForYAxis() {
		return parameterForYAxis;
	}

	/**
	 * Sets the parameter for the y axis..
	 * 
	 * @param parameterForYAxis
	 *            the new parameter for the y axis.
	 */
	public void setParameterForYAxis(Parameter parameterForYAxis) {
		this.parameterForYAxis = parameterForYAxis;
	}

	/**
	 * Gets the decorations color.
	 * 
	 * @return the decorations color.
	 */
	public Color getDecorationsColor() {
		return decorationsColor;
	}

	/**
	 * Sets the decorations color.
	 * 
	 * @param decorationsColor
	 *            the new decorations color.
	 */
	public void setDecorationsColor(Color decorationsColor) {
		this.decorationsColor = decorationsColor;
	}

	/**
	 * Gets the autofit state for the x axis.
	 * 
	 * @return the autofit state for the x axis.
	 */
	public boolean isAutofitX() {
		return autofitX;
	}

	/**
	 * Sets the autofit state for the x axis.
	 * 
	 * @param autofitX
	 *            the new autofit state for the x axis.
	 */
	public void setAutofitX(boolean autofitX) {
		this.autofitX = autofitX;
	}

	/**
	 * Gets the autofit state for the y axis..
	 * 
	 * @return the autofit state for the y axis.
	 */
	public boolean isAutofitY() {
		return autofitY;
	}

	/**
	 * Sets the autofit state for the y axis..
	 * 
	 * @param autofitY
	 *            the new autofit state for the y axis.
	 */
	public void setAutofitY(boolean autofitY) {
		this.autofitY = autofitY;
	}

	/**
	 * Autofits the x axis.
	 * @param dataSheet the data sheet
	 */
	public void autofitX(DataSheet dataSheet) {
		this.minX = Double.POSITIVE_INFINITY;
		this.maxX = Double.NEGATIVE_INFINITY;
		Parameter param = this.getParameterForXAxis();
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			double x = dataSheet.getDesign(i).getDoubleValue(param);
			if (x > this.maxX)
				this.maxX = x;
			if (x < this.minX)
				this.minX = x;
		}
	}

	/**
	 * Autofits the y axis.
	 * @param dataSheet the data sheet
	 */
	public void autofitY(DataSheet dataSheet) {
		this.minY = Double.POSITIVE_INFINITY;
		this.maxY = Double.NEGATIVE_INFINITY;
		Parameter param = this.getParameterForYAxis();
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			double y = dataSheet.getDesign(i).getDoubleValue(param);
			if (y > this.maxY)
				this.maxY = y;
			if (y < this.minY)
				this.minY = y;
		}
	}

	/**
	 * Gets the min value for the x-axis.
	 * 
	 * @return the min value for the x-axis.
	 */
	public double getMinX() {
		return minX;
	}

	/**
	 * Sets the min value for the x-axis.
	 * 
	 * @param minX
	 *            the new min value for the x-axis.
	 */
	public void setMinX(double minX) {
		this.minX = minX;
	}

	/**
	 * Gets the max value for the x-axis.
	 * 
	 * @return the max value for the x-axis.
	 */
	public double getMaxX() {
		return maxX;
	}

	/**
	 * Sets the max value for the x-axis..
	 * 
	 * @param maxX
	 *            the new max value for the x-axis.
	 */
	public void setMaxX(double maxX) {
		this.maxX = maxX;
	}

	/**
	 * Gets the min value for the y-axis..
	 * 
	 * @return the min value for the y-axis.
	 */
	public double getMinY() {
		return minY;
	}

	/**
	 * Sets the min value for the y-axis.
	 * 
	 * @param minY
	 *            the new min value for the y-axis.
	 */
	public void setMinY(double minY) {
		this.minY = minY;
	}

	/**
	 * Gets the max value for the y-axis.
	 * 
	 * @return the max value for the y-axis.
	 */
	public double getMaxY() {
		return maxY;
	}

	/**
	 * Sets the max value for the y-axis.
	 * 
	 * @param maxY
	 *            the new max value for the y-axis.
	 */
	public void setMaxY(double maxY) {
		this.maxY = maxY;
	}

	/**
	 * Gets the number of tics on the x axis.
	 * 
	 * @return the number of tics on the x axis.
	 */
	public int getTicCountX() {
		return ticCountX;
	}

	/**
	 * Sets the number of tics on the x axis.
	 * 
	 * @param ticCountX
	 *            the new number of tics on the x axis.
	 */
	public void setTicCountX(int ticCountX) {
		this.ticCountX = ticCountX;
	}

	/**
	 * Gets the number of tics on the y axis.
	 * 
	 * @return the number of tics on the y axis.
	 */
	public int getTicCountY() {
		return ticCountY;
	}

	/**
	 * Gets the tic size.
	 * 
	 * @return this tic size.
	 */
	public int getTicSize() {
		return ticSize;
	}

	/**
	 * Sets the number of tics on the y axis.
	 * 
	 * @param ticCountY
	 *            the new number of tics on the y axis.
	 */
	public void setTicCountY(int ticCountY) {
		this.ticCountY = ticCountY;
	}

	/**
	 * Gets the x axis label font size.
	 * 
	 * @return the axis label font size.
	 */
	public int getAxisLabelFontSizeX() {
		return axisLabelFontSizeX;
	}

	/**
	 * Sets the x axis label font size..
	 * 
	 * @param axisLabelFontSizeX
	 *            the new axis label font size.
	 */
	public void setAxisLabelFontSizeX(int axisLabelFontSizeX) {
		this.axisLabelFontSizeX = axisLabelFontSizeX;
	}

	/**
	 * Gets the y axis label font size.
	 * 
	 * @return the axis label font size.
	 */
	public int getAxisLabelFontSizeY() {
		return axisLabelFontSizeY;
	}

	/**
	 * Sets the y axis label font size..
	 * 
	 * @param axisLabelFontSizeY
	 *            the new axis label font size.
	 */
	public void setAxisLabelFontSizeY(int axisLabelFontSizeY) {
		this.axisLabelFontSizeY = axisLabelFontSizeY;
	}

	/**
	 * Gets the x axis tic label font size.
	 * 
	 * @return the tic label font size.
	 */
	public int getTicLabelFontSizeX() {
		return ticLabelFontSizeX;
	}

	/**
	 * Sets the x axis tic label font size.
	 * 
	 * @param ticLabelFontSizeX
	 *            the new tic label font size.
	 */
	public void setTicLabelFontSizeX(int ticLabelFontSizeX) {
		this.ticLabelFontSizeX = ticLabelFontSizeX;
	}

	/**
	 * Gets the y axis tic label font size.
	 * 
	 * @return the tic label font size.
	 */
	public int getTicLabelFontSizeY() {
		return ticLabelFontSizeY;
	}

	/**
	 * Sets the y axis tic label font size.
	 * 
	 * @param ticLabelFontSizeY
	 *            the new tic label font size.
	 */
	public void setTicLabelFontSizeY(int ticLabelFontSizeY) {
		this.ticLabelFontSizeY = ticLabelFontSizeY;
	}

	/**
	 * Gets the plot area distance to the left border
	 * @param ticLabelOffset the tic label offset
	 * 
	 * @return the plot area distance to the left border
	 */
	public int getPlotAreaDistanceToLeft(int ticLabelOffset) {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + this.axisLabelFontSizeY + 2 * AXIS_LABEL_PADDING + 2 * TIC_LABEL_PADDING + ticLabelOffset;
		}
		return distance;
	}

	/**
	 * Gets the plot area distance to the right border
	 * 
	 * @return the plot area distance to the right border
	 */
	public int getPlotAreaDistanceToRight() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + 2 * AXIS_LABEL_PADDING;
		}
		return distance;
	}

	/**
	 * Gets the plot area distance to the top border
	 * 
	 * @return the plot area distance to the top border
	 */
	public int getPlotAreaDistanceToTop() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + 2 * AXIS_LABEL_PADDING;
		}
		return distance;
	}

	/**
	 * Gets the plot area distance to the bottom border
	 * 
	 * @return the plot area distance to the bottom border
	 */
	public int getPlotAreaDistanceToBottom() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + this.axisLabelFontSizeX + 2 * AXIS_LABEL_PADDING + this.ticLabelFontSizeX + 2 * TIC_LABEL_PADDING;
		}
		return distance;
	}

	/**
	 * Gets the show decorations switch.
	 * 
	 * @return true, if axes etc. should be displayed
	 */
	public boolean isShowDecorations() {
		return showDecorations;
	}

	/**
	 * Reset display settings to default.
	 */
	public void resetDisplaySettingsToDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.setDisplayedDesignSelectionMode(userPreferences.getScatterChart2DDisplayMode());
		this.setAutofitX(userPreferences.isScatterChart2DAutofitX());
		this.setAutofitY(userPreferences.isScatterChart2DAutofitY());
		this.setAxisLabelFontSizeX(userPreferences.getScatterChart2DAxisTitleFontsizeX());
		this.setAxisLabelFontSizeY(userPreferences.getScatterChart2DAxisTitleFontsizeY());
		this.setTicCountX(userPreferences.getScatterChart2DTicCountX());
		this.setTicCountY(userPreferences.getScatterChart2DTicCountY());
		this.setTicLabelFontSizeX(userPreferences.getScatterChart2DTicLabelFontsizeX());
		this.setTicLabelFontSizeY(userPreferences.getScatterChart2DTicLabelFontsizeY());
		this.setDotRadius(userPreferences.getScatterChart2DDataPointSize());
		this.setDecorationsColor(userPreferences.getScatterChart2DForegroundColor());
		this.setBackGroundColor(userPreferences.getScatterChart2DBackgroundColor());
		this.setActiveDesignColor(userPreferences.getScatterChart2DActiveDesignColor());
		this.setSelectedDesignColor(userPreferences.getScatterChart2DSelectedDesignColor());
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ScatterPlot2D.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
