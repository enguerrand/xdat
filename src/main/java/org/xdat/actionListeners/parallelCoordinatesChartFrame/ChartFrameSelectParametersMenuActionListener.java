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

package org.xdat.actionListeners.parallelCoordinatesChartFrame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParameterSetSelectionDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinatesChartFrameSelectParametersMenu;

/**
 * ActionListener for a
 * {@link ParallelCoordinatesChartFrameSelectParametersMenu}.
 */
public class ChartFrameSelectParametersMenuActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/** The chart. */
	private ParallelCoordinatesChart chart;

	/**
	 * Instantiates a new chart frame select parameters menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 * @param chart
	 *            the chart
	 */

	public ChartFrameSelectParametersMenuActionListener(Main mainWindow, ChartFrame chartFrame, ParallelCoordinatesChart chart) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.chart = chart;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		log("Action Command = " + actionCommand);
		if (actionCommand.equals("Select All")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					// ((JCheckBoxMenuItem)menuComps[i]).setState(true);
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(true);
				}
			}
		} else if (actionCommand.equals("Unselect All")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					// ((JCheckBoxMenuItem)menuComps[i]).setState(false);
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(false);
				}
			}
		} else if (actionCommand.equals("Reverse Selection")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					// ((JCheckBoxMenuItem)menuComps[i]).setState(!((JCheckBoxMenuItem)menuComps[i]).getState());
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(!this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).isActive());
				}
			}
		} else if (actionCommand.equals("Custom Selection")) {
			log("creating ParameterSetSelectionDialog");
			new ParameterSetSelectionDialog(this.mainWindow, this.chartFrame);
		} else {
			// log("(actionCommand.equals(\"Reverse Selection\"))="+(actionCommand.equals("Reverse Selection")));
			log("Axis " + actionCommand + " active is " + this.chart.getAxis(actionCommand).isActive());
			this.chart.getAxis(actionCommand).setActive(!this.chart.getAxis(actionCommand).isActive());
			log("After state change axis " + actionCommand + " active is " + this.chart.getAxis(actionCommand).isActive());
		}
		this.chartFrame.getChartPanel().setSize(this.chartFrame.getChartPanel().getPreferredSize());
		this.chartFrame.validate();
		this.chartFrame.repaint();
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartFrameSelectParametersMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
