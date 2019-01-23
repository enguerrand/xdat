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

package org.xdat.actionListeners.mainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.menus.mainWIndow.MainDataRemoveParameterMenu;

/**
 * ActionListener for a {@link MainDataRemoveParameterMenu}.
 */
public class MainDataRemoveParameterMenuActionListener implements ActionListener {

	/** The main window. */
	private Main mainWindow;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/**
	 * Instantiates a new main data remove parameter menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainDataRemoveParameterMenuActionListener(Main mainWindow) {
		log("constructor called.");
		this.mainWindow = mainWindow;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String paramName = e.getActionCommand();
		log(" Parameter to be removed: " + paramName);
		Chart[] charts = new Chart[this.mainWindow.getCurrentSession().getChartCount()];
		for (int i = 0; i < charts.length; i++) {
			charts[i] = (this.mainWindow.getCurrentSession().getChart(i));
			if (charts[i].getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) charts[i]).removeAxis(paramName);
			}
		}
		log("Removing paramter " + this.mainWindow.getDataSheet().removeParameter(paramName).getName());
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (MainDataRemoveParameterMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
