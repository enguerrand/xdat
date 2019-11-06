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

package org.xdat.gui.menus.mainWindow;

import org.xdat.Main;

import javax.swing.JMenuBar;

public class MainMenuBar extends JMenuBar {

	private final MainFileMenu mainFileMenu;
	private final MainDataMenu mainDataMenu;
	private final MainChartMenu mainChartMenu;
	private final MainOptionsMenu mainOptionsMenu;
	private final MainHelpMenu mainHelpMenu;

	public MainMenuBar(Main mainWindow) {
		this.mainFileMenu = new MainFileMenu(mainWindow);
		this.mainDataMenu = new MainDataMenu(mainWindow);
		this.mainChartMenu = new MainChartMenu(mainWindow);
		this.mainOptionsMenu = new MainOptionsMenu(mainWindow);
		this.mainHelpMenu = new MainHelpMenu(mainWindow);

		this.add(mainFileMenu);
		this.add(mainDataMenu);
		this.add(mainChartMenu);
		this.add(mainOptionsMenu);
		this.add(mainHelpMenu);

	}

	public void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.mainDataMenu.setItemsRequiringDataSheetEnabled(enabled);
		this.mainChartMenu.setItemsRequiringDataSheetEnabled(enabled);
	}
}
