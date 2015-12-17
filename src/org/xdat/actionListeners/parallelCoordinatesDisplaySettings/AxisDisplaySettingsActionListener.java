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
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.panels.AxisDisplaySettingsPanel;

/**
 * ActionListener that is used for the controls on the
 * {@link AxisDisplaySettingsPanel}.
 * <p>
 * Remembers changes made by the user and applies them when the user confirms by
 * pressing ok on the dialog.
 * <p>
 * When the Axis that is currently being edited changes the user is asked
 * whether he wants to save the changes to the current Axis. This is a
 * workaround to avoid having to store each setting along with the information
 * for which Axis it was made. Ideally all settings should be memorized and
 * applied when the user closes the dialog with the Ok button.
 */
public class AxisDisplaySettingsActionListener implements ActionListener, ChangeListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The display settings dialog. */
	private JDialog dialog;

	/**
	 * The axis color.
	 * 
	 * @see org.xdat.chart.Axis#getAxisColor()
	 */
	private Color axisColor;

	/**
	 * The axis label color.
	 * 
	 * @see org.xdat.chart.Axis#getAxisLabelFontColor()
	 */
	private Color axisLabelColor;

	/**
	 * The tic label color.
	 * 
	 * @see org.xdat.chart.Axis#getTicLabelFontSize()
	 */
	private Color ticLabelColor;

	/**
	 * Specifies whether the Filters are inverted.
	 * 
	 * @see org.xdat.chart.Filter
	 * @see org.xdat.chart.Axis#isFilterInverted()
	 */
	private boolean invertFilter;

	/**
	 * Specifies whether the Axis is inverted.
	 * 
	 * @see org.xdat.chart.Axis#isAxisInverted()
	 */
	private boolean invertAxis;

	/**
	 * Specifies whether Axes should be autofitted.
	 * 
	 * @see org.xdat.chart.Axis
	 * */
	private boolean autoFitAxis;

	/**
	 * The panel on which the display settings controls are located.
	 * 
	 * @see org.xdat.chart.Axis#isAutoFit()
	 */
	private AxisDisplaySettingsPanel panel;

	/**
	 * The chart for which the settings are edited.
	 * <p>
	 * Only applies if constructor
	 * {@link AxisDisplaySettingsActionListener#AxisDisplaySettingsActionListener(JDialog, AxisDisplaySettingsPanel, ParallelCoordinatesChart)}
	 * was used.
	 */
	private ParallelCoordinatesChart parallelCoordinatesChart;

	/**
	 * The Axis currently being edited. Only applies if constructor
	 * {@link AxisDisplaySettingsActionListener#AxisDisplaySettingsActionListener(JDialog, AxisDisplaySettingsPanel, ParallelCoordinatesChart)}
	 * was used.
	 */
	private Axis currentAxis;

	/**
	 * Remembers when the spinner value was modified.
	 * <p>
	 * This is needed to decide whether settings were modified and require a
	 * save operation when the user closes the dialog or changes the Axis to be
	 * edited.
	 */
	private boolean spinnerValueChanged = false;

	/**
	 * Instantiates a new axis display settings action listener for editing the
	 * default settings.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog
	 * @param panel
	 *            the panel
	 */
	public AxisDisplaySettingsActionListener(Main mainWindow, JDialog dialog, AxisDisplaySettingsPanel panel) {
		log("constructor for default settings called");
		this.panel = panel;
		this.dialog = dialog;
		axisColor = UserPreferences.getInstance().getParallelCoordinatesAxisColor();
		axisLabelColor = UserPreferences.getInstance().getParallelCoordinatesAxisLabelFontColor();
		ticLabelColor = UserPreferences.getInstance().getParallelCoordinatesAxisTicLabelFontColor();
		invertFilter = UserPreferences.getInstance().isFilterInverted();
		autoFitAxis = UserPreferences.getInstance().isParallelCoordinatesAutoFitAxis();

	}

	/**
	 * Instantiates a new axis display settings action listener for a specific
	 * Chart.
	 * 
	 * @param dialog
	 *            the dialog
	 * @param panel
	 *            the panel
	 * @param chart
	 *            the chart
	 */
	public AxisDisplaySettingsActionListener(JDialog dialog, AxisDisplaySettingsPanel panel, ParallelCoordinatesChart chart) {
		log("constructor for chart specific settings called");
		this.dialog = dialog;
		this.panel = panel;
		this.parallelCoordinatesChart = chart;
		readSettings();

	}

	/**
	 * Read settings from Axis to initialise the settings in the memory.
	 */
	private void readSettings() {
		Axis axis = parallelCoordinatesChart.getAxis(panel.getAxisChoiceCombo().getSelectedItem().toString());
		log("readSettings: reading axis " + axis.getName());
		this.currentAxis = axis;
		axisColor = axis.getAxisColor();
		axisLabelColor = axis.getAxisLabelFontColor();
		ticLabelColor = axis.getAxisTicLabelFontColor();
		invertFilter = axis.isFilterInverted();
		invertAxis = axis.isAxisInverted();
		autoFitAxis = axis.isAutoFit();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent
	 * )
	 */
	public void stateChanged(ChangeEvent e) {
		this.spinnerValueChanged = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand == "Axis Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Axis Color", this.axisColor);
			if (newColor != null)
				this.axisColor = newColor;
			this.panel.getAxisColorButton().setCurrentColor(this.axisColor);
		} else if (actionCommand == "Axis Label Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Axis Label Color", this.axisLabelColor);
			if (newColor != null)
				this.axisLabelColor = newColor;
			this.panel.getAxisLabelColorButton().setCurrentColor(this.axisLabelColor);
		} else if (actionCommand == "Tic Label Color") {
			Color newColor = JColorChooser.showDialog(dialog, "Tic Label Color", this.ticLabelColor);
			if (newColor != null)
				this.ticLabelColor = newColor;
			this.panel.getTicLabelColorButton().setCurrentColor(this.ticLabelColor);
		} else if (actionCommand == "invertFilterTrue") {
			this.invertFilter = true;
		} else if (actionCommand == "invertFilterFalse") {
			this.invertFilter = false;
		} else if (actionCommand == "invertAxisTrue") {
			this.invertAxis = true;
		} else if (actionCommand == "invertAxisFalse") {
			this.invertAxis = false;
		} else if (actionCommand == "autoFitAxisTrue") {
			this.autoFitAxis = true;
			this.panel.setAxisRangeFieldsEnabled(false);
		} else if (actionCommand == "autoFitAxisFalse") {
			this.autoFitAxis = false;
			this.panel.setAxisRangeFieldsEnabled(true);
		} else if (e.getActionCommand().equals("comboBoxChanged")) {
			log(" current axis 1 is " + this.currentAxis.getName());
			log(" selected axis 1 is " + panel.getAxisChoiceCombo().getSelectedItem().toString());

			// Workaround solution. TODO: Implement memorization of all settings
			// for all Axes.
			if (this.isSettingsChanged()) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.panel, "Save changes made to axis " + currentAxis.getName() + "?", "Axis Display Settings", JOptionPane.YES_NO_OPTION))
					applySettings(currentAxis);
				else
					this.dialog.repaint();
			}
			readSettings();
			log(" current axis 2 is " + this.currentAxis.getName());
			log(" selected axis 2 is " + panel.getAxisChoiceCombo().getSelectedItem().toString());

			this.panel.setStates(this.parallelCoordinatesChart.getAxis(panel.getAxisChoiceCombo().getSelectedItem().toString()));
			this.spinnerValueChanged = false;

		} else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (AxisDisplaySettingsActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the axis color.
	 * 
	 * @return the axis color
	 */
	public Color getAxisColor() {
		return axisColor;
	}

	/**
	 * Gets the axis label color.
	 * 
	 * @return the axis label color
	 */
	public Color getAxisLabelColor() {
		return axisLabelColor;
	}

	/**
	 * Checks if filters are inverted.
	 * 
	 * @return true, if filters are inverted.
	 */
	public boolean isInvertFilter() {
		return invertFilter;
	}

	/**
	 * Gets the tic label color.
	 * 
	 * @return the tic label color
	 */
	public Color getTicLabelColor() {
		return ticLabelColor;
	}

	/**
	 * Gets the axis that is currently being edited.
	 * 
	 * @return the axis that is currently being edited.
	 */
	public Axis getCurrentAxis() {
		return currentAxis;
	}

	/**
	 * Sets the current axis.
	 * 
	 * @param currentAxis
	 *            the new current axis
	 */
	public void setCurrentAxis(Axis currentAxis) {
		this.currentAxis = currentAxis;
	}

	/**
	 * Apply settings.
	 * 
	 * @param axis
	 *            the axis
	 */
	public void applySettings(Axis axis) {
		double upperFilterValue;
		double lowerFilterValue;

		upperFilterValue = Math.max(axis.getUpperFilter().getValue(), axis.getLowerFilter().getValue());
		lowerFilterValue = Math.min(axis.getUpperFilter().getValue(), axis.getLowerFilter().getValue());

		axis.setAxisColor(this.axisColor);
		axis.setWidth((Integer) this.panel.getAxisWidthSpinner().getValue());
		axis.setAxisLabelFontColor(this.axisLabelColor);
		axis.setAxisLabelFontSize((Integer) this.panel.getAxisLabelFontSizeSpinner().getValue());
		axis.setTicLength((Integer) this.panel.getTicSizeSpinner().getValue());
		axis.setTicCount((Integer) this.panel.getTicCountSpinner().getValue());
		axis.setTicLabelFontColor(this.ticLabelColor);
		axis.setTicLabelFontSize((Integer) this.panel.getTicLabelFontSizeSpinner().getValue());
		axis.setFilterInverted(this.invertFilter);
		axis.setAxisInverted(this.invertAxis);
		axis.setAutoFit(this.autoFitAxis);
		if (this.autoFitAxis) {
			axis.autofit();
		} else {
			if (panel.getAxisMin() < panel.getAxisMax())
				axis.setMin(panel.getAxisMin());
			if (panel.getAxisMax() > axis.getMin())
				axis.setMax(panel.getAxisMax());
		}
		if (axis.isAxisInverted()) {
			log("applySettings: axis is inverted. Upper filter value should be the minimum of " + upperFilterValue + " and " + lowerFilterValue);
			axis.getUpperFilter().setValue(Math.min(upperFilterValue, lowerFilterValue));
			log("applySettings: Upper filter set to  " + axis.getUpperFilter().getValue());
			axis.getLowerFilter().setValue(Math.max(upperFilterValue, lowerFilterValue));
		} else {
			axis.getUpperFilter().setValue(Math.max(upperFilterValue, lowerFilterValue));
			axis.getLowerFilter().setValue(Math.min(upperFilterValue, lowerFilterValue));
		}
		this.panel.getChartFrame().getChartPanel().setSize(this.panel.getChartFrame().getChartPanel().getPreferredSize());
		this.panel.getChartFrame().repaint();
	}

	/**
	 * Checks if is settings changed.
	 * 
	 * @return true, if is settings changed
	 */
	private boolean isSettingsChanged() {
		boolean settingsUnChanged = (axisColor == this.currentAxis.getAxisColor() && axisLabelColor == this.currentAxis.getAxisLabelFontColor() && ticLabelColor == this.currentAxis.getAxisLabelFontColor() && invertFilter == this.currentAxis.isFilterInverted() && invertAxis == this.currentAxis.isAxisInverted() && autoFitAxis == this.currentAxis.isAutoFit() && (panel.getAxisMin() == this.currentAxis.getMin() || !this.currentAxis.getParameter().isNumeric()) && (panel.getAxisMax() == this.currentAxis.getMax() || !this.currentAxis.getParameter().isNumeric()));

		return ((!settingsUnChanged) || this.spinnerValueChanged);
	}

	/**
	 * Checks if is Axis is inverted.
	 * 
	 * @return true, if is Axis is inverted.
	 */
	public boolean isInvertAxis() {
		return invertAxis;
	}

	/**
	 * Checks if Axis is autofitted.
	 * 
	 * @return true, if Axis is autofitted
	 */
	public boolean isAutoFitAxis() {
		return autoFitAxis;
	}
}
