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
import org.xdat.actionListeners.mainMenu.MainDataMenuActionListener;

/**
 * Data menu for the {@link org.xdat.Main} window.
 */
public class MainDataMenu extends JMenu {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** The import data with headers menu item. */
	private JMenuItem importDataWithHeadersMenuItem = new JMenuItem("Import Data with Headers", 'i');

	/** The import data without headers menu item. */
	private JMenuItem importDataWithoutHeadersMenuItem = new JMenuItem("Import Data without Headers", 'w');

	/** The update data with headers menu item. */
	private JMenuItem updateDataWithHeadersMenuItem = new JMenuItem("Update Data from File with Headers", 'u');

	/** The update data without headers menu item. */
	private JMenuItem updateDataWithoutHeadersMenuItem = new JMenuItem("Update Data from File without Headers", 'o');

	/** The remove selected designs menu item. */
	private JMenuItem removeSelectedDesignsMenuItem = new JMenuItem("Remove selected designs", 'd');

	/** The unselect all menu item. */
	private JMenuItem unselectAllMenuItem = new JMenuItem("Unselect all designs", Event.ESCAPE);

	/** The remove parameters menu. */
	private MainDataRemoveParameterMenu removeParametersMenu;

	/** The clustering menu item. */
	private JMenuItem clusteringMenuItem = new JMenuItem("Clustering", 'c');

	/**
	 * Instantiates a new main data menu.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainDataMenu(Main mainWindow) {
		super("Data");
		this.setMnemonic(KeyEvent.VK_D);

		removeParametersMenu = new MainDataRemoveParameterMenu(mainWindow);
		MainDataMenuActionListener cmd = new MainDataMenuActionListener(mainWindow);

		// Import Data
		importDataWithHeadersMenuItem.setMnemonic(KeyEvent.VK_I);
		importDataWithHeadersMenuItem.addActionListener(cmd);
		this.add(importDataWithHeadersMenuItem);
		importDataWithoutHeadersMenuItem.setMnemonic(KeyEvent.VK_W);
		importDataWithoutHeadersMenuItem.addActionListener(cmd);
		this.add(importDataWithoutHeadersMenuItem);
		// Separator
		this.addSeparator();
		// Update Data
		updateDataWithHeadersMenuItem.setMnemonic(KeyEvent.VK_U);
		updateDataWithHeadersMenuItem.addActionListener(cmd);
		this.add(updateDataWithHeadersMenuItem);
		updateDataWithoutHeadersMenuItem.setMnemonic(KeyEvent.VK_O);
		updateDataWithoutHeadersMenuItem.addActionListener(cmd);
		this.add(updateDataWithoutHeadersMenuItem);
		// Separator
		this.addSeparator();
		// Edit Data
		removeSelectedDesignsMenuItem.setMnemonic(KeyEvent.VK_DELETE);
		removeSelectedDesignsMenuItem.addActionListener(cmd);
		this.add(removeSelectedDesignsMenuItem);
		this.add(removeParametersMenu);
		// Separator
		this.addSeparator();
		// Unselect designs
		unselectAllMenuItem.addActionListener(cmd);
		this.add(unselectAllMenuItem);
		// Separator
		this.addSeparator();
		// Clustering
		clusteringMenuItem.setMnemonic(KeyEvent.VK_C);
		clusteringMenuItem.addActionListener(cmd);
		this.add(clusteringMenuItem);

		this.setItemsRequiringDataSheetEnabled(false);

	}

	/**
	 * Specifies whether the menu items requiring an active datasheet are
	 * enabled.
	 * 
	 * @param enabled
	 *            specifies whether the menu items requiring an active datasheet
	 *            are enabled.
	 */
	public void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.updateDataWithHeadersMenuItem.setEnabled(enabled);
		this.updateDataWithoutHeadersMenuItem.setEnabled(enabled);
		this.removeSelectedDesignsMenuItem.setEnabled(enabled);
		this.removeParametersMenu.setEnabled(enabled);
		this.unselectAllMenuItem.setEnabled(enabled);
		this.clusteringMenuItem.setEnabled(enabled);
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
