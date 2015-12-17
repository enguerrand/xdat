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
import org.xdat.gui.buttons.MinMaxSpinnerModel;
import org.xdat.gui.frames.ChartFrame;

/**
 * Panel to modify display settings for axes on the
 * {@link org.xdat.chart.ScatterChart2D}.
 */
public class Scatter2DChartAxisPanel extends JPanel {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/** Settings for X-Axis. */
	public static final int X_AXIS = 1;

	/** Settings for Y-Axis. */
	public static final int Y_AXIS = 2;

	/** Settings for which this panel is used. */
	private int axis;

	/** The chart. */
	private ScatterChart2D chart;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The text field to set the axis min value */
	private JTextField axisMinTextField = new JTextField();

	/** The text field to set the axis max value */
	private JTextField axisMaxTextField = new JTextField();

	/** The autofit checkbox */
	private JCheckBox autoFitAxisCheckbox = new JCheckBox();

	/** The tic count spinner */
	private JSpinner ticCountSpinner = new JSpinner(new MinMaxSpinnerModel(1, 500));

	/**
	 * Instantiates a new axis display settings panel that is used to modify the
	 * settings of a particular chart. These changes are not stored in the
	 * preferences and are lost when the chart is closed.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame to which the settings apply.
	 * @param chart
	 *            the chart to which the settings apply.
	 * @param axis
	 *            the axis type (x or y)
	 */
	public Scatter2DChartAxisPanel(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart, int axis) {
		// main panel
		Scatter2DChartAxisPanelActionListener cmd = new Scatter2DChartAxisPanelActionListener(mainWindow, chartFrame, chart, this, axis);
		String title;
		String selectedParam = "";
		this.chart = chart;
		this.axis = axis;
		if (axis == X_AXIS) {
			title = "X-Axis";
			selectedParam = chart.getScatterPlot2D().getParameterForXAxis().getName();
		} else if (axis == Y_AXIS) {
			title = "Y-Axis";
			selectedParam = chart.getScatterPlot2D().getParameterForYAxis().getName();
		} else {
			throw new RuntimeException("Invalid axis type " + axis);
		}
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
		parameterSelectionList.addListSelectionListener(new Scatter2DChartAxisSelectionListener(chartFrame, chart, this, axis));
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
		axisLabelFontSizeSpinner.setName("axisLabelFontSizeSpinner");
		axisLabelFontSizeSpinner.addChangeListener(cmd);
		ticCountSpinner.setName("ticCountSpinner");
		ticCountSpinner.addChangeListener(cmd);
		JSpinner ticLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));
		ticLabelFontSizeSpinner.setName("ticLabelFontSizeSpinner");
		ticLabelFontSizeSpinner.addChangeListener(cmd);
		if (axis == X_AXIS) {
			axisLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getAxisLabelFontSizeX());
			ticCountSpinner.setValue(chart.getScatterPlot2D().getTicCountX());
			ticLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getTicLabelFontSizeX());
		} else {
			axisLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getAxisLabelFontSizeY());
			ticCountSpinner.setValue(chart.getScatterPlot2D().getTicCountY());
			ticLabelFontSizeSpinner.setValue(chart.getScatterPlot2D().getTicLabelFontSizeY());
		}

		controlsPanel.add(autoFitAxisCheckbox);
		controlsPanel.add(this.axisMinTextField);
		controlsPanel.add(this.axisMaxTextField);
		controlsPanel.add(axisLabelFontSizeSpinner);
		controlsPanel.add(ticCountSpinner);
		controlsPanel.add(ticLabelFontSizeSpinner);

		autoFitAxisCheckbox.addActionListener(cmd);
		autoFitAxisCheckbox.setActionCommand("autofitAxis");
		this.axisMinTextField.addActionListener(cmd);
		this.axisMinTextField.setName("minTextField");
		this.axisMaxTextField.addActionListener(cmd);
		this.axisMaxTextField.setName("maxTextField");

		if (axis == X_AXIS) {
			if (chart.getScatterPlot2D().isAutofitX()) {
				autoFitAxisCheckbox.setSelected(true);
				this.setTextFieldsEnabled(false);
			} else {
				autoFitAxisCheckbox.setSelected(false);
				this.setTextFieldsEnabled(true);
			}
		} else {
			if (chart.getScatterPlot2D().isAutofitY()) {
				autoFitAxisCheckbox.setSelected(true);
				this.setTextFieldsEnabled(false);
			} else {
				autoFitAxisCheckbox.setSelected(false);
				this.setTextFieldsEnabled(true);
			}
		}

	}

	/**
	 * Sets the state of the text boxes for the min and max of the axis
	 * 
	 * @param enabled
	 *            the new state
	 */
	public void setTextFieldsEnabled(boolean enabled) {
		this.axisMaxTextField.setEnabled(enabled);
		this.axisMinTextField.setEnabled(enabled);
		if (this.axis == X_AXIS) {
			this.axisMaxTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMaxX()));
			this.axisMinTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMinX()));
		} else {
			this.axisMaxTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMaxY()));
			this.axisMinTextField.setText(Double.toString(this.chart.getScatterPlot2D().getMinY()));
		}
	}

	/**
	 * Sets the state of the tic count spinner according to whether or not the
	 * parameter is numeric
	 */
	public void updateTicCountSpinnerEnabled() {
		if (this.axis == X_AXIS) {
			if (this.chart.getScatterPlot2D().getParameterForXAxis().isNumeric()) {
				this.ticCountSpinner.setEnabled(true);
			} else {
				this.ticCountSpinner.setEnabled(false);
			}
		} else if (this.axis == Y_AXIS) {
			if (this.chart.getScatterPlot2D().getParameterForYAxis().isNumeric()) {
				this.ticCountSpinner.setEnabled(true);
			} else {
				this.ticCountSpinner.setEnabled(false);
			}
		}
	}

	/**
	 * Gets the axis min text field
	 * 
	 * @return the min text field
	 */
	public JTextField getAxisMinTextField() {
		return axisMinTextField;
	}

	/**
	 * Gets the axis max text field
	 * 
	 * @return the max text field
	 */
	public JTextField getAxisMaxTextField() {
		return axisMaxTextField;
	}

	/**
	 * Gets the axis autofit checkbox
	 * 
	 * @return the axis autofit checkbox
	 */
	public JCheckBox getAutoFitAxisCheckbox() {
		return autoFitAxisCheckbox;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */

	private void log(String message) {
		if (AxisDisplaySettingsPanel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
