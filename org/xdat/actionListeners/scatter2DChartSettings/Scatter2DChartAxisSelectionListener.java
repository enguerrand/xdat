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

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.xdat.chart.ScatterChart2D;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.Scatter2DChartAxisPanel;

/**
 * List selection listener for a parameter selection list for a
 * {@link org.xdat.chart.ScatterChart2D}.
 */
public class Scatter2DChartAxisSelectionListener implements ListSelectionListener {
	/** the scatter 2d chart */
	private ScatterChart2D chart;

	/** the scatter 2d chart frame */
	private ChartFrame frame;

	/** specifies whether changes should be applied to x or y axis */
	private int axis;

	/** the panel */
	private Scatter2DChartAxisPanel panel;

	/**
	 * Instantiates a new axis selection listener for scatter 2d charts
	 * @param frame
	 * 		the chart frame
	 * @param chart
	 * 		the chart
	 * @param panel
	 * 		the axis settings panel
	 * @param axis
	 * 		the axis
	 */
	public Scatter2DChartAxisSelectionListener(ChartFrame frame, ScatterChart2D chart, Scatter2DChartAxisPanel panel, int axis) {
		this.frame = frame;
		this.chart = chart;
		this.axis = axis;
		this.panel = panel;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		int selected_row = ((JList) e.getSource()).getSelectedIndex();
		// System.out.println(selected_row);

		if (axis == Scatter2DChartAxisPanel.Y_AXIS) {
			chart.getScatterPlot2D().setParameterForYAxis(chart.getDataSheet().getParameter(selected_row));
			this.panel.updateTicCountSpinnerEnabled();
		} else if (axis == Scatter2DChartAxisPanel.X_AXIS) {
			chart.getScatterPlot2D().setParameterForXAxis(chart.getDataSheet().getParameter(selected_row));
			this.panel.updateTicCountSpinnerEnabled();
		} else {
			throw new RuntimeException("Invalid axis type " + this.axis);
		}
		this.frame.repaint();
	}

}
