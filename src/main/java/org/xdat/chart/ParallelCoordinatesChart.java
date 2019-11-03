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
import org.xdat.data.Design;

import javax.swing.ProgressMonitor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * A serializable representation of all relevant settings for a Parallel
 * coordinates chart which is displayed on a ChartFrame.
 * <p>
 * The data that was imported by the user can be displayed on a Parallel
 * coordinates Chart. The Parallel coordinates Chart is built by as many
 * vertical Axes as Parameters are present in the underlying DataSheet and each
 * Axis represents one of these Parameters.
 * <p>
 * The Designs (or, in other words, rows of the data table) are represented by
 * lines that connect points on the Axes. Each Design's line crosses each Axis
 * exactly at the ordinate that corresponds to the value for the respective
 * Parameter in the Design. This allows for displaying the whole DataSheet in
 * just one Chart, irrespective of how many dimensions it has.
 * <p>
 * The Chart also provides interactivity through a pair of draggable Filters
 * that are present on each Axis. Depending on the positions of these filters,
 * certain Designs are filtered from the display. This means that they are
 * either displayed in a different color or hidden completely.
 * 
 * 
 * @see org.xdat.gui.frames.ChartFrame
 * @see Axis
 * @see Filter
 * @see org.xdat.data.Parameter
 * @see org.xdat.data.DataSheet
 * @see org.xdat.data.Design
 */
public class ParallelCoordinatesChart extends Chart implements Serializable {

	static final long serialVersionUID = 4;
	private static final int BOTTOM_PADDING = 60;
	private Color backGroundColor;
	private int topMargin = 10;
	private boolean verticallyOffsetAxisLabels = true;
	private int axisLabelVerticalDistance = 10;
	private List<Axis> axes = new LinkedList<>();
	private int designLabelFontSize;
	private int lineThickness = 1;
	private int selectedDesignsLineThickness = 1;
	private Color activeDesignColor;
	private Color activeDesignColorNoAlpha;
	private Color selectedDesignColor;
	private Color filteredDesignColor;
	private Color filteredDesignColorNoAlpha;
	private Color filterColor;
	private boolean showDesignIDs;
	private boolean showFilteredDesigns;
	private boolean showOnlySelectedDesigns;
	private int filterHeight;
	private int filterWidth;
	public ParallelCoordinatesChart(DataSheet dataSheet, ProgressMonitor progressMonitor, int id) {
		super(dataSheet, id);
		this.setLocation(new Point(100, 100));
		this.setFrameSize(new Dimension(1280, 800));
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.backGroundColor = userPreferences.getParallelCoordinatesDefaultBackgroundColor();
		this.showDesignIDs = userPreferences.isParallelCoordinatesShowDesignIDs();
		this.showFilteredDesigns = userPreferences.isParallelCoordinatesShowFilteredDesigns();
		this.verticallyOffsetAxisLabels = userPreferences.isParallelCoordinatesVerticallyOffsetAxisLabels();
		this.setActiveDesignColor(userPreferences.getParallelCoordinatesActiveDesignDefaultColor());
		this.selectedDesignColor = userPreferences.getParallelCoordinatesSelectedDesignDefaultColor();
		this.setFilteredDesignColor(userPreferences.getParallelCoordinatesFilteredDesignDefaultColor());
		this.designLabelFontSize = userPreferences.getParallelCoordinatesDesignLabelFontSize();
		this.lineThickness = userPreferences.getParallelCoordinatesLineThickness();
		this.selectedDesignsLineThickness = userPreferences.getParallelCoordinatesSelectedDesignLineThickness();
		this.filterColor = userPreferences.getParallelCoordinatesFilterDefaultColor();
		this.showOnlySelectedDesigns = userPreferences.isParallelCoordinatesShowOnlySelectedDesigns();
		this.filterHeight = userPreferences.getParallelCoordinatesFilterHeight();
		this.filterWidth = userPreferences.getParallelCoordinatesFilterWidth();
		progressMonitor.setMaximum(dataSheet.getParameterCount() - 1);
		progressMonitor.setNote("Building Chart...");
		for (int i = 0; i < dataSheet.getParameterCount() && !progressMonitor.isCanceled(); i++) {
			// log("constructor: Creating axis "+dataSheet.getParameter(i).getName());
			Axis newAxis = new Axis(dataSheet, this, dataSheet.getParameter(i));
			this.addAxis(newAxis);
			progressMonitor.setProgress(i);
		}

		if (!progressMonitor.isCanceled()) {
			progressMonitor.setNote("Building Filters...");
			progressMonitor.setProgress(0);
			// log("constructor: axes created. Creating filters...");
			for (int i = 0; i < dataSheet.getParameterCount() && !progressMonitor.isCanceled(); i++) {
				this.axes.get(i).addFilters();
				progressMonitor.setProgress(i);
			}
			// log("constructor: filters created. ");
		}

	}

	public String getTitle() {
		return "Parallel Coordinates Chart " + this.getID();
	}

