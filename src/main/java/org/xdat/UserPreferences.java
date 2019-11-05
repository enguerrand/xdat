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

import java.awt.Color;
import java.util.Locale;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class UserPreferences {
	
	private static final UserPreferences INSTANCE = new UserPreferences();

	private Preferences prefs;

	/**
	 * Current Release Version to be able to store version-specific boolean
	 * value for the click-wrap license.
	 **/
	private String versionString;

	// File Options
	/** Open file import browse dialog in the user's home directory by default. */
	public static final int IMPORT_FROM_HOMEDIR = 0;

	/** Open file import browse dialog in the last opened directory by default. */
	public static final int IMPORT_FROM_LASTDIR = 1;

	/** Open file import browse dialog in a userspecified directory by default. */
	public static final int IMPORT_FROM_USERDIR = 2;

	public static final int LOCALE_US = 0;
	public static final int LOCALE_DE = 1;

	private UserPreferences() {
		this.prefs = Preferences.userNodeForPackage(getClass());
	}
	
	public static UserPreferences getInstance() {
		return INSTANCE;
	}

	public boolean isLicenseAccepted() {
		return this.prefs.getBoolean("version" + this.versionString + "licenseAcceptedBy" + System.getProperty("user.name"), false);
	}

	public void setLicenseAccepted(boolean licenseAccepted) {
		this.prefs.putBoolean("version" + this.versionString + "licenseAcceptedBy" + System.getProperty("user.name"), licenseAccepted);
	}

	public int getScatterChart2DDisplayMode() {
		return this.prefs.getInt("ScatterChart2DDisplayMode", ScatterPlot2D.SHOW_ALL_DESIGNS);
	}

	public void setScatterChart2DDisplayMode(int ScatterChart2DDisplayMode) {
		this.prefs.putInt("ScatterChart2DDisplayMode", ScatterChart2DDisplayMode);
	}

	public boolean isScatterChart2DAutofitX() {
		return this.prefs.getBoolean("scatterChart2DAutofitX", true);
	}

	public void setScatterChart2DAutofitX(boolean scatterChart2DAutofitX) {
		this.prefs.putBoolean("scatterChart2DAutofitX", scatterChart2DAutofitX);
	}

	public boolean isScatterChart2DAutofitY() {
		return this.prefs.getBoolean("scatterChart2DAutofitY", true);
	}

	public void setScatterChart2DAutofitY(boolean scatterChart2DAutofitY) {
		this.prefs.putBoolean("scatterChart2DAutofitY", scatterChart2DAutofitY);
	}

	public int getScatterChart2DAxisTitleFontsizeX() {
		return this.prefs.getInt("scatterChart2DAxisTitleFontsizeX", 20);
	}

	public void setScatterChart2DAxisTitleFontsizeX(int scatterChart2DAxisTitleFontsizeX) {
		this.prefs.putInt("scatterChart2DAxisTitleFontsizeX", scatterChart2DAxisTitleFontsizeX);
	}

	public int getScatterChart2DAxisTitleFontsizeY() {
		return this.prefs.getInt("scatterChart2DAxisTitleFontsizeY", 20);
	}

	public void setScatterChart2DAxisTitleFontsizeY(int scatterChart2DAxisTitleFontsizeY) {
		this.prefs.putInt("scatterChart2DAxisTitleFontsizeY", scatterChart2DAxisTitleFontsizeY);
	}

	public int getScatterChart2DTicCountX() {
		return this.prefs.getInt("scatterChart2DTicCountX", 2);
	}

	public void setScatterChart2DTicCountX(int scatterChart2DTicCountX) {
		this.prefs.putInt("scatterChart2DTicCountX", scatterChart2DTicCountX);
	}

	public int getScatterChart2DTicCountY() {
		return this.prefs.getInt("scatterChart2DTicCountY", 2);
	}

	public void setScatterChart2DTicCountY(int scatterChart2DTicCountY) {
		this.prefs.putInt("scatterChart2DTicCountY", scatterChart2DTicCountY);
	}

	public int getScatterChart2DTicLabelFontsizeX() {
		return this.prefs.getInt("scatterChart2DTicLabelFontsizeX", 12);
	}

	public void setScatterChart2DTicLabelFontsizeX(int scatterChart2DTicLabelFontsizeX) {
		this.prefs.putInt("scatterChart2DTicLabelFontsizeX", scatterChart2DTicLabelFontsizeX);
	}

	public int getScatterChart2DTicLabelFontsizeY() {
		return this.prefs.getInt("scatterChart2DTicLabelFontsizeY", 12);
	}

	public void setScatterChart2DTicLabelFontsizeY(int scatterChart2DTicLabelFontsizeY) {
		this.prefs.putInt("scatterChart2DTicLabelFontsizeY", scatterChart2DTicLabelFontsizeY);
	}

	public int getScatterChart2DDataPointSize() {
		return this.prefs.getInt("scatterChart2DDataPointSize", 3);
	}

	public void setScatterChart2DDataPointSize(int scatterChart2DDataPointSize) {
		this.prefs.putInt("scatterChart2DDataPointSize", scatterChart2DDataPointSize);
	}

	public Color getScatterChart2DForegroundColor() {
		int r = this.prefs.getInt("scatterChart2DForegroundColorRed", 0);
		int g = this.prefs.getInt("scatterChart2DForegroundColorGreen", 0);
		int b = this.prefs.getInt("scatterChart2DForegroundColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DForegroundColor(Color scatterChart2DForegroundColor) {
		this.prefs.putInt("scatterChart2DForegroundColorRed", scatterChart2DForegroundColor.getRed());
		this.prefs.putInt("scatterChart2DForegroundColorGreen", scatterChart2DForegroundColor.getGreen());
		this.prefs.putInt("scatterChart2DForegroundColorBlue", scatterChart2DForegroundColor.getBlue());
	}

	public Color getScatterChart2DBackgroundColor() {
		int r = this.prefs.getInt("scatterChart2DBackgroundColorRed", 255);
		int g = this.prefs.getInt("scatterChart2DBackgroundColorGreen", 255);
		int b = this.prefs.getInt("scatterChart2DBackgroundColorBlue", 255);
		return new Color(r, g, b);
	}

	public void setScatterChart2DBackgroundColor(Color scatterChart2DBackgroundColor) {
		this.prefs.putInt("scatterChart2DBackgroundColorRed", scatterChart2DBackgroundColor.getRed());
		this.prefs.putInt("scatterChart2DBackgroundColorGreen", scatterChart2DBackgroundColor.getGreen());
		this.prefs.putInt("scatterChart2DBackgroundColorBlue", scatterChart2DBackgroundColor.getBlue());
	}

	public Color getScatterChart2DActiveDesignColor() {
		int r = this.prefs.getInt("scatterChart2DActiveDesignColorRed", 0);
		int g = this.prefs.getInt("scatterChart2DActiveDesignColorGreen", 150);
		int b = this.prefs.getInt("scatterChart2DActiveDesignColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DActiveDesignColor(Color scatterChart2DActiveDesignColor) {
		this.prefs.putInt("scatterChart2DActiveDesignColorRed", scatterChart2DActiveDesignColor.getRed());
		this.prefs.putInt("scatterChart2DActiveDesignColorGreen", scatterChart2DActiveDesignColor.getGreen());
		this.prefs.putInt("scatterChart2DActiveDesignColorBlue", scatterChart2DActiveDesignColor.getBlue());
	}

	public Color getScatterChart2DSelectedDesignColor() {
		int r = this.prefs.getInt("scatterChart2DSelectedDesignColorRed", 0);
		int g = this.prefs.getInt("scatterChart2DSelectedDesignColorGreen", 0);
		int b = this.prefs.getInt("scatterChart2DSelectedDesignColorBlue", 150);
		return new Color(r, g, b);
	}

	public void setScatterChart2DSelectedDesignColor(Color scatterChart2DSelectedDesignColor) {
		this.prefs.putInt("scatterChart2DSelectedDesignColorRed", scatterChart2DSelectedDesignColor.getRed());
		this.prefs.putInt("scatterChart2DSelectedDesignColorGreen", scatterChart2DSelectedDesignColor.getGreen());
		this.prefs.putInt("scatterChart2DSelectedDesignColorBlue", scatterChart2DSelectedDesignColor.getBlue());
	}

	public Color getParallelCoordinatesAxisColor() {
		int r = this.prefs.getInt("ParallelCoordinatesAxisColorRed", 0);
		int g = this.prefs.getInt("ParallelCoordinatesAxisColorGreen", 0);
		int b = this.prefs.getInt("ParallelCoordinatesAxisColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisColor(Color axisColor) {
		this.prefs.putInt("ParallelCoordinatesAxisColorRed", axisColor.getRed());
		this.prefs.putInt("ParallelCoordinatesAxisColorGreen", axisColor.getGreen());
		this.prefs.putInt("ParallelCoordinatesAxisColorBlue", axisColor.getBlue());
	}

	public boolean isParallelCoordinatesVerticallyOffsetAxisLabels() {
		return this.prefs.getBoolean("ParallelCoordinatesVerticallyOffsetAxisLabels", true);
	}

	public void setParallelCoordinatesVerticallyOffsetAxisLabels(boolean verticallyOffsetAxisLabels) {
		this.prefs.putBoolean("ParallelCoordinatesVerticallyOffsetAxisLabels", verticallyOffsetAxisLabels);
	}

	public Color getParallelCoordinatesAxisLabelFontColor() {
		int r = this.prefs.getInt("ParallelCoordinatesAxisLabelFontColorRed", 0);
		int g = this.prefs.getInt("ParallelCoordinatesAxisLabelFontColorGreen", 0);
		int b = this.prefs.getInt("ParallelCoordinatesAxisLabelFontColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisLabelFontColor(Color axisLabelFontColor) {
		this.prefs.putInt("ParallelCoordinatesAxisLabelFontColorRed", axisLabelFontColor.getRed());
		this.prefs.putInt("ParallelCoordinatesAxisLabelFontColorGreen", axisLabelFontColor.getGreen());
		this.prefs.putInt("ParallelCoordinatesAxisLabelFontColorBlue", axisLabelFontColor.getBlue());
	}

	public int getParallelCoordinatesAxisLabelFontSize() {
		return this.prefs.getInt("ParallelCoordinatesAxisLabelFontSize", 20);
	}

	public void setParallelCoordinatesAxisLabelFontSize(int axisLabelFontSize) {
		this.prefs.putInt("ParallelCoordinatesAxisLabelFontSize", axisLabelFontSize);
	}

	public int getParallelCoordinatesAxisTicCount() {
		return this.prefs.getInt("ParallelCoordinatesAxisTicCount", 11);
	}

	public void setParallelCoordinatesAxisTicCount(int axisTicCount) {
		this.prefs.putInt("ParallelCoordinatesAxisTicCount", axisTicCount);
	}

	public Color getParallelCoordinatesAxisTicLabelFontColor() {
		int r = this.prefs.getInt("ParallelCoordinatesAxisTicLabelFontColorRed", 0);
		int g = this.prefs.getInt("ParallelCoordinatesAxisTicLabelFontColorGreen", 0);
		int b = this.prefs.getInt("ParallelCoordinatesAxisTicLabelFontColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesAxisTicLabelFontColor(Color axisTicLabelFontColor) {
		this.prefs.putInt("ParallelCoordinatesAxisTicLabelFontColorRed", axisTicLabelFontColor.getRed());
		this.prefs.putInt("ParallelCoordinatesAxisTicLabelFontColorGreen", axisTicLabelFontColor.getGreen());
		this.prefs.putInt("ParallelCoordinatesAxisTicLabelFontColorBlue", axisTicLabelFontColor.getBlue());
	}

	public String getParallelCoordinatesAxisTicLabelFormat() {
		return this.prefs.get("ParallelCoordinatesAxisTicLabelFormat", "%4.3f");
	}

	public void setParallelCoordinatesAxisTicLabelFormat(String axisTicLabelFormat) {
		this.prefs.put("ParallelCoordinatesAxisTicLabelFormat", axisTicLabelFormat);
	}

	public int getParallelCoordinatesAxisTicLabelDigitCount(){
		return this.prefs.getInt("ParallelCoordinatesAxisTicLengthTicLabelDigitCount", 3);
	}

	public void setParallelCoordinatesAxisTicLabelDigitCount(int count){
		this.prefs.putInt("ParallelCoordinatesAxisTicLengthTicLabelDigitCount", count);
	}

	public int getParallelCoordinatesAxisTicLength() {
		return this.prefs.getInt("ParallelCoordinatesAxisTicLength", 4);
	}

	public void setParallelCoordinatesAxisTicLength(int axisTicLength) {
		this.prefs.putInt("ParallelCoordinatesAxisTicLength", axisTicLength);
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
		return this.prefs.getInt("ParallelCoordinatesAxisWidth", 200);
	}

	public void setParallelCoordinatesAxisWidth(int axisWidth) {
		this.prefs.putInt("ParallelCoordinatesAxisWidth", axisWidth);
	}

	public Color getParallelCoordinatesFilterDefaultColor() {
		int r = this.prefs.getInt("ParallelCoordinatesFilterColorRed", 255);
		int g = this.prefs.getInt("ParallelCoordinatesFilterColorGreen", 0);
		int b = this.prefs.getInt("ParallelCoordinatesFilterColorBlue", 0);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesFilterColor(Color filterColor) {
		this.prefs.putInt("ParallelCoordinatesFilterColorRed", filterColor.getRed());
		this.prefs.putInt("ParallelCoordinatesFilterColorGreen", filterColor.getGreen());
		this.prefs.putInt("ParallelCoordinatesFilterColorBlue", filterColor.getBlue());
	}

	public int getParallelCoordinatesFilterHeight() {
		return this.prefs.getInt("ParallelCoordinatesFilterHeight", 10);
	}

	public void setParallelCoordinatesFilterHeight(int filterHeight) {
		this.prefs.putInt("ParallelCoordinatesFilterHeight", filterHeight);
	}

	/**
	 * Gets the width of one half triangle that represents a filter in pixels.
	 * In other words, the filter triangle will be twice as large as the value
	 * entered here.
	 * 
	 * @return the filter width
	 */
	public int getParallelCoordinatesFilterWidth() {
		return this.prefs.getInt("ParallelCoordinatesFilterWidth", 7);
	}

	public void setParallelCoordinatesFilterWidth(int filterWidth) {
		this.prefs.putInt("ParallelCoordinatesFilterWidth", filterWidth);
	}

	public int getParallelCoordinatesAxisTicLabelFontSize() {
		return this.prefs.getInt("ticLabelFontSize", 10);
	}

	public void setParallelCoordinatesAxisTicLabelFontSize(int ticLabelFontSize) {
		this.prefs.putInt("ticLabelFontSize", ticLabelFontSize);
	}

	public int getParallelCoordinatesDesignLabelFontSize() {
		return this.prefs.getInt("designLabelFontSize", 10);
	}

	public void setParallelCoordinatesDesignLabelFontSize(int designLabelFontSize) {
		this.prefs.putInt("designLabelFontSize", designLabelFontSize);
	}

	public int getParallelCoordinatesLineThickness() {
		return this.prefs.getInt("lineThickness", 1);
	}

	public void setParallelCoordinatesLineThickness(int lineThickness) {
		this.prefs.putInt("lineThickness", lineThickness);
	}

	public int getParallelCoordinatesSelectedDesignLineThickness() {
		return this.prefs.getInt("selectedDesignLineThickness", 2);
	}

	public void setParallelCoordinatesSelectedDesignLineThickness(int lineThickness) {
		this.prefs.putInt("selectedDesignLineThickness", lineThickness);
	}

	public boolean isParallelCoordinatesShowFilteredDesigns() {
		return this.prefs.getBoolean("showFilteredDesigns", false);
	}

	public void setParallelCoordinatesShowFilteredDesigns(boolean showFilteredDesigns) {
		this.prefs.putBoolean("showFilteredDesigns", showFilteredDesigns);
	}

	public boolean isParallelCoordinatesShowOnlySelectedDesigns() {
		return this.prefs.getBoolean("parallelCoordinatesShowOnlySelectedDesigns", false);
	}

	public void setParallelCoordinatesShowOnlySelectedDesigns(boolean showOnlySelectedDesigns) {
		this.prefs.putBoolean("parallelCoordinatesShowOnlySelectedDesigns", showOnlySelectedDesigns);
	}

	public Color getParallelCoordinatesActiveDesignDefaultColor() {
		int r = this.prefs.getInt("activeDesignDefaultColorRed", 0);
		int g = this.prefs.getInt("activeDesignDefaultColorGreen", 150);
		int b = this.prefs.getInt("activeDesignDefaultColorBlue", 0);
		int a = this.prefs.getInt("activeDesignDefaultColorAlpha", 128);
		return new Color(r, g, b, a);
	}

	public void setParallelCoordinatesActiveDesignDefaultColor(Color activeDesignDefaultColor) {
		this.prefs.putInt("activeDesignDefaultColorRed", activeDesignDefaultColor.getRed());
		this.prefs.putInt("activeDesignDefaultColorGreen", activeDesignDefaultColor.getGreen());
		this.prefs.putInt("activeDesignDefaultColorBlue", activeDesignDefaultColor.getBlue());
		this.prefs.putInt("activeDesignDefaultColorAlpha", activeDesignDefaultColor.getAlpha());
	}

	public void setParallelCoordinatesSelectedDesignDefaultColor(Color selectedDesignDefaultColor) {
		this.prefs.putInt("selectedDesignDefaultColorRed", selectedDesignDefaultColor.getRed());
		this.prefs.putInt("selectedDesignDefaultColorGreen", selectedDesignDefaultColor.getGreen());
		this.prefs.putInt("selectedDesignDefaultColorBlue", selectedDesignDefaultColor.getBlue());
	}

	public Color getParallelCoordinatesSelectedDesignDefaultColor() {
		int r = this.prefs.getInt("selectedDesignDefaultColorRed", 0);
		int g = this.prefs.getInt("selectedDesignDefaultColorGreen", 0);
		int b = this.prefs.getInt("selectedDesignDefaultColorBlue", 255);
		return new Color(r, g, b);
	}

	public Color getParallelCoordinatesFilteredDesignDefaultColor() {
		int r = this.prefs.getInt("inActiveDesignDefaultColorRed", 200);
		int g = this.prefs.getInt("inActiveDesignDefaultColorGreen", 200);
		int b = this.prefs.getInt("inActiveDesignDefaultColorBlue", 200);
		int a = this.prefs.getInt("inActiveDesignDefaultColorAlpha", 100);
		return new Color(r, g, b, a);
	}

	public void setParallelCoordinatesInactiveDesignDefaultColor(Color inActiveDesignDefaultColor) {
		this.prefs.putInt("inActiveDesignDefaultColorRed", inActiveDesignDefaultColor.getRed());
		this.prefs.putInt("inActiveDesignDefaultColorGreen", inActiveDesignDefaultColor.getGreen());
		this.prefs.putInt("inActiveDesignDefaultColorBlue", inActiveDesignDefaultColor.getBlue());
		this.prefs.putInt("inActiveDesignDefaultColorAlpha", inActiveDesignDefaultColor.getAlpha());
	}

	public boolean isParallelCoordinatesShowDesignIDs() {
		return this.prefs.getBoolean("showDesignIDs", true);
	}

	public void setParallelCoordinatesShowDesignIDs(boolean showDesignIDs) {
		this.prefs.putBoolean("showDesignIDs", showDesignIDs);
	}
	
	public boolean isAntiAliasing() {
		return this.prefs.getBoolean("antiAliasing", true);
	}
	
	public void setAntiAliasing(boolean antiAliasing) {
		this.prefs.putBoolean("antiAliasing", antiAliasing);
	}
	
	public boolean isUseAlpha() {
		return this.prefs.getBoolean("useAlpha", false);
	}
	
	public void setUseAlpha(boolean useAlpha) {
		this.prefs.putBoolean("useAlpha", useAlpha);
	}

	public int getParallelCoordinatesDesignIDFontSize() {
		return this.prefs.getInt("designIDFontSize", 10);
	}

	public void setParallelCoordinatesDesignIDFontSize(int designIDFontSize) {
		this.prefs.putInt("designIDFontSize", designIDFontSize);
	}

	public Color getParallelCoordinatesDefaultBackgroundColor() {
		int r = this.prefs.getInt("backgroundColorRed", 255);
		int g = this.prefs.getInt("backgroundColorGreen", 255);
		int b = this.prefs.getInt("backgroundColorBlue", 255);
		return new Color(r, g, b);
	}

	public void setParallelCoordinatesDefaultBackgroundColor(Color backgroundColor) {
		this.prefs.putInt("backgroundColorRed", backgroundColor.getRed());
		this.prefs.putInt("backgroundColorGreen", backgroundColor.getGreen());
		this.prefs.putInt("backgroundColorBlue", backgroundColor.getBlue());
	}

	public boolean isFilterInverted() {
		return this.prefs.getBoolean("ParallelCoordinatesFilterInverted", false);
	}

	public void setFilterInverted(boolean filterInverted) {
		this.prefs.putBoolean("ParallelCoordinatesFilterInverted", filterInverted);
	}

	public boolean isParallelCoordinatesAxisInverted() {
		return this.prefs.getBoolean("ParallelCoordinatesAxisInverted", false);
	}

	public void setParallelCoordinatesAxisInverted(boolean axisInverted) {
		this.prefs.putBoolean("ParallelCoordinatesAxisInverted", axisInverted);
	}

	public boolean isParallelCoordinatesAutoFitAxis() {
		return this.prefs.getBoolean("ParallelCoordinatesAutoFitAxis", true);
	}

	public void setParallelCoordinatesAutoFitAxis(boolean autoFitAxis) {
		this.prefs.putBoolean("ParallelCoordinatesAutoFitAxis", autoFitAxis);
	}

	public double getParallelCoordinatesAxisDefaultMin() {
		return this.prefs.getDouble("ParallelCoordinatesAxisDefaultMin", -10);
	}

	public void setParallelCoordinatesAxisDefaultMin(double axisDefaultMin) {
		this.prefs.putDouble("ParallelCoordinatesAxisDefaultMin", axisDefaultMin);
	}

	public double getParallelCoordinatesAxisDefaultMax() {
		return this.prefs.getDouble("ParallelCoordinatesAxisDefaultMax", 10.0);
	}

	public void setParallelCoordinatesAxisDefaultMax(double axisDefaultMax) {
		this.prefs.putDouble("ParallelCoordinatesAxisDefaultMax", axisDefaultMax);
	}

	public int getDirToImportFrom() {
		return this.prefs.getInt("dirToImportFrom", IMPORT_FROM_LASTDIR);
	}

	public void setDirToImportFrom(int dirToImportFrom) {
		this.prefs.putInt("dirToImportFrom", dirToImportFrom);
	}

	public String getLastFile() {
		return this.prefs.get("lastFileBrowsingDirectory", System.getProperty("user.home"));
	}

	public void setLastFile(String lastFileBrowsingDirectory) {
		this.prefs.put("lastFileBrowsingDirectory", lastFileBrowsingDirectory);
	}

	public boolean isLastFileInitialised() {
		return this.prefs.getBoolean("lastFileInitialised", false);
	}

	public String getHomeDir() {
		return this.prefs.get("homeDir", System.getProperty("user.home"));
	}

	public void setHomeDir(String homeDir) {
		this.prefs.put("homeDir", homeDir);
	}

	public String getUserDir() {
		return this.prefs.get("userDir", System.getProperty("user.home"));
	}

	public void setUserDir(String userDir) {
		this.prefs.put("userDir", userDir);
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
		return this.prefs.get("delimiter", "\\s");
	}

	public void setDelimiter(String delimiter) {
		this.prefs.put("delimiter", delimiter);
	}

	public boolean isTreatConsecutiveAsOne() {
		return this.prefs.getBoolean("treatConsecutiveAsOne", true);
	}

	public void setTreatConsecutiveAsOne(boolean treatConsecutiveAsOne) {
		this.prefs.putBoolean("treatConsecutiveAsOne", treatConsecutiveAsOne);
	}

	public String getOtherDelimiter() {
		return this.prefs.get("otherDelimiter", "");
	}

	public void setOtherDelimiter(String otherDelimiter) {
		this.prefs.put("otherDelimiter", otherDelimiter);
	}

	public Locale getLocale() {
		int locale = this.prefs.getInt("locale", LOCALE_US);
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
		this.prefs.putInt("locale", locale);
	}

	public void resetToDefault() {
		try {
			this.prefs.clear();
		} catch (BackingStoreException e) {
			System.err.println(e.getMessage());
		}
		this.setLicenseAccepted(true);
	}
}
