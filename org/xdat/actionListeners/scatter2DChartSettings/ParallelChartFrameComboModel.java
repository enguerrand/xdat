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

import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.MutableComboBoxModel;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.chart.ScatterPlot2D;
import org.xdat.gui.frames.ChartFrame;

/**
 * Combobox Model that is used to choose from all
 * {@link org.xdat.gui.frames.ChartFrame}s that carry a
 * {@link org.xdat.chart.ParallelCoordinatesChart}.
 */
public class ParallelChartFrameComboModel extends AbstractListModel implements MutableComboBoxModel {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main Window */
	private Main mainWindow;

	/** The chart frame which should be repainted upon changed selection. */
	private ChartFrame chartFrame;

	/** The 2D scatter plot */
	private ScatterPlot2D plot;

	/** the combobox list entries */
	private Vector<String> chartNames = new Vector<String>();

	/**
	 * Instantiates parallel chart frame combo box model
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame 
	 * 				the chart frame
	 * @param plot 
	 * 				the plot
	 */
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
		log("setSelectedItem");
		this.chartFrame.repaint();
	}

	@Override
	public Object getSelectedItem() {
		log("getSelectedItem");
		if (this.plot.getParallelCoordinatesChartForFiltering() == null) {
			return "No chart selected             ";
		} else {
			return this.plot.getParallelCoordinatesChartForFiltering().getTitle();
		}
	}

	@Override
	public Object getElementAt(int index) {
		log("getElementAt");
		if (this.chartNames.size() == 0 && index == 0) {
			return "No chart selected             ";
		} else {
			return this.chartNames.get(index);
		}
	}

	@Override
	public int getSize() {
		log("getSize " + Math.max(this.chartNames.size(), 1));
		return Math.max(this.chartNames.size(), 1);
	}

	@Override
	public void addElement(Object obj) {
		this.chartNames.add(obj.toString());
		this.fireIntervalAdded(this, 0, this.chartNames.size());
		log("addElement");
	}

	@Override
	public void insertElementAt(Object obj, int index) {
		this.chartNames.insertElementAt(obj.toString(), index);
		this.fireIntervalAdded(this, 0, this.chartNames.size());
		log("insertElementAt");

	}

	@Override
	public void removeElement(Object obj) {
		this.chartNames.remove(obj);
		this.fireIntervalRemoved(this, 0, this.chartNames.size());
		log("removeElement");

	}

	@Override
	public void removeElementAt(int index) {
		this.chartNames.remove(index);
		this.fireIntervalRemoved(this, 0, this.chartNames.size());
		log("removeElementAt");
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParallelChartFrameComboModel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
