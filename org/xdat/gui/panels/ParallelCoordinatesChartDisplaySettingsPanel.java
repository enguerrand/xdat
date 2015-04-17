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
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.ChartSpecificDisplaySettingsDialogActionListener;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.DefaultDisplaySettingsDialogActionListener;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.ParallelChartDisplaySettingsActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.buttons.ColorChoiceButton;
import org.xdat.gui.buttons.MinMaxSpinnerModel;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;

/**
 * Panel to modify display settings for a
 * {@link org.xdat.chart.ParallelCoordinatesChart}.
 */
public class ParallelCoordinatesChartDisplaySettingsPanel extends JPanel {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The dialog on which the panel is located. */
	private ParallelCoordinatesDisplaySettingsDialog dialog;

	/** The chart frame to which the settings apply. */
	private ChartFrame chartFrame;

	/** The action listener */
	private ParallelChartDisplaySettingsActionListener cmd;

	/** Checkbox to set whether axis labels should be vertically offset. */
	private JCheckBox axisLabelVerticalOffsetCheckbox = new JCheckBox();
	
	/** Checkbox to set whether anti aliasing should be used. */
	private JCheckBox antiasingCheckbox = new JCheckBox();

	/** Checkbox to set whether transparency  should be used. */
	private JCheckBox alphaCheckbox = new JCheckBox();

	/** The back ground color button. */
	private ColorChoiceButton backGroundColorButton;

	/** The active design color button. */
	private ColorChoiceButton activeDesignColorButton;

	/** The selected design color button. */
	private ColorChoiceButton selectedDesignColorButton;

	/** The filtered design color button. */
	private ColorChoiceButton filteredDesignColorButton;

	/** The filter color button. */
	private ColorChoiceButton filterColorButton;

	/** Checkbox to set whether only selected designs should be shown. */
	private JCheckBox showOnlySelectedDesignsCheckBox = new JCheckBox();

	/** The showfiltered designs true button. */
	private JRadioButton showfilteredDesignsTrueButton = new JRadioButton("Yes");

	/** The showfiltered designs false button. */
	private JRadioButton showfilteredDesignsFalseButton = new JRadioButton("No");

	/** The show design IDs true button. */
	private JRadioButton showDesignIDsTrueButton = new JRadioButton("Yes");

	/** The show design IDs false button. */
	private JRadioButton showDesignIDsFalseButton = new JRadioButton("No");

	/** The show filtered designs button group. */
	private ButtonGroup showfilteredDesignsButtonGroup = new ButtonGroup();

	/** The show design IDs button group. */
	private ButtonGroup showDesignIDsButtonGroup = new ButtonGroup();

	/** The design line thickness spinner. */
	private JSpinner designLineThicknessSpinner = new JSpinner(new MinMaxSpinnerModel(0, 10));

	/** The selected design line thickness spinner. */
	private JSpinner selectedDesignLineThicknessSpinner = new JSpinner(new MinMaxSpinnerModel(0, 10));

	/** The design label font size spinner. */
	private JSpinner designLabelFontSizeSpinner = new JSpinner(new MinMaxSpinnerModel(0, 100));

	/** The filter width spinner. */
	private JSpinner filterWidthSpinner = new JSpinner(new MinMaxSpinnerModel(1, 30));

	/** The filter height spinner. */
	private JSpinner filterHeightSpinner = new JSpinner(new MinMaxSpinnerModel(1, 60));

	/** The cancel button. */
	private JButton cancelButton = new JButton("Cancel");

	/** The ok button. */
	private JButton okButton = new JButton("Ok");

