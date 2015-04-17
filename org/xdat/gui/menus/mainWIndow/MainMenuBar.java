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

import javax.swing.JMenuBar;

import org.xdat.Main;

/**
 * Menu Bar for the {@link org.xdat.Main} window.
 */
public class MainMenuBar extends JMenuBar {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** File menu. */
	private MainFileMenu mainFileMenu;

	/** Data menu. */
	private MainDataMenu mainDataMenu;

	/** Chart menu. */
	private MainChartMenu mainChartMenu;

	/** Options menu. */
	private MainOptionsMenu mainOptionsMenu;

	/** Help menu. */
	private MainHelpMenu mainHelpMenu;

	/**
	 * Instantiates a new main menu bar.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
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

	/**
	 * Gets the main chart menu.
	 * 
	 * @return the main chart menu
	 */
	public MainChartMenu getMainChartMenu() {
		return mainChartMenu;
	}

	/**
	 * Gets the main data menu.
	 * 
	 * @return the main data menu
	 */
	public MainDataMenu getMainDataMenu() {
		return mainDataMenu;
	}

	/**
	 * Gets the main file menu.
	 * 
	 * @return the main file menu
	 */
	public MainFileMenu getMainFileMenu() {
		return mainFileMenu;
	}

	/**
	 * Gets the main help menu.
	 * 
	 * @return the main help menu
	 */
	public MainHelpMenu getMainHelpMenu() {
		return mainHelpMenu;
	}

	/**
	 * Gets the main options menu.
	 * 
	 * @return the main options menu
	 */
	public MainOptionsMenu getMainOptionsMenu() {
		return mainOptionsMenu;
	}

	/**
	 * Tells the main menu bar whether the {@link org.xdat.data.DataSheet} is
	 * enabled. This is required because some functionality is only available
	 * when data is loaded.
	 * <p>
	 * When called, this function calls the relevant functions on the submenus
	 * to enable/disable the functionality that depends on the datasheet status.
	 * 
	 * @param enabled
	 *            specifies whether the data sheet contains data.
	 */
	public void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.mainDataMenu.setItemsRequiringDataSheetEnabled(enabled);
		this.mainChartMenu.setItemsRequiringDataSheetEnabled(enabled);
	}
}
