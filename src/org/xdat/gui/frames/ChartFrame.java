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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ParallelChartSidebarActionListener;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.chart.ScatterChart2D;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.ChartFrameClosingAdapter;
import org.xdat.gui.menus.ScatterChart2D.ScatterChart2DFrameMenuBar;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinateChartFrameMenuBar;
import org.xdat.gui.panels.ChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartSidebarPanel;
import org.xdat.gui.panels.ScatterChart2DPanel;
import org.xdat.gui.panels.SidebarPanel;

/**
 * A frame that us used to display a {@link org.xdat.chart.Chart}.
 */
public class ChartFrame extends JFrame implements ComponentListener {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The chart. */
	private Chart chart;

	/** The panel on which the chart is painted. */
	private ChartPanel chartPanel;
	
	/** The side bar panel (if available, null otherwise) */
	private SidebarPanel sidePanel;

	/** */
	private Vector<JDialog> registeredDialog = new Vector<JDialog>(0);

	/**
	 * Instantiates a new chart frame and identifies the right chart type to
	 * choose appropriate GUI elements accordingly.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chart
	 *            the chart
	 * @throws NoParametersDefinedException
	 * 				When no parameters are defined
	 */
	public ChartFrame(Main mainWindow, Chart chart) throws NoParametersDefinedException {
		super(chart.getTitle());
		log("constructor invoked.");
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

			ParallelCoordinatesChartSidebarPanel sidebar = new ParallelCoordinatesChartSidebarPanel(mainWindow, this, (ParallelCoordinatesChartPanel) this.chartPanel, (ParallelCoordinatesChart)chart);

			wrapper.add(scrollPane, BorderLayout.CENTER);
			wrapper.add(sidebar, BorderLayout.EAST);

			this.add(wrapper);
			
			this.sidePanel = sidebar;
		} else if (chart.getClass().equals(ScatterChart2D.class)) {
			this.chartPanel = new ScatterChart2DPanel(mainWindow, (ScatterChart2D) this.chart);
			this.setJMenuBar(new ScatterChart2DFrameMenuBar(mainWindow, this, (ScatterChart2D) chart));
			this.add(this.chartPanel, BorderLayout.CENTER);
		} else {
			throw new RuntimeException("Unknown Chart Type!");
		}

		this.setLocation(this.chart.getLocation());
		this.getContentPane().setPreferredSize(this.chart.getFrameSize());
		this.pack();
		this.addComponentListener(this);
		setVisible(true);
	}

	/**
	 * Gets the chart.
	 * 
	 * @return the chart
	 */
	public Chart getChart() {
		return chart;
	}

	/**
	 * Gets the chart panel.
	 * 
	 * @return the chart panel
	 */
	public ChartPanel getChartPanel() {
		return chartPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	public void dispose() {
		log("dispose called");
		mainWindow.removeChartFrame(this);
		super.dispose();
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartFrame.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Overridden to implement the painting of the chart frame.
	 * <p>
	 * Also repaints all dialogs that were registered with this frame for
	 * repaint notification.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < this.registeredDialog.size(); i++) {
			registeredDialog.get(i).repaint();
		}
	}

	/**
	 * Registers a dialog with this frame for repaint notification.
	 * @param comp the component
	 */
	public void registerComponentForRepaint(JDialog comp) {
		log("registerComponentForRepaint");
		this.registeredDialog.add(comp);
	}

	/**
	 * Unregisters a dialog with this frame for repaint notification.
	 * @param comp the component
	 */
	public void unRegisterComponentForRepaint(JDialog comp) {
		log("unRegisterComponentForRepaint");
		if (this.registeredDialog.contains(comp))
			this.registeredDialog.remove(comp);
	}

	/** Gets the side panel 
	 * 
	 * @return the side Panel
	 */
	public SidebarPanel getSidePanel() {
		return sidePanel;
	}
	
	/**
	 * Gets the main window
	 * 
	 * @return the main Window
	 */
	public Main getMainWindow() {
		return mainWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentHidden(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentHidden(ComponentEvent arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentMoved(java.awt.event.ComponentEvent
	 * )
	 */
	public void componentMoved(ComponentEvent arg0) {
		this.chart.setLocation(this.getLocation());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.
	 * ComponentEvent)
	 */
	public void componentResized(ComponentEvent arg0) {
		this.chart.setFrameSize(this.getContentPane().getSize());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ComponentListener#componentShown(java.awt.event.ComponentEvent
	 * )
	 */
	public void componentShown(ComponentEvent arg0) {
	}
}
