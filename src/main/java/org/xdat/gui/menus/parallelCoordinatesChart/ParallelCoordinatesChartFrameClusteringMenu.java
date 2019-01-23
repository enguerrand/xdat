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

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ChartFrameClusteringMenuActionListener;
import org.xdat.gui.frames.ChartFrame;

/**
 * Menu for a {@link org.xdat.gui.frames.ChartFrame} to edit
 * {@link org.xdat.data.Cluster}s.
 */
public class ParallelCoordinatesChartFrameClusteringMenu extends JMenu {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/**
	 * Instantiates a new chart frame clustering menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chartFrame
	 *            the chart frame
	 */
	public ParallelCoordinatesChartFrameClusteringMenu(Main mainWindow, ChartFrame chartFrame) {
		super("Clustering");
		log("constructor called.");
		this.setMnemonic(KeyEvent.VK_C);

		// Edit clusters
		ChartFrameClusteringMenuActionListener cmd = new ChartFrameClusteringMenuActionListener(mainWindow, chartFrame);

		JMenuItem mi;
		mi = new JMenuItem("Edit Clusters");
		mi.addActionListener(cmd);
		this.add(mi);

		// Add to Cluster
		ParallelCoordinatesChartFrameAddDesignToClusterMenu clustersMenu;
		clustersMenu = new ParallelCoordinatesChartFrameAddDesignToClusterMenu(mainWindow, chartFrame);
		this.add(clustersMenu);

	}

	/**
	 * Sets the ctrl accelerator.
	 * 
	 * @param mi
	 *            the menuitem
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
		if (ParallelCoordinatesChartFrameClusteringMenu.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
