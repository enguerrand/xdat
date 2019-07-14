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
import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;
import org.xdat.data.Parameter;

/**
 * A serializable representation of all relevant settings for an Axis on a
 * Parallel coordinates shart.
 * <p>
 * An Axis is used to represent a Parameter. Each Axis has an upper Filter and a
 * lower Filter, which are represented by triangles and can be dragged by the
 * user. The positions of the Filters determine which Designs are displayed, and
 * which are not.
 * 
 * @see ParallelCoordinatesChart
 * @see Filter
 * @see org.xdat.data.Parameter
 * @see org.xdat.data.Design
 */
public class Axis implements Serializable {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 5;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The Chart to which this Axis belongs. */
	private ParallelCoordinatesChart chart;

	/**
	 * Specifies whether this Axis is autofitted.
	 * <p>
	 * If true, the {@link #autoFit} method is called before painting the Axis
	 * on the Chart.
	 * */
	private boolean autoFit;

	/** The maximum value of this Axis. */
	private double max;

	/** The minimum value of this Axis. */
	private double min;

	/**
	 * The data sheet that is displayed with the Chart to which this Axis
	 * belongs.
	 */
	private DataSheet dataSheet;

	/** The parameter that is represented by this Axis. */
	private Parameter parameter;

	/**
	 * Determines the distance to the adjacent Axes in pixels.
	 * <p>
	 * The distance to an adjacent Axis is determined by half the sum of both
	 * axes widths.
	 */
	private int width;

	/** The number of tics on this Axis. */
	private int ticCount;

	private int ticLabelDigitCount = 3;

	/**
	 * The axis color.
	 * <p>
	 * The Color in which the Axis is displayed on the Chart.
	 */
	private Color axisColor;

	/**
	 * The axis label font color.
	 * <p>
	 * Each Axis is labeled with the Parameter name on top of the Axis. This
	 * field specifies which Color should be used for this label.
	 * */
	private Color axisLabelFontColor;

	/**
	 * The tic label font color.
	 * <p>
	 * Each tic has a label showing the value to which the tic corresponds. This
	 * field specifies which Color should be used for this label.
	 * */
	private Color ticLabelFontColor;

	/**
	 * The axis label font size.
	 * <p>
	 * Each Axis is labeled with the Parameter name on top of the Axis. This
	 * field specifies which font size should be used for this label.
	 */
	private int axisLabelFontSize;

	/**
	 * The tic label font size.
	 * <p>
	 * Each tic has a label showing the value to which the tic corresponds. This
	 * field specifies which font size should be used for this label.
	 */
	private int ticLabelFontSize;

	/** The tic length in pixels. */
	private int ticLength;

	/** Specifies whether the Axis is displayed on the Chart. */
	private boolean active = true;

	/**
	 * The upper Filter on the Axis.
	 * 
	 * @see org.xdat.chart.Filter
	 */
	private Filter upperFilter;

	/**
	 * The lower Filter on the Axis.
	 * 
	 * @see org.xdat.chart.Filter
	 */
	private Filter lowerFilter;

	/**
	 * Specifies whether the Filters should be inverted.
	 * <p>
	 * If true, designs become inactive if they have values between the upper
	 * and lower Filter and vice versa otherwise.
	 * 
	 * @see Filter
	 * */
	private boolean filterInverted;

	/**
	 * Specifies whether the Axis should be displayed upside down.
	 * <p>
	 * If true, values are ascending from top to bottom. If false, values are
	 * ascending from bottom to top.
	 */
	private boolean axisInverted;

	/**
	 * Instantiates a new Axis.
	 * 
	 * @param dataSheet
	 *            the data sheet
	 * @param chart
	 *            the Chart to which this Axis belongs
	 * @param parameter
	 *            the Parameter represented by this Axis
	 */
	public Axis(DataSheet dataSheet, ParallelCoordinatesChart chart, Parameter parameter) {
		log("constructor invoked. Parameter name: " + parameter.getName());
		log("constructor invoked. Read Base settings.");
		this.dataSheet = dataSheet;
		this.chart = chart;
		this.parameter = parameter;
		log("constructor: Base settings read. Initialise settings...");
		initialiseSettings();
		log("constructor: settings initialised. Autofit...");
		if (this.autoFit)
			autofit();
		log("constructor: Autofit done");
	}

