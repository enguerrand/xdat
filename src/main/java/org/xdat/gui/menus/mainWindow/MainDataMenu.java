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
import org.xdat.actionListeners.mainMenu.MainDataMenuActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Event;
import java.awt.event.KeyEvent;

public class MainDataMenu extends JMenu {

	private final JMenuItem importDataWithHeadersMenuItem = new JMenuItem("Import Data with Headers", 'i');
	private final JMenuItem updateDataWithHeadersMenuItem = new JMenuItem("Update Data from File with Headers", 'u');
	private final JMenuItem updateDataWithoutHeadersMenuItem = new JMenuItem("Update Data from File without Headers", 'o');
	private final JMenuItem removeSelectedDesignsMenuItem = new JMenuItem("Remove selected designs", 'd');
	private final JMenuItem unselectAllMenuItem = new JMenuItem("Unselect all designs", Event.ESCAPE);
	private final MainDataRemoveParameterMenu removeParametersMenu;
	private final JMenuItem clusteringMenuItem = new JMenuItem("Clustering", 'c');

	MainDataMenu(Main mainWindow) {
		super("Data");
		this.setMnemonic(KeyEvent.VK_D);

		removeParametersMenu = new MainDataRemoveParameterMenu(mainWindow);
		MainDataMenuActionListener cmd = new MainDataMenuActionListener(mainWindow);

		// Import Data
		importDataWithHeadersMenuItem.setMnemonic(KeyEvent.VK_I);
		importDataWithHeadersMenuItem.addActionListener(cmd);
		this.add(importDataWithHeadersMenuItem);
		JMenuItem importDataWithoutHeadersMenuItem = new JMenuItem("Import Data without Headers", 'w');
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

	void setItemsRequiringDataSheetEnabled(boolean enabled) {
		this.updateDataWithHeadersMenuItem.setEnabled(enabled);
		this.updateDataWithoutHeadersMenuItem.setEnabled(enabled);
		this.removeSelectedDesignsMenuItem.setEnabled(enabled);
		this.removeParametersMenu.setEnabled(enabled);
		this.unselectAllMenuItem.setEnabled(enabled);
		this.clusteringMenuItem.setEnabled(enabled);
	}

}
