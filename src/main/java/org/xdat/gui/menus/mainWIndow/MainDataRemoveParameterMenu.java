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

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.xdat.Main;
import org.xdat.actionListeners.mainMenu.MainDataRemoveParameterMenuActionListener;

/**
 * Submenu that lists all parameters. The selected parameter will be removed
 * from the datasheet.
 */
public class MainDataRemoveParameterMenu extends JMenu implements MenuListener {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/**
	 * Instantiates a new Main Data Remove Parameter Menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainDataRemoveParameterMenu(Main mainWindow) {
		super("Remove Parameter");
		this.mainWindow = mainWindow;
		this.setMnemonic(KeyEvent.VK_P);
		this.addMenuListener(this);

	}

	private void updateParameterList() {
		this.removeAll();
		MainDataRemoveParameterMenuActionListener cmd = new MainDataRemoveParameterMenuActionListener(mainWindow);
		// List of parameters
		JMenuItem[] menuItems = new JMenuItem[mainWindow.getDataSheet().getParameterCount()];
		for (int i = 0; i < menuItems.length; i++) {
			menuItems[i] = new JMenuItem(mainWindow.getDataSheet().getParameterName(i));
			menuItems[i].addActionListener(cmd);
			this.add(menuItems[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuCanceled(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuCanceled(MenuEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuDeselected(javax.swing.event.MenuEvent
	 * )
	 */
	@Override
	public void menuDeselected(MenuEvent e) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.event.MenuListener#menuSelected(javax.swing.event.MenuEvent)
	 */
	@Override
	public void menuSelected(MenuEvent e) {
		log("menuSelected called");
		this.updateParameterList();

	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (MainDataRemoveParameterMenu.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
