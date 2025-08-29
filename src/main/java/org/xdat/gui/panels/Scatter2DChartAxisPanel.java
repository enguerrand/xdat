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

package org.xdat.gui.panels;

import org.xdat.Main;
import org.xdat.actionListeners.scatter2DChartSettings.Scatter2DChartAxisPanelActionListener;
import org.xdat.chart.ScatterChart2D;
import org.xdat.data.AxisType;
import org.xdat.data.Parameter;
import org.xdat.gui.controls.MinMaxSpinnerModel;
import org.xdat.gui.controls.RightAlignedSpinner;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.settings.Key;
import org.xdat.settings.SettingsGroup;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

public class Scatter2DChartAxisPanel extends JPanel {
	private final SettingsGroup settings;
	private final AxisType axisType;
	private final ScatterChart2D chart;
	private final JTextField axisMinTextField = new JTextField();
	private final JTextField axisMaxTextField = new JTextField();
	private final JSpinner ticCountSpinner = new RightAlignedSpinner(new MinMaxSpinnerModel(1, 500));
	private final JSpinner ticLabelDigitCountSpinner;
	private final JSpinner axisLabelFontSizeSpinner;
	private final JSpinner ticLabelFontSizeSpinner;
	private boolean updating = false;
	private final SettingComponents autoFitComp;

