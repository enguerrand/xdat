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

package org.xdat.gui.panels;

import org.xdat.gui.panels.ParallelCoordinatesChartPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ParallelChartSidebarActionListener;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.Cluster;
import org.xdat.data.ClusterSet;
import org.xdat.gui.buttons.ColorChoiceButton;
import org.xdat.gui.buttons.CustomButton;
import org.xdat.gui.frames.ChartFrame;

/**
 * Panel to modify display settings for a
 * {@link org.xdat.chart.Chart}.
 */
public abstract class SidebarPanel extends JPanel {
	/**
	 * The version tracking unique identifier for Serialization.
	 */
	static final long serialVersionUID = 0000;

	/**
	 * Flag to enable debug message printing for this class.
	 */
	static final boolean printLog = false;
	
	
	/**
	 * The main Window
	 */
	private Main mainWindow;

	/**
	 * The chart frame
	 */
	private ChartFrame chartFrame;
	
	
	/**
	 * The chart panel
	 */
	private ChartPanel chartPanel;
	

	/**
	 * the panel with chart specific content
	 */
	private JPanel contentPanel;
	
	/**
	 * the header panel
	 */
	private JPanel headerPanel;
	
	/**
	 * The chart frame to which the settings apply.
	 */
	private Chart chart;
	
	/**
	 * Instantiates a new side panel 
	 * @param mainWindow
	 * 			     the main window
	 * @param chartFrame
	 *       		the chart frame
	 * @param chartPanel
	 *       		the chart panel
	 * @param chart
	 *            the chart
	 */
	public SidebarPanel(Main mainWindow, ChartFrame chartFrame, ChartPanel chartPanel, Chart chart) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.chartPanel = chartPanel;
		this.chart = chart;
		this.headerPanel = new JPanel();
		this.contentPanel = new JPanel();
		initialize();
		updateView(true);
	}

	private void updateView(boolean maximized) {
		buildHeaderPanel(maximized);
		this.setLayout(new BorderLayout());
		this.add(headerPanel, BorderLayout.NORTH);
		contentPanel.removeAll();
		if(maximized){
			buildPanel(this.contentPanel);
			this.add(contentPanel, BorderLayout.CENTER);
		}
		this.chartFrame.validate();
		this.chartFrame.repaint();
	}

	/**
	 * Builds the header panel
	 */
	private void buildHeaderPanel(final boolean maximized) {
		this.headerPanel.removeAll();
		this.headerPanel.setLayout(new BorderLayout());
		CustomButton toggleButton;
		if(maximized){
			toggleButton = new CustomButton("Collapse", "images" + "/minimize.png", "images" + "/minimize_pressed.png", "Collapse");
		}
		else{
			toggleButton = new CustomButton("Expand", "images" + "/maximize.png", "images" + "/maximize_pressed.png", "Expand");
		}
		this.headerPanel.add(toggleButton, BorderLayout.WEST);
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateView(!maximized);
			}
		});
	}

	/**
	 * Gets the main window
	 * @return the main window
	 */
	public Main getMainWindow() {
		return mainWindow;
	}

	/**
	 * Gets the chart
	 * @return the chart
	 */
	public Chart getChart() {
		return chart;
	}
	
	/**
	 * gets the chart panel
	 * @return the chart panel
	 */
	public ChartPanel getChartPanel() {
		return chartPanel;
	}
	
	/**
	 * Gets the chart frame
	 * @return the chart frame
	 */
	public ChartFrame getChartFrame() {
		return chartFrame;
	}
	
	/**
	 * Builds the panel
	 */
	protected abstract void buildPanel(JPanel contentPanel);

	/**
	 * Initializes members needed before building the panel
	 */
	protected abstract void initialize();

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (SidebarPanel.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
	

}
