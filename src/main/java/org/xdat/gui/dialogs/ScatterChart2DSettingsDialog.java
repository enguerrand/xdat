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

package org.xdat.gui.dialogs;

import org.xdat.Main;
import org.xdat.actionListeners.scatter2DChartSettings.ParallelChartFrameComboModel;
import org.xdat.actionListeners.scatter2DChartSettings.Scatter2DChartDisplaySettingsActionListener;
import org.xdat.chart.ScatterChart2D;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.data.AxisType;
import org.xdat.gui.buttons.ColorChoiceButton;
import org.xdat.gui.buttons.MinMaxSpinnerModel;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.Scatter2DChartAxisPanel;
import org.xdat.gui.panels.TitledSubPanel;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

public class ScatterChart2DSettingsDialog extends JDialog {

	static final long serialVersionUID = 2L;
	private ColorChoiceButton fgColorButton;
	private ColorChoiceButton bgColorButton;
	private ColorChoiceButton standardDesignColorButton;
	private ColorChoiceButton selectedDesignColorButton;
	private ChartFrame chartFrame;

	public ScatterChart2DSettingsDialog(Main mainWindow, ChartFrame chartFrame, ScatterChart2D scatterChart2D) {
		super(chartFrame, scatterChart2D.getTitle() + " Settings");
		this.chartFrame = chartFrame;
		chartFrame.registerComponentForRepaint(this);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setModal(false);
		buildPanel(mainWindow, chartFrame, scatterChart2D);

		Dimension parentSize = chartFrame.getSize();
		int xPos = Math.max(chartFrame.getX()+(int) (0.5 * (parentSize.width - this.getSize().getWidth())), 0);
		int yPos = Math.max(chartFrame.getY()+((int) (0.5 * (parentSize.height - this.getSize().getHeight()))), 0);
		setLocation(xPos, yPos);
		this.setVisible(true);
	}

