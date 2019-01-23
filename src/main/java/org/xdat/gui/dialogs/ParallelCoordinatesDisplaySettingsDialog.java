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

package org.xdat.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.AxisDisplaySettingsActionListener;
import org.xdat.actionListeners.parallelCoordinatesDisplaySettings.ParallelChartDisplaySettingsActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.AxisDisplaySettingsPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartDisplaySettingsPanel;

/**
 * Dialog to modify display settings for a
 * {@link org.xdat.chart.ParallelCoordinatesChart}s, its axes or the
 * {@link org.xdat.UserPreferences} for these display settings
 */
public class ParallelCoordinatesDisplaySettingsDialog extends JDialog {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0002;

	/** Flag to enable debug message printing for this class. */
	private static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The chart display settings panel. */
	private ParallelCoordinatesChartDisplaySettingsPanel chartDisplaySettingsPanel;

	/** The axis display settings panel. */
	private AxisDisplaySettingsPanel axisDisplaySettingsPanel;

	/**
	 * Instantiates a new display settings dialog.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @throws HeadlessException
	 *             the headless exception
	 */
	public ParallelCoordinatesDisplaySettingsDialog(Main mainWindow) throws HeadlessException {
		super(mainWindow, "Parallel Coord Settings", true);
		this.mainWindow = mainWindow;

		chartDisplaySettingsPanel = new ParallelCoordinatesChartDisplaySettingsPanel(this.mainWindow, this);
		axisDisplaySettingsPanel = new AxisDisplaySettingsPanel(this.mainWindow, this);
		chartDisplaySettingsPanel.setActionListener(new ParallelChartDisplaySettingsActionListener(mainWindow, chartDisplaySettingsPanel, this));
		axisDisplaySettingsPanel.setActionListener(new AxisDisplaySettingsActionListener(mainWindow, this, axisDisplaySettingsPanel));
		buildDialog();

		chartDisplaySettingsPanel.setOkCancelButtonTargetDefaultSettings();
		axisDisplaySettingsPanel.setOkCancelButtonTargetDefaultSettings();

		this.setVisible(true);
	}

	/**
	 * Instantiates a new display settings dialog.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chart
	 *            the chart
	 * @param chartFrame
	 *            the chart frame
	 * @throws HeadlessException
	 *             the headless exception
	 */
	public ParallelCoordinatesDisplaySettingsDialog(Main mainWindow, ParallelCoordinatesChart chart, ChartFrame chartFrame) throws HeadlessException {
		super(chartFrame, "Display Settings");
		this.setModal(true);
		this.mainWindow = mainWindow;

		chartDisplaySettingsPanel = new ParallelCoordinatesChartDisplaySettingsPanel(this.mainWindow, this, chartFrame);
		axisDisplaySettingsPanel = new AxisDisplaySettingsPanel(this.mainWindow, this, chartFrame);
		chartDisplaySettingsPanel.setActionListener(new ParallelChartDisplaySettingsActionListener(mainWindow, chartDisplaySettingsPanel, chart, this));
		axisDisplaySettingsPanel.setActionListener(new AxisDisplaySettingsActionListener(this, axisDisplaySettingsPanel, chart));

		buildDialog();

		chartDisplaySettingsPanel.setOkCancelButtonTargetChart(chart);
		axisDisplaySettingsPanel.setOkCancelButtonTargetChart(chart);

		log("preferred size : " + this.getPreferredSize().width + ", " + this.getPreferredSize().getHeight());
		this.setVisible(true);

	}

	/**
	 * Builds the dialog.
	 */
	private void buildDialog() {
		log("constructor called");
		this.addWindowListener(new WindowClosingAdapter(false));
		this.setResizable(false);

		// create components
		JTabbedPane tabbedPane = new JTabbedPane();

		// set Layouts
		this.setLayout(new BorderLayout());

		// add components
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.add("General", chartDisplaySettingsPanel);
		tabbedPane.add("Axis-specific", axisDisplaySettingsPanel);
		// pack
		this.pack();

		// set location and make visible
		int left = (int) (0.5 * this.mainWindow.getSize().width) - (int) (this.getSize().width * 0.5) + this.mainWindow.getLocation().x;
		int top = (int) (0.5 * this.mainWindow.getSize().height) - (int) (this.getSize().height * 0.5) + this.mainWindow.getLocation().y;
		this.setLocation(left, top);
	}

	/**
	 * Gets the axis display settings panel.
	 * 
	 * @return the axis display settings panel
	 */
	public AxisDisplaySettingsPanel getAxisDisplaySettingsPanel() {
		return axisDisplaySettingsPanel;
	}

	/**
	 * Gets the chart display settings panel.
	 * 
	 * @return the chart display settings panel
	 */
	public ParallelCoordinatesChartDisplaySettingsPanel getChartDisplaySettingsPanel() {
		return chartDisplaySettingsPanel;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private static final void log(String message) {
		if (ParallelCoordinatesDisplaySettingsDialog.printLog && Main.isLoggingEnabled()) {
			System.out.println("DisplaySettingsDialog." + message);
		}
	}

}