	public int getWidth() {
		int width = 0;
		if (this.getAxis(0).isActive()) {
			width = width + (int) (0.5 * this.getAxis(0).getWidth());
		}
		for (int i = 1; i < this.getAxisCount(); i++) {
			if (this.getAxis(i).isActive()) {
				width = width + (int) (this.getAxis(i).getWidth());
			}
		}
		return width;
	}

	public int getHeight() {
		int height = getAxisTopPos() + getAxisHeight();
		return height;
	}

	public int getAxisMaxWidth() {
		int width = 0;
		for (int i = 0; i < this.getAxisCount(); i++) {
			if (this.getAxis(i).isActive()) {
				if (width < this.getAxis(i).getWidth()) {
					width = this.getAxis(i).getWidth();
				}
			}
		}
		return width;
	}

	public void setAxisWidth(int width) {
		for (int i = 0; i < axes.size(); i++) {
			axes.get(i).setWidth(width);
		}
	}

	public void incrementAxisWidth(int deltaWidth) {
		for (int i = 0; i < axes.size(); i++) {
			axes.get(i).setWidth(Math.max(0, axes.get(i).getWidth() + deltaWidth));
		}
	}

	public void setAxisColor(Color color) {
		for (int i = 0; i < axes.size(); i++) {
			axes.get(i).setAxisColor(color);
		}
	}

	public int getAxisTopPos() {
		int topPos;
		if (this.verticallyOffsetAxisLabels) {
			topPos = 2 * getMaxAxisLabelFontSize() + this.axisLabelVerticalDistance + this.getTopMargin() * 2 + this.getFilterHeight();
		} else {
			topPos = getMaxAxisLabelFontSize() + this.getTopMargin() * 2 + this.getFilterHeight();
		}
		return topPos;
	}

	public Axis getAxis(int index) {

		return axes.get(index);
	}

	public Axis getAxis(String parameterName) {
		for (int i = 0; i < this.axes.size(); i++) {
			if (parameterName.equals(this.axes.get(i).getParameter().getName())) {
				return this.axes.get(i);
			}
		}
		throw new IllegalArgumentException("Axis " + parameterName + " not found");
	}

	public int getMaxAxisLabelFontSize() {
		int maxAxisLabelFontSize = 0;
		for (int i = 0; i < axes.size(); i++) {
			if (maxAxisLabelFontSize < axes.get(i).getAxisLabelFontSize()) {
				maxAxisLabelFontSize = axes.get(i).getAxisLabelFontSize();
			}
		}
		return maxAxisLabelFontSize;
	}

	public int getActiveAxisCount() {
		int axesCount = 0;
		for (int i = 0; i < axes.size(); i++) {
			if (axes.get(i).isActive())
				axesCount++;
		}
		return axesCount;
	}

	public int getAxisCount() {
		return axes.size();
	}

	public int getAxisLabelVerticalDistance() {
		return axisLabelVerticalDistance;
	}

	public void setAxisLabelVerticalDistance(int axisLabelVerticalDistance) {
		this.axisLabelVerticalDistance = axisLabelVerticalDistance;
	}

	public boolean isVerticallyOffsetAxisLabels() {
		return verticallyOffsetAxisLabels;
	}

	public void setVerticallyOffsetAxisLabels(boolean verticallyOffsetAxisLabels) {
		this.verticallyOffsetAxisLabels = verticallyOffsetAxisLabels;
	}

	public void addAxis(Axis axis) {
		this.axes.add(axis);
	}

	public void addAxis(int index, Axis axis) {
		this.axes.add(index, axis);
	}

	public void removeAxis(int index) {
		this.axes.remove(index);
	}

	public void removeAxis(String parameterName) {
		for (int i = 0; i < this.axes.size(); i++) {
			if (parameterName.equals(this.axes.get(i).getParameter().getName())) {
				this.axes.remove(i);
				return;
			}
		}
		throw new IllegalArgumentException("Axis " + parameterName + " not found");
	}

	public void moveAxis(int oldIndex, int newIndex) {
		Axis axis = this.axes.remove(oldIndex);
		this.axes.add(newIndex, axis);
	}

	public int getAxisHeight() {
		return this.getFrameSize().height - this.getAxisTopPos() - BOTTOM_PADDING;
	}

	public int getDesignLabelFontSize() {
		return designLabelFontSize;
	}

	public void setDesignLabelFontSize(int designLabelFontSize) {
		this.designLabelFontSize = designLabelFontSize;
	}

	public int getLineThickness() {
		return lineThickness;
	}

	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	public Color getDesignColor(Design design, boolean designActive, boolean useAlpha) // design active is function argument to improve  performance
	{
		if (designActive && design.hasGradientColor()) {
			return design.getGradientColor();
		} else if (designActive && design.getCluster() != null) {
			return design.getCluster().getActiveDesignColor(useAlpha);
		} else if (designActive) {
			return useAlpha ? activeDesignColor : activeDesignColorNoAlpha;
		}
		else {
			return useAlpha ? filteredDesignColor : filteredDesignColorNoAlpha;
		}
	}

