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

package org.xdat.actionListeners.scatter2DChartSettings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xdat.Main;
import org.xdat.chart.ScatterChart2D;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.Scatter2DChartAxisPanel;

/**
 * ActionListener for a {@link Scatter2DChartAxisPanel} that allows to modify
 * the Display Settings of an axis of a {@link ScatterChart2D}.
 */
public class Scatter2DChartAxisPanelActionListener implements ActionListener, ChangeListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** the main window */
	private Main mainWindow;

	/** the scatter 2d chart */
	private ScatterChart2D chart;

	/** the scatter 2d chart frame */
	private ChartFrame frame;

	/** the axis */
	private int axis;

	/** the axis settings panel */
	private Scatter2DChartAxisPanel panel;

	/**
	 * Instantiates a new scatter 2d axis panel action listener to edit settings
	 * for a specific axis.
	 * 
	 * @param mainWindow 
	 * 				the main window
	 * @param chartFrame
	 *            the chartFrame
	 * @param chart
	 *            the chart
	 * @param panel
	 *            the panel carrying the controls
	 * @param axis
	 * 			  the axis
	 */
	public Scatter2DChartAxisPanelActionListener(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart, Scatter2DChartAxisPanel panel, int axis) {
		this.mainWindow = mainWindow;
		this.frame = chartFrame;
		this.chart = chart;
		this.panel = panel;
		this.axis = axis;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand == "autofitAxis" && this.axis == Scatter2DChartAxisPanel.X_AXIS) {
			boolean newState = this.panel.getAutoFitAxisCheckbox().isSelected();
			chart.getScatterPlot2D().setAutofitX(newState);
			if (newState) {
				this.panel.setTextFieldsEnabled(false);
				this.chart.getScatterPlot2D().autofitX(this.mainWindow.getDataSheet());
			} else {
				this.panel.setTextFieldsEnabled(true);
			}
		} else if (actionCommand == "autofitAxis" && this.axis == Scatter2DChartAxisPanel.Y_AXIS) {
			boolean newState = this.panel.getAutoFitAxisCheckbox().isSelected();
			this.chart.getScatterPlot2D().setAutofitY(newState);
			if (newState) {
				this.panel.setTextFieldsEnabled(false);
				this.chart.getScatterPlot2D().autofitY(this.mainWindow.getDataSheet());
			} else {
				this.panel.setTextFieldsEnabled(true);
			}
		} else if (((JTextField) e.getSource()).getName().equals("minTextField")) {
			if (this.axis == Scatter2DChartAxisPanel.X_AXIS) {
				double newMinValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMinX());
				this.chart.getScatterPlot2D().setMinX(newMinValue);
				((JTextField) e.getSource()).setText(Double.toString(newMinValue));
			} else {
				double newMinValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMinY());
				this.chart.getScatterPlot2D().setMinY(newMinValue);
				((JTextField) e.getSource()).setText(Double.toString(newMinValue));
			}
		} else if (((JTextField) e.getSource()).getName().equals("maxTextField")) {
			if (this.axis == Scatter2DChartAxisPanel.X_AXIS) {
				double newMaxValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMaxX());
				this.chart.getScatterPlot2D().setMaxX(newMaxValue);
				((JTextField) e.getSource()).setText(Double.toString(newMaxValue));
			} else {
				double newMaxValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMaxY());
				this.chart.getScatterPlot2D().setMaxY(newMaxValue);
				((JTextField) e.getSource()).setText(Double.toString(newMaxValue));
			}
		} else {
			System.out.println("Scatter2DChartDisplaySettingsActionListener: command: " + e.getActionCommand());
			System.out.println("Scatter2DChartDisplaySettingsActionListener: source: " + ((JTextField) e.getSource()).getName());
		}
		this.frame.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		int value = Integer.parseInt(((JSpinner) e.getSource()).getValue().toString());
		String source = ((JSpinner) e.getSource()).getName();
		ScatterPlot2D plot = this.chart.getScatterPlot2D();
		if (source.equals("ticCountSpinner")) {
			if (this.axis == Scatter2DChartAxisPanel.X_AXIS) {
				plot.setTicCountX(value);
			} else {
				plot.setTicCountY(value);
			}
		} else if (source.equals("ticLabelFontSizeSpinner")) {
			if (this.axis == Scatter2DChartAxisPanel.X_AXIS) {
				plot.setTicLabelFontSizeX(value);
			} else {
				plot.setTicLabelFontSizeY(value);
			}
		} else if (source.equals("axisLabelFontSizeSpinner")) {
			if (this.axis == Scatter2DChartAxisPanel.X_AXIS) {
				plot.setAxisLabelFontSizeX(value);
			} else {
				plot.setAxisLabelFontSizeY(value);
			}
		} else {
			System.out.println("Scatter2DChartDisplaySettingsChangeListener: value: " + value);
			System.out.println("Scatter2DChartDisplaySettingsChangeListener: source: " + source);
		}
		frame.repaint();
	}

	private double validateDoubleInput(String input, double fallBackValue) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			return fallBackValue;
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (Scatter2DChartAxisPanelActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
