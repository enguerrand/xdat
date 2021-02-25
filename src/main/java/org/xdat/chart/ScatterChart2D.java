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

import org.jetbrains.annotations.Nullable;
import org.xdat.UserPreferences;
import org.xdat.data.AxisType;
import org.xdat.data.DataSheet;
import org.xdat.settings.Key;
import org.xdat.settings.SettingsGroup;
import org.xdat.settings.SettingsGroupFactory;
import org.xdat.settings.SettingsTransaction;

import java.awt.Color;
import java.awt.Dimension;
import java.util.stream.Stream;

public class ScatterChart2D extends Chart {

	static final long serialVersionUID = 1;
	private final ScatterPlot2D scatterPlot2D;
	private final SettingsGroup axisSettingsX;
	private final SettingsGroup axisSettingsY;

	public ScatterChart2D(DataSheet dataSheet, boolean showDecorations, Dimension frameSize, int id) {
		super(dataSheet, id);
		this.axisSettingsX = SettingsGroupFactory.build2DScatterChartAxisSettingsGroup(AxisType.X);
		this.axisSettingsY = SettingsGroupFactory.build2DScatterChartAxisSettingsGroup(AxisType.Y);
		Stream.concat(
				this.axisSettingsX.getSettings().values().stream(),
				this.axisSettingsY.getSettings().values().stream()
		).forEach(s ->
				s.addListener((source, transaction) ->
						handleSettingChange(this::fireChanged, transaction))
		);
		this.scatterPlot2D = new ScatterPlot2D(dataSheet, showDecorations, axisSettingsX, axisSettingsY);
		this.setFrameSize(frameSize);
	}

	public void setCurrentSettingsAsDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		userPreferences.setScatterChart2DDisplayMode(this.scatterPlot2D.getDisplayedDesignSelectionMode());
		userPreferences.setScatterChart2DAxisTitleFontsizeX(this.scatterPlot2D.getAxisLabelFontSize(AxisType.X));
		userPreferences.setScatterChart2DAxisTitleFontsizeY(this.scatterPlot2D.getAxisLabelFontSize(AxisType.Y));
		userPreferences.setScatterChart2DTicCountX(this.scatterPlot2D.getTicCount(AxisType.X));
		userPreferences.setScatterChart2DTicCountY(this.scatterPlot2D.getTicCount(AxisType.Y));
		userPreferences.setScatterChart2DTicLabelFontsizeX(this.scatterPlot2D.getTicLabelFontSize(AxisType.X));
		userPreferences.setScatterChart2DTicLabelFontsizeY(this.scatterPlot2D.getTicLabelFontSize(AxisType.Y));
		userPreferences.setScatterChart2DDataPointSize(this.scatterPlot2D.getDotRadius());
		userPreferences.setScatterChart2DForegroundColor(this.scatterPlot2D.getDecorationsColor());
		userPreferences.setScatterChart2DBackgroundColor(this.scatterPlot2D.getBackGroundColor());
		userPreferences.setScatterChart2DActiveDesignColor(this.scatterPlot2D.getActiveDesignColor());
		userPreferences.setScatterChart2DSelectedDesignColor(this.scatterPlot2D.getSelectedDesignColor());
		this.axisSettingsX.applyAllAsDefault();
		this.axisSettingsY.applyAllAsDefault();
	}

	public int getWidth() {
		return this.getFrameSize().width;
	}

	public int getHeight() {
		return this.getFrameSize().height;
	}

	public String getTitle() {
		return "2D Scatter Chart " + this.getID();
	}

	public ScatterPlot2D getScatterPlot2D() {
		return this.scatterPlot2D;
	}

	public Color getBackGroundColor() {
		return this.scatterPlot2D.getBackGroundColor();
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.scatterPlot2D.setBackGroundColor(backGroundColor);
	}

	public boolean isAutoFit(AxisType axisType) {
		return getAxisSettings(axisType).getBoolean(Key.getScatterChartAutoFitAxis(axisType));
	}

	public SettingsGroup getAxisSettingsX() {
		return axisSettingsX;
	}

	public SettingsGroup getAxisSettingsY() {
		return axisSettingsY;
	}

	public SettingsGroup getAxisSettings(AxisType axisType) {
		switch (axisType) {
			case X:
				return getAxisSettingsX();
			case Y:
				return getAxisSettingsY();
			default:
				throw new IllegalStateException("illegal axis type " + axisType);
		}
	}


	public void resetDisplaySettingsToDefault(DataSheet dataSheet) {
		this.scatterPlot2D.resetDisplaySettingsToDefault();
		this.axisSettingsX.resetToDefault();
		this.axisSettingsY.resetToDefault();
	}

	@Override
	public void initTransientDataImpl() {
		this.axisSettingsX.initTransientData();
		this.axisSettingsY.initTransientData();
	}

	private void handleSettingChange(Runnable changeHandler, @Nullable SettingsTransaction transaction) {
		if (transaction == null) {
			changeHandler.run();
		} else {
			transaction.handleOnce(changeHandler);
		}
	}
}