	public int getDesignLineThickness(Design design) {
		if (design.getCluster() != null) {
			return design.getCluster().getLineThickness();
		} else {
			return this.lineThickness;
		}
	}

	public int getSelectedDesignsLineThickness() {
		return selectedDesignsLineThickness;
	}

	public void setSelectedDesignsLineThickness(int selectedDesignsLineThickness) {
		this.selectedDesignsLineThickness = selectedDesignsLineThickness;
	}

	public Color getDefaultDesignColor(boolean designActive, boolean useAlpha) {
		if (designActive)
			return useAlpha ? activeDesignColor : activeDesignColorNoAlpha;
		else
			return useAlpha ? filteredDesignColor : filteredDesignColorNoAlpha;
	}

	public void setActiveDesignColor(Color activeDesignColor) {
		this.activeDesignColor = activeDesignColor;
		this.activeDesignColorNoAlpha = new Color(activeDesignColor.getRed(), activeDesignColor.getGreen(), activeDesignColor.getBlue());
	}

	public void setFilteredDesignColor(Color filteredDesignColor) {
		this.filteredDesignColor = filteredDesignColor;
		this.filteredDesignColorNoAlpha = new Color(filteredDesignColor.getRed(), filteredDesignColor.getGreen(), filteredDesignColor.getBlue());
	}

	public Color getSelectedDesignColor() {
		return selectedDesignColor;
	}

	public void setSelectedDesignColor(Color selectedDesignColor) {
		this.selectedDesignColor = selectedDesignColor;
	}

	public boolean isShowDesignIDs() {
		return showDesignIDs;
	}

	public void setShowDesignIDs(boolean showDesignIDs) {
		this.showDesignIDs = showDesignIDs;
	}

	public boolean isShowFilteredDesigns() {
		return showFilteredDesigns;
	}

	public void setShowFilteredDesigns(boolean showFilteredDesigns) {
		this.showFilteredDesigns = showFilteredDesigns;
	}

	public boolean isShowOnlySelectedDesigns() {
		return showOnlySelectedDesigns;
	}

	public void setShowOnlySelectedDesigns(boolean showOnlySelectedDesigns) {
		this.showOnlySelectedDesigns = showOnlySelectedDesigns;
	}

	public Color getFilterColor() {
		return filterColor;
	}

	public void setFilterColor(Color filterColor) {
		this.filterColor = filterColor;
	}

	public int getTopMargin() {
		return topMargin;
	}

	public void resetDisplaySettingsToDefault() {
		UserPreferences userPreferences = UserPreferences.getInstance();
		this.backGroundColor = userPreferences.getParallelCoordinatesDefaultBackgroundColor();
		this.showDesignIDs = userPreferences.isParallelCoordinatesShowDesignIDs();
		this.showFilteredDesigns = userPreferences.isParallelCoordinatesShowFilteredDesigns();
		this.showOnlySelectedDesigns = userPreferences.isParallelCoordinatesShowOnlySelectedDesigns();
		this.setActiveDesignColor(userPreferences.getParallelCoordinatesActiveDesignDefaultColor());
		this.selectedDesignColor = userPreferences.getParallelCoordinatesSelectedDesignDefaultColor();
		this.setFilteredDesignColor(userPreferences.getParallelCoordinatesFilteredDesignDefaultColor());
		this.designLabelFontSize = userPreferences.getParallelCoordinatesDesignLabelFontSize();
		this.lineThickness = userPreferences.getParallelCoordinatesLineThickness();
		this.selectedDesignsLineThickness = userPreferences.getParallelCoordinatesSelectedDesignLineThickness();
		this.verticallyOffsetAxisLabels = userPreferences.isParallelCoordinatesVerticallyOffsetAxisLabels();
		this.filterColor = userPreferences.getParallelCoordinatesFilterDefaultColor();
		this.filterHeight = userPreferences.getParallelCoordinatesFilterHeight();
		this.filterWidth = userPreferences.getParallelCoordinatesFilterWidth();
		for (int i = 0; i < axes.size(); i++) {
			axes.get(i).resetSettingsToDefault();
		}
	}

	public Color getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(Color backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public int getFilterHeight() {
		return filterHeight;
	}

	public void setFilterHeight(int filterHeight) {
		this.filterHeight = filterHeight;
	}

	public int getFilterWidth() {
		return filterWidth;
	}

	public void setFilterWidth(int filterWidth) {
		this.filterWidth = filterWidth;
	}

	public void applyAllFilters() {
		for (int i = 0; i < this.getDataSheet().getParameterCount(); i++) {
			this.axes.get(i).applyFilters();
		}
	}

	public void autofitAllAxes() {
		for (int i = 0; i < this.axes.size(); i++) {
			this.axes.get(i).autofit();
		}
	}
}