	/**
	 * Initialise display settings.
	 */
	public void initialiseSettings() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.width = userPreferences.getParallelCoordinatesAxisWidth();
		this.ticCount = userPreferences.getParallelCoordinatesAxisTicCount();
		this.axisColor = userPreferences.getParallelCoordinatesAxisColor();
		this.axisLabelFontColor = userPreferences.getParallelCoordinatesAxisLabelFontColor();
		this.ticLabelFontColor = userPreferences.getParallelCoordinatesAxisTicLabelFontColor();
		this.axisLabelFontSize = userPreferences.getParallelCoordinatesAxisLabelFontSize();
		this.ticLabelFontSize = userPreferences.getParallelCoordinatesAxisTicLabelFontSize();
		this.ticLabelDigitCount = userPreferences.getParallelCoordinatesAxisTicLabelDigitCount();
		this.ticLength = userPreferences.getParallelCoordinatesAxisTicLength();
		this.filterInverted = userPreferences.isFilterInverted();
		this.axisInverted = userPreferences.isParallelCoordinatesAxisInverted();
		this.autoFit = userPreferences.isParallelCoordinatesAutoFitAxis();
		this.min = userPreferences.getParallelCoordinatesAxisDefaultMin();
		this.max = userPreferences.getParallelCoordinatesAxisDefaultMax();
		this.dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	/**
	 * Reset display settings to default.
	 */
	public void resetSettingsToDefault() {
		initialiseSettings();
		autofit();
	}

	/**
	 * Adds the Filters.
	 */
	public void addFilters() {
		this.upperFilter = new Filter(this.dataSheet, this, Filter.UPPER_FILTER);
		this.lowerFilter = new Filter(this.dataSheet, this, Filter.LOWER_FILTER);
	}

	/**
	 * Sets the Axis display range such that all Designs lie within the upper
	 * and the lower bound of this Axis.
	 */
	public void autofit() {
		this.max = this.dataSheet.getMaxValueOf(this.parameter);
		this.min = this.dataSheet.getMinValueOf(this.parameter);
		this.dataSheet.evaluateBoundsForAllDesigns(this.chart);
		log("autofit: max = " + max + ", min = " + min);
	}

	/**
	 * Gets the axis label font color.
	 * 
	 * @return the axis label font color
	 */
	public Color getAxisLabelFontColor() {
		return axisLabelFontColor;
	}

	/**
	 * Sets the axis label font color.
	 * 
	 * @param axisLabelFontColor
	 *            the new axis label font color
	 */
	public void setAxisLabelFontColor(Color axisLabelFontColor) {
		this.axisLabelFontColor = axisLabelFontColor;
	}

	/**
	 * Gets the axis label font size.
	 * 
	 * @return the axis label font size
	 */
	public int getAxisLabelFontSize() {
		return axisLabelFontSize;
	}

	/**
	 * Sets the axis label font size.
	 * 
	 * @param axisLabelFontSize
	 *            the new axis label font size
	 */
	public void setAxisLabelFontSize(int axisLabelFontSize) {
		double[] upperFilterValues = new double[this.chart.getAxisCount()];
		double[] lowerFilterValues = new double[this.chart.getAxisCount()];
		for (int i = 0; i < this.chart.getAxisCount(); i++) {
			Axis axis = this.chart.getAxis(i);
			lowerFilterValues[i] = axis.getLowerFilter().getValue();
			upperFilterValues[i] = axis.getUpperFilter().getValue();
		}
		this.axisLabelFontSize = axisLabelFontSize;
		for (int i = 0; i < this.chart.getAxisCount(); i++) {
			Axis axis = this.chart.getAxis(i);
			axis.getLowerFilter().setValue(lowerFilterValues[i]);
			axis.getUpperFilter().setValue(upperFilterValues[i]);
		}
	}

	/**
	 * Gets the maximum value of this Axis.
	 * 
	 * @return the maximum value of this Axis
	 */
	public double getMax() {
		if (!this.parameter.isNumeric())
			return this.dataSheet.getMaxValueOf(this.parameter);
		else
			return max;
	}

