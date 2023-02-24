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

package org.xdat.gui.frames;

import org.xdat.Main;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.chart.ScatterChart2D;
import org.xdat.data.Cluster;
import org.xdat.data.ClusterListener;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.ChartFrameClosingAdapter;
import org.xdat.gui.menus.ScatterChart2D.ScatterChart2DFrameMenuBar;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinateChartFrameMenuBar;
import org.xdat.gui.panels.ChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartSidebarPanel;
import org.xdat.gui.panels.ScatterChart2DPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

public class ChartFrame extends JFrame implements ComponentListener {

	private final Main mainWindow;
	private final Chart chart;
	private final ChartPanel chartPanel;
	private final List<Runnable> closeHooks = new ArrayList<>();

	public ChartFrame(Main mainWindow, Chart chart) throws NoParametersDefinedException {
		super(chart.getTitle());
		if (mainWindow.getDataSheet().getParameterCount() < 1) {
			throw new NoParametersDefinedException();
		}
		addWindowListener(new ChartFrameClosingAdapter(this, mainWindow));
		this.mainWindow = mainWindow;

		this.setIconImage(mainWindow.getIconImage());
		this.setPreferredSize(chart.getFrameSize());

		this.chart = chart;
		mainWindow.addChartFrame(this);
		this.setLayout(new GridLayout(1, 1));

		if (chart.getClass().equals(ParallelCoordinatesChart.class)) {
			JPanel wrapper = new JPanel();
			wrapper.setLayout(new BorderLayout());

			this.chartPanel = new ParallelCoordinatesChartPanel(mainWindow, this, (ParallelCoordinatesChart) this.chart);
			this.setJMenuBar(new ParallelCoordinateChartFrameMenuBar(mainWindow, this, (ParallelCoordinatesChart) this.chart));
			JScrollPane scrollPane = new JScrollPane(this.chartPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

			ParallelCoordinatesChartSidebarPanel sidebar = new ParallelCoordinatesChartSidebarPanel(mainWindow, this, this.chartPanel, (ParallelCoordinatesChart)chart);

			wrapper.add(scrollPane, BorderLayout.CENTER);
			wrapper.add(sidebar, BorderLayout.EAST);
			closeHooks.add(sidebar::onClosed);
			this.add(wrapper);
		} else if (chart.getClass().equals(ScatterChart2D.class)) {
			this.chartPanel = new ScatterChart2DPanel(mainWindow, (ScatterChart2D) this.chart);
			this.setJMenuBar(new ScatterChart2DFrameMenuBar(mainWindow, this, (ScatterChart2D) chart));
			this.add(this.chartPanel, BorderLayout.CENTER);
		} else {
			throw new RuntimeException("Unknown Chart Type!");
		}
		ClusterListener.ClusterAdapter clusterListener = new ClusterListener.ClusterAdapter() {
			@Override
			public void onColorChanged(Cluster source) {
				repaint();
			}
		};
		this.closeHooks.add(() -> mainWindow.removeChartFrame(this));

		this.setLocation(this.chart.getLocation());
		this.getContentPane().setPreferredSize(this.chart.getFrameSize());
		this.pack();
		this.addComponentListener(this);
		setVisible(true);
		mainWindow.getCurrentClusterSet().addClusterListener(clusterListener);
		this.closeHooks.add(() -> mainWindow.getCurrentClusterSet().removeClusterListener(clusterListener));
	}

	public Chart getChart() {
		return chart;
	}

	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	public void dispose() {
		closeHooks.forEach(Runnable::run);
		super.dispose();
	}

	public Main getMainWindow() {
		return mainWindow;
	}

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
		this.chart.setLocation(this.getLocation());
	}

	public void componentResized(ComponentEvent arg0) {
		this.chart.setFrameSize(this.getContentPane().getSize());
	}

	public void componentShown(ComponentEvent arg0) {
	}
}