	public void buildPanel(Main mainWindow, ChartFrame chartFrame, ScatterChart2D scatterChart2D) {
		Scatter2DChartDisplaySettingsActionListener cmd = new Scatter2DChartDisplaySettingsActionListener(mainWindow, chartFrame, scatterChart2D, this);

		// create components
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Panel to select display mode
		TitledSubPanel displayModeSelectionPanel = new TitledSubPanel("Display Mode");
		displayModeSelectionPanel.setLayout(new GridLayout(3, 1));
		ButtonGroup displayModeButtonGroup = new ButtonGroup();
		int displayMode = scatterChart2D.getScatterPlot2D().getDisplayedDesignSelectionMode();
		JRadioButton displayAllDesignsButton = new JRadioButton("Display all designs", displayMode == ScatterPlot2D.SHOW_ALL_DESIGNS);
		JRadioButton displaySelectedDesignsButton = new JRadioButton("Display selected designs", displayMode == ScatterPlot2D.SHOW_SELECTED_DESIGNS);
		JRadioButton displayActiveDesignsButton = new JRadioButton("Display designs visible in parallel chart: ", displayMode == ScatterPlot2D.SHOW_DESIGNS_ACTIVE_IN_PARALLEL_CHART);
		displayModeButtonGroup.add(displayAllDesignsButton);
		displayModeButtonGroup.add(displaySelectedDesignsButton);
		displayModeButtonGroup.add(displayActiveDesignsButton);
		JComboBox parallelChartSelectionComboBox = new JComboBox();
		ParallelChartFrameComboModel comboModel = new ParallelChartFrameComboModel(mainWindow, this.chartFrame, scatterChart2D.getScatterPlot2D());
		parallelChartSelectionComboBox.setModel(comboModel);
		mainWindow.registerComboModel(comboModel);
		JPanel visibleDesignsPanel = new JPanel(new GridLayout(1, 2));
		visibleDesignsPanel.add(displayActiveDesignsButton);
		visibleDesignsPanel.add(parallelChartSelectionComboBox);
		displayModeSelectionPanel.add(displayAllDesignsButton);
		displayModeSelectionPanel.add(displaySelectedDesignsButton);
		displayModeSelectionPanel.add(visibleDesignsPanel);
		mainPanel.add(displayModeSelectionPanel, BorderLayout.NORTH);

		displayAllDesignsButton.addActionListener(cmd);
		displaySelectedDesignsButton.addActionListener(cmd);
		displayActiveDesignsButton.addActionListener(cmd);

		// Panel to select parameters for x- and y-axis

		JPanel axisSettingsPanel = new JPanel(new GridLayout(1, 2));
		Scatter2DChartAxisPanel xAxisPanel = new Scatter2DChartAxisPanel(mainWindow, chartFrame, scatterChart2D, AxisType.X);
		Scatter2DChartAxisPanel yAxisPanel = new Scatter2DChartAxisPanel(mainWindow, chartFrame, scatterChart2D, AxisType.Y);
		axisSettingsPanel.add(yAxisPanel);
		axisSettingsPanel.add(xAxisPanel);
		mainPanel.add(axisSettingsPanel, BorderLayout.CENTER);

		// Panel for other settings
		TitledSubPanel designDisplaySettingsPanelOuter = new TitledSubPanel("Design display settings");
		designDisplaySettingsPanelOuter.setLayout(new BorderLayout());
		JPanel designDisplaySettingsPanel = new JPanel(new BorderLayout());
		designDisplaySettingsPanelOuter.add(designDisplaySettingsPanel, BorderLayout.WEST);
		designDisplaySettingsPanel.setLayout(new BorderLayout());
		JPanel designDisplaySettingsLabelPanel = new JPanel(new GridLayout(0, 1));
		JPanel designDisplaySettingsControlsPanel = new JPanel(new GridLayout(0, 1));
		designDisplaySettingsPanel.add(designDisplaySettingsLabelPanel, BorderLayout.CENTER);
		designDisplaySettingsPanel.add(designDisplaySettingsControlsPanel, BorderLayout.EAST);
		mainPanel.add(designDisplaySettingsPanelOuter, BorderLayout.SOUTH);

		JLabel fgColorLabel = new JLabel("Foreground Color   ");
		JLabel bgColorLabel = new JLabel("Background Color   ");
		JLabel standardDesignColorLabel = new JLabel("Designs standard Color   ");
		JLabel selectedDesignColorLabel = new JLabel("Selected designs Color   ");
		JLabel dataPointSizeLabel = new JLabel("Data Point Size   ");

		this.fgColorButton = new ColorChoiceButton(scatterChart2D.getScatterPlot2D().getDecorationsColor(), "Foreground Color");
		this.bgColorButton = new ColorChoiceButton(scatterChart2D.getScatterPlot2D().getBackGroundColor(), "Background Color");
		this.standardDesignColorButton = new ColorChoiceButton(scatterChart2D.getScatterPlot2D().getActiveDesignColor(), "Active Design Color");
		this.selectedDesignColorButton = new ColorChoiceButton(scatterChart2D.getScatterPlot2D().getSelectedDesignColor(), "Selected Design Color");
		JSpinner dataPointSizeSpinner = new JSpinner(new MinMaxSpinnerModel(1, 20));
		dataPointSizeSpinner.setName("dataPointSizeSpinner");
		dataPointSizeSpinner.addChangeListener(cmd);
		dataPointSizeSpinner.setValue(scatterChart2D.getScatterPlot2D().getDotRadius());

		designDisplaySettingsLabelPanel.add(fgColorLabel);
		designDisplaySettingsLabelPanel.add(bgColorLabel);
		designDisplaySettingsLabelPanel.add(standardDesignColorLabel);
		designDisplaySettingsLabelPanel.add(selectedDesignColorLabel);
		designDisplaySettingsControlsPanel.add(fgColorButton);
		designDisplaySettingsControlsPanel.add(bgColorButton);
		designDisplaySettingsControlsPanel.add(standardDesignColorButton);
		designDisplaySettingsControlsPanel.add(selectedDesignColorButton);

		fgColorButton.addActionListener(cmd);
		bgColorButton.addActionListener(cmd);
		standardDesignColorButton.addActionListener(cmd);
		selectedDesignColorButton.addActionListener(cmd);

		JPanel dataPointPanel = new JPanel(new GridLayout(1, 2));
		dataPointPanel.add(dataPointSizeLabel);
		dataPointPanel.add(dataPointSizeSpinner);
		designDisplaySettingsPanel.add(dataPointPanel, BorderLayout.SOUTH);

		// Button panel to set as / load from default settings
		JPanel panelForMainPanelAndLowerButtonsPanel = new JPanel(new BorderLayout());
		panelForMainPanelAndLowerButtonsPanel.add(mainPanel, BorderLayout.CENTER);
		TitledSubPanel lowerButtonsPanel = new TitledSubPanel("Save / restore Settings");
		lowerButtonsPanel.setLayout(new GridLayout(1, 2));
		JButton setAsDefaultButton = new JButton("Set current settings as default");
		setAsDefaultButton.addActionListener(cmd);
		JButton loadFromDefaultButton = new JButton("Load default settings");
		loadFromDefaultButton.addActionListener(cmd);
		lowerButtonsPanel.add(setAsDefaultButton);
		lowerButtonsPanel.add(loadFromDefaultButton);
		panelForMainPanelAndLowerButtonsPanel.add(lowerButtonsPanel, BorderLayout.SOUTH);

		this.add(panelForMainPanelAndLowerButtonsPanel, BorderLayout.CENTER);

		this.pack();
		this.repaint();
	}

	@Override
	public void dispose() {
		this.chartFrame.unRegisterComponentForRepaint(this);
		super.dispose();
	}

	public ColorChoiceButton getFgColorButton() {
		return fgColorButton;
	}

	public ColorChoiceButton getBgColorButton() {
		return bgColorButton;
	}

	public ColorChoiceButton getStandardDesignColorButton() {
		return standardDesignColorButton;
	}

	public ColorChoiceButton getSelectedDesignColorButton() {
		return selectedDesignColorButton;
	}
}
