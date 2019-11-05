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

import org.xdat.UserPreferences;
import org.xdat.data.DataSheet;
import org.xdat.data.Parameter;

import java.awt.Color;
import java.io.Serializable;

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
	static final long serialVersionUID = 6L;
	private ParallelCoordinatesChart chart;
	private boolean autoFit;
	private double max;
	private double min;
	private Parameter parameter;
	private int width;
	private int ticCount;
	private Color axisColor;
	private Color axisLabelFontColor;
	private Color ticLabelFontColor;
	private int axisLabelFontSize;
	private int ticLabelFontSize;
	private int ticLength;
	private boolean active = true;
	private Filter upperFilter;
	private Filter lowerFilter;
	private boolean filterInverted;
	private boolean axisInverted;
	public Axis(DataSheet dataSheet, ParallelCoordinatesChart chart, Parameter parameter) {
		this.chart = chart;
		this.parameter = parameter;
		initialiseSettings(dataSheet);
		if (this.autoFit) {
			autofit(dataSheet);
		}
	}

	private void initialiseSettings(DataSheet dataSheet) {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.width = userPreferences.getParallelCoordinatesAxisWidth();
		this.ticCount = userPreferences.getParallelCoordinatesAxisTicCount();
		this.axisColor = userPreferences.getParallelCoordinatesAxisColor();
		this.axisLabelFontColor = userPreferences.getParallelCoordinatesAxisLabelFontColor();
		this.ticLabelFontColor = userPreferences.getParallelCoordinatesAxisTicLabelFontColor();
		this.axisLabelFontSize = userPreferences.getParallelCoordinatesAxisLabelFontSize();
		this.ticLabelFontSize = userPreferences.getParallelCoordinatesAxisTicLabelFontSize();
		this.ticLength = userPreferences.getParallelCoordinatesAxisTicLength();
		this.filterInverted = userPreferences.isFilterInverted();
		this.axisInverted = userPreferences.isParallelCoordinatesAxisInverted();
		this.autoFit = userPreferences.isParallelCoordinatesAutoFitAxis();
		this.min = userPreferences.getParallelCoordinatesAxisDefaultMin();
		this.max = userPreferences.getParallelCoordinatesAxisDefaultMax();
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	void resetSettingsToDefault(DataSheet dataSheet) {
		initialiseSettings(dataSheet);
		autofit(dataSheet);
	}

	void addFilters(DataSheet dataSheet) {
		this.upperFilter = new Filter(dataSheet, this, Filter.UPPER_FILTER);
		this.lowerFilter = new Filter(dataSheet, this, Filter.LOWER_FILTER);
	}

	public void autofit(DataSheet dataSheet) {
		this.max = dataSheet.getMaxValueOf(this.parameter);
		this.min = dataSheet.getMinValueOf(this.parameter);
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public Color getAxisLabelFontColor() {
		return axisLabelFontColor;
	}

	public void setAxisLabelFontColor(Color axisLabelFontColor) {
		this.axisLabelFontColor = axisLabelFontColor;
	}

	public int getAxisLabelFontSize() {
		return axisLabelFontSize;
	}

	public void setAxisLabelFontSize(int axisLabelFontSize, DataSheet dataSheet) {
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
			axis.getLowerFilter().setValue(lowerFilterValues[i], dataSheet);
			axis.getUpperFilter().setValue(upperFilterValues[i], dataSheet);
		}
	}

	public double getMax(DataSheet dataSheet) {
		if (!this.parameter.isNumeric())
			return dataSheet.getMaxValueOf(this.parameter);
		else
			return max;
	}

	public void setMax(double max, DataSheet dataSheet) {
		this.max = max;
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public double getMin(DataSheet dataSheet) {
		if (!this.parameter.isNumeric())
			return dataSheet.getMinValueOf(this.parameter);
		else
			return min;
	}

	public void setMin(double min, DataSheet dataSheet) {
		this.min = min;
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public double getRange() {
		if (this.parameter.isNumeric())
			return max - min;
		else
			return this.parameter.getDiscreteLevelCount() - 1;
	}

	public int getTicCount() {
		if (this.parameter.isNumeric() && this.getRange() > 0)
			return ticCount;
		else if (this.parameter.isNumeric())
			return 1;
		else
			return this.parameter.getDiscreteLevelCount();
	}

	public void setTicCount(int ticCount, DataSheet dataSheet) {
		this.ticCount = ticCount;
		if (ticCount < 2) {
			this.applyFilters(dataSheet);
		}
	}

	public void setTicLabelDigitCount(int value) {
		this.parameter.setTicLabelDigitCount(value);
	}

	public int getTicLabelDigitCount(){
		return this.parameter.getTicLabelDigitCount();
	}

	public int getTicLabelFontSize() {
		return ticLabelFontSize;
	}

	public void setTicLabelFontSize(int ticLabelFontSize) {
		this.ticLabelFontSize = ticLabelFontSize;
	}

	public String getTicLabelFormat() {
        return this.parameter.getTicLabelFormat();
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

	public boolean isFilterInverted() {
		return filterInverted;
	}

	public void setFilterInverted(boolean filterInverted) {
		this.filterInverted = filterInverted;
	}

	public boolean isAxisInverted() {
		return this.axisInverted;
	}

	public void setAxisInverted(boolean axisInverted, DataSheet dataSheet) {
		double maxFilterValue = this.getMaximumFilter().getValue();
		double minFilterValue = this.getMinimumFilter().getValue();
		this.axisInverted = axisInverted;
		this.getMaximumFilter().setValue(maxFilterValue, dataSheet);
		this.getMinimumFilter().setValue(minFilterValue, dataSheet);
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

	public void setFilterAsNewRange(DataSheet dataSheet) {
		// log("setFilterAsNewRange: current range: "+this.getMin()+" and "+this.getMax()+" for axis "+this.getName());
		// log("setFilterAsNewRange:filterPositions: "+this.lowerFilter.getValue()+" and "+this.upperFilter.getValue()+" for axis "+this.getName());
		this.setAutoFit(false);
		double minFilterValue = this.getMinimumFilter().getValue();
		double maxFilterValue = this.getMaximumFilter().getValue();
		this.setMin(minFilterValue, dataSheet);
		this.setMax(maxFilterValue, dataSheet);
		// log("setFilterAsNewRange: new range set to "+this.getMin()+" and "+this.getMax()+" for axis "+this.getName());
		this.resetFilters(dataSheet);
	}

	public void applyFilters(DataSheet dataSheet) {
		this.upperFilter.apply(dataSheet);
		this.lowerFilter.apply(dataSheet);
	}

	public void resetFilters(DataSheet dataSheet) {
		this.upperFilter.reset(dataSheet);
		this.lowerFilter.reset(dataSheet);
	}

}
