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

import org.xdat.Main;
import org.xdat.gui.dialogs.ClusterDialog;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

class ParallelCoordinatesChartFrameClusteringMenu extends JMenu {

	ParallelCoordinatesChartFrameClusteringMenu(Main mainWindow, ChartFrame chartFrame) {
		super("Clustering");
		this.setMnemonic(KeyEvent.VK_C);

		JMenuItem mi;
		mi = new JMenuItem("Edit Clusters");
		mi.addActionListener(actionEvent -> new ClusterDialog(chartFrame, mainWindow));
		this.add(mi);

		ParallelCoordinatesChartFrameAddDesignToClusterMenu clustersMenu;
		clustersMenu = new ParallelCoordinatesChartFrameAddDesignToClusterMenu(mainWindow, chartFrame);
		this.add(clustersMenu);
	}
}
