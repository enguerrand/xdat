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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;
import org.xdat.gui.panels.AxisDisplaySettingsPanel;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AxisDisplaySettingsActionListener implements ActionListener, ChangeListener {

	private JDialog dialog;
	private Color axisColor;
	private Color axisLabelColor;
	private Color ticLabelColor;
	private boolean invertFilter;
	private boolean invertAxis;
	private boolean autoFitAxis;
	private Main mainWindow;
	private AxisDisplaySettingsPanel panel;
	private ParallelCoordinatesChart parallelCoordinatesChart;
	private Axis currentAxis;
	private boolean spinnerValueChanged = false;

	public AxisDisplaySettingsActionListener(Main mainWindow, JDialog dialog, AxisDisplaySettingsPanel panel) {
		this.mainWindow = mainWindow;
		this.panel = panel;
		this.dialog = dialog;
		axisColor = UserPreferences.getInstance().getParallelCoordinatesAxisColor();
		axisLabelColor = UserPreferences.getInstance().getParallelCoordinatesAxisLabelFontColor();
		ticLabelColor = UserPreferences.getInstance().getParallelCoordinatesAxisTicLabelFontColor();
		invertFilter = UserPreferences.getInstance().isFilterInverted();
		autoFitAxis = UserPreferences.getInstance().isParallelCoordinatesAutoFitAxis();

	}

	public AxisDisplaySettingsActionListener(JDialog dialog, AxisDisplaySettingsPanel panel, ParallelCoordinatesChart chart) {
		this.dialog = dialog;
		this.panel = panel;
		this.parallelCoordinatesChart = chart;
		readSettings();

	}

	private void readSettings() {
		Axis axis = parallelCoordinatesChart.getAxis(panel.getAxisChoiceCombo().getSelectedItem().toString());
		this.currentAxis = axis;
		axisColor = axis.getAxisColor();
		axisLabelColor = axis.getAxisLabelFontColor();
		ticLabelColor = axis.getAxisTicLabelFontColor();
		invertFilter = axis.isFilterInverted();
		invertAxis = axis.isAxisInverted();
		autoFitAxis = axis.isAutoFit();

	}

	public void stateChanged(ChangeEvent e) {
		this.spinnerValueChanged = true;
	}

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

			// Workaround solution. TODO: Implement memorization of all settings
			// for all Axes.
			if (this.isSettingsChanged()) {
				if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this.panel, "Save changes made to axis " + currentAxis.getName() + "?", "Axis Display Settings", JOptionPane.YES_NO_OPTION))
					applySettings(currentAxis);
				else
					this.dialog.repaint();
			}
			readSettings();

			this.panel.setStates(this.parallelCoordinatesChart.getAxis(panel.getAxisChoiceCombo().getSelectedItem().toString()));
			this.spinnerValueChanged = false;

		} else {
			System.out.println(e.getActionCommand());
		}
	}

	public Color getAxisColor() {
		return axisColor;
	}

	public Color getAxisLabelColor() {
		return axisLabelColor;
	}

	public boolean isInvertFilter() {
		return invertFilter;
	}

	public Color getTicLabelColor() {
		return ticLabelColor;
	}

	public void applySettings(Axis axis) {
		double upperFilterValue;
		double lowerFilterValue;
		DataSheet dataSheet = this.mainWindow.getDataSheet();

		upperFilterValue = Math.max(axis.getUpperFilter().getValue(), axis.getLowerFilter().getValue());
		lowerFilterValue = Math.min(axis.getUpperFilter().getValue(), axis.getLowerFilter().getValue());

		axis.setAxisColor(this.axisColor);
		axis.setWidth((Integer) this.panel.getAxisWidthSpinner().getValue());
		axis.setAxisLabelFontColor(this.axisLabelColor);
		axis.setAxisLabelFontSize((Integer) this.panel.getAxisLabelFontSizeSpinner().getValue(), dataSheet);
		axis.setTicLength((Integer) this.panel.getTicSizeSpinner().getValue());
		axis.setTicCount((Integer) this.panel.getTicCountSpinner().getValue(), dataSheet);
		axis.setTicLabelDigitCount((Integer) this.panel.getTicLabelDigitCountSpinner().getValue());
		axis.setTicLabelFontColor(this.ticLabelColor);
		axis.setTicLabelFontSize((Integer) this.panel.getTicLabelFontSizeSpinner().getValue());
		axis.setFilterInverted(this.invertFilter);
		axis.setAxisInverted(this.invertAxis, dataSheet);
		axis.setAutoFit(this.autoFitAxis);
		if (this.autoFitAxis) {
			axis.autofit(mainWindow.getDataSheet());
		} else {
			if (panel.getAxisMin() < panel.getAxisMax())
				axis.setMin(panel.getAxisMin(), mainWindow.getDataSheet());
			if (panel.getAxisMax() > axis.getMin(mainWindow.getDataSheet()))
				axis.setMax(panel.getAxisMax(), mainWindow.getDataSheet());
		}
		if (axis.isAxisInverted()) {
			axis.getUpperFilter().setValue(Math.min(upperFilterValue, lowerFilterValue), dataSheet);
			axis.getLowerFilter().setValue(Math.max(upperFilterValue, lowerFilterValue), dataSheet);
		} else {
			axis.getUpperFilter().setValue(Math.max(upperFilterValue, lowerFilterValue), dataSheet);
			axis.getLowerFilter().setValue(Math.min(upperFilterValue, lowerFilterValue), dataSheet);
		}
		this.panel.getChartFrame().getChartPanel().setSize(this.panel.getChartFrame().getChartPanel().getPreferredSize());
		this.panel.getChartFrame().repaint();
	}

	private boolean isSettingsChanged() {
		boolean settingsUnChanged = (
				axisColor == this.currentAxis.getAxisColor() &&
				axisLabelColor == this.currentAxis.getAxisLabelFontColor() &&
				ticLabelColor == this.currentAxis.getAxisLabelFontColor() &&
				invertFilter == this.currentAxis.isFilterInverted() &&
				invertAxis == this.currentAxis.isAxisInverted() &&
				autoFitAxis == this.currentAxis.isAutoFit() && (
						panel.getAxisMin() == this.currentAxis.getMin(mainWindow.getDataSheet()) || !this.currentAxis.getParameter().isNumeric()
				) && (
						panel.getAxisMax() == this.currentAxis.getMax(mainWindow.getDataSheet()) || !this.currentAxis.getParameter().isNumeric()
				));

		return ((!settingsUnChanged) || this.spinnerValueChanged);
	}

	public boolean isInvertAxis() {
		return invertAxis;
	}

	public boolean isAutoFitAxis() {
		return autoFitAxis;
	}
}
