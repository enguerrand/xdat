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

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.xdat.Main;
import org.xdat.actionListeners.scatter2DChartSettings.Scatter2DChartAxisPanelActionListener;
import org.xdat.actionListeners.scatter2DChartSettings.Scatter2DChartAxisSelectionListener;
import org.xdat.chart.ScatterChart2D;
import org.xdat.data.AxisType;
import org.xdat.gui.buttons.MinMaxSpinnerModel;
import org.xdat.gui.frames.ChartFrame;

public class Scatter2DChartAxisPanel extends JPanel {
	private AxisType axisType;
	private ScatterChart2D chart;
	private JTextField axisMinTextField = new JTextField();
	private JTextField axisMaxTextField = new JTextField();
	private JCheckBox autoFitAxisCheckbox = new JCheckBox();
	private JSpinner ticCountSpinner = new JSpinner(new MinMaxSpinnerModel(1, 500));

	/**
	 * Instantiates a new axis display settings panel that is used to modify the
	 * settings of a particular chart. These changes are not stored in the
	 * preferences and are lost when the chart is closed.
	 */
	public Scatter2DChartAxisPanel(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart, AxisType axisType) {
		this.chart = chart;
		this.axisType = axisType;
		// main panel
		Scatter2DChartAxisPanelActionListener cmd = new Scatter2DChartAxisPanelActionListener(mainWindow, chartFrame, chart, axisType);
		String title = axisType.getLabel();
		String selectedParam = chart.getScatterPlot2D().getParameterForAxis(axisType).getName();
		TitledSubPanel mainPanel = new TitledSubPanel(title);
		this.setLayout(new GridLayout(1, 1));
		this.add(mainPanel);
		mainPanel.setLayout(new BorderLayout());

		// Parameter Selection
		JList parameterSelectionList = new JList(mainWindow.getDataSheet());
		parameterSelectionList.setVisibleRowCount(6);
		parameterSelectionList.setAutoscrolls(true);
		parameterSelectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		for (int i = 0; i < parameterSelectionList.getModel().getSize(); i++) {
			if (parameterSelectionList.getModel().getElementAt(i).equals(selectedParam)) {
				parameterSelectionList.setSelectedIndex(i);
				break;
			}
		}
		parameterSelectionList.addListSelectionListener(new Scatter2DChartAxisSelectionListener(chartFrame, chart, this, this.axisType));
		JScrollPane scrollPane = new JScrollPane(parameterSelectionList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		mainPanel.add(scrollPane, BorderLayout.NORTH);

		// Other controls

		JPanel otherControlsPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
		otherControlsPanel.add(labelPanel, BorderLayout.WEST);
		otherControlsPanel.add(controlsPanel, BorderLayout.CENTER);
		mainPanel.add(otherControlsPanel, BorderLayout.CENTER);

		JLabel autoFitAxisLabel = new JLabel("Autofit axis  ");
		JLabel axisMinLabel = new JLabel("Axis minimum   ");
		JLabel axisMaxLabel = new JLabel("Axis maximum   ");
		JLabel axisLabelFontSizeLabel = new JLabel("Axis Title Fontsize  ");
		JLabel nrOfTicsLabel = new JLabel("Number of Tics  ");
		JLabel ticLabelFontSizeLabel = new JLabel("Tic Label Font Size  ");

		labelPanel.add(autoFitAxisLabel);
		labelPanel.add(axisMinLabel);
		labelPanel.add(axisMaxLabel);
		labelPanel.add(axisLabelFontSizeLabel);
		labelPanel.add(nrOfTicsLabel);
		labelPanel.add(ticLabelFontSizeLabel);

		JSpinner axisLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));
		axisLabelFontSizeSpinner.addChangeListener(changeEvent ->
				cmd.axisLabelFontsizeUpdated((Integer) axisLabelFontSizeSpinner.getValue()));
		ticCountSpinner.addChangeListener(changeEvent ->
				cmd.ticCountUpdated((Integer) ticCountSpinner.getValue()));
		JSpinner ticLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));
		ticLabelFontSizeSpinner.addChangeListener(changeEvent ->
				cmd.ticLabelFontsizeUpdated((Integer) ticLabelFontSizeSpinner.getValue()));
		axisLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getAxisLabelFontSize(axisType));
		ticCountSpinner.setValue(chart.getScatterPlot2D().getTicCount(axisType));
		ticLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getTicLabelFontSize(axisType));

		controlsPanel.add(autoFitAxisCheckbox);
		controlsPanel.add(this.axisMinTextField);
		controlsPanel.add(this.axisMaxTextField);
		controlsPanel.add(axisLabelFontSizeSpinner);
		controlsPanel.add(ticCountSpinner);
		controlsPanel.add(ticLabelFontSizeSpinner);

		autoFitAxisCheckbox.addActionListener(actionEvent -> {
			boolean newState = autoFitAxisCheckbox.isSelected();
			setTextFieldsEnabled(!newState);
			cmd.updateAutofitAxis(newState);
		});
		this.axisMinTextField.addActionListener(cmd::minTextFieldUpdated);
		this.axisMaxTextField.addActionListener(cmd::maxTextFieldUpdated);

		if (chart.getScatterPlot2D().isAutofit(axisType)) {
			autoFitAxisCheckbox.setSelected(true);
			this.setTextFieldsEnabled(false);
		} else {
			autoFitAxisCheckbox.setSelected(false);
			this.setTextFieldsEnabled(true);
		}
	}

	private void setTextFieldsEnabled(boolean enabled) {
		this.axisMaxTextField.setEnabled(enabled);
		this.axisMinTextField.setEnabled(enabled);
		this.axisMaxTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMax(this.axisType)));
		this.axisMinTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMin(this.axisType)));
	}

	public void updateTicCountSpinnerEnabled() {
		if (this.chart.getScatterPlot2D().getParameterForAxis(this.axisType).isNumeric()) {
			this.ticCountSpinner.setEnabled(true);
		} else {
			this.ticCountSpinner.setEnabled(false);
		}
	}
}
