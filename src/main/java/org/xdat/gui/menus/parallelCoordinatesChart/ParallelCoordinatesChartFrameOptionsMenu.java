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
import org.xdat.actionListeners.chartFrames.ParallelChartFrameOptionsMenuActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

class ParallelCoordinatesChartFrameOptionsMenu extends JMenu {

	ParallelCoordinatesChartFrameOptionsMenu(Main mainWindow, ChartFrame chartFrame) {
		super("Options");
		this.setMnemonic(KeyEvent.VK_O);
		JMenuItem mi;
		ParallelChartFrameOptionsMenuActionListener cmd = new ParallelChartFrameOptionsMenuActionListener(mainWindow, (ParallelCoordinatesChart) chartFrame.getChart(), chartFrame);

		mi = new JMenuItem("Display Settings", 'd');
		mi.setMnemonic(KeyEvent.VK_D);
		mi.addActionListener(cmd::displaySettings);
		this.add(mi);

		mi = new JMenuItem("Reset to Default", 'r');
		mi.setMnemonic(KeyEvent.VK_R);
		mi.addActionListener(cmd::resetToDefault);
		this.add(mi);

		mi = new JMenuItem("Export to png", 'p');
		mi.setMnemonic(KeyEvent.VK_P);
		mi.addActionListener(cmd::exportToPng);
		this.add(mi);

		mi = new JMenuItem("Export to svg", 's');
		mi.setMnemonic(KeyEvent.VK_S);
		mi.addActionListener(cmd::exportToSvg);
		this.add(mi);
	}
}
