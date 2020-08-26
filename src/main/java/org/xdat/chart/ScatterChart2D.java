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

import java.awt.Color;
import java.awt.Dimension;

public class ScatterChart2D extends Chart {

	static final long serialVersionUID = 1;
	private ScatterPlot2D scatterPlot2D;

	public ScatterChart2D(DataSheet dataSheet, boolean showDecorations, Dimension frameSize, int id) {
		super(dataSheet, id);
		this.scatterPlot2D = new ScatterPlot2D(dataSheet, showDecorations);
		this.setFrameSize(frameSize);

	}

	public void setCurrentSettingsAsDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		userPreferences.setScatterChart2DDisplayMode(this.scatterPlot2D.getDisplayedDesignSelectionMode());
		userPreferences.setScatterChart2DAutofitX(this.scatterPlot2D.isAutofit(AxisType.X));
		userPreferences.setScatterChart2DAutofitY(this.scatterPlot2D.isAutofit(AxisType.Y));
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

	public void resetDisplaySettingsToDefault(DataSheet dataSheet) {
		this.scatterPlot2D.resetDisplaySettingsToDefault();
	}

	@Override
	public void initTransientDataImpl() {

	}

}
