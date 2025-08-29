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

	public static int getInt(Key key, int defaultValue){
	    return PREFS.getInt(key.getId(), defaultValue);
    }

    public static void putInt(Key key, int value) {
	    PREFS.putInt(key.getId(), value);
    }

    public static boolean getBoolean(Key key, boolean defaultValue){
        return PREFS.getBoolean(key.getId(), defaultValue);
    }

    public static void putBoolean(Key key, boolean value) {
        PREFS.putBoolean(key.getId(), value);
    }

    public static double getDouble(Key key, double defaultValue){
        return PREFS.getDouble(key.getId(), defaultValue);
    }

    public static void putDouble(Key key, double value) {
        PREFS.putDouble(key.getId(), value);
    }

    public static String getString(Key key, String defaultValue){
        return PREFS.get(key.getId(), defaultValue);
    }

    public static void putString(Key key, String value) {
        PREFS.put(key.getId(), value);
    }

	public static Color getColor(Key key, Color defaultValue) {
		int r = PREFS.getInt(key.getId() + "Red", defaultValue.getRed());
		int g = PREFS.getInt(key.getId() + "Green", defaultValue.getGreen());
		int b = PREFS.getInt(key.getId() + "Blue", defaultValue.getBlue());
		int a = PREFS.getInt(key.getId() + "Alpha", defaultValue.getAlpha());
		return new Color(r, g, b, a);
	}

	public static void putColor(Key key, Color value) {
		PREFS.putInt(key.getId() + "Red", value.getRed());
		PREFS.putInt(key.getId() + "Green", value.getGreen());
		PREFS.putInt(key.getId() + "Blue", value.getBlue());
		PREFS.putInt(key.getId() + "Alpha", value.getAlpha());
	}

	public static UserPreferences getInstance() {
		return INSTANCE;
	}

	public int getScatterChart2DDisplayMode() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_DISPLAY_MODE.getId(), ScatterPlot2D.SHOW_ALL_DESIGNS);
	}

	public void setScatterChart2DDisplayMode(int ScatterChart2DDisplayMode) {
		PREFS.putInt(Key.SCATTER_CHART_2D_DISPLAY_MODE.getId(), ScatterChart2DDisplayMode);
	}

	public int getScatterChart2DAxisTitleFontsizeX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_X.getId(), 20);
	}

	public void setScatterChart2DAxisTitleFontsizeX(int scatterChart2DAxisTitleFontsizeX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_X.getId(), scatterChart2DAxisTitleFontsizeX);
	}

	public int getScatterChart2DAxisTitleFontsizeY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_Y.getId(), 20);
	}

	public void setScatterChart2DAxisTitleFontsizeY(int scatterChart2DAxisTitleFontsizeY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_Y.getId(), scatterChart2DAxisTitleFontsizeY);
	}

	public int getScatterChart2DTicCountX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_COUNT_X.getId(), 2);
	}

	public void setScatterChart2DTicCountX(int scatterChart2DTicCountX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_COUNT_X.getId(), scatterChart2DTicCountX);
	}

	public int getScatterChart2DTicCountY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_COUNT_Y.getId(), 2);
	}

	public void setScatterChart2DTicCountY(int scatterChart2DTicCountY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_COUNT_Y.getId(), scatterChart2DTicCountY);
	}

	public int getScatterChart2DTicLabelFontsizeX() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_X.getId(), 12);
	}

	public void setScatterChart2DTicLabelFontsizeX(int scatterChart2DTicLabelFontsizeX) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_X.getId(), scatterChart2DTicLabelFontsizeX);
	}

	public int getScatterChart2DTicLabelFontsizeY() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_Y.getId(), 12);
	}

	public void setScatterChart2DTicLabelFontsizeY(int scatterChart2DTicLabelFontsizeY) {
		PREFS.putInt(Key.SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_Y.getId(), scatterChart2DTicLabelFontsizeY);
	}

	public int getScatterChart2DDataPointSize() {
		return PREFS.getInt(Key.SCATTER_CHART_2D_DATA_POINT_SIZE.getId(), 3);
	}

	public void setScatterChart2DDataPointSize(int scatterChart2DDataPointSize) {
		PREFS.putInt(Key.SCATTER_CHART_2D_DATA_POINT_SIZE.getId(), scatterChart2DDataPointSize);
	}

	public Color getScatterChart2DForegroundColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Green", 0);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DForegroundColor(Color scatterChart2DForegroundColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Red", scatterChart2DForegroundColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Green", scatterChart2DForegroundColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_FOREGROUND_COLOR.getId() + "Blue", scatterChart2DForegroundColor.getBlue());
	}

	public Color getScatterChart2DBackgroundColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Red", 255);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Green", 255);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Blue", 255);
		return new Color(r, g, b);
	}

	public void setScatterChart2DBackgroundColor(Color scatterChart2DBackgroundColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Red", scatterChart2DBackgroundColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Green", scatterChart2DBackgroundColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_BACKGROUND_COLOR.getId() + "Blue", scatterChart2DBackgroundColor.getBlue());
	}

	public Color getScatterChart2DActiveDesignColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Green", 150);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Blue", 0);
		return new Color(r, g, b);
	}

	public void setScatterChart2DActiveDesignColor(Color scatterChart2DActiveDesignColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Red", scatterChart2DActiveDesignColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Green", scatterChart2DActiveDesignColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR.getId() + "Blue", scatterChart2DActiveDesignColor.getBlue());
	}

	public Color getScatterChart2DSelectedDesignColor() {
		int r = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Red", 0);
		int g = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Green", 0);
		int b = PREFS.getInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Blue", 150);
		return new Color(r, g, b);
	}

	public void setScatterChart2DSelectedDesignColor(Color scatterChart2DSelectedDesignColor) {
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Red", scatterChart2DSelectedDesignColor.getRed());
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Green", scatterChart2DSelectedDesignColor.getGreen());
		PREFS.putInt(Key.SCATTER_CHART_2D_SELECTED_DESIGN_COLOR.getId() + "Blue", scatterChart2DSelectedDesignColor.getBlue());
	}

	public int getParallelCoordinatesAxisTicLabelDigitCount(){
		return PREFS.getInt(Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_DIGIT_COUNT.getId(), 3);
	}

	public Color getParallelCoordinatesActiveDesignDefaultColor() {
		int r = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Red", 0);
		int g = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Green", 150);
		int b = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Blue", 0);
		int a = PREFS.getInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Alpha", 128);
		return new Color(r, g, b, a);
	}

	public void setParallelCoordinatesActiveDesignDefaultColor(Color activeDesignDefaultColor) {
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Red", activeDesignDefaultColor.getRed());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Green", activeDesignDefaultColor.getGreen());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Blue", activeDesignDefaultColor.getBlue());
		PREFS.putInt(Key.ACTIVE_DESIGN_DEFAULT_COLOR.getId() + "Alpha", activeDesignDefaultColor.getAlpha());
	}

	public boolean isAntiAliasing() {
		return PREFS.getBoolean(Key.ANTI_ALIASING.getId(), true);
	}
	
	public boolean isUseAlpha() {
		return PREFS.getBoolean(Key.USE_ALPHA.getId(), false);
	}

	public int getDirToImportFrom() {
		return PREFS.getInt(Key.DIRECTORY_TO_IMPORT_FROM.getId(), IMPORT_FROM_LASTDIR);
	}

	public void setDirToImportFrom(int dirToImportFrom) {
		PREFS.putInt(Key.DIRECTORY_TO_IMPORT_FROM.getId(), dirToImportFrom);
	}

	public String getLastFile() {
		return PREFS.get(Key.LAST_FILE_BROWSING_DIRECTORY.getId(), System.getProperty("user.home"));
	}

	public void setLastFile(String lastFileBrowsingDirectory) {
		PREFS.put(Key.LAST_FILE_BROWSING_DIRECTORY.getId(), lastFileBrowsingDirectory);
	}

	public String getHomeDir() {
		return System.getProperty("user.home");
	}

	public String getUserDir() {
		return PREFS.get(Key.USER_DIR.getId(), getHomeDir());
	}

	public void setUserDir(String userDir) {
		PREFS.put(Key.USER_DIR.getId(), userDir);
	}

	public String getCurrentDir() {
		switch (this.getDirToImportFrom()) {
			case (IMPORT_FROM_LASTDIR): {
				return this.getLastFile();
			}
			case (IMPORT_FROM_USERDIR): {
				return this.getUserDir();
			}
			case (IMPORT_FROM_HOMEDIR):
				// deliberate fallthrough
			default: {
				return this.getHomeDir();
			}
		}

	}

	public String getDelimiter() {
		return PREFS.get(Key.DELIMITER.getId(), "\\s");
	}

	public void setDelimiter(String delimiter) {
		PREFS.put(Key.DELIMITER.getId(), delimiter);
	}

	public boolean isTreatConsecutiveAsOne() {
		return PREFS.getBoolean(Key.TREAT_CONSECUTIVE_AS_ONE.getId(), true);
	}

	public void setTreatConsecutiveAsOne(boolean treatConsecutiveAsOne) {
		PREFS.putBoolean(Key.TREAT_CONSECUTIVE_AS_ONE.getId(), treatConsecutiveAsOne);
	}

	public String getOtherDelimiter() {
		return PREFS.get(Key.OTHER_DELIMITER.getId(), "");
	}

	public void setOtherDelimiter(String otherDelimiter) {
		PREFS.put(Key.OTHER_DELIMITER.getId(), otherDelimiter);
	}

	public Locale getLocale() {
		int locale = PREFS.getInt(Key.LOCALE.getId(), LOCALE_US);
		if (locale == LOCALE_DE) {
			return Locale.GERMANY;
		}
		return Locale.US;
	}

	public void setLocale(int locale) {
		PREFS.putInt(Key.LOCALE.getId(), locale);
	}

	public void resetToDefault() {
		try {
			PREFS.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
}
