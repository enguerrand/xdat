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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.AxisDisplaySettingsActionListener;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.ChartSpecificDisplaySettingsDialogActionListener;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.DefaultDisplaySettingsDialogActionListener;
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.buttons.ColorChoiceButton;
import org.xdat.gui.buttons.MinMaxSpinnerModel;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;

/**
 * Panel to modify display settings for Objects of type
 * {@link org.xdat.chart.Axis} of the
 * {@link org.xdat.chart.ParallelCoordinatesChart}.
 */
public class AxisDisplaySettingsPanel extends JPanel {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The dialog on which the panel is located. */
	private ParallelCoordinatesDisplaySettingsDialog dialog;

	/** The user preferences. */
	private UserPreferences userPreferences;

	/** The action listener for this panel. */
	private AxisDisplaySettingsActionListener cmd;

	/** The content panel. */
	private TitledSubPanel contentPanel = new TitledSubPanel("");

	/** The axis color button. */
	private ColorChoiceButton axisColorButton;

	/** The axis label color button. */
	private ColorChoiceButton axisLabelColorButton;

	/** The axis label font size spinner. */
	private JSpinner axisLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));

	/** The axis width spinner. */
	private JSpinner axisWidthSpinner = new JSpinner(new MinMaxSpinnerModel(0, 1000));

	/** The tic size spinner. */
	private JSpinner ticSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));

	/** The tic count spinner. */
	private JSpinner ticCountSpinner = new JSpinner(new MinMaxSpinnerModel(1, 500));

	/** The tic label color button. */
	private ColorChoiceButton ticLabelColorButton;

	/** The tic label font size spinner. */
	private JSpinner ticLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));

	/** The invert filter true button. */
	private JRadioButton invertFilterTrueButton = new JRadioButton("Yes");

	/** The invert filter false button. */
	private JRadioButton invertFilterFalseButton = new JRadioButton("No");

	/** The invert axis true button. */
	private JRadioButton invertAxisTrueButton = new JRadioButton("Yes");

	/** The invert axis false button. */
	private JRadioButton invertAxisFalseButton = new JRadioButton("No");

	/** The auto fit axis true button. */
	private JRadioButton autoFitAxisTrueButton = new JRadioButton("Yes");

	/** The auto fit axis false button. */
	private JRadioButton autoFitAxisFalseButton = new JRadioButton("No");

	/** The axis minimum value text field. */
	private JTextField axisMinTextField = new JTextField();

	/** The axis maximum value text field. */
	private JTextField axisMaxTextField = new JTextField();

	/** The invert filter button group. */
	private ButtonGroup invertFilterButtonGroup = new ButtonGroup();

	/** The invert axis button group. */
	private ButtonGroup invertAxisButtonGroup = new ButtonGroup();

	/** The auto fit axis button group. */
	private ButtonGroup autoFitAxisButtonGroup = new ButtonGroup();

	/** The axis choice combo. */
	private JComboBox axisChoiceCombo;

	/** The cancel button. */
	private JButton cancelButton = new JButton("Cancel");

	/** The ok button. */
	private JButton okButton = new JButton("Ok");

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates a new axis display settings panel that is used to modify the
	 * user preferences.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog on which the panel is located
	 */
	public AxisDisplaySettingsPanel(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog) {
		this.mainWindow = mainWindow;
		this.userPreferences = UserPreferences.getInstance();
		this.dialog = dialog;

		buildPanel();

		// set states
		this.setInvertFilterSelection(this.userPreferences.isFilterInverted());
		this.setInvertAxisSelection(this.userPreferences.isParallelCoordinatesAxisInverted());
		this.setAutoFitAxisSelection(this.userPreferences.isParallelCoordinatesAutoFitAxis());
		this.axisLabelFontSizeSpinner.setValue(this.userPreferences.getParallelCoordinatesAxisLabelFontSize());
		this.axisWidthSpinner.setValue(this.userPreferences.getParallelCoordinatesAxisWidth());
		this.ticSizeSpinner.setValue(this.userPreferences.getParallelCoordinatesAxisTicLength());
		this.ticCountSpinner.setValue(this.userPreferences.getParallelCoordinatesAxisTicCount());
		this.ticLabelFontSizeSpinner.setValue(this.userPreferences.getParallelCoordinatesAxisTicLabelFontSize());
		Dimension maxMinTextFieldsPreferredSizes = new Dimension(60, 25);
		this.axisMinTextField.setText(Double.toString(this.userPreferences.getParallelCoordinatesAxisDefaultMin()));
		this.axisMaxTextField.setText(Double.toString(this.userPreferences.getParallelCoordinatesAxisDefaultMax()));
		this.axisMinTextField.setPreferredSize(maxMinTextFieldsPreferredSizes);
		this.axisMaxTextField.setPreferredSize(maxMinTextFieldsPreferredSizes);
	}

	/**
	 * Instantiates a new axis display settings panel that is used to modify the
	 * settings of a particular chart. These changes are not stored in the
	 * preferences and are lost when the chart is closed.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog on which the panel is located
	 * @param chartFrame
	 *            the chart frame to which the settings apply.
	 */
	public AxisDisplaySettingsPanel(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.userPreferences = UserPreferences.getInstance();
		this.dialog = dialog;
		this.chartFrame = chartFrame;
		ParallelCoordinatesChart chart = (ParallelCoordinatesChart) chartFrame.getChart();
		buildPanel();

		Vector<String> axes = new Vector<String>(0, 1);
		for (int i = 0; i < chart.getAxisCount(); i++) {
			if (chart.getAxis(i).isActive())
				axes.add(chart.getAxis(i).getName());
		}
		axisChoiceCombo = new JComboBox(axes);
		// axisChoiceCombo.addActionListener(new
		// AxisDisplaySettingsAxisChoiceComboActionListener(chart, this,
		// axisChoiceCombo));
		axisChoiceCombo.setSelectedIndex(0);
		axisChoiceCombo.setPreferredSize(new Dimension(100, 25));
		contentPanel.add(axisChoiceCombo, BorderLayout.NORTH);
		setStates(chart.getAxis(0));

	}

	/**
	 * Sets the initial states of all controls.
	 * 
	 * @param axis
	 *            the new states
	 */
	public void setStates(Axis axis) {
		this.setInvertFilterSelection(axis.isFilterInverted());
		this.setInvertAxisSelection(axis.isAxisInverted());
		this.setAutoFitAxisSelection(axis.isAutoFit());
		log("setStates: setting InvertAxisSelection to " + axis.isAxisInverted());
		this.axisLabelFontSizeSpinner.setValue(axis.getAxisLabelFontSize());
		this.axisWidthSpinner.setValue(axis.getWidth());
		this.ticSizeSpinner.setValue(axis.getTicLength());
		this.ticCountSpinner.setValue(axis.getTicCount());
		this.ticLabelFontSizeSpinner.setValue(axis.getTicLabelFontSize());
		this.axisColorButton.setCurrentColor(axis.getAxisColor());
		this.axisLabelColorButton.setCurrentColor(axis.getAxisLabelFontColor());
		this.ticLabelColorButton.setCurrentColor(axis.getAxisTicLabelFontColor());
		this.setAutoFitAxisSelection(axis.isAutoFit());
		if (axis.getParameter().isNumeric()) {
			this.autoFitAxisFalseButton.setEnabled(true);
			this.autoFitAxisTrueButton.setEnabled(true);
			this.axisMinTextField.setText(Double.toString(axis.getMin()));
			this.axisMaxTextField.setText(Double.toString(axis.getMax()));
		} else {
			this.autoFitAxisFalseButton.setEnabled(false);
			this.autoFitAxisTrueButton.setEnabled(false);

			log("setStates: setting min text field to " + axis.getParameter().getStringValueOf(axis.getMin()));
			this.axisMinTextField.setText(axis.getParameter().getStringValueOf(axis.getMin()));
			log("setStates: setting max text field to " + axis.getParameter().getStringValueOf(axis.getMax()));
			this.axisMaxTextField.setText(axis.getParameter().getStringValueOf(axis.getMax()));

		}
		this.dialog.repaint();
	}

	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		// create components

		TitledSubPanel buttonsPanel = new TitledSubPanel("");
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
		JPanel invertFilterRadioButtonsPanel = new JPanel(new GridLayout(1, 2));
		JPanel invertAxisRadioButtonsPanel = new JPanel(new GridLayout(1, 2));
		JPanel autoFitAxisRadioButtonsPanel = new JPanel(new GridLayout(1, 2));
		JPanel cancelButtonPanel = new JPanel();
		JPanel okButtonPanel = new JPanel();

		JLabel axisColorLabel = new JLabel("Axis Color");
		JLabel axisLabelColorLabel = new JLabel("Axis Label Color");
		JLabel axisLabelFontSizeLabel = new JLabel("Axis Label Fontsize");
		JLabel axisWidthLabel = new JLabel("Axis Spacing");
		JLabel ticSizeLabel = new JLabel("Tic Size");
		JLabel nrOfTicsLabel = new JLabel("Number of Tics");
		JLabel ticLabelColorLabel = new JLabel("Tic Label Color");
		JLabel ticLabelFontSizeLabel = new JLabel("Tic Label Font Size");
		JLabel invertFilterLabel = new JLabel("Invert Filter");
		JLabel invertAxisLabel = new JLabel("Invert Axis");
		JLabel autoFitAxisLabel = new JLabel("Autofit Axis");
		JLabel axisMinLabel = new JLabel("Min");
		JLabel axisMaxLabel = new JLabel("Max");

		this.axisColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesAxisColor(), "Axis Color");
		this.axisLabelColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesAxisLabelFontColor(), "Axis Label Color");
		this.ticLabelColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesAxisTicLabelFontColor(), "Tic Label Color");
		invertFilterButtonGroup.add(invertFilterTrueButton);
		invertFilterButtonGroup.add(invertFilterFalseButton);
		invertAxisButtonGroup.add(invertAxisTrueButton);
		invertAxisButtonGroup.add(invertAxisFalseButton);
		autoFitAxisButtonGroup.add(autoFitAxisTrueButton);
		autoFitAxisButtonGroup.add(autoFitAxisFalseButton);

		// set Layouts
		this.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		cancelButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setLayout(new GridLayout(1, 2));

		// add components
		this.add(contentPanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		contentPanel.add(labelPanel, BorderLayout.CENTER);
		contentPanel.add(controlsPanel, BorderLayout.EAST);

		labelPanel.add(axisColorLabel);
		JPanel axisColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		axisColorButtonPanel.add(axisColorButton);
		controlsPanel.add(axisColorButtonPanel);
		labelPanel.add(axisLabelColorLabel);
		JPanel axisLabelColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		axisLabelColorButtonPanel.add(axisLabelColorButton);
		controlsPanel.add(axisLabelColorButtonPanel);
		labelPanel.add(axisLabelFontSizeLabel);
		controlsPanel.add(axisLabelFontSizeSpinner);
		labelPanel.add(axisWidthLabel);
		controlsPanel.add(axisWidthSpinner);
		labelPanel.add(ticSizeLabel);
		controlsPanel.add(ticSizeSpinner);
		labelPanel.add(nrOfTicsLabel);
		controlsPanel.add(ticCountSpinner);
		labelPanel.add(ticLabelColorLabel);
		JPanel ticLabelColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ticLabelColorButtonPanel.add(ticLabelColorButton);
		controlsPanel.add(ticLabelColorButtonPanel);
		labelPanel.add(ticLabelFontSizeLabel);
		controlsPanel.add(ticLabelFontSizeSpinner);
		labelPanel.add(invertFilterLabel);
		invertFilterRadioButtonsPanel.add(invertFilterTrueButton);
		invertFilterRadioButtonsPanel.add(invertFilterFalseButton);
		invertFilterTrueButton.setActionCommand("invertFilterTrue");
		invertFilterFalseButton.setActionCommand("invertFilterFalse");
		controlsPanel.add(invertFilterRadioButtonsPanel);
		labelPanel.add(invertAxisLabel);
		invertAxisRadioButtonsPanel.add(invertAxisTrueButton);
		invertAxisRadioButtonsPanel.add(invertAxisFalseButton);
		invertAxisTrueButton.setActionCommand("invertAxisTrue");
		invertAxisFalseButton.setActionCommand("invertAxisFalse");
		controlsPanel.add(invertAxisRadioButtonsPanel);
		labelPanel.add(autoFitAxisLabel);
		autoFitAxisRadioButtonsPanel.add(autoFitAxisTrueButton);
		autoFitAxisRadioButtonsPanel.add(autoFitAxisFalseButton);
		autoFitAxisTrueButton.setActionCommand("autoFitAxisTrue");
		autoFitAxisFalseButton.setActionCommand("autoFitAxisFalse");
		controlsPanel.add(autoFitAxisRadioButtonsPanel);
		labelPanel.add(axisMinLabel);
		controlsPanel.add(axisMinTextField);
		labelPanel.add(axisMaxLabel);
		controlsPanel.add(axisMaxTextField);

		// buttons panel
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(okButtonPanel);
		cancelButtonPanel.add(cancelButton);
		okButtonPanel.add(okButton);
	}

	/**
	 * Sets the action listeners to the controls.
	 * 
	 * @param cmd
	 *            the new action listener
	 */
	public void setActionListener(AxisDisplaySettingsActionListener cmd) {
		this.cmd = cmd;
		axisColorButton.addActionListener(cmd);
		axisLabelColorButton.addActionListener(cmd);
		ticLabelColorButton.addActionListener(cmd);
		invertFilterTrueButton.addActionListener(cmd);
		invertFilterFalseButton.addActionListener(cmd);
		invertAxisTrueButton.addActionListener(cmd);
		invertAxisFalseButton.addActionListener(cmd);
		autoFitAxisTrueButton.addActionListener(cmd);
		autoFitAxisFalseButton.addActionListener(cmd);
		if (axisChoiceCombo != null)
			axisChoiceCombo.addActionListener(cmd);
		axisLabelFontSizeSpinner.addChangeListener(cmd);
		axisWidthSpinner.addChangeListener(cmd);
		ticSizeSpinner.addChangeListener(cmd);
		ticCountSpinner.addChangeListener(cmd);
		ticLabelFontSizeSpinner.addChangeListener(cmd);

	}

	/**
	 * Tells the panel that the settings should be applied to the user
	 * preferences
	 */
	public void setOkCancelButtonTargetDefaultSettings() {
		DefaultDisplaySettingsDialogActionListener cmd = new DefaultDisplaySettingsDialogActionListener(dialog);
		log("setOkCancelButtonTargetDefaultSettings called");
		cancelButton.addActionListener(cmd);
		okButton.addActionListener(cmd);
	}

	/**
	 * Tells the panel that the settings should be applied to the a specific
	 * chart
	 * 
	 * @param chart
	 *            chart to which the settings should be applied
	 */
	public void setOkCancelButtonTargetChart(ParallelCoordinatesChart chart) {
		log("setOkCancelButtonTargetChart called");
		cancelButton.addActionListener(new ChartSpecificDisplaySettingsDialogActionListener(this.mainWindow, dialog, chart, chartFrame));
		okButton.addActionListener(new ChartSpecificDisplaySettingsDialogActionListener(this.mainWindow, dialog, chart, chartFrame));
	}

	/**
	 * Gets the axis display settings action listener.
	 * 
	 * @return the axis display settings action listener
	 */
	public AxisDisplaySettingsActionListener getAxisDisplaySettingsActionListener() {
		return this.cmd;
	}

	/**
	 * Sets the invert filter selection.
	 * 
	 * @param invertFilterSelection
	 *            the new invert filter selection
	 */
	public void setInvertFilterSelection(boolean invertFilterSelection) {
		if (invertFilterSelection)
			invertFilterButtonGroup.setSelected(invertFilterTrueButton.getModel(), true);
		else
			invertFilterButtonGroup.setSelected(invertFilterFalseButton.getModel(), true);

	}

	/**
	 * Gets the invert filter selection.
	 * 
	 * @return the invert filter selection
	 */
	public boolean getInvertFilterSelection() {
		if (invertFilterTrueButton.getModel().equals(invertFilterButtonGroup.getSelection())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the invert axis selection.
	 * 
	 * @param invertAxisSelection
	 *            the new invert axis selection
	 */
	public void setInvertAxisSelection(boolean invertAxisSelection) {
		if (invertAxisSelection)
			invertAxisButtonGroup.setSelected(invertAxisTrueButton.getModel(), true);
		else
			invertAxisButtonGroup.setSelected(invertAxisFalseButton.getModel(), true);

	}

	/**
	 * Gets the invert axis selection.
	 * 
	 * @return the invert axis selection
	 */
	public boolean getInvertAxisSelection() {
		if (invertAxisTrueButton.getModel().equals(invertAxisButtonGroup.getSelection())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the auto fit axis selection.
	 * 
	 * @param autoFitAxisSelection
	 *            the new auto fit axis selection
	 */
	public void setAutoFitAxisSelection(boolean autoFitAxisSelection) {
		if (autoFitAxisSelection) {
			autoFitAxisButtonGroup.setSelected(autoFitAxisTrueButton.getModel(), true);
			axisMaxTextField.setEnabled(false);
			axisMinTextField.setEnabled(false);
		} else {
			autoFitAxisButtonGroup.setSelected(autoFitAxisFalseButton.getModel(), true);
			axisMaxTextField.setEnabled(true);
			axisMinTextField.setEnabled(true);
		}

	}

	/**
	 * Gets the auto fit axis selection.
	 * 
	 * @return the auto fit axis selection
	 */
	public boolean getAutoFitAxisSelection() {
		if (autoFitAxisTrueButton.getModel().equals(autoFitAxisButtonGroup.getSelection())) {
			return true;
		} else {
			return false;
		}
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

	/**
	 * Gets the axis label font size spinner.
	 * 
	 * @return the axis label font size spinner
	 */
	public JSpinner getAxisLabelFontSizeSpinner() {
		return axisLabelFontSizeSpinner;
	}

	/**
	 * Gets the tic count spinner.
	 * 
	 * @return the tic count spinner
	 */
	public JSpinner getTicCountSpinner() {
		return ticCountSpinner;
	}

	/**
	 * Gets the tic label font size spinner.
	 * 
	 * @return the tic label font size spinner
	 */
	public JSpinner getTicLabelFontSizeSpinner() {
		return ticLabelFontSizeSpinner;
	}

	/**
	 * Gets the tic size spinner.
	 * 
	 * @return the tic size spinner
	 */
	public JSpinner getTicSizeSpinner() {
		return ticSizeSpinner;
	}

	/**
	 * Gets the axis width spinner.
	 * 
	 * @return the axis width spinner
	 */
	public JSpinner getAxisWidthSpinner() {
		return axisWidthSpinner;
	}

	/**
	 * Gets the axis color button.
	 * 
	 * @return the axis color button
	 */
	public ColorChoiceButton getAxisColorButton() {
		return axisColorButton;
	}

	/**
	 * Gets the axis label color button.
	 * 
	 * @return the axis label color button
	 */
	public ColorChoiceButton getAxisLabelColorButton() {
		return axisLabelColorButton;
	}

	/**
	 * Gets the tic label color button.
	 * 
	 * @return the tic label color button
	 */
	public ColorChoiceButton getTicLabelColorButton() {
		return ticLabelColorButton;
	}

	/**
	 * Gets the axis choice combo.
	 * 
	 * @return the axis choice combo
	 */
	public JComboBox getAxisChoiceCombo() {
		return axisChoiceCombo;
	}

	/**
	 * Gets the chart frame.
	 * 
	 * @return the chart frame
	 */
	public ChartFrame getChartFrame() {
		return chartFrame;
	}

	/**
	 * Gets the axis max.
	 * 
	 * @return the axis max
	 */
	public double getAxisMax() {
		double max;
		if (this.chartFrame == null) {
			max = UserPreferences.getInstance().getParallelCoordinatesAxisDefaultMax();
		} else {
			max = ((ParallelCoordinatesChart) this.chartFrame.getChart()).getAxis(this.axisChoiceCombo.getSelectedItem().toString()).getMax();
		}
		try {
			return Double.parseDouble(axisMaxTextField.getText());
		} catch (NumberFormatException e) {
			axisMaxTextField.setText(Double.toString(max));
			return max;
		}
	}

	/**
	 * Gets the axis min.
	 * 
	 * @return the axis min
	 */
	public double getAxisMin() {
		double min;
		if (this.chartFrame == null) {
			min = UserPreferences.getInstance().getParallelCoordinatesAxisDefaultMin();
		} else {
			min = ((ParallelCoordinatesChart) this.chartFrame.getChart()).getAxis(this.axisChoiceCombo.getSelectedItem().toString()).getMin();
		}
		try {
			return Double.parseDouble(axisMinTextField.getText());
		} catch (NumberFormatException e) {
			axisMinTextField.setText(Double.toString(min));
			return min;
		}
	}

	/**
	 * When autofitting is switched off, this method is used to enable the
	 * fields where the ranges can be entered.
	 * 
	 * @param fieldsEnabled
	 *            specifies whether the axis range fields are enabled
	 */
	public void setAxisRangeFieldsEnabled(boolean fieldsEnabled) {
		this.axisMinTextField.setEnabled(fieldsEnabled);
		this.axisMaxTextField.setEnabled(fieldsEnabled);
	}
}
