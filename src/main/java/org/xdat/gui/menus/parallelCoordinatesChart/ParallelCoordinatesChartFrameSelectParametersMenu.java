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
import org.xdat.actionListeners.chartFrames.ChartFrameSelectParametersMenuActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.KeyEvent;

class ParallelCoordinatesChartFrameSelectParametersMenu extends JMenu implements MenuListener {

	private final Main mainWindow;
	private final ChartFrameSelectParametersMenuActionListener cmd;
	private final ParallelCoordinatesChart chart;

	ParallelCoordinatesChartFrameSelectParametersMenu(Main mainWindow, ChartFrame chartFrame, ParallelCoordinatesChart chart) {
		super("Parameters");
		this.addMenuListener(this);
		this.mainWindow = mainWindow;
		this.chart = chart;
		this.setMnemonic(KeyEvent.VK_P);
		this.cmd = new ChartFrameSelectParametersMenuActionListener(mainWindow, chartFrame, chart);

	}

	private void buildMenu() {
		this.removeAll();

		JMenuItem mi;
		mi = new JMenuItem("Select All");
		mi.setMnemonic(KeyEvent.VK_S);
		mi.addActionListener(e -> cmd.setAllSelected(true));
		this.add(mi);

		mi = new JMenuItem("Unselect All");
		mi.setMnemonic(KeyEvent.VK_U);
		mi.addActionListener(e -> cmd.setAllSelected(false));
		this.add(mi);

		mi = new JMenuItem("Reverse Selection");
		mi.setMnemonic(KeyEvent.VK_R);
		mi.addActionListener(cmd::reverseSelection);
		this.add(mi);

		mi = new JMenuItem("Custom Selection");
		mi.setMnemonic(KeyEvent.VK_C);
		mi.addActionListener(cmd::customSelection);
		this.add(mi);

		this.addSeparator();
		JCheckBoxMenuItem[] checkBoxMenuItems = new JCheckBoxMenuItem[this.mainWindow.getDataSheet().getParameterCount()];
		for (int i = 0; i < checkBoxMenuItems.length; i++) {
			checkBoxMenuItems[i] = new JCheckBoxMenuItem(this.mainWindow.getDataSheet().getParameter(i).getName());
			checkBoxMenuItems[i].addActionListener(cmd::toggleParameter);
			this.chart.getAxis(0);
			checkBoxMenuItems[i].setState(this.chart.getAxis(this.mainWindow.getDataSheet().getParameter(i).getName()).isActive());

			this.add(checkBoxMenuItems[i]);
		}
	}

	@Override
	public void menuCanceled(MenuEvent arg0) {
	}

	@Override
	public void menuDeselected(MenuEvent arg0) {
	}

	@Override
	public void menuSelected(MenuEvent arg0) {
		buildMenu();
	}
}