	/**
	 * Instantiates a new chart display settings panel the allows editing the
	 * default settings in the user preferences.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog on which the panel is located
	 * 
	 * @see DefaultDisplaySettingsDialogActionListener
	 */
	public ParallelCoordinatesChartDisplaySettingsPanel(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog) {
		this.mainWindow = mainWindow;
		this.dialog = dialog;

		buildPanel();
		// set states
		this.axisLabelVerticalOffsetCheckbox.setSelected(UserPreferences.getInstance().isParallelCoordinatesVerticallyOffsetAxisLabels());
		this.antiasingCheckbox.setSelected(UserPreferences.getInstance().isAntiAliasing());
		this.alphaCheckbox.setSelected(UserPreferences.getInstance().isUseAlpha());
		this.setShowFilteredDesignsSelection(UserPreferences.getInstance().isParallelCoordinatesShowFilteredDesigns());
		this.setShowDesignIDsSelection(UserPreferences.getInstance().isParallelCoordinatesShowDesignIDs());
		this.designLabelFontSizeSpinner.setValue(UserPreferences.getInstance().getParallelCoordinatesDesignLabelFontSize());
		this.designLineThicknessSpinner.setValue(UserPreferences.getInstance().getParallelCoordinatesLineThickness());
		this.selectedDesignLineThicknessSpinner.setValue(UserPreferences.getInstance().getParallelCoordinatesSelectedDesignLineThickness());
		this.backGroundColorButton.setCurrentColor(UserPreferences.getInstance().getParallelCoordinatesDefaultBackgroundColor());
		this.activeDesignColorButton.setCurrentColor(UserPreferences.getInstance().getParallelCoordinatesActiveDesignDefaultColor());
		this.selectedDesignColorButton.setCurrentColor(UserPreferences.getInstance().getParallelCoordinatesSelectedDesignDefaultColor());
		this.filteredDesignColorButton.setCurrentColor(UserPreferences.getInstance().getParallelCoordinatesFilteredDesignDefaultColor());
		this.filterColorButton.setCurrentColor(UserPreferences.getInstance().getParallelCoordinatesFilterDefaultColor());
		this.showOnlySelectedDesignsCheckBox.setSelected(UserPreferences.getInstance().isParallelCoordinatesShowOnlySelectedDesigns());
		this.filterWidthSpinner.setValue(UserPreferences.getInstance().getParallelCoordinatesFilterWidth());
		this.filterHeightSpinner.setValue(UserPreferences.getInstance().getParallelCoordinatesFilterHeight());

	}

	/**
	 * Instantiates a new chart display settings panel that allows editing a
	 * particular chart.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog on which the panel is located
	 * @param chartFrame
	 *            the chart which should be modified
	 * 
	 * @see ChartSpecificDisplaySettingsDialogActionListener
	 */
	public ParallelCoordinatesChartDisplaySettingsPanel(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.dialog = dialog;
		this.chartFrame = chartFrame;
		ParallelCoordinatesChart c = (ParallelCoordinatesChart) chartFrame.getChart();

		buildPanel();
		// set states
		this.axisLabelVerticalOffsetCheckbox.setSelected(c.isVerticallyOffsetAxisLabels());
		this.antiasingCheckbox.setSelected(c.isAntiAliasing());
		this.alphaCheckbox.setSelected(c.isUseAlpha());
		this.setShowFilteredDesignsSelection(c.isShowFilteredDesigns());
		this.setShowDesignIDsSelection(c.isShowDesignIDs());
		this.designLabelFontSizeSpinner.setValue(c.getDesignLabelFontSize());
		this.designLineThicknessSpinner.setValue(c.getLineThickness());
		this.selectedDesignLineThicknessSpinner.setValue(c.getSelectedDesignsLineThickness());
		this.backGroundColorButton.setCurrentColor(c.getBackGroundColor());
		this.activeDesignColorButton.setCurrentColor(c.getDefaultDesignColor(true, chartFrame.getChart().isUseAlpha()));
		this.selectedDesignColorButton.setCurrentColor(c.getSelectedDesignColor());
		this.filteredDesignColorButton.setCurrentColor(c.getDefaultDesignColor(false, chartFrame.getChart().isUseAlpha()));
		this.filterColorButton.setCurrentColor(c.getFilterColor());
		this.showOnlySelectedDesignsCheckBox.setSelected(c.isShowOnlySelectedDesigns());
		this.filterWidthSpinner.setValue(c.getFilterWidth());
		this.filterHeightSpinner.setValue(c.getFilterHeight());

	}