	/**
	 * Instantiates a new axis display settings panel that is used to modify the
	 * settings of a particular chart. These changes are not stored in the
	 * preferences and are lost when the chart is closed.
	 */
	public Scatter2DChartAxisPanel(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart, AxisType axisType) {
		this.chart = chart;
		this.axisType = axisType;
		this.settings = chart.getAxisSettings(axisType);

		// main panel
		Scatter2DChartAxisPanelActionListener cmd = new Scatter2DChartAxisPanelActionListener(mainWindow, chartFrame, chart, axisType);
		String title = axisType.getLabel();
		String selectedParam = chart.getScatterPlot2D().getParameterForAxis(axisType).getName();
		TitledSubPanel mainPanel = new TitledSubPanel(title);
		this.setLayout(new GridLayout(1, 1));
		this.add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		// Parameter Selection
		Vector<Parameter> parametersList = new Vector<>(mainWindow.getDataSheet().getParameters());
		JList<Parameter> parameterSelectionList = new JList<>(parametersList);
		parameterSelectionList.setVisibleRowCount(6);
		parameterSelectionList.setAutoscrolls(true);
		parameterSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		parameterSelectionList.setSelectedValue(selectedParam, true);

		parameterSelectionList.addListSelectionListener(listSelectionEvent -> {
			Parameter selectedValue = parameterSelectionList.getSelectedValue();
			if (selectedValue != null) {
				chart.getScatterPlot2D().setParameterForAxis(axisType, selectedValue);
				currentParameterChanged();
				chartFrame.repaint();
			}
		});
		JScrollPane scrollPane = new JScrollPane(parameterSelectionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(scrollPane, BorderLayout.NORTH);

		// setting controls
		autoFitComp = SettingsPanelFactory.standaloneFrom(settings.getBooleanSetting(Key.getScatterChartAutoFitAxis(axisType)));
		// Other controls

		JPanel otherControlsPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
		otherControlsPanel.add(labelPanel, BorderLayout.WEST);
		otherControlsPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(otherControlsPanel, BorderLayout.CENTER);

		JComponent autoFitAxisLabel = autoFitComp.getLabel();
		JLabel axisMinLabel = new JLabel("Axis minimum   ");
		JLabel axisMaxLabel = new JLabel("Axis maximum   ");
		JLabel axisLabelFontSizeLabel = new JLabel("Axis Title Fontsize  ");
		JLabel nrOfTicsLabel = new JLabel("Number of Tics  ");
		JLabel ticLabelFontSizeLabel =   new JLabel("Tic Label Font Size  ");
		JLabel ticLabelDigitCountLabel = new JLabel("Tic Label Digit Count  ");

		labelPanel.add(autoFitAxisLabel);
		labelPanel.add(axisMinLabel);
		labelPanel.add(axisMaxLabel);
		labelPanel.add(axisLabelFontSizeLabel);
		labelPanel.add(nrOfTicsLabel);
		labelPanel.add(ticLabelFontSizeLabel);
		labelPanel.add(ticLabelDigitCountLabel);

		axisLabelFontSizeSpinner = new RightAlignedSpinner(new MinMaxSpinnerModel(0, 100));
		axisLabelFontSizeSpinner.addChangeListener(changeEvent -> {
			if (updating) {
				return;
			}
			cmd.axisLabelFontsizeUpdated((Integer) axisLabelFontSizeSpinner.getValue());
		});
		ticCountSpinner.addChangeListener(changeEvent -> {
			if (updating) {
				return;
			}
			cmd.ticCountUpdated((Integer) ticCountSpinner.getValue());
		});
		ticLabelFontSizeSpinner = new RightAlignedSpinner(new MinMaxSpinnerModel(0, 100));
		ticLabelFontSizeSpinner.addChangeListener(changeEvent -> {
			if (updating) {
				return;
			}
			cmd.ticLabelFontsizeUpdated((Integer) ticLabelFontSizeSpinner.getValue());
		});

		ticLabelDigitCountSpinner = new RightAlignedSpinner(new MinMaxSpinnerModel(0, 20));

		controlsPanel.add(autoFitComp.getControl());
		controlsPanel.add(this.axisMinTextField);
		controlsPanel.add(this.axisMaxTextField);
		controlsPanel.add(axisLabelFontSizeSpinner);
		controlsPanel.add(ticCountSpinner);
		controlsPanel.add(ticLabelFontSizeSpinner);
		controlsPanel.add(ticLabelDigitCountSpinner);

		this.settings.getBooleanSetting(Key.getScatterChartAutoFitAxis(axisType)).addListener((source, transaction) -> {
			if (updating) {
				return;
			}
			boolean newState = source.get();
			this.updating = true;
			updateMinMaxFields();
			this.updating = false;
			if (newState) {
				this.chart.getScatterPlot2D().autofit(mainWindow.getDataSheet(), this.axisType);
			}
		});
		this.axisMinTextField.addActionListener(e -> {
			if (updating) {
				return;
			}
			cmd.minTextFieldUpdated(e);
		});
		this.axisMaxTextField.addActionListener(e -> {
			if (updating) {
				return;
			}
			cmd.maxTextFieldUpdated(e);
		});

		ticLabelDigitCountSpinner.addChangeListener(changeEvent -> {
			if (updating) {
				return;
			}
			cmd.ticLabelDigitCountUpdated((Integer) ticLabelDigitCountSpinner.getValue());
		});

		currentParameterChanged();
	}

	private void updateMinMaxFields() {
		boolean autoFit = settings.getBoolean(Key.getScatterChartAutoFitAxis(this.axisType));
		boolean numeric = this.chart.getScatterPlot2D().getParameterForAxis(this.axisType).isNumeric();
		this.axisMaxTextField.setEnabled(!autoFit && numeric);
		this.axisMinTextField.setEnabled(!autoFit && numeric);
		this.axisMaxTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMax(this.axisType)));
		this.axisMinTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMin(this.axisType)));
	}

	private void currentParameterChanged() {
		this.updating = true;
		Parameter parameter = chart.getScatterPlot2D().getParameterForAxis(this.axisType);
		boolean numeric = parameter.isNumeric();
		if (parameter.isNumeric()) {
			ticLabelDigitCountSpinner.setValue(parameter.getTicLabelDigitCount());
			ticLabelDigitCountSpinner.setEnabled(true);
		} else {
			ticLabelDigitCountSpinner.setValue(0);
			ticLabelDigitCountSpinner.setEnabled(false);
		}
		ticCountSpinner.setEnabled(numeric);
		ticCountSpinner.setValue(numeric ? chart.getScatterPlot2D().getTicCount(axisType) : 0);
		axisLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getAxisLabelFontSize(axisType));
		ticLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getTicLabelFontSize(axisType));
		updateMinMaxFields();

		autoFitComp.getControl().setEnabled(numeric);
		this.updating = false;
	}
}