	/**
	 * Sets the maximum value of this Axis.
	 * 
	 * @param max
	 *            the new maximum value of this Axis
	 */
	public void setMax(double max) {
		this.max = max;
		this.dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	/**
	 * Gets the minimum value of this Axis.
	 * 
	 * @return the minimum value of this Axis
	 */
	public double getMin() {
		if (!this.parameter.isNumeric())
			return this.dataSheet.getMinValueOf(this.parameter);
		else
			return min;
	}

	/**
	 * Sets the minimum value of this Axis.
	 * 
	 * @param min
	 *            the new minimum value of this Axis
	 */
	public void setMin(double min) {
		this.min = min;
		this.dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	/**
	 * Gets the range of this Axis.
	 * 
	 * @return the range of this Axis
	 */
	public double getRange() {
		if (this.parameter.isNumeric())
			return max - min;
		else
			return this.parameter.getDiscreteLevelCount() - 1;
	}

	/**
	 * Gets the tic count.
	 * 
	 * @return the tic count
	 */
	public int getTicCount() {
		if (this.parameter.isNumeric() && this.getRange() > 0)
			return ticCount;
		else if (this.parameter.isNumeric())
			return 1;
		else
			return this.parameter.getDiscreteLevelCount();
	}

	public void setTicCount(int ticCount) {
		this.ticCount = ticCount;
		if (ticCount < 2) {
			this.applyFilters();
		}
	}

	public void setTicLabelDigitCount(int value) {
		this.ticLabelDigitCount = value;
	}

	public int getTicLabelDigitCount(){
		return this.ticLabelDigitCount;
	}

	public int getTicLabelFontSize() {
		return ticLabelFontSize;
	}

	public void setTicLabelFontSize(int ticLabelFontSize) {
		this.ticLabelFontSize = ticLabelFontSize;
	}

	public String getTicLabelFormat() {
        int digitCount = getTicLabelDigitCount();
        return "%"+(digitCount+1)+"."+digitCount+"f";
	}

	public int getTicLength() {
		return ticLength;
	}

	public void setTicLength(int ticLength) {
		this.ticLength = ticLength;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public DataSheet getDataSheet() {
		return dataSheet;
	}

	public void setDataSheet(DataSheet dataSheet) {
		this.dataSheet = dataSheet;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public void setAxisColor(Color axisColor) {
		this.axisColor = axisColor;
	}

	public Color getAxisTicLabelFontColor() {
		return ticLabelFontColor;
	}

	public void setTicLabelFontColor(Color ticLabelFontColor) {
		this.ticLabelFontColor = ticLabelFontColor;
	}

	public String getName() {
		return this.parameter.getName();
	}

	public Filter getLowerFilter() {
		return lowerFilter;
	}

	public Filter getUpperFilter() {
		return upperFilter;
	}

	public Filter getMinimumFilter() {
		if (this.isAxisInverted())
			return this.upperFilter;
		else
			return lowerFilter;
	}

	public Filter getMaximumFilter() {
		if (this.isAxisInverted())
			return this.lowerFilter;
		else
			return this.upperFilter;
	}

	public ParallelCoordinatesChart getChart() {
		return chart;
	}

	private void log(String message) {
		if (Axis.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	public boolean isFilterInverted() {
		return filterInverted;
	}

	public void setFilterInverted(boolean filterInverted) {
		this.filterInverted = filterInverted;
	}

	public boolean isAxisInverted() {
		return this.axisInverted;
	}

	public void setAxisInverted(boolean axisInverted) {
		double maxFilterValue = this.getMaximumFilter().getValue();
		double minFilterValue = this.getMinimumFilter().getValue();
		this.axisInverted = axisInverted;
		this.getMaximumFilter().setValue(maxFilterValue);
		this.getMinimumFilter().setValue(minFilterValue);
	}

	public boolean isAutoFit() {
		if (this.parameter.isNumeric())
			return autoFit;
		else
			return true;
	}

	public void setAutoFit(boolean autoFit) {
		this.autoFit = autoFit;
	}

	/**
	 * Takes the current filter values and sets them as new min and max values
	 * 
	 */
	public void setFilterAsNewRange() {
		// log("setFilterAsNewRange: current range: "+this.getMin()+" and "+this.getMax()+" for axis "+this.getName());
		// log("setFilterAsNewRange:filterPositions: "+this.lowerFilter.getValue()+" and "+this.upperFilter.getValue()+" for axis "+this.getName());
		this.setAutoFit(false);
		double minFilterValue = this.getMinimumFilter().getValue();
		double maxFilterValue = this.getMaximumFilter().getValue();
		this.setMin(minFilterValue);
		this.setMax(maxFilterValue);
		// log("setFilterAsNewRange: new range set to "+this.getMin()+" and "+this.getMax()+" for axis "+this.getName());
		this.resetFilters();
	}

	/**
	 * Apply filters to designs
	 * 
	 * @see Filter
	 */
	public void applyFilters() {
		this.upperFilter.apply();
		this.lowerFilter.apply();
	}

	/**
	 * Resets both filters to the Axis min and max values..
	 * 
	 * @see Filter
	 */
	public void resetFilters() {
		this.upperFilter.reset();
		this.lowerFilter.reset();
	}

}
