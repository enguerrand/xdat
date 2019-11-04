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
import org.xdat.data.AxisType;
import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.data.Parameter;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * A serializable representation of all relevant settings for a two-dimensional
 * scatter chart which is displayed on a ChartFrame.
 * 
 * @see org.xdat.gui.frames.ChartFrame
 */
public class ScatterPlot2D extends Plot {
	static final long serialVersionUID = 4;
	public static final int SHOW_ALL_DESIGNS = 0;
	public static final int SHOW_SELECTED_DESIGNS = 1;
	public static final int SHOW_DESIGNS_ACTIVE_IN_PARALLEL_CHART = 2;
	public static final int AXIS_LABEL_PADDING = 10;
	public static final int TIC_LABEL_PADDING = 5;
	public static final String TIC_LABEL_FORMAT = "%4.3f";
	private int displayedDesignSelectionMode = SHOW_ALL_DESIGNS;
	private ParallelCoordinatesChart parallelCoordinatesChartForFiltering;
	private int dotRadius = 4;
	private Color activeDesignColor = new Color(0, 150, 0);
	private Color selectedDesignColor = Color.BLUE;
	private Parameter parameterForXAxis;
	private Parameter parameterForYAxis;
	private boolean showDecorations;
	private Color decorationsColor = Color.BLACK;
	private boolean autofitX = true;
	private boolean autofitY = true;
	private int ticCountX = 2;
	private int ticCountY = 2;
	private int ticSize = 5;
	private final Map<String, Double> minValues = new HashMap<>();
	private final Map<String, Double> maxValues = new HashMap<>();
	private int axisLabelFontSizeX = 20;
	private int axisLabelFontSizeY = 20;
	private int ticLabelFontSizeX = 12;
	private int ticLabelFontSizeY = 12;

	ScatterPlot2D(DataSheet dataSheet, boolean showDecorations) {
		super();
		this.showDecorations = showDecorations;
		if (dataSheet.getParameterCount() > 1) {
			this.parameterForXAxis = dataSheet.getParameter(0);
			this.parameterForYAxis = dataSheet.getParameter(1);
		} else if (dataSheet.getParameterCount() > 0) {
			this.parameterForXAxis = dataSheet.getParameter(0);
			this.parameterForYAxis = dataSheet.getParameter(0);
		}
		for (int i = 0; i < dataSheet.getParameterCount(); i++) {
			autofitParam(dataSheet.getParameter(i));
		}

		resetDisplaySettingsToDefault();
	}

	public Color getDesignColor(Design design) {
		if (design.getCluster() != null) {
			return design.getCluster().getActiveDesignColor(false);
		} else {
			return activeDesignColor;
		}
	}

	public Color getActiveDesignColor() {
		return activeDesignColor;
	}

	public void setActiveDesignColor(Color activeDesignColor) {
		this.activeDesignColor = activeDesignColor;
	}

	public int getDisplayedDesignSelectionMode() {
		return displayedDesignSelectionMode;
	}

	public void setDisplayedDesignSelectionMode(int displayedDesignSelectionMode) {
		this.displayedDesignSelectionMode = displayedDesignSelectionMode;
	}

	public ParallelCoordinatesChart getParallelCoordinatesChartForFiltering() {
		return parallelCoordinatesChartForFiltering;
	}

	public void setParallelCoordinatesChartForFiltering(ParallelCoordinatesChart parallelCoordinatesChartForFiltering) {
		this.parallelCoordinatesChartForFiltering = parallelCoordinatesChartForFiltering;
	}

	public int getDotRadius() {
		return dotRadius;
	}

	public void setDotRadius(int dotRadius) {
		this.dotRadius = dotRadius;
	}

	public Color getSelectedDesignColor() {
		return selectedDesignColor;
	}

	public void setSelectedDesignColor(Color selectedDesignColor) {
		this.selectedDesignColor = selectedDesignColor;
	}

