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

package org.xdat.gui.menus.mainWIndow;

import org.xdat.Main;
import org.xdat.actionListeners.mainMenu.MainChartMenuActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

public class MainChartMenu extends JMenu {

	private final JMenuItem createPCChartMenuItem = new JMenuItem("Create Parallel Coordinates Chart", 'p');
	private final JMenuItem createScatter2DChartMenuItem = new JMenuItem("Create Scatter Chart 2D", 's');

	MainChartMenu(Main mainWindow) {
		super("Chart");
		this.setMnemonic(KeyEvent.VK_C);
		MainChartMenuActionListener cmd = new MainChartMenuActionListener(mainWindow);
	
		createPCChartMenuItem.setMnemonic(KeyEvent.VK_P);
		createPCChartMenuItem.addActionListener(cmd);
		createPCChartMenuItem.setEnabled(false);
		this.add(createPCChartMenuItem);

		createScatter2DChartMenuItem.setMnemonic(KeyEvent.VK_S);
		createScatter2DChartMenuItem.addActionListener(cmd);
		createScatter2DChartMenuItem.setEnabled(false);
		this.add(createScatter2DChartMenuItem);

		this.setItemsRequiringDataSheetEnabled(false);
	}

	void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.createPCChartMenuItem.setEnabled(enabled);
		this.createScatter2DChartMenuItem.setEnabled(enabled);
	}
}
