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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.ParallelCoordinatesChartDisplaySettingsPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartSidebarPanel;

/**
 * ActionListener for the Ok button of a
 * {@link ParallelCoordinatesChartDisplaySettingsPanel} that was instantiated
 * using the constructor form
 * {@link ParallelCoordinatesChartDisplaySettingsPanel}.
 * <p>
 * When a ChartDisplaySettingsPanel is instantiated with a ChartFrame object as
 * the last argument, the settings made in the panel are applied to that
 * specific Chart, rather than to the default settings in the
 * {@link UserPreferences}. In order to do this correctly when the Ok button is
 * pressed, the button must use this dedicated ActionListener.
 * 
 * @see DefaultDisplaySettingsDialogActionListener
 */
public class ChartSpecificDisplaySettingsDialogActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The dialog. */
	private ParallelCoordinatesDisplaySettingsDialog dialog;

	/** The chart. */
	private ParallelCoordinatesChart chart;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates a new chart specific display settings dialog action
	 * listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog
	 * @param chart
	 *            the chart
	 * @param chartFrame
	 *            the chart frame
	 */
	public ChartSpecificDisplaySettingsDialogActionListener(Main mainWindow, ParallelCoordinatesDisplaySettingsDialog dialog, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		this.chart = chart;
		this.dialog = dialog;
		this.chartFrame = chartFrame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand == "Ok") {
			Axis axis = this.chart.getAxis(this.dialog.getAxisDisplaySettingsPanel().getAxisChoiceCombo().getSelectedItem().toString());

			log(" Ok pressed");
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
			axis.setAxisLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getAxisLabelFontSizeSpinner().getValue());
			axis.setTicLength((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicSizeSpinner().getValue());
			axis.setTicCount((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicCountSpinner().getValue());
			axis.setTicLabelFontColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getTicLabelColor());
			axis.setTicLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicLabelFontSizeSpinner().getValue());
			axis.setFilterInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertFilter());
			axis.setAxisInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertAxis());
			axis.setAutoFit(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isAutoFitAxis());
			if (axis.isAutoFit()) {
				axis.autofit();
			} else {
				if (this.dialog.getAxisDisplaySettingsPanel().getAxisMin() < this.dialog.getAxisDisplaySettingsPanel().getAxisMax())
					axis.setMin(this.dialog.getAxisDisplaySettingsPanel().getAxisMin());
				if (this.dialog.getAxisDisplaySettingsPanel().getAxisMax() > axis.getMin())
					axis.setMax(this.dialog.getAxisDisplaySettingsPanel().getAxisMax());
			}

			this.chartFrame.getChartPanel().setSize(this.chartFrame.getChartPanel().getPreferredSize());
			this.chartFrame.repaint();
			this.dialog.dispose();
		} else if (actionCommand == "Cancel") {
			this.dialog.dispose();
		} else if (actionCommand == "Yes" || actionCommand == "No") {
			// Do nothing
		} else {
			System.out.println("ChartSpecificDisplaySettingsDialogActionListener: " + e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartSpecificDisplaySettingsDialogActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
