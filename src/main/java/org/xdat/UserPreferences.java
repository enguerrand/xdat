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

package org.xdat;

import org.xdat.chart.ScatterPlot2D;
import org.xdat.settings.Key;

import java.awt.Color;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserPreferences {

    private static final UserPreferences INSTANCE = new UserPreferences();

	private static final Preferences PREFS = Preferences.userNodeForPackage(UserPreferences.class);

	public static final int IMPORT_FROM_HOMEDIR = 0;

	public static final int IMPORT_FROM_LASTDIR = 1;

	public static final int IMPORT_FROM_USERDIR = 2;

	public static final int LOCALE_US = 0;
	public static final int LOCALE_DE = 1;

    private UserPreferences() {
	}

	public static int getInt(String key, int defaultValue){
	    return PREFS.getInt(key, defaultValue);
    }

    public static void putInt(String key, int value) {
	    PREFS.putInt(key, value);
    }

    public static boolean getBoolean(String key, boolean defaultValue){
        return PREFS.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        PREFS.putBoolean(key, value);
    }

    public static double getDouble(String key, double defaultValue){
        return PREFS.getDouble(key, defaultValue);
    }

    public static void putDouble(String key, double value) {
        PREFS.putDouble(key, value);
    }

    public static String getString(String key, String defaultValue){
        return PREFS.get(key, defaultValue);
    }

    public static void putString(String key, String value) {
        PREFS.put(key, value);
    }

	public static Color getColor(String key, Color defaultValue) {
		int r = PREFS.getInt(key + "Red", defaultValue.getRed());
		int g = PREFS.getInt(key + "Green", defaultValue.getGreen());
		int b = PREFS.getInt(key + "Blue", defaultValue.getBlue());
		int a = PREFS.getInt(key + "Alpha", defaultValue.getAlpha());
		return new Color(r, g, b);
	}

	public static void putColor(String key, Color value) {
		PREFS.putInt(key + "Red", value.getRed());
		PREFS.putInt(key + "Green", value.getGreen());
		PREFS.putInt(key + "Blue", value.getBlue());
		PREFS.putInt(key + "Alpha", value.getAlpha());
	}

	public static UserPreferences getInstance() {
		return INSTANCE;
	}

	public int getScatterChart2DDisplayMode() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_DISPLAY_MODE, ScatterPlot2D.SHOW_ALL_DESIGNS);
	}

	public void setScatterChart2DDisplayMode(int ScatterChart2DDisplayMode) {
		PREFS.putInt(Key.SCATTER_CHART_2D_DISPLAY_MODE, ScatterChart2DDisplayMode);
	}

	public boolean isScatterChart2DAutofitX() {
		return PREFS.getBoolean(Key.SCATTER_CHART_2D_AUTOFIT_X, true);
	}

	public void setScatterChart2DAutofitX(boolean scatterChart2DAutofitX) {
		PREFS.putBoolean(Key.SCATTER_CHART_2D_AUTOFIT_X, scatterChart2DAutofitX);
	}

	public boolean isScatterChart2DAutofitY() {
		return PREFS.getBoolean(Key.SCATTER_CHART_2D_AUTOFIT_Y, true);
	}

	public void setScatterChart2DAutofitY(boolean scatterChart2DAutofitY) {
		PREFS.putBoolean(Key.SCATTER_CHART_2D_AUTOFIT_Y, scatterChart2DAutofitY);
	}


	public int getScatterChart2DAxisTitleFontsizeX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_X, 20);
	}

	public void setScatterChart2DAxisTitleFontsizeX(int scatterChart2DAxisTitleFontsizeX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_X, scatterChart2DAxisTitleFontsizeX);
	}

	public int getScatterChart2DAxisTitleFontsizeY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_Y, 20);
	}

	public void setScatterChart2DAxisTitleFontsizeY(int scatterChart2DAxisTitleFontsizeY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_Y, scatterChart2DAxisTitleFontsizeY);
	}

	public int getScatterChart2DTicCountX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_COUNT_X, 2);
	}

	public void setScatterChart2DTicCountX(int scatterChart2DTicCountX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_COUNT_X, scatterChart2DTicCountX);
	}

	public int getScatterChart2DTicCountY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_COUNT_Y, 2);
	}

	public void setScatterChart2DTicCountY(int scatterChart2DTicCountY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_COUNT_Y, scatterChart2DTicCountY);
	}

	public int getScatterChart2DTicLabelFontsizeX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_X, 12);
	}

	public void setScatterChart2DTicLabelFontsizeX(int scatterChart2DTicLabelFontsizeX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_X, scatterChart2DTicLabelFontsizeX);
	}

	public int getScatterChart2DTicLabelFontsizeY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_Y, 12);
	}

	public void setScatterChart2DTicLabelFontsizeY(int scatterChart2DTicLabelFontsizeY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_Y, scatterChart2DTicLabelFontsizeY);
	}

	public int getScatterChart2DDataPointSize() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_DATA_POINT_SIZE, 3);
	}

	public void setScatterChart2DDataPointSize(int scatterChart2DDataPointSize) {
		PREFS.putInt(Key.SCATTER_CHART_2D_DATA_POINT_SIZE, scatterChart2DDataPointSize);
	}

	public Color getScatterChart2DForegroundColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DForegroundColor(Color scatterChart2DForegroundColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Red", scatterChart2DForegroundColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Green", scatterChart2DForegroundColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR + "Blue", scatterChart2DForegroundColor.getBlue());
	}

	public Color getScatterChart2DBackgroundColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Red", 255);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Green", 255);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Blue", 255);
		return new Color(r, g, b);
	}

	public void setScatterChart2DBackgroundColor(Color scatterChart2DBackgroundColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Red", scatterChart2DBackgroundColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Green", scatterChart2DBackgroundColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR + "Blue", scatterChart2DBackgroundColor.getBlue());
	}

	public Color getScatterChart2DActiveDesignColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Green", 150);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DActiveDesignColor(Color scatterChart2DActiveDesignColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Red", scatterChart2DActiveDesignColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Green", scatterChart2DActiveDesignColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR + "Blue", scatterChart2DActiveDesignColor.getBlue());
	}

	public Color getScatterChart2DSelectedDesignColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Blue", 150);
		return new Color(r, g, b);
	}

	public void setScatterChart2DSelectedDesignColor(Color scatterChart2DSelectedDesignColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Red", scatterChart2DSelectedDesignColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Green", scatterChart2DSelectedDesignColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR + "Blue", scatterChart2DSelectedDesignColor.getBlue());
	}

	public Color getParallelCoordinatesAxisColor() {
		int r = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisColor(Color axisColor) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Red", axisColor.getRed());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Green", axisColor.getGreen());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_COLOR + "Blue", axisColor.getBlue());
	}

	public boolean isParallelCoordinatesVerticallyOffsetAxisLabels() {
		return PREFS.getBoolean(Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS, true);
	}

	public void setParallelCoordinatesVerticallyOffsetAxisLabels(boolean verticallyOffsetAxisLabels) {
		PREFS.putBoolean(Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS, verticallyOffsetAxisLabels);
	}

	public Color getParallelCoordinatesAxisLabelFontColor() {
		int r = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisLabelFontColor(Color axisLabelFontColor) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Red", axisLabelFontColor.getRed());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Green", axisLabelFontColor.getGreen());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR + "Blue", axisLabelFontColor.getBlue());
	}

	public int getParallelCoordinatesAxisLabelFontSize() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_SIZE, 20);
	}

	public void setParallelCoordinatesAxisLabelFontSize(int axisLabelFontSize) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_SIZE1, axisLabelFontSize);
	}

	public int getParallelCoordinatesAxisTicCount() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_COUNT, 11);
	}

	public void setParallelCoordinatesAxisTicCount(int axisTicCount) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_COUNT1, axisTicCount);
	}

	public Color getParallelCoordinatesAxisTicLabelFontColor() {
		int r = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisTicLabelFontColor(Color axisTicLabelFontColor) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Red", axisTicLabelFontColor.getRed());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Green", axisTicLabelFontColor.getGreen());
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR + "Blue", axisTicLabelFontColor.getBlue());
	}

	public String getParallelCoordinatesAxisTicLabelFormat() {
		return PREFS.get(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FORMAT, "%4.3f");
	}

	public void setParallelCoordinatesAxisTicLabelFormat(String axisTicLabelFormat) {
		PREFS.put(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FORMAT1, axisTicLabelFormat);
	}

	public int getParallelCoordinatesAxisTicLabelDigitCount(){
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH_TIC_LABEL_DIGIT_COUNT, 3);
	}

	public void setParallelCoordinatesAxisTicLabelDigitCount(int count){
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH_TIC_LABEL_DIGIT_COUNT, count);
	}

	public int getParallelCoordinatesAxisTicLength() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH, 4);
	}

	public void setParallelCoordinatesAxisTicLength(int axisTicLength) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH, axisTicLength);
	}

	/**
	 * Gets the width in pixels that is used by one axis on parallel coordinate
	 * charts. This setting is used to define the axis spacing. The distance of
	 * two axes is defined by the sum of their respective widths, divided by
	 * two.
	 * 
	 * @return the axis width
	 */
	public int getParallelCoordinatesAxisWidth() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_WIDTH, 200);
	}

	public void setParallelCoordinatesAxisWidth(int axisWidth) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_AXIS_WIDTH, axisWidth);
	}

	public Color getParallelCoordinatesFilterDefaultColor() {
		int r = PREFS.getInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Red", 255);
		int g = PREFS.getInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesFilterColor(Color filterColor) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Red", filterColor.getRed());
		PREFS.putInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Green", filterColor.getGreen());
		PREFS.putInt(Key.PARALLEL_COORDINATES_FILTER_COLOR + "Blue", filterColor.getBlue());
	}

	public int getParallelCoordinatesFilterHeight() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_FILTER_HEIGHT, 10);
	}

	public void setParallelCoordinatesFilterHeight(int filterHeight) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_FILTER_HEIGHT, filterHeight);
	}

	/**
	 * Gets the width of one half triangle that represents a filter in pixels.
	 * In other words, the filter triangle will be twice as large as the value
	 * entered here.
	 * 
	 * @return the filter width
	 */
	public int getParallelCoordinatesFilterWidth() {
		return PREFS.getInt(Key.PARALLEL_COORDINATES_FILTER_WIDTH, 7);
	}

	public void setParallelCoordinatesFilterWidth(int filterWidth) {
		PREFS.putInt(Key.PARALLEL_COORDINATES_FILTER_WIDTH, filterWidth);
	}

	public int getParallelCoordinatesAxisTicLabelFontSize() {
		return PREFS.getInt(Key.TIC_LABEL_FONT_SIZE, 10);
	}

	public void setParallelCoordinatesAxisTicLabelFontSize(int ticLabelFontSize) {
		PREFS.putInt(Key.TIC_LABEL_FONT_SIZE, ticLabelFontSize);
	}

	public int getParallelCoordinatesDesignLabelFontSize() {
		return PREFS.getInt(Key.DESIGN_LABEL_FONT_SIZE, 10);
	}

	public void setParallelCoordinatesDesignLabelFontSize(int designLabelFontSize) {
		PREFS.putInt(Key.DESIGN_LABEL_FONT_SIZE, designLabelFontSize);
	}

	public int getParallelCoordinatesLineThickness() {
		return PREFS.getInt(Key.LINE_THICKNESS, 1);
	}

	public void setParallelCoordinatesLineThickness(int lineThickness) {
		PREFS.putInt(Key.LINE_THICKNESS, lineThickness);
	}

	public int getParallelCoordinatesSelectedDesignLineThickness() {
		return PREFS.getInt(Key.SELECTED_DESIGN_LINE_THICKNESS, 2);
	}

	public void setParallelCoordinatesSelectedDesignLineThickness(int lineThickness) {
		PREFS.putInt(Key.SELECTED_DESIGN_LINE_THICKNESS, lineThickness);
	}

	public boolean isParallelCoordinatesShowFilteredDesigns() {
		return PREFS.getBoolean(Key.SHOW_FILTERED_DESIGNS, false);
	}

	public void setParallelCoordinatesShowFilteredDesigns(boolean showFilteredDesigns) {
		PREFS.putBoolean(Key.SHOW_FILTERED_DESIGNS, showFilteredDesigns);
	}

	public boolean isParallelCoordinatesShowOnlySelectedDesigns() {
		return PREFS.getBoolean(Key.PARALLEL_COORDINATES_SHOW_ONLY_SELECTED_DESIGNS, false);
	}

	public void setParallelCoordinatesShowOnlySelectedDesigns(boolean showOnlySelectedDesigns) {
		PREFS.putBoolean(Key.PARALLEL_COORDINATES_SHOW_ONLY_SELECTED_DESIGNS, showOnlySelectedDesigns);
	}

	public Color getParallelCoordinatesActiveDesignDefaultColor() {
		int r = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Green", 150);
		int b = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Blue", 0);
		int a = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Alpha", 128);
		return new Color(r, g, b, a);
	}

	public void setParallelCoordinatesActiveDesignDefaultColor(Color activeDesignDefaultColor) {
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Red", activeDesignDefaultColor.getRed());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Green", activeDesignDefaultColor.getGreen());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Blue", activeDesignDefaultColor.getBlue());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR + "Alpha", activeDesignDefaultColor.getAlpha());
	}

	public void setParallelCoordinatesSelectedDesignDefaultColor(Color selectedDesignDefaultColor) {
		PREFS.putInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Red", selectedDesignDefaultColor.getRed());
		PREFS.putInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Green", selectedDesignDefaultColor.getGreen());
		PREFS.putInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Blue", selectedDesignDefaultColor.getBlue());
	}

	public Color getParallelCoordinatesSelectedDesignDefaultColor() {
		int r = PREFS.getInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Red", 0);
		int g = PREFS.getInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Green", 0);
		int b = PREFS.getInt(Key.SELECTED_DESIGN_DEFAULT_COLOR + "Blue", 255);
		return new Color(r, g, b);
	}

	public Color getParallelCoordinatesFilteredDesignDefaultColor() {
		int r = PREFS.getInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Red", 200);
		int g = PREFS.getInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Green", 200);
		int b = PREFS.getInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Blue", 200);
		int a = PREFS.getInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Alpha", 100);
		return new Color(r, g, b, a);
	}

	public void setParallelCoordinatesInactiveDesignDefaultColor(Color inActiveDesignDefaultColor) {
		PREFS.putInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Red", inActiveDesignDefaultColor.getRed());
		PREFS.putInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Green", inActiveDesignDefaultColor.getGreen());
		PREFS.putInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Blue", inActiveDesignDefaultColor.getBlue());
		PREFS.putInt(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR + "Alpha", inActiveDesignDefaultColor.getAlpha());
	}

	public boolean isParallelCoordinatesShowDesignIDs() {
		return PREFS.getBoolean(Key.SHOW_DESIGN_IDS, true);
	}

	public void setParallelCoordinatesShowDesignIDs(boolean showDesignIDs) {
		PREFS.putBoolean(Key.SHOW_DESIGN_IDS, showDesignIDs);
	}
	
	public boolean isAntiAliasing() {
		return PREFS.getBoolean(Key.ANTI_ALIASING, true);
	}
	
	public void setAntiAliasing(boolean antiAliasing) {
		PREFS.putBoolean(Key.ANTI_ALIASING, antiAliasing);
	}
	
	public boolean isUseAlpha() {
		return PREFS.getBoolean(Key.USE_ALPHA, false);
	}

	
	public void setUseAlpha(boolean useAlpha) {
		PREFS.putBoolean(Key.USE_ALPHA, useAlpha);
	}

	public int getParallelCoordinatesDesignIDFontSize() {
		return PREFS.getInt(Key.DESIGN_ID_FONT_SIZE, 10);
	}

	public void setParallelCoordinatesDesignIDFontSize(int designIDFontSize) {
		PREFS.putInt(Key.DESIGN_ID_FONT_SIZE, designIDFontSize);
	}

	public Color getParallelCoordinatesDefaultBackgroundColor() {
		int r = PREFS.getInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Red", 255);
		int g = PREFS.getInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Green", 255);
		int b = PREFS.getInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Blue", 255);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesDefaultBackgroundColor(Color backgroundColor) {
		PREFS.putInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Red", backgroundColor.getRed());
		PREFS.putInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Green", backgroundColor.getGreen());
		PREFS.putInt(Key.PARALLEL_CHART_BACKGROUND_COLOR + "Blue", backgroundColor.getBlue());
	}

	public boolean isFilterInverted() {
		return PREFS.getBoolean(Key.PARALLEL_COORDINATES_FILTER_INVERTED, false);
	}

	public void setFilterInverted(boolean filterInverted) {
		PREFS.putBoolean(Key.PARALLEL_COORDINATES_FILTER_INVERTED, filterInverted);
	}

	public boolean isParallelCoordinatesAxisInverted() {
		return PREFS.getBoolean(Key.PARALLEL_COORDINATES_AXIS_INVERTED, false);
	}

	public void setParallelCoordinatesAxisInverted(boolean axisInverted) {
		PREFS.putBoolean(Key.PARALLEL_COORDINATES_AXIS_INVERTED, axisInverted);
	}

	public boolean isParallelCoordinatesAutoFitAxis() {
		return PREFS.getBoolean(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS, true);
	}

	public void setParallelCoordinatesAutoFitAxis(boolean autoFitAxis) {
		PREFS.putBoolean(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS, autoFitAxis);
	}

	public double getParallelCoordinatesAxisDefaultMin() {
		return PREFS.getDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN, -10);
	}

	public void setParallelCoordinatesAxisDefaultMin(double axisDefaultMin) {
		PREFS.putDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN, axisDefaultMin);
	}

	public double getParallelCoordinatesAxisDefaultMax() {
		return PREFS.getDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX, 10.0);
	}

	public void setParallelCoordinatesAxisDefaultMax(double axisDefaultMax) {
		PREFS.putDouble(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX, axisDefaultMax);
	}

	public int getDirToImportFrom() {
		return PREFS.getInt(Key.DIRECTORY_TO_IMPORT_FROM, IMPORT_FROM_LASTDIR);
	}

	public void setDirToImportFrom(int dirToImportFrom) {
		PREFS.putInt(Key.DIRECTORY_TO_IMPORT_FROM, dirToImportFrom);
	}

	public String getLastFile() {
		return PREFS.get(Key.LAST_FILE_BROWSING_DIRECTORY, System.getProperty("user.home"));
	}

	public void setLastFile(String lastFileBrowsingDirectory) {
		PREFS.put(Key.LAST_FILE_BROWSING_DIRECTORY, lastFileBrowsingDirectory);
	}

	public String getHomeDir() {
		return System.getProperty("user.home");
	}

	public String getUserDir() {
		return PREFS.get(Key.USER_DIR, getHomeDir());
	}

	public void setUserDir(String userDir) {
		PREFS.put(Key.USER_DIR, userDir);
	}

	public String getCurrentDir() {
		switch (this.getDirToImportFrom()) {
			case (IMPORT_FROM_HOMEDIR): {
				return this.getHomeDir();
			}
			case (IMPORT_FROM_LASTDIR): {
				return this.getLastFile();
			}
			case (IMPORT_FROM_USERDIR): {
				return this.getUserDir();
			}
			default: {
				return this.getHomeDir();
			}
		}

	}

	public String getDelimiter() {
		return PREFS.get(Key.DELIMITER, "\\s");
	}

	public void setDelimiter(String delimiter) {
		PREFS.put(Key.DELIMITER, delimiter);
	}

	public boolean isTreatConsecutiveAsOne() {
		return PREFS.getBoolean(Key.TREAT_CONSECUTIVE_AS_ONE, true);
	}

	public void setTreatConsecutiveAsOne(boolean treatConsecutiveAsOne) {
		PREFS.putBoolean(Key.TREAT_CONSECUTIVE_AS_ONE, treatConsecutiveAsOne);
	}

	public String getOtherDelimiter() {
		return PREFS.get(Key.OTHER_DELIMITER, "");
	}

	public void setOtherDelimiter(String otherDelimiter) {
		PREFS.put(Key.OTHER_DELIMITER, otherDelimiter);
	}

	public Locale getLocale() {
		int locale = PREFS.getInt(Key.LOCALE, LOCALE_US);
		switch (locale) {
			case (LOCALE_DE): {
				return Locale.GERMANY;
			}
			default: {
				return Locale.US;
			}
		}
	}

	public void setLocale(int locale) {
		PREFS.putInt(Key.LOCALE, locale);
	}

	public void resetToDefault() {
		try {
			PREFS.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