	/**
	 * Builds the panel.
	 */
	private void buildPanel() {
		// create components

		this.backGroundColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesDefaultBackgroundColor(), "Background Color");
		this.activeDesignColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesActiveDesignDefaultColor(), "Active Design Color");
		this.selectedDesignColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesSelectedDesignDefaultColor(), "Selected Design Color");
		this.filteredDesignColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesFilteredDesignDefaultColor(), "Filtered Design Color");
		this.filterColorButton = new ColorChoiceButton(UserPreferences.getInstance().getParallelCoordinatesFilterDefaultColor(), "Filter Color");
		TitledSubPanel contentPanel = new TitledSubPanel("");
		JPanel contentInnerPanel = new JPanel(new BorderLayout());
		JPanel labelPanel = new JPanel(new GridLayout(0, 1));
		JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
		TitledSubPanel buttonsPanel = new TitledSubPanel("");
		JPanel showfilteredDesignsRadioButtonsPanel = new JPanel(new GridLayout(1, 2));
		JPanel showDesignIDsRadioButtonsPanel = new JPanel(new GridLayout(1, 2));

		JLabel axisLabelVerticalOffsetLabel = new JLabel("Offset Axis Labels");
		JLabel antiAliasingLabel = new JLabel("Use Anti Aliasing");
		JLabel useAlphaLabel = new JLabel("Use Transparency");
		JLabel backGroundColorLabel = new JLabel("Background Color");
		JLabel activeDesignColorLabel = new JLabel("Active Design Color");
		JLabel selectedDesignColorLabel = new JLabel("Selected Design Color");
		JLabel filteredDesignColorLabel = new JLabel("Filtered Design Color");
		JLabel filterColorLabel = new JLabel("Filter Color");
		JLabel showOnlySelectedDesignsLabel = new JLabel("Show only selected Designs");
		JLabel showfilteredDesignsLabel = new JLabel("Show filtered Designs");
		JLabel showDesignIDsLabel = new JLabel("Show Design IDs");
		showfilteredDesignsButtonGroup.add(showfilteredDesignsTrueButton);
		showfilteredDesignsButtonGroup.add(showfilteredDesignsFalseButton);
		showDesignIDsButtonGroup.add(showDesignIDsTrueButton);
		showDesignIDsButtonGroup.add(showDesignIDsFalseButton);
		JLabel designLabelFontSizeLabel = new JLabel("Design Label Font Size ");
		JLabel designLineThicknessLabel = new JLabel("Design Line Thickness ");
		JLabel selectedDesignLineThicknessLabel = new JLabel("Selected Design Line Thickness ");
		JLabel filterWidthLabel = new JLabel("Filter Symbol Width");
		JLabel filterHeightLabel = new JLabel("Filter Symbol Height");

		JPanel cancelButtonPanel = new JPanel();
		JPanel okButtonPanel = new JPanel();

		// set Layouts
		this.setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		cancelButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setLayout(new GridLayout(1, 2));

		// add components
		this.add(contentPanel, BorderLayout.CENTER);
		contentPanel.add(contentInnerPanel, BorderLayout.NORTH);
		contentInnerPanel.add(labelPanel, BorderLayout.CENTER);
		contentInnerPanel.add(controlsPanel, BorderLayout.EAST);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		labelPanel.add(axisLabelVerticalOffsetLabel);
		JPanel axisLabelVerticalOffsetCheckboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		axisLabelVerticalOffsetCheckboxPanel.add(axisLabelVerticalOffsetCheckbox);
		controlsPanel.add(axisLabelVerticalOffsetCheckboxPanel);
		labelPanel.add(antiAliasingLabel);
		JPanel antiAliasingCheckboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		antiAliasingCheckboxPanel.add(antiasingCheckbox);
		controlsPanel.add(antiAliasingCheckboxPanel);
		labelPanel.add(useAlphaLabel);
		JPanel alphaCheckboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		alphaCheckboxPanel.add(alphaCheckbox);
		controlsPanel.add(alphaCheckboxPanel);
		labelPanel.add(backGroundColorLabel);
		JPanel backGroundColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		backGroundColorButtonPanel.add(backGroundColorButton);
		controlsPanel.add(backGroundColorButtonPanel);
		labelPanel.add(activeDesignColorLabel);
		JPanel activeDesignColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlsPanel.add(activeDesignColorButtonPanel);
		activeDesignColorButtonPanel.add(activeDesignColorButton);
		labelPanel.add(selectedDesignColorLabel);
		JPanel selectedDesignColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlsPanel.add(selectedDesignColorButtonPanel);
		selectedDesignColorButtonPanel.add(selectedDesignColorButton);
		labelPanel.add(filteredDesignColorLabel);
		JPanel filteredDesignColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filteredDesignColorButtonPanel.add(filteredDesignColorButton);
		controlsPanel.add(filteredDesignColorButtonPanel);
		labelPanel.add(filterColorLabel);
		JPanel filterColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		filterColorButtonPanel.add(filterColorButton);
		controlsPanel.add(filterColorButtonPanel);
		labelPanel.add(showfilteredDesignsLabel);
		controlsPanel.add(showfilteredDesignsRadioButtonsPanel);
		showfilteredDesignsRadioButtonsPanel.add(showfilteredDesignsTrueButton);
		showfilteredDesignsRadioButtonsPanel.add(showfilteredDesignsFalseButton);
		labelPanel.add(showDesignIDsLabel);
		controlsPanel.add(showDesignIDsRadioButtonsPanel);
		labelPanel.add(showOnlySelectedDesignsLabel);
		JPanel showOnlySelectedDesignsCheckboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		showOnlySelectedDesignsCheckboxPanel.add(showOnlySelectedDesignsCheckBox);
		controlsPanel.add(showOnlySelectedDesignsCheckboxPanel);
		labelPanel.add(designLabelFontSizeLabel);
		controlsPanel.add(designLabelFontSizeSpinner);
		labelPanel.add(selectedDesignLineThicknessLabel);
		controlsPanel.add(selectedDesignLineThicknessSpinner);
		labelPanel.add(designLineThicknessLabel);
		controlsPanel.add(designLineThicknessSpinner);
		showDesignIDsRadioButtonsPanel.add(showDesignIDsTrueButton);
		showDesignIDsRadioButtonsPanel.add(showDesignIDsFalseButton);
		showfilteredDesignsTrueButton.setActionCommand("showfilteredDesignsTrue");
		showfilteredDesignsFalseButton.setActionCommand("showfilteredDesignsFalse");
		showDesignIDsTrueButton.setActionCommand("showDesignIDsTrue");
		showDesignIDsFalseButton.setActionCommand("showDesignIDsFalse");
		labelPanel.add(filterWidthLabel);
		controlsPanel.add(filterWidthSpinner);
		labelPanel.add(filterHeightLabel);
		controlsPanel.add(filterHeightSpinner);
		// buttons panel
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(okButtonPanel);
		cancelButtonPanel.add(cancelButton);
		okButtonPanel.add(okButton);
	}

	/**
	 * Sets the action listener.
	 * 
	 * @param cmd
	 *            the new action listener
	 */
	public void setActionListener(ParallelChartDisplaySettingsActionListener cmd) {
		this.cmd = cmd;
		backGroundColorButton.addActionListener(cmd);
		activeDesignColorButton.addActionListener(cmd);
		selectedDesignColorButton.addActionListener(cmd);
		filteredDesignColorButton.addActionListener(cmd);
		filterColorButton.addActionListener(cmd);
		showfilteredDesignsTrueButton.addActionListener(cmd);
		showfilteredDesignsFalseButton.addActionListener(cmd);
		showDesignIDsTrueButton.addActionListener(cmd);
		showDesignIDsFalseButton.addActionListener(cmd);
	}

	/**
	 * Tells the panel that the settings should be applied to the user
	 * preferences.
	 * 
	 * @see DefaultDisplaySettingsDialogActionListener
	 */
	public void setOkCancelButtonTargetDefaultSettings() {
		DefaultDisplaySettingsDialogActionListener cmd = new DefaultDisplaySettingsDialogActionListener(dialog);
		log("setOkCancelButtonTargetDefaultSettings called");
		cancelButton.addActionListener(cmd);
		okButton.addActionListener(cmd);
	}

	/**
	 * Tells the panel that the settings should be applied to the the chart
	 * specified in the argument.
	 * 
	 * @param chart
	 *            specifies which chart the settings should be applied to.
	 * 
	 * @see ChartSpecificDisplaySettingsDialogActionListener
	 */
	public void setOkCancelButtonTargetChart(ParallelCoordinatesChart chart) {
		log("setOkCancelButtonTargetChart called");
		cancelButton.addActionListener(new ChartSpecificDisplaySettingsDialogActionListener(this.mainWindow, dialog, chart, chartFrame));
		okButton.addActionListener(new ChartSpecificDisplaySettingsDialogActionListener(this.mainWindow, dialog, chart, chartFrame));
	}

	/**
	 * Gets the chart display settings action listener.
	 * 
	 * @return the chart display settings action listener
	 */
	public ParallelChartDisplaySettingsActionListener getChartDisplaySettingsActionListener() {
		return this.cmd;
	}

	/**
	 * Gets the show filtered designs selection.
	 * 
	 * @return the show filtered designs selection
	 */
	public boolean getShowFilteredDesignsSelection() {
		if (showfilteredDesignsTrueButton.getModel().equals(showfilteredDesignsButtonGroup.getSelection())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the show filtered designs selection.
	 * 
	 * @param showFilteredDesignsSelection
	 *            the new show filtered designs selection
	 */
	public void setShowFilteredDesignsSelection(boolean showFilteredDesignsSelection) {
		if (showFilteredDesignsSelection)
			showfilteredDesignsButtonGroup.setSelected(showfilteredDesignsTrueButton.getModel(), true);
		else
			showfilteredDesignsButtonGroup.setSelected(showfilteredDesignsFalseButton.getModel(), true);

	}

	/**
	 * Gets the show design IDs selection.
	 * 
	 * @return the show design IDs selection
	 */
	public boolean getShowDesignIDsSelection() {
		if (showfilteredDesignsTrueButton.getModel().equals(showfilteredDesignsButtonGroup.getSelection())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Sets the show design IDs selection.
	 * 
	 * @param showFilteredDesignsSelection
	 *            the new show design IDs selection
	 */
	public void setShowDesignIDsSelection(boolean showFilteredDesignsSelection) {
		log("setShowDesignIDsSelection: argument is " + showFilteredDesignsSelection);
		if (showFilteredDesignsSelection)
			showDesignIDsButtonGroup.setSelected(showDesignIDsTrueButton.getModel(), true);
		else
			showDesignIDsButtonGroup.setSelected(showDesignIDsFalseButton.getModel(), true);

	}

	/**
	 * Gets the design label font size spinner.
	 * 
	 * @return the design label font size spinner
	 */
	public JSpinner getDesignLabelFontSizeSpinner() {
		return designLabelFontSizeSpinner;
	}

	/**
	 * Gets the design line thickness spinner.
	 * 
	 * @return the design line thickness spinner
	 */
	public JSpinner getDesignLineThicknessSpinner() {
		return designLineThicknessSpinner;
	}

	/**
	 * Gets the selected design line thickness spinner.
	 * 
	 * @return the selected design line thickness spinner
	 */
	public JSpinner getSelectedDesignLineThicknessSpinner() {
		return selectedDesignLineThicknessSpinner;
	}

	/**
	 * Gets the vertically offset axis label checkbox.
	 * 
	 * @return the vertically offset axis label checkbox
	 */
	public JCheckBox getAxisLabelVerticalOffsetCheckbox() {
		return axisLabelVerticalOffsetCheckbox;
	}
	
	/**
	 * Gets the anti aliasing checkbox.
	 * 
	 * @return the anti aliasing checkbox
	 */
	public JCheckBox getAntiAliasingCheckbox() {
		return antiasingCheckbox;
	}

	/**
	 * Gets the alpha checkbox.
	 * 
	 * @return the alpha checkbox
	 */
	public JCheckBox getAlphaCheckbox() {
		return alphaCheckbox;
	}
	
	/**
	 * Gets the checkbox that sets whether only selected designs should be
	 * shown.
	 * 
	 * @return the checkbox that sets whether only selected designs should be
	 *         shown.
	 */
	public JCheckBox getShowOnlySelectedDesignsCheckBox() {
		return showOnlySelectedDesignsCheckBox;
	}

	/**
	 * Gets the active design color button.
	 * 
	 * @return the active design color button
	 */
	public ColorChoiceButton getActiveDesignColorButton() {
		return activeDesignColorButton;
	}

	/**
	 * Gets the selected design color button.
	 * 
	 * @return the selected design color button
	 */
	public ColorChoiceButton getSelectedDesignColorButton() {
		return selectedDesignColorButton;
	}

	/**
	 * Gets the back ground color button.
	 * 
	 * @return the back ground color button
	 */
	public ColorChoiceButton getBackGroundColorButton() {
		return backGroundColorButton;
	}

	/**
	 * Gets the filter color button.
	 * 
	 * @return the filter color button
	 */
	public ColorChoiceButton getFilterColorButton() {
		return filterColorButton;
	}

	/**
	 * Gets the filtered design color button.
	 * 
	 * @return the filtered design color button
	 */
	public ColorChoiceButton getFilteredDesignColorButton() {
		return filteredDesignColorButton;
	}

	/**
	 * Gets the filter height spinner.
	 * 
	 * @return the filter height spinner
	 */
	public JSpinner getFilterHeightSpinner() {
		return filterHeightSpinner;
	}

	/**
	 * Gets the filter width spinner.
	 * 
	 * @return the filter width spinner
	 */
	public JSpinner getFilterWidthSpinner() {
		return filterWidthSpinner;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParallelCoordinatesChartDisplaySettingsPanel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
