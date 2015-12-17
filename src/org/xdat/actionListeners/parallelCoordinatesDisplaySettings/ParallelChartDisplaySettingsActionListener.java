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

/**
 * ActionListener for a {@link ParallelCoordinatesChartDisplaySettingsPanel}
 * that allows to modify the Display Settings of a
 * {@link ParallelCoordinatesChart}.
 */
public class ParallelChartDisplaySettingsActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;
	
	/** The display settings dialog. */
	private JDialog dialog;

	/** The user preferences. */
	private UserPreferences userPreferences;

	/** The panel on which the settings controls are located. */
	private ParallelCoordinatesChartDisplaySettingsPanel panel;

	/**
	 * The Chart background color.
	 * 
	 * @see ParallelCoordinatesChart#getBackGroundColor()
	 */
	private Color backGroundColor;

	/**
	 * The active design color.
	 * 
	 * @see ParallelCoordinatesChart#getDefaultDesignColor(boolean)
	 */
	private Color activeDesignColor;

	/**
	 * The selected design color.
	 * 
	 * @see ParallelCoordinatesChart#getSelectedDesignColor()
	 */
	private Color selectedDesignColor;

	/**
	 * The filtered design color.
	 * 
	 * @see ParallelCoordinatesChart#getDefaultDesignColor(boolean)
	 */
	private Color filteredDesignColor;

	/**
	 * The filter color.
	 * 
	 * @see ParallelCoordinatesChart#getFilterColor()
	 */
	private Color filterColor;

	/**
	 * The show filtered designs flag.
	 * 
	 * @see ParallelCoordinatesChart#isShowFilteredDesigns()
	 */
	private boolean showFilteredDesigns;

	/**
	 * The show design IDs flag.
	 * 
	 * @see ParallelCoordinatesChart#isShowDesignIDs()
	 */
	private boolean showDesignIDs;
	
	/**
	 * Instantiates a new chart display settings action listener to edit default
	 * settings.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param panel
	 *            the panel
	 * @param dialog 
	 * 			the dialog
	 */
	public ParallelChartDisplaySettingsActionListener(Main mainWindow, ParallelCoordinatesChartDisplaySettingsPanel panel, JDialog dialog) {
		log("constructor called for default settings.");
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
		log("constructor called for chart specific settings.");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand == "Background Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Background Color", this.backGroundColor);
			if (newColor != null)
				this.backGroundColor = newColor;
			this.panel.getBackGroundColorButton().setCurrentColor(this.backGroundColor);

		} else if (actionCommand == "Active Design Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Active Design Color", this.activeDesignColor);
			if (newColor != null)
				this.activeDesignColor = newColor;
			this.panel.getActiveDesignColorButton().setCurrentColor(this.activeDesignColor);
		} else if (actionCommand == "Selected Design Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Selected Design Color", this.selectedDesignColor);
			if (newColor != null)
				this.selectedDesignColor = newColor;
			this.panel.getSelectedDesignColorButton().setCurrentColor(this.selectedDesignColor);
		} else if (actionCommand == "Filtered Design Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Filtered Design Color", this.filteredDesignColor);
			if (newColor != null)
				this.filteredDesignColor = newColor;
			this.panel.getFilteredDesignColorButton().setCurrentColor(this.filteredDesignColor);
		} else if (actionCommand == "Filter Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Filter Color", this.filterColor);
			if (newColor != null)
				this.filterColor = newColor;
			this.panel.getFilterColorButton().setCurrentColor(this.filterColor);
		} else if (actionCommand == "showfilteredDesignsTrue") {
			this.showFilteredDesigns = true;
		} else if (actionCommand == "showfilteredDesignsFalse") {
			this.showFilteredDesigns = false;
		} else if (actionCommand == "showDesignIDsTrue") {
			this.showDesignIDs = true;
		} else if (actionCommand == "showDesignIDsFalse") {
			this.showDesignIDs = false;
		} else if (actionCommand == "Yes" || actionCommand == "No") {
			// Do nothing
		} else {
			log(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParallelChartDisplaySettingsActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the active design color.
	 * 
	 * @return the active design color
	 */
	public Color getActiveDesignColor() {
		return activeDesignColor;
	}

	/**
	 * Gets the selected design color.
	 * 
	 * @return the selected design color
	 */
	public Color getSelectedDesignColor() {
		return selectedDesignColor;
	}

	/**
	 * Gets the back ground color.
	 * 
	 * @return the back ground color
	 */
	public Color getBackGroundColor() {
		return backGroundColor;
	}

	/**
	 * Gets the filter color.
	 * 
	 * @return the filter color
	 */
	public Color getFilterColor() {
		return filterColor;
	}

	/**
	 * Gets the filtered design color.
	 * 
	 * @return the filtered design color
	 */
	public Color getFilteredDesignColor() {
		return filteredDesignColor;
	}

	/**
	 * Checks if design IDs are shown.
	 * 
	 * @return true, if design IDs are shown.
	 */
	public boolean isShowDesignIDs() {
		return showDesignIDs;
	}

	/**
	 * Checks if filtered designs are shown.
	 * 
	 * @return true, if filtered designs are shown.
	 */
	public boolean isShowFilteredDesigns() {
		return showFilteredDesigns;
	}
}
