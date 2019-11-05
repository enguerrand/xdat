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
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.ParallelCoordinatesChartSidebarPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartSpecificDisplaySettingsDialogActionListener implements ActionListener {

	private final Main mainWindow;
	private ParallelCoordinatesDisplaySettingsDialog dialog;
	private ParallelCoordinatesChart chart;
	private ChartFrame chartFrame;

	public ChartSpecificDisplaySettingsDialogActionListener(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.chart = chart;
		this.dialog = dialog;
		this.chartFrame = chartFrame;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
        DataSheet dataSheet = this.mainWindow.getDataSheet();
		if (actionCommand.equals("Ok")) {
			Axis axis = this.chart.getAxis(this.dialog.getAxisDisplaySettingsPanel().getAxisChoiceCombo().getSelectedItem().toString());

			chart.setVerticallyOffsetAxisLabels(this.dialog.getChartDisplaySettingsPanel().getAxisLabelVerticalOffsetCheckbox().isSelected());
			chart.setAntiAliasing(this.dialog.getChartDisplaySettingsPanel().getAntiAliasingCheckbox().isSelected());
			chart.setUseAlpha(this.dialog.getChartDisplaySettingsPanel().getAlphaCheckbox().isSelected());
			((ParallelCoordinatesChartSidebarPanel)chartFrame.getSidePanel()).setAlphaSlidersEnabled(chart.isUseAlpha());
			chart.setBackGroundColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getBackGroundColor());
			chart.setActiveDesignColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getActiveDesignColor());
			chart.setSelectedDesignColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getSelectedDesignColor());
			chart.setFilteredDesignColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getFilteredDesignColor());
			chart.setFilterColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getFilterColor());
			chart.setShowOnlySelectedDesigns(this.dialog.getChartDisplaySettingsPanel().getShowOnlySelectedDesignsCheckBox().isSelected());
			chart.setShowDesignIDs(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().isShowDesignIDs());
			chart.setShowFilteredDesigns(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().isShowFilteredDesigns());
			chart.setDesignLabelFontSize((Integer) this.dialog.getChartDisplaySettingsPanel().getDesignLabelFontSizeSpinner().getValue());
			chart.setLineThickness((Integer) this.dialog.getChartDisplaySettingsPanel().getDesignLineThicknessSpinner().getValue());
			chart.setSelectedDesignsLineThickness((Integer) this.dialog.getChartDisplaySettingsPanel().getSelectedDesignLineThicknessSpinner().getValue());
			chart.setFilterWidth((Integer) this.dialog.getChartDisplaySettingsPanel().getFilterWidthSpinner().getValue());
			chart.setFilterHeight((Integer) this.dialog.getChartDisplaySettingsPanel().getFilterHeightSpinner().getValue());

			axis.setAxisColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getAxisColor());
			axis.setWidth((Integer) this.dialog.getAxisDisplaySettingsPanel().getAxisWidthSpinner().getValue());
			axis.setAxisLabelFontColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getAxisLabelColor());
			axis.setAxisLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getAxisLabelFontSizeSpinner().getValue(), dataSheet);
			axis.setTicLength((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicSizeSpinner().getValue());
			axis.setTicCount((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicCountSpinner().getValue(), dataSheet);
			axis.setTicLabelDigitCount((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicLabelDigitCountSpinner().getValue());
			axis.setTicLabelFontColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getTicLabelColor());
			axis.setTicLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicLabelFontSizeSpinner().getValue());
			axis.setFilterInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertFilter());
			axis.setAxisInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertAxis(), dataSheet);
			axis.setAutoFit(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isAutoFitAxis());
			if (axis.isAutoFit()) {
				axis.autofit(mainWindow.getDataSheet());
			} else {
				if (this.dialog.getAxisDisplaySettingsPanel().getAxisMin() < this.dialog.getAxisDisplaySettingsPanel().getAxisMax())
					axis.setMin(this.dialog.getAxisDisplaySettingsPanel().getAxisMin(), mainWindow.getDataSheet());
				if (this.dialog.getAxisDisplaySettingsPanel().getAxisMax() > axis.getMin(mainWindow.getDataSheet()))
					axis.setMax(this.dialog.getAxisDisplaySettingsPanel().getAxisMax(), mainWindow.getDataSheet());
			}

			this.chartFrame.getChartPanel().setSize(this.chartFrame.getChartPanel().getPreferredSize());
			this.chartFrame.repaint();
			this.dialog.dispose();
		} else if (actionCommand.equals("Cancel")) {
			this.dialog.dispose();
		} else {
			System.out.println("ChartSpecificDisplaySettingsDialogActionListener: " + e.getActionCommand());
		}
	}
}
