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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;

public abstract class Chart implements Serializable {

	static final long serialVersionUID = 1;
	private Point location;
	private int id;
	private Dimension frameSize;
	private final DataSheet dataSheet;
	private boolean antiAliasing;
	private boolean useAlpha;
	
	public Chart(DataSheet dataSheet, int id) {
		this.dataSheet = dataSheet;
		this.id = id;
		this.location = new Point(100, 100);
		this.frameSize = new Dimension(800, 600);
		this.antiAliasing = UserPreferences.getInstance().isAntiAliasing();
		this.useAlpha = UserPreferences.getInstance().isUseAlpha();
	}

	public abstract String getTitle();

	public int getID() {
		return this.id;
	}

	public abstract int getWidth();

	public abstract int getHeight();

	public DataSheet getDataSheet() {
		return dataSheet;
	}

	public abstract Color getBackGroundColor();

	public abstract void setBackGroundColor(Color backGroundColor);

	public Dimension getFrameSize() {
		return frameSize;
	}

	public void setFrameSize(Dimension size) {
		this.frameSize = size;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public abstract void resetDisplaySettingsToDefault(DataSheet dataSheet);
	
	public boolean isAntiAliasing() {
		return antiAliasing;
	}
	
	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}
	
	public boolean isUseAlpha() {
		return useAlpha;
	}
	
	public void setUseAlpha(boolean useAlpha) {
		this.useAlpha = useAlpha;
	}
}
