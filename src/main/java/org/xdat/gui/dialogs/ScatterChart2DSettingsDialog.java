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
import org.xdat.chart.ScatterChart2D;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.data.AxisType;
import org.xdat.data.DatasheetListener;
import org.xdat.gui.controls.ColorChoiceButton;
import org.xdat.gui.controls.MinMaxSpinnerModel;
import org.xdat.gui.controls.RightAlignedSpinner;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.Scatter2DChartAxisPanel;
import org.xdat.gui.panels.TitledSubPanel;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ScatterChart2DSettingsDialog extends JDialog {

	private ChartFrame chartFrame;
	private final Runnable onClosed;

	public ScatterChart2DSettingsDialog(Main mainWindow, ChartFrame chartFrame, ScatterChart2D scatterChart2D) {
		super(chartFrame, scatterChart2D.getTitle() + " Settings");
		this.chartFrame = chartFrame;
		DatasheetListener listener = new DatasheetListener() {
			@Override
			public void onClustersChanged() {
				repaint();
			}

			@Override
			public void onDataPanelUpdateRequired() {
			}

			@Override
			public void onDataChanged(boolean[] autoFitRequired, boolean[] filterResetRequired, boolean[] applyFiltersRequired, boolean parametersChanged) {
				repaint();
			}
		};
		mainWindow.addDataSheetSubListener(listener);
		onClosed = () -> mainWindow.removeDataSheetSubListener(listener);
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
		// create components
		this.getContentPane().removeAll();
		this.getContentPane().setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());

		// Panel to select display mode
		TitledSubPanel displayModeSelectionPanel = new TitledSubPanel("Display Mode");
		displayModeSelectionPanel.setLayout(new GridLayout(3, 1));
		ButtonGroup displayModeButtonGroup = new ButtonGroup();
		ScatterPlot2D scatterPlot2D = scatterChart2D.getScatterPlot2D();
		int displayMode = scatterPlot2D.getDisplayedDesignSelectionMode();
		JRadioButton displayAllDesignsButton = buildDisplayModeButton("Display all designs", displayMode, ScatterPlot2D.SHOW_ALL_DESIGNS, scatterPlot2D, chartFrame);
		JRadioButton displaySelectedDesignsButton = buildDisplayModeButton("Display selected designs", displayMode, ScatterPlot2D.SHOW_SELECTED_DESIGNS, scatterPlot2D, chartFrame);
		JRadioButton displayActiveDesignsButton = buildDisplayModeButton("Display designs visible in parallel chart: ", displayMode, ScatterPlot2D.SHOW_DESIGNS_ACTIVE_IN_PARALLEL_CHART, scatterPlot2D, chartFrame);
		displayModeButtonGroup.add(displayAllDesignsButton);
		displayModeButtonGroup.add(displaySelectedDesignsButton);
		displayModeButtonGroup.add(displayActiveDesignsButton);
		JComboBox<String> parallelChartSelectionComboBox = new JComboBox<>();
		ParallelChartFrameComboModel comboModel = new ParallelChartFrameComboModel(mainWindow, this.chartFrame, scatterPlot2D);
		parallelChartSelectionComboBox.setModel(comboModel);
		mainWindow.registerComboModel(comboModel);
		JPanel visibleDesignsPanel = new JPanel(new GridLayout(1, 2));
		visibleDesignsPanel.add(displayActiveDesignsButton);
		visibleDesignsPanel.add(parallelChartSelectionComboBox);
		displayModeSelectionPanel.add(displayAllDesignsButton);
		displayModeSelectionPanel.add(displaySelectedDesignsButton);
		displayModeSelectionPanel.add(visibleDesignsPanel);
		mainPanel.add(displayModeSelectionPanel, BorderLayout.NORTH);

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

		ColorChoiceButton fgColorButton = buildColorChoiceButton(
				"Foreground Color",
				scatterPlot2D::getDecorationsColor,
				scatterPlot2D::setDecorationsColor,
				chartFrame
		);

		ColorChoiceButton bgColorButton = buildColorChoiceButton(
				"Background Color",
				scatterPlot2D::getBackGroundColor,
				scatterPlot2D::setBackGroundColor,
				chartFrame
		);

		ColorChoiceButton standardDesignColorButton = buildColorChoiceButton(
				"Active Design Color",
				scatterPlot2D::getActiveDesignColor,
				scatterPlot2D::setActiveDesignColor,
				chartFrame
		);

		ColorChoiceButton selectedDesignColorButton = buildColorChoiceButton(
				"Selected Design Color",
				scatterPlot2D::getSelectedDesignColor,
				scatterPlot2D::setSelectedDesignColor,
				chartFrame
		);

		JSpinner dataPointSizeSpinner = new RightAlignedSpinner(new MinMaxSpinnerModel(1, 20));
		dataPointSizeSpinner.addChangeListener(changeEvent -> {
			int value = Integer.parseInt(dataPointSizeSpinner.getValue().toString());
			scatterPlot2D.setDotRadius(value);
			chartFrame.repaint();
		});
		dataPointSizeSpinner.setValue(scatterPlot2D.getDotRadius());

		designDisplaySettingsLabelPanel.add(fgColorLabel);
		designDisplaySettingsLabelPanel.add(bgColorLabel);
		designDisplaySettingsLabelPanel.add(standardDesignColorLabel);
		designDisplaySettingsLabelPanel.add(selectedDesignColorLabel);
		designDisplaySettingsControlsPanel.add(fgColorButton);
		designDisplaySettingsControlsPanel.add(bgColorButton);
		designDisplaySettingsControlsPanel.add(standardDesignColorButton);
		designDisplaySettingsControlsPanel.add(selectedDesignColorButton);

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
		setAsDefaultButton.addActionListener(e -> scatterChart2D.setCurrentSettingsAsDefault());
		JButton loadFromDefaultButton = new JButton("Load default settings");
		loadFromDefaultButton.addActionListener(e -> {
			scatterChart2D.resetDisplaySettingsToDefault(mainWindow.getDataSheet());
			buildPanel(mainWindow, chartFrame, scatterChart2D);
		});
		lowerButtonsPanel.add(setAsDefaultButton);
		lowerButtonsPanel.add(loadFromDefaultButton);
		panelForMainPanelAndLowerButtonsPanel.add(lowerButtonsPanel, BorderLayout.SOUTH);

		this.add(panelForMainPanelAndLowerButtonsPanel, BorderLayout.CENTER);

		this.pack();
		this.repaint();
	}

	private JRadioButton buildDisplayModeButton(String label, int currentDisplayMode, int buttonDisplayMode, ScatterPlot2D plot, ChartFrame chartFrame) {
		JRadioButton displayAllDesignsButton = new JRadioButton(label, currentDisplayMode == buttonDisplayMode);
		displayAllDesignsButton.addActionListener(actionEvent -> {
			plot.setDisplayedDesignSelectionMode(buttonDisplayMode);
			chartFrame.repaint();
		});
		return displayAllDesignsButton;
	}

	private ColorChoiceButton buildColorChoiceButton(String label, Supplier<Color> getter, Consumer<Color> setter, ChartFrame chartFrame) {
		ColorChoiceButton colorChoiceButton = new ColorChoiceButton(getter.get(), label);
		colorChoiceButton.addActionListener(actionEvent -> {
			Color newColor = JColorChooser.showDialog(this, label, getter.get());
			if (newColor != null) {
				setter.accept(newColor);
				colorChoiceButton.setCurrentColor(newColor);
				chartFrame.repaint();
			}
		});
		return colorChoiceButton;
	}

	@Override
	public void dispose() {
		super.dispose();
		onClosed.run();
	}
}
