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
import org.xdat.actionListeners.chartFrames.ChartFrameAddDesignToClusterMenuActionListener;
import org.xdat.data.ClusterSet;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.KeyEvent;

public class ParallelCoordinatesChartFrameAddDesignToClusterMenu extends JMenu implements MenuListener {

	private final Main mainWindow;
	private final ChartFrame chartFrame;

	ParallelCoordinatesChartFrameAddDesignToClusterMenu(Main mainWindow, ChartFrame chartFrame) {
		super("Add filtered designs to cluster");
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.setMnemonic(KeyEvent.VK_A);
		this.addMenuListener(this);
	}

	private void updateClusterList() {
		this.removeAll();
		ChartFrameAddDesignToClusterMenuActionListener cmd = new ChartFrameAddDesignToClusterMenuActionListener(mainWindow, chartFrame);

		JMenuItem mi;
		ClusterSet clusterSet = mainWindow.getCurrentClusterSet();
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
}