    public Parameter getParameterForAxis(AxisType axisType) {
		switch (axisType) {
			case X:
				return parameterForXAxis;
			case Y:
				return parameterForYAxis;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public void setParameterForAxis(AxisType axisType, Parameter parameter) {
		switch (axisType) {
			case X:
				this.parameterForXAxis = parameter;
				break;
			case Y:
                this.parameterForYAxis = parameter;
                break;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public Color getDecorationsColor() {
		return decorationsColor;
	}

	public void setDecorationsColor(Color decorationsColor) {
		this.decorationsColor = decorationsColor;
	}

	public boolean isAutofit(AxisType axisType) {
		switch (axisType) {
			case X: return autofitX;
			case Y: return autofitY;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public void setAutofit(AxisType axisType, boolean autofit) {
		switch (axisType) {
			case X:
				this.autofitX = autofit;
				break;
			case Y:
				this.autofitY = autofit;
				break;
			default:
				throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public void autofit(AxisType axisType) {
		Parameter parameter = getParameterForAxis(axisType);
		autofitParam(parameter);
	}

	private void autofitParam(Parameter parameter) {
		setMin(parameter, parameter.getMinValue());
		setMax(parameter, parameter.getMaxValue());
	}

	public void setMin(AxisType axisType, double value) {
        Parameter parameter = getParameterForAxis(axisType);
		setMin(parameter, value);
	}

	private void setMin(Parameter parameter, double value) {
		this.minValues.put(parameter.getName(), value);
	}

	public double getMin(AxisType axisType) {
        Parameter parameter = getParameterForAxis(axisType);
        return this.minValues.get(parameter.getName());
	}

	public void setMax(AxisType axisType, double value) {
        Parameter parameter = getParameterForAxis(axisType);
		setMax(parameter, value);
	}

	private void setMax(Parameter parameter, double value) {
		this.maxValues.put(parameter.getName(), value);
	}

	public double getMax(AxisType axisType) {
        Parameter parameter = getParameterForAxis(axisType);
		return this.maxValues.get(parameter.getName());
	}

	public int getTicSize() {
		return ticSize;
	}


	public void setTicCount(AxisType axisType, int value) {
		switch (axisType) {
			case X:
				this.ticCountX = value;
				break;
			case Y:
				this.ticCountY = value;
				break;
			default:
				throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public int getTicCount(AxisType axisType) {
		switch (axisType) {
			case X:
				return ticCountX;
			case Y:
				return ticCountY;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public void setAxisLabelFontSize(AxisType axisType, int value) {
		switch (axisType) {
			case X:
				this.axisLabelFontSizeX = value;
				break;
			case Y:
				this.axisLabelFontSizeY = value;
				break;
			default:
				throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public int getAxisLabelFontSize(AxisType axisType) {
		switch (axisType) {
			case X:
				return axisLabelFontSizeX;
			case Y:
				return axisLabelFontSizeY;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public void setTicLabelFontSize(AxisType axisType, int value) {
		switch (axisType) {
			case X:
				this.ticLabelFontSizeX = value;
				break;
			case Y:
				this.ticLabelFontSizeY = value;
				break;
			default:
				throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public int getTicLabelFontSize(AxisType axisType) {
		switch (axisType) {
			case X:
				return ticLabelFontSizeX;
			case Y:
				return ticLabelFontSizeY;
			default: throw new IllegalArgumentException("Unknown axis type "+axisType);
		}
	}

	public int getPlotAreaDistanceToLeft(int ticLabelOffset) {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + this.axisLabelFontSizeY + 2 * AXIS_LABEL_PADDING + 2 * TIC_LABEL_PADDING + ticLabelOffset;
		}
		return distance;
	}

	public int getPlotAreaDistanceToRight() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + 2 * AXIS_LABEL_PADDING;
		}
		return distance;
	}

	public int getPlotAreaDistanceToTop() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + 2 * AXIS_LABEL_PADDING;
		}
		return distance;
	}

	public int getPlotAreaDistanceToBottom() {
		int distance = this.getMargin();
		if (this.showDecorations) {
			distance = distance + this.axisLabelFontSizeX + 2 * AXIS_LABEL_PADDING + this.ticLabelFontSizeX + 2 * TIC_LABEL_PADDING;
		}
		return distance;
	}

	public boolean isShowDecorations() {
		return showDecorations;
	}

	public void resetDisplaySettingsToDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.setDisplayedDesignSelectionMode(userPreferences.getScatterChart2DDisplayMode());
		this.autofitX = userPreferences.isScatterChart2DAutofitX();
		this.autofitY = userPreferences.isScatterChart2DAutofitY();
		this.axisLabelFontSizeX = userPreferences.getScatterChart2DAxisTitleFontsizeX();
		this.axisLabelFontSizeY = userPreferences.getScatterChart2DAxisTitleFontsizeY();
		this.ticCountX = userPreferences.getScatterChart2DTicCountX();
		this.ticCountY = userPreferences.getScatterChart2DTicCountY();
		this.ticLabelFontSizeX = userPreferences.getScatterChart2DTicLabelFontsizeX();
		this.ticLabelFontSizeY = userPreferences.getScatterChart2DTicLabelFontsizeY();
		this.setDotRadius(userPreferences.getScatterChart2DDataPointSize());
		this.setDecorationsColor(userPreferences.getScatterChart2DForegroundColor());
		this.setBackGroundColor(userPreferences.getScatterChart2DBackgroundColor());
		this.setActiveDesignColor(userPreferences.getScatterChart2DActiveDesignColor());
		this.setSelectedDesignColor(userPreferences.getScatterChart2DSelectedDesignColor());
	}
}
