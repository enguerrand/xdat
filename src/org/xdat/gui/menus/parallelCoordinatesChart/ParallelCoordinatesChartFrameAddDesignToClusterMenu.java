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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ChartFrameAddDesignToClusterMenuActionListener;
import org.xdat.data.ClusterSet;
import org.xdat.gui.frames.ChartFrame;

/**
 * Menu for a {@link org.xdat.gui.frames.ChartFrame} to add a
 * {@link org.xdat.data.Design} to a {@link org.xdat.data.Cluster}.
 */
public class ParallelCoordinatesChartFrameAddDesignToClusterMenu extends JMenu implements MenuListener {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0002;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates a new chartFrame menu from within which a design can be
	 * added to a cluster.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 */
	public ParallelCoordinatesChartFrameAddDesignToClusterMenu(Main mainWindow, ChartFrame chartFrame) {
		super("Add filtered designs to cluster");
		log("constructor called.");
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.setMnemonic(KeyEvent.VK_A);
		this.addMenuListener(this);
	}

	/**
	 * Update {@link org.xdat.data.Cluster} list.
	 */
	private void updateClusterList() {
		this.removeAll();
		ChartFrameAddDesignToClusterMenuActionListener cmd = new ChartFrameAddDesignToClusterMenuActionListener(mainWindow, chartFrame);

		JMenuItem mi;
		ClusterSet clusterSet = mainWindow.getDataSheet().getClusterSet();
		if (clusterSet.getClusterCount() == 0) {
			mi = new JMenuItem("No clusters defined");
			this.add(mi);
			mi.setEnabled(false);
		} else {
			for (int i = 0; i < clusterSet.getClusterCount(); i++) {
				mi = new JMenuItem(clusterSet.getCluster(i).getName());
				mi.addActionListener(cmd);
				this.add(mi);
			}
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

	@Override
	public void menuCanceled(MenuEvent e) {
	}

	@Override
	public void menuDeselected(MenuEvent e) {
	}

	@Override
	public void menuSelected(MenuEvent e) {
		updateClusterList();
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParallelCoordinatesChartFrameAddDesignToClusterMenu.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
