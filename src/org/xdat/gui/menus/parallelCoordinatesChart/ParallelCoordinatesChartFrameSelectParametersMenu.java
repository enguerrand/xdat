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

package org.xdat.gui.menus.parallelCoordinatesChart;

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ChartFrameSelectParametersMenuActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.frames.ChartFrame;

/**
 * Menu of the {@link org.xdat.gui.frames.ChartFrame} that allows to select the
 * {@link org.xdat.data.Parameter}s to be displayed.
 */
public class ParallelCoordinatesChartFrameSelectParametersMenu extends JMenu implements MenuListener {

	/** The Main Window. */
	private Main mainWindow;

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The actionlistener. */
	private ChartFrameSelectParametersMenuActionListener cmd;

	/** The Chart. */
	private ParallelCoordinatesChart chart;

	/**
	 * Instantiates a new chart frame select parameters menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 * @param chart
	 *            the chart
	 */
	public ParallelCoordinatesChartFrameSelectParametersMenu(Main mainWindow, ChartFrame chartFrame, ParallelCoordinatesChart chart) {
		super("Parameters");
		log("constructor invoked.");
		this.addMenuListener(this);
		this.mainWindow = mainWindow;
		this.chart = chart;
		this.setMnemonic(KeyEvent.VK_P);

		this.cmd = new ChartFrameSelectParametersMenuActionListener(mainWindow, chartFrame, chart);

	}

	/**
	 * Builds all the menu items
	 * 
	 */
	private void buildMenu() {
		this.removeAll();

		JMenuItem mi;
		// select all
		mi = new JMenuItem("Select All");
		mi.setMnemonic(KeyEvent.VK_S);
		mi.addActionListener(cmd);
		this.add(mi);

		// unselect all
		mi = new JMenuItem("Unselect All");
		mi.setMnemonic(KeyEvent.VK_U);
		mi.addActionListener(cmd);
		this.add(mi);

		// reverse selection
		mi = new JMenuItem("Reverse Selection");
		mi.setMnemonic(KeyEvent.VK_R);
		mi.addActionListener(cmd);
		this.add(mi);

		// selection dialog
		mi = new JMenuItem("Custom Selection");
		mi.setMnemonic(KeyEvent.VK_C);
		mi.addActionListener(cmd);
		this.add(mi);

		// Separator
		this.addSeparator();
		JCheckBoxMenuItem[] checkBoxMenuItems = new JCheckBoxMenuItem[this.mainWindow.getDataSheet().getParameterCount()];
		for (int i = 0; i < checkBoxMenuItems.length; i++) {
			checkBoxMenuItems[i] = new JCheckBoxMenuItem(this.mainWindow.getDataSheet().getParameter(i).getName());
			checkBoxMenuItems[i].addActionListener(cmd);
			// menuItems.add(jcbmi);
			this.chart.getAxis(0);
			checkBoxMenuItems[i].setState(this.chart.getAxis(this.mainWindow.getDataSheet().getParameter(i).getName()).isActive());

			this.add(checkBoxMenuItems[i]);
		}
	}

	/**
	 * Sets the ctrl accelerator.
	 * 
	 * @param mi
	 *            the menu item
	 * @param acc
	 *            the accelerator
	 */
	private void setCtrlAccelerator(JMenuItem mi, char acc) {
		KeyStroke ks = KeyStroke.getKeyStroke(acc, Event.CTRL_MASK);
		mi.setAccelerator(ks);
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParallelCoordinatesChartFrameSelectParametersMenu.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuCanceled(MenuEvent arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent
	 * )
	 */
	@Override
	public void menuDeselected(MenuEvent arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuSelected(MenuEvent arg0) {
		buildMenu();
	}

}
