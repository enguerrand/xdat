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
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;
import java.util.LinkedList;
import java.util.List;

public class ParallelChartFrameComboModel extends AbstractListModel<String> implements MutableComboBoxModel<String> {

	static final long serialVersionUID = 0L;
	private Main mainWindow;
	private ChartFrame chartFrame;
	private ScatterPlot2D plot;
	private List<String> chartNames = new LinkedList<>();

	public ParallelChartFrameComboModel(Main mainWindow, ChartFrame chartFrame, ScatterPlot2D plot) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.plot = plot;
		for (int i = 0; i < mainWindow.getChartFrameCount(); i++) {
			if (mainWindow.getChartFrame(i).getChart().getClass().equals(ParallelCoordinatesChart.class)) {
				this.addElement(mainWindow.getChartFrame(i).getChart().getTitle());
			}
		}
	}

	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem.toString().equals("No chart selected             ")) {
			this.plot.setParallelCoordinatesChartForFiltering(null);
		} else {
			this.plot.setParallelCoordinatesChartForFiltering((ParallelCoordinatesChart) this.mainWindow.getChartFrame(anItem.toString()).getChart());
		}
		this.chartFrame.repaint();
	}

	@Override
	public Object getSelectedItem() {
		if (this.plot.getParallelCoordinatesChartForFiltering() == null) {
			return "No chart selected             ";
		} else {
			return this.plot.getParallelCoordinatesChartForFiltering().getTitle();
		}
	}

	@Override
	public String getElementAt(int index) {
		if (this.chartNames.size() == 0 && index == 0) {
			return "No chart selected             ";
		} else {
			return this.chartNames.get(index);
		}
	}

	@Override
	public int getSize() {
		return Math.max(this.chartNames.size(), 1);
	}

	@Override
	public void addElement(String obj) {
		this.chartNames.add(obj);
		this.fireIntervalAdded(this, 0, this.chartNames.size());
	}

	@Override
	public void insertElementAt(String obj, int index) {
		this.chartNames.add(index, obj);
		this.fireIntervalAdded(this, 0, this.chartNames.size());
	}

	@Override
	public void removeElement(Object obj) {
		this.chartNames.remove(obj.toString());
		this.fireIntervalRemoved(this, 0, this.chartNames.size());
	}

	@Override
	public void removeElementAt(int index) {
		this.chartNames.remove(index);
		this.fireIntervalRemoved(this, 0, this.chartNames.size());
	}
}
