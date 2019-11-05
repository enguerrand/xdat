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

import org.xdat.Main;
import org.xdat.chart.ScatterChart2D;
import org.xdat.data.AxisType;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JTextField;
import java.awt.event.ActionEvent;

public class Scatter2DChartAxisPanelActionListener {

	private Main mainWindow;
	private ScatterChart2D chart;
	private ChartFrame frame;
	private AxisType axisType;

	public Scatter2DChartAxisPanelActionListener(Main mainWindow, ChartFrame chartFrame, ScatterChart2D chart, AxisType axisType) {
		this.mainWindow = mainWindow;
		this.frame = chartFrame;
		this.chart = chart;
		this.axisType = axisType;
	}

	public void maxTextFieldUpdated(ActionEvent e) {
		double newMaxValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMax(this.axisType));
		this.chart.getScatterPlot2D().setMax(this.axisType, newMaxValue);
		((JTextField) e.getSource()).setText(Double.toString(newMaxValue));
		this.frame.repaint();
	}

	public void minTextFieldUpdated(ActionEvent e) {
		double newMinValue = validateDoubleInput(e.getActionCommand(), this.chart.getScatterPlot2D().getMin(this.axisType));
		this.chart.getScatterPlot2D().setMin(this.axisType, newMinValue);
		((JTextField) e.getSource()).setText(Double.toString(newMinValue));
		this.frame.repaint();
	}

	public void updateAutofitAxis(boolean newState) {
		this.chart.getScatterPlot2D().setAutofit(this.axisType, newState);
		if (newState) {
			this.chart.getScatterPlot2D().autofit(this.mainWindow.getDataSheet(), this.axisType);
		}
		this.frame.repaint();
	}

	public void ticLabelFontsizeUpdated(int value) {
		this.chart.getScatterPlot2D().setTicLabelFontSize(this.axisType, value);
		frame.repaint();
	}

	public void ticCountUpdated(int value) {
		this.chart.getScatterPlot2D().setTicCount(this.axisType, value);
		frame.repaint();
	}

	public void ticLabelDigitCountUpdated(int value) {
	    this.chart.getScatterPlot2D().getParameterForAxis(this.axisType).setTicLabelDigitCount(value);
	    frame.repaint();
    }

	public void axisLabelFontsizeUpdated(int value) {
		this.chart.getScatterPlot2D().setAxisLabelFontSize(this.axisType, value);
		frame.repaint();
	}

	private double validateDoubleInput(String input, double fallBackValue) {
		try {
			return Double.parseDouble(input);
		} catch (NumberFormatException e) {
			return fallBackValue;
		}
	}
}
