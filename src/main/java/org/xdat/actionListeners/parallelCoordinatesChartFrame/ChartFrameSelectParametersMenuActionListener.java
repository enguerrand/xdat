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

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParameterSetSelectionDialog;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JCheckBoxMenuItem;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChartFrameSelectParametersMenuActionListener implements ActionListener {

	private Main mainWindow;
	private ChartFrame chartFrame;
	private ParallelCoordinatesChart chart;

	public ChartFrameSelectParametersMenuActionListener(Main mainWindow, ChartFrame chartFrame, ParallelCoordinatesChart chart) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.chart = chart;
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Select All")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(true);
				}
			}
		} else if (actionCommand.equals("Unselect All")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(false);
				}
			}
		} else if (actionCommand.equals("Reverse Selection")) {
			Component[] menuComps = this.chartFrame.getJMenuBar().getMenu(0).getMenuComponents();
			for (int i = 0; i < menuComps.length; i++) {
				if (menuComps[i].getClass().equals(JCheckBoxMenuItem.class)) {
					this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).setActive(!this.chart.getAxis(((JCheckBoxMenuItem) menuComps[i]).getText()).isActive());
				}
			}
		} else if (actionCommand.equals("Custom Selection")) {
			new ParameterSetSelectionDialog(this.mainWindow, this.chartFrame);
		} else {
			this.chart.getAxis(actionCommand).setActive(!this.chart.getAxis(actionCommand).isActive());
		}
		this.chartFrame.getChartPanel().setSize(this.chartFrame.getChartPanel().getPreferredSize());
		this.chartFrame.validate();
		this.chartFrame.repaint();
	}
}
