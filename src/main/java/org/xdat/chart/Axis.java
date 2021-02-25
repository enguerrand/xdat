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

import org.xdat.data.DataSheet;
import org.xdat.data.Parameter;
import org.xdat.settings.IntegerSetting;
import org.xdat.settings.Key;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsGroup;
import org.xdat.settings.SettingsGroupFactory;

import java.awt.Color;
import java.io.Serializable;

/**
 * A serializable representation of all relevant settings for an Axis on a
 * Parallel coordinates chart.
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
	static final long serialVersionUID = 7L;
	private ParallelCoordinatesChart chart;
	private Parameter parameter;
	private Filter upperFilter;
	private Filter lowerFilter;
	private final SettingsGroup settings;

	public Axis(DataSheet dataSheet, ParallelCoordinatesChart chart, Parameter parameter) {
		this.settings = SettingsGroupFactory.buildParallelCoordinatesChartAxisSettingsGroup();
		this.chart = chart;
		this.parameter = parameter;
		initialiseSettings(dataSheet);
		if (this.settings.getBoolean(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS)) {
			autofit(dataSheet);
		}
		IntegerSetting ticLabelDigitCountSetting = this.settings.getIntegerSetting(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_DIGIT_COUNT);
		ticLabelDigitCountSetting.set(parameter.getTicLabelDigitCount());
		ticLabelDigitCountSetting.addListener((source, transaction) -> parameter.setTicLabelDigitCount(source.get()));
		this.settings.getBooleanSetting(Key.PARALLEL_COORDINATES_AXIS_INVERTED).addListener((source, transaction) ->
				onAxisInverted(dataSheet)
		);

		this.settings.getBooleanSetting(Key.PARALLEL_COORDINATES_FILTER_INVERTED).addListener((source, transaction) ->
				applyFilters(dataSheet)
		);

	}

	private void initialiseSettings(DataSheet dataSheet) {
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
		this.setMax(dataSheet.getMaxValueOf(this.parameter), dataSheet);
		this.setMin(dataSheet.getMinValueOf(this.parameter), dataSheet);
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public Color getAxisLabelFontColor() {
		return this.settings.getColor(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR);
	}

	public int getAxisLabelFontSize() {
		return this.settings.getInteger(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_SIZE);
	}

	public double getMax() {
		if (!this.parameter.isNumeric())
			return this.parameter.getDiscreteLevelCount() - 1;
		else
			return this.settings.getDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX);
	}

	public void setMax(double max, DataSheet dataSheet) {
		this.settings.getDoubleSetting(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX).set(max);
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public double getMin() {
		if (!this.parameter.isNumeric())
			return 0.0;
		else
			return this.settings.getDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN);
	}

	public void setMin(double min, DataSheet dataSheet) {
		this.settings.getDoubleSetting(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN).set(min);
		dataSheet.evaluateBoundsForAllDesigns(this.chart);
	}

	public double getRange() {
		if (this.parameter.isNumeric())
			return getMax() - getMin();
		else
			return this.parameter.getDiscreteLevelCount() - 1;
	}

	public int getTicCount() {
		if (this.parameter.isNumeric() && this.getRange() > 0)
			return this.settings.getInteger(Key.PARALLEL_COORDINATES_AXIS_TIC_COUNT);
		else if (this.parameter.isNumeric())
			return 1;
		else
			return this.parameter.getDiscreteLevelCount();
	}

	public void setTicCount(int ticCount, DataSheet dataSheet) {
		this.settings.getIntegerSetting(Key.PARALLEL_COORDINATES_AXIS_TIC_COUNT).set(ticCount);
		if (ticCount < 2) {
			this.applyFilters(dataSheet);
		}
	}

	public int getTicLabelFontSize() {
		return settings.getInteger(Key.TIC_LABEL_FONT_SIZE);
	}

	public String getTicLabelFormat() {
        return this.parameter.getTicLabelFormat();
	}

	public int getTicLength() {
		return settings.getInteger(Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH);
	}

	public int getWidth() {
		return settings.getInteger(Key.PARALLEL_COORDINATES_AXIS_WIDTH);
	}

	public void setWidth(int width) {
		this.getSettings().getIntegerSetting(Key.PARALLEL_COORDINATES_AXIS_WIDTH).set(width);
	}

	public boolean isActive() {
		return settings.getBoolean(Key.PARALLEL_COORDINATES_AXIS_ACTIVE);
	}

	public void setActive(boolean active) {
		this.settings.getBooleanSetting(Key.PARALLEL_COORDINATES_AXIS_ACTIVE).set(active);
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public Color getAxisColor() {
		return this.settings.getColor(Key.PARALLEL_COORDINATES_AXIS_COLOR);
	}

	public Color getAxisTicLabelFontColor() {
		return this.settings.getColor(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR);
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
		return this.settings.getBoolean(Key.PARALLEL_COORDINATES_FILTER_INVERTED);
	}

	public boolean isAxisInverted() {
		return this.settings.getBoolean(Key.PARALLEL_COORDINATES_AXIS_INVERTED);
	}

	public void setAxisInverted(boolean axisInverted, DataSheet dataSheet) {
		this.settings.getBooleanSetting(Key.PARALLEL_COORDINATES_AXIS_INVERTED).set(axisInverted);
		onAxisInverted(dataSheet);
	}

	private void onAxisInverted(DataSheet dataSheet) {
		double maxFilterValue = this.getMaximumFilter().getValue();
		double minFilterValue = this.getMinimumFilter().getValue();
		this.getMaximumFilter().setValue(minFilterValue, dataSheet);
		this.getMinimumFilter().setValue(maxFilterValue, dataSheet);
	}

	public boolean isAutoFit() {
		if (this.parameter.isNumeric()) {
			return this.settings.getBoolean(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS);
		} else {
			return true;
		}
	}

	public void setAutoFit(boolean autoFit) {
		this.settings.getBooleanSetting(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS).set(autoFit);
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

	public void initTransientData(){
		this.settings.getSettings().values().forEach(Setting::initTransientData);
	}

	public SettingsGroup getSettings() {
		return this.settings;
	}

	@Override
	public String toString() {
		return parameter.getName();
	}
}
