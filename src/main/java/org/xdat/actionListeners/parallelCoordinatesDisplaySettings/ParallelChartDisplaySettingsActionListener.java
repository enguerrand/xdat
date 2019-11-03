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

package org.xdat.actionListeners.parallelCoordinatesDisplaySettings;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.JDialog;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.panels.ParallelCoordinatesChartDisplaySettingsPanel;
public class ParallelChartDisplaySettingsActionListener implements ActionListener {
	private JDialog dialog;
	private UserPreferences userPreferences;
	private ParallelCoordinatesChartDisplaySettingsPanel panel;
	private Color backGroundColor;
	private Color activeDesignColor;
	private Color selectedDesignColor;
	private Color filteredDesignColor;
	private Color filterColor;
	private boolean showFilteredDesigns;
	private boolean showDesignIDs;
	public ParallelChartDisplaySettingsActionListener(Main mainWindow, ParallelCoordinatesChartDisplaySettingsPanel panel, JDialog dialog) {
		this.dialog = dialog;
		this.userPreferences = UserPreferences.getInstance();
		this.panel = panel;
		this.backGroundColor = userPreferences.getParallelCoordinatesDefaultBackgroundColor();
		this.activeDesignColor = userPreferences.getParallelCoordinatesActiveDesignDefaultColor();
		this.selectedDesignColor = userPreferences.getParallelCoordinatesSelectedDesignDefaultColor();
		this.filteredDesignColor = userPreferences.getParallelCoordinatesFilteredDesignDefaultColor();
		this.filterColor = userPreferences.getParallelCoordinatesFilterDefaultColor();
		this.showFilteredDesigns = userPreferences.isParallelCoordinatesShowFilteredDesigns();
		this.showDesignIDs = userPreferences.isParallelCoordinatesShowDesignIDs();
	}

	/**
	 * Instantiates a new chart display settings action listener to edit
	 * settings for a specific chart.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param panel
	 *            the panel
	 * @param chart
	 *            the chart
	 * @param dialog
	 * 			the dialog
	 */
	public ParallelChartDisplaySettingsActionListener(Main mainWindow, ParallelCoordinatesChartDisplaySettingsPanel panel, ParallelCoordinatesChart chart, JDialog dialog) {
		this.dialog = dialog;
		this.userPreferences = UserPreferences.getInstance();
		this.panel = panel;
		this.backGroundColor = chart.getBackGroundColor();
		this.activeDesignColor = chart.getDefaultDesignColor(true, true);
		this.selectedDesignColor = chart.getSelectedDesignColor();
		this.filteredDesignColor = chart.getDefaultDesignColor(false, true);
		this.filterColor = chart.getFilterColor();
		this.showFilteredDesigns = chart.isShowFilteredDesigns();
		this.showDesignIDs = chart.isShowDesignIDs();
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Background Color")) {
			Color newColor = JColorChooser.showDialog(dialog, "Background Color", this.backGroundColor);
			if (newColor != null)
				this.backGroundColor = newColor;
			this.panel.getBackGroundColorButton().setCurrentColor(this.backGroundColor);

		} else if (actionCommand.equals("Active Design Color")) {
			Color newColor = JColorChooser.showDialog(dialog, "Active Design Color", this.activeDesignColor);
			if (newColor != null)
				this.activeDesignColor = newColor;
			this.panel.getActiveDesignColorButton().setCurrentColor(this.activeDesignColor);
		} else if (actionCommand.equals("Selected Design Color")) {
			Color newColor = JColorChooser.showDialog(dialog, "Selected Design Color", this.selectedDesignColor);
			if (newColor != null)
				this.selectedDesignColor = newColor;
			this.panel.getSelectedDesignColorButton().setCurrentColor(this.selectedDesignColor);
		} else if (actionCommand.equals("Filtered Design Color")) {
			Color newColor = JColorChooser.showDialog(dialog, "Filtered Design Color", this.filteredDesignColor);
			if (newColor != null)
				this.filteredDesignColor = newColor;
			this.panel.getFilteredDesignColorButton().setCurrentColor(this.filteredDesignColor);
		} else if (actionCommand.equals("Filter Color")) {
			Color newColor = JColorChooser.showDialog(dialog, "Filter Color", this.filterColor);
			if (newColor != null)
				this.filterColor = newColor;
			this.panel.getFilterColorButton().setCurrentColor(this.filterColor);
		} else if (actionCommand.equals("showfilteredDesignsTrue")) {
			this.showFilteredDesigns = true;
		} else if (actionCommand.equals("showfilteredDesignsFalse")) {
			this.showFilteredDesigns = false;
		} else if (actionCommand.equals("showDesignIDsTrue")) {
			this.showDesignIDs = true;
		} else if (actionCommand.equals("showDesignIDsFalse")) {
			this.showDesignIDs = false;
		}
	}

	public Color getActiveDesignColor() {
		return activeDesignColor;
	}

	public Color getSelectedDesignColor() {
		return selectedDesignColor;
	}

	public Color getBackGroundColor() {
		return backGroundColor;
	}

	public Color getFilterColor() {
		return filterColor;
	}

	public Color getFilteredDesignColor() {
		return filteredDesignColor;
	}

	public boolean isShowDesignIDs() {
		return showDesignIDs;
	}

	public boolean isShowFilteredDesigns() {
		return showFilteredDesigns;
	}
}
