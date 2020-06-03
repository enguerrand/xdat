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

package org.xdat.actionListeners.chartFrames;

import org.xdat.Main;
import org.xdat.chart.Axis;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.tables.DataSheetTableColumnModel;

import java.awt.Color;
import java.awt.event.ActionEvent;

public class ParallelChartContextMenuActionListener {
	private final Main mainWindow;
	private final ChartFrame chartFrame;
	private final Axis axis;

	public ParallelChartContextMenuActionListener(Main mainWindow, ChartFrame chartFrame, Axis axis) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.axis = axis;
	}

	public void setCurrentFiltersAsNewRange(ActionEvent e) {
		this.axis.setFilterAsNewRange(mainWindow.getDataSheet());
	}

	public void resetFilter(ActionEvent e) {
		this.axis.resetFilters(this.mainWindow.getDataSheet());
	}

	public void autofit(ActionEvent e) {
		this.axis.autofit(mainWindow.getDataSheet());
		this.chartFrame.repaint();
	}

	public void moveAxisLeft(ActionEvent e) {
		DataSheetTableColumnModel cm = (DataSheetTableColumnModel) this.mainWindow.getDataSheetTablePanel().getDataTable().getColumnModel();
		int currentIndex = this.mainWindow.getDataSheet().getParameterIndex(this.axis.getName());
		boolean jumpedAxisWasInactive = true;
		while (currentIndex > 0 && jumpedAxisWasInactive) {
			jumpedAxisWasInactive = !((ParallelCoordinatesChart) this.chartFrame.getChart()).getAxis(currentIndex - 1).isActive();
			cm.moveColumn(currentIndex + 1, currentIndex); // column index starts at one, param index at 0
			currentIndex--;
		}
	}

	public void moveAxisRight(ActionEvent e) {
		DataSheetTableColumnModel cm = (DataSheetTableColumnModel) this.mainWindow.getDataSheetTablePanel().getDataTable().getColumnModel();
		int currentIndex = this.mainWindow.getDataSheet().getParameterIndex(this.axis.getName());
		boolean jumpedAxisWasInactive = true;
		while (currentIndex + 2 < cm.getColumnCount() && jumpedAxisWasInactive) {
			jumpedAxisWasInactive = !((ParallelCoordinatesChart) this.chartFrame.getChart()).getAxis(currentIndex + 1).isActive();
			cm.moveColumn(currentIndex + 1, currentIndex + 2); // column index starts at one, param index at 0
			currentIndex++;
		}
	}

	public void hideAxis(ActionEvent e) {
		this.axis.setActive(false);
		this.chartFrame.repaint();
	}

	public void addTic(ActionEvent e) {
		this.axis.setTicCount(this.axis.getTicCount() + 1, this.mainWindow.getDataSheet());
		this.chartFrame.repaint();
	}

	public void removeTic(ActionEvent e) {
		this.axis.setTicCount(Math.max(2, this.axis.getTicCount() - 1), this.mainWindow.getDataSheet());
		this.chartFrame.repaint();
	}

	public void addDistanceThisAxis(int delta) {
		this.axis.setWidth(Math.max(0, axis.getWidth() + delta));
		this.chartFrame.repaint();
	}

	public void resetAllFilters(ActionEvent e) {
		ParallelCoordinatesChart chart = this.axis.getChart();
		for (int i = 0; i < chart.getAxisCount(); i++) {
			chart.getAxis(i).resetFilters(this.mainWindow.getDataSheet());
		}
	}

	public void reduceDistanceAllAxes(ActionEvent e) {
		ParallelCoordinatesChart chart = this.axis.getChart();
		for (int i = 0; i < chart.getAxisCount(); i++) {
			chart.getAxis(i).setWidth(Math.max(0, chart.getAxis(i).getWidth() - 10));
		}
		this.chartFrame.repaint();
	}

	public void increaseDistanceAllAxes(ActionEvent e) {
		ParallelCoordinatesChart chart = this.axis.getChart();
		for (int i = 0; i < chart.getAxisCount(); i++) {
			chart.getAxis(i).setWidth(Math.max(0, chart.getAxis(i).getWidth() + 10));
		}
		this.chartFrame.repaint();
	}

	public void invertAxis(ActionEvent e) {
		this.axis.setAxisInverted(!this.axis.isAxisInverted(), this.mainWindow.getDataSheet());
	}

	public void applyColorGradient(ActionEvent e) {
		ParallelCoordinatesChart chart = (ParallelCoordinatesChart) this.chartFrame.getChart();
		DataSheet datasheet = chart.getDataSheet();

		double axisRange = this.axis.getMax() - this.axis.getMin();

		for (int designID = 0; designID < datasheet.getDesignCount(); designID++) {
			Design currentDesign = datasheet.getDesign(designID);
			double value = currentDesign.getDoubleValue(datasheet.getParameter(this.axis.getName()));

			if (axisRange == 0) {
				currentDesign.setAxisGradientColor(new Color(0, 0, 255, 150));
			} else {
				double ratio;
				int alpha = 255;
				if (axis.isAxisInverted()) {
					ratio = (axis.getMax() - value) / axisRange;
				} else {
					ratio = (value - axis.getMin()) / axisRange;
				}

				if (ratio > 1 || ratio < 0) {
					currentDesign.removeAxisGradientColor();
				} else if (ratio > 0.9) {
					ratio = (ratio * 10.) - 9.;
					int r = 165 + (int) (50. * (1. - ratio));
					int g = (int) (48. * (1. - ratio));
					int b = 38 + (int) (1. * (1. - ratio));

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.8) {
					ratio = (ratio * 10.) - 8.;
					int r = 215 + (int) (29. * (1. - ratio));
					int g = 48 + (int) (61. * (1. - ratio));
					int b = 39 + (int) (28. * (1. - ratio));

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.7) {
					ratio = (ratio * 10.) - 7.;
					int r = 244 + (int) (9. * (1. - ratio));
					int g = 109 + (int) (65. * (1. - ratio));
					int b = 67 + (int) (30. * (1. - ratio));

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.6) {
					ratio = (ratio * 10.) - 6;
					int r = 253 + (int) (1. * (1. - ratio));
					int g = 174 + (int) (50. * (1. - ratio));
					int b = 97 + (int) (47. * (1. - ratio));

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.5) {
					ratio = (ratio * 10.) - 5.;
					int r = 254 + (int) (1. * (1. - ratio));
					int g = 224 + (int) (31. * (1. - ratio));
					int b = 144 + (int) (47. * (1. - ratio));
					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.4) {
					ratio = (ratio * 10.) - 4;
					int r = 255 - (int) (31. * ratio);
					int g = 255 - (int) (12. * ratio);
					int b = 191 + (int) (57. * (1. - ratio));
					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.3) {
					ratio = (ratio * 10.) - 3.;
					int r = 224 - (int) (53. * ratio);
					int g = 243 - (int) (26. * ratio);
					int b = 248 - (int) (15. * ratio);

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.2) {
					ratio = (ratio * 10.) - 2.;
					int r = 171 - (int) (55. * ratio);
					int g = 217 - (int) (44. * ratio);
					int b = 233 - (int) (24. * ratio);

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else if (ratio > 0.1) {
					ratio = (ratio * 10.) - 1.;
					int r = 116 - (int) (67. * ratio);
					int g = 173 - (int) (56. * ratio);
					int b = 209 - (int) (29. * ratio);

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				} else {
					ratio = ratio * 10.;
					int r = 69 - (int) (20. * ratio);
					int g = 117 - (int) (63. * ratio);
					int b = 180 - (int) (31. * ratio);

					currentDesign.setAxisGradientColor(new Color(r, g, b, alpha));
				}
			}
		}

		this.mainWindow.repaintAllChartFrames();
	}

	public void resetColorGradient(ActionEvent e) {
		DataSheet datasheet = this.chartFrame.getChart().getDataSheet();

		for (int designID = 0; designID < datasheet.getDesignCount(); designID++) {
			datasheet.getDesign(designID).removeAxisGradientColor();
		}
		this.mainWindow.repaintAllChartFrames();
	}
}
