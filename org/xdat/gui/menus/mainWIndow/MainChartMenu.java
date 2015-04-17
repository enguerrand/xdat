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

import java.awt.Event;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.xdat.Main;
import org.xdat.actionListeners.mainMenu.MainChartMenuActionListener;

/**
 * Chart menu for the {@link org.xdat.Main} window.
 */
public class MainChartMenu extends JMenu {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** The create parallel coordinates chart menu item. */
	private JMenuItem createPCChartMenuItem = new JMenuItem("Create Parallel Coordinates Chart", 'p');

	/** The create 2D scatter chart menu item. */
	private JMenuItem createScatter2DChartMenuItem = new JMenuItem("Create Scatter Chart 2D", 's');

	/**
	 * Instantiates a new main chart menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainChartMenu(Main mainWindow) {
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

	/**
	 * Specifies whether the menu item createMenuItem is enabled. This is
	 * required because this item is only available when data is loaded.
	 * 
	 * @param enabled
	 *            specifies whether the menu item createMenuItem is enabled.
	 */
	public void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.createPCChartMenuItem.setEnabled(enabled);
		this.createScatter2DChartMenuItem.setEnabled(enabled);
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

}
