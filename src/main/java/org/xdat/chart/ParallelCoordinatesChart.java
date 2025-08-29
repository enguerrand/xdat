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
import org.xdat.data.Cluster;
import org.xdat.data.DataSheet;
import org.xdat.data.DatasheetListener;
import org.xdat.data.Design;
import org.xdat.settings.Key;
import org.xdat.settings.SettingsGroup;
import org.xdat.settings.SettingsGroupFactory;
import org.xdat.settings.SettingsTransaction;

import javax.swing.ProgressMonitor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

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
	private static final int TOP_MARGIN = 10;
	private final List<Axis> axes = new LinkedList<>();
	private final SettingsGroup chartSettings;
	public ParallelCoordinatesChart(DataSheet dataSheet, ProgressMonitor progressMonitor, int id) {
		super(dataSheet, id);
		this.chartSettings = SettingsGroupFactory.buildGeneralParallelCoordinatesChartSettingsGroup();
		this.setLocation(new Point(100, 100));
		this.setFrameSize(new Dimension(1280, 800));
		progressMonitor.setMaximum(dataSheet.getParameterCount() - 1);
		progressMonitor.setNote("Building Chart...");
		for (int i = 0; i < dataSheet.getParameterCount() && !progressMonitor.isCanceled(); i++) {
			Axis newAxis = new Axis(dataSheet, this, dataSheet.getParameter(i));
			this.addAxis(newAxis);
			progressMonitor.setProgress(i);
		}

		if (!progressMonitor.isCanceled()) {
			progressMonitor.setNote("Building Filters...");
			progressMonitor.setProgress(0);
			for (int i = 0; i < dataSheet.getParameterCount() && !progressMonitor.isCanceled(); i++) {
				this.axes.get(i).addFilters(dataSheet);
				progressMonitor.setProgress(i);
			}
		}
		Stream.concat(
				this.chartSettings.getSettings().values().stream(),
				axes.stream()
						.map(Axis::getSettings)
						.flatMap(g -> g.getSettings().values().stream())
		).forEach(s ->
				s.addListener((source, transaction) ->
						handleSettingChange(this::fireChanged, transaction))
		);

		dataSheet.addListener(new DatasheetListener() {
			@Override
			public void onClustersChanged() {
				fireChanged();
			}

			@Override
			public void onDataPanelUpdateRequired() {
			}

			@Override
			public void onDataChanged(boolean[] autoFitRequired, boolean[] filterResetRequired, boolean[] applyFiltersRequired, boolean parametersChanged) {
				boolean changed = false;
				if (parametersChanged) {
					// we never add, so only check for removal is needed
					changed = axes.removeIf(axis -> !dataSheet.parameterExists(axis.getParameter()));
				}
				for (int i = 0; i < axes.size(); i++) {
					Axis axis = axes.get(i);
					if (autoFitRequired[i]) {
						changed = true;
						axis.autofit(dataSheet);
					}
					if (filterResetRequired[i]) {
						changed = true;
						axis.resetFilters(dataSheet);
					}
					if (applyFiltersRequired[i]) {
						changed = true;
						axis.applyFilters(dataSheet);
					}
				}
				if (changed) {
					fireChanged();
				}
			}
		});
	}

	private void handleSettingChange(Runnable changeHandler, @Nullable SettingsTransaction transaction) {
		if (transaction == null) {
			changeHandler.run();
		} else {
			transaction.handleOnce(changeHandler);
		}
	}

	@Override
	protected void fireChanged() {
		applyAutoFitIfNeeded();
		super.fireChanged();
	}

	private void applyAutoFitIfNeeded() {
		for (Axis axis : getAxes()) {
			if (axis.isAutoFit()) {
				axis.autofit(getDataSheet());
			}
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
				width = width + this.getAxis(i).getWidth();
			}
		}
		return width;
	}

	public int getHeight() {
		return getAxisTopPos() + getAxisHeight();
	}

	public void incrementAxisWidth(int deltaWidth) {
		for (Axis axis : axes) {
			axis.setWidth(Math.max(0, axis.getWidth() + deltaWidth));
		}
	}

	public int getAxisTopPos() {
		boolean verticallyOffsetAxisLabels = this.chartSettings.getBoolean(Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS);
		int topPos;
		if (verticallyOffsetAxisLabels) {
			topPos = 2 * getMaxAxisLabelFontSize() + this.getAxisLabelVerticalDistance() + this.getTopMargin() * 2 + this.getFilterHeight();
		} else {
			topPos = getMaxAxisLabelFontSize() + this.getTopMargin() * 2 + this.getFilterHeight();
		}
		return topPos;
	}

	public Axis getAxis(int index) {
		return axes.get(index);
	}

	public Axis getAxis(String parameterName) {
		for (Axis axis : this.axes) {
			if (parameterName.equals(axis.getParameter().getName())) {
				return axis;
			}
		}
		throw new IllegalArgumentException("Axis " + parameterName + " not found");
	}

	public int getMaxAxisLabelFontSize() {
		int maxAxisLabelFontSize = 0;
		for (Axis axis : axes) {
			if (maxAxisLabelFontSize < axis.getAxisLabelFontSize()) {
				maxAxisLabelFontSize = axis.getAxisLabelFontSize();
			}
		}
		return maxAxisLabelFontSize;
	}

	public int getAxisCount() {
		return axes.size();
	}

	public int getAxisLabelVerticalDistance() {
		return chartSettings.getInteger(Key.PARALLEL_COORDINATES_LABELS_VERTICAL_DISTANCE);
	}

	public boolean isVerticallyOffsetAxisLabels() {
		return chartSettings.getBoolean(Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS);
	}

	public void addAxis(Axis axis) {
		this.axes.add(axis);
	}

	public void moveAxis(int oldIndex, int newIndex) {
		Axis axis = this.axes.remove(oldIndex);
		this.axes.add(newIndex, axis);
	}

	public int getAxisHeight() {
		return this.getFrameSize().height - this.getAxisTopPos() - BOTTOM_PADDING;
	}

	public int getDesignLabelFontSize() {
		return chartSettings.getInteger(Key.DESIGN_LABEL_FONT_SIZE);
	}

	public int getLineThickness() {
		return chartSettings.getInteger(Key.LINE_THICKNESS);
	}

	public void setActiveDesignColor(Color newColor) {
		chartSettings.getColorSetting(Key.ACTIVE_DESIGN_DEFAULT_COLOR).set(newColor);
	}

	public Color getActiveDesignColor() {
		return chartSettings.getColor(Key.ACTIVE_DESIGN_DEFAULT_COLOR);
	}

	public Color getActiveDesignColorNoAlpha() {
		return new Color(getActiveDesignColor().getRGB());
	}

	public Color getFilteredDesignColor() {
		return chartSettings.getColor(Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR);
	}

	public Color getFilteredDesignColorNoAlpha() {
		return new Color(getFilteredDesignColor().getRGB());
	}

	public Color getDesignColor(Design design, boolean designActive, boolean useAlpha, Color activeDesignColor, Color activeDesignColorNoAlpha, Color filteredDesignColor, Color filteredDesignColorNoAlpha) // design active is function argument to improve  performance
	{
		if (designActive && design.hasGradientColor()) {
			return design.getGradientColor();
		} else {
			@Nullable Cluster cluster = design.getCluster();
			if (designActive && cluster != null) {
				return cluster.getActiveDesignColor(useAlpha);
			} else if (designActive) {
				return useAlpha ? activeDesignColor : activeDesignColorNoAlpha;
			} else {
				return useAlpha ? filteredDesignColor : filteredDesignColorNoAlpha;
			}
		}
	}

	public int getSelectedDesignsLineThickness() {
		return chartSettings.getInteger(Key.SELECTED_DESIGN_LINE_THICKNESS);
	}

	public Color getDefaultDesignColor(boolean designActive, boolean useAlpha) {
		if (designActive)
			return useAlpha ? getActiveDesignColor() : getActiveDesignColorNoAlpha();
		else
			return useAlpha ? getFilteredDesignColor() : getFilteredDesignColorNoAlpha();
	}

	public Color getSelectedDesignColor() {
		return chartSettings.getColor(Key.SELECTED_DESIGN_DEFAULT_COLOR);
	}

	public boolean isShowDesignIDs() {
		return chartSettings.getBoolean(Key.SHOW_DESIGN_IDS);
	}

	public boolean isShowFilteredDesigns() {
		return chartSettings.getBoolean(Key.SHOW_FILTERED_DESIGNS);
	}

	public boolean isShowOnlySelectedDesigns() {
		return chartSettings.getBoolean(Key.PARALLEL_COORDINATES_SHOW_ONLY_SELECTED_DESIGNS);
	}

	public Color getFilterColor() {
		return chartSettings.getColor(Key.PARALLEL_COORDINATES_FILTER_COLOR);
	}

	public String getFontFamily() {
		return chartSettings.getMultipleChoiceSetting(Key.FONT_FAMILY).get();
	}

	public int getTopMargin() {
		return TOP_MARGIN;
	}

	public void resetDisplaySettingsToDefault(DataSheet dataSheet) {
		this.chartSettings.resetToDefault();
		for (Axis axis : axes) {
			axis.resetSettingsToDefault(dataSheet);
		}
	}

	public Color getBackGroundColor() {
		return chartSettings.getColor(Key.PARALLEL_CHART_BACKGROUND_COLOR);
	}

	@Override
	public void setBackGroundColor(Color backGroundColor) {
		this.chartSettings.getColorSetting(Key.PARALLEL_CHART_BACKGROUND_COLOR).set(backGroundColor);
	}

	public int getFilterHeight() {
		return chartSettings.getInteger(Key.PARALLEL_COORDINATES_FILTER_HEIGHT);
	}

	public int getFilterWidth() {
		return chartSettings.getInteger(Key.PARALLEL_COORDINATES_FILTER_WIDTH);
	}

	@Override
	public void initTransientDataImpl() {
		this.chartSettings.initTransientData();
		this.axes.forEach(Axis::initTransientData);
	}

	public SettingsGroup getChartSettingsGroup() {
		return this.chartSettings;
	}

	public List<Axis> getAxes() {
		return Collections.unmodifiableList(this.axes);
	}
}
