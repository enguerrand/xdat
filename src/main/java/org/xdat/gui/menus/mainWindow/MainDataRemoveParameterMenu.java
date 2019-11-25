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

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.KeyEvent;

public class MainDataRemoveParameterMenu extends JMenu implements MenuListener {

	private final Main mainWindow;

	public MainDataRemoveParameterMenu(Main mainWindow) {
		super("Remove Parameter");
		this.mainWindow = mainWindow;
		this.setMnemonic(KeyEvent.VK_P);
		this.addMenuListener(this);

	}

	private void updateParameterList() {
		this.removeAll();
		JMenuItem[] menuItems = new JMenuItem[mainWindow.getDataSheet().getParameterCount()];
		for (int i = 0; i < menuItems.length; i++) {
			String parameterName = mainWindow.getDataSheet().getParameterName(i);
			menuItems[i] = new JMenuItem(parameterName);
			menuItems[i].addActionListener(actionEvent ->
					mainWindow.removeParameter(parameterName));
			this.add(menuItems[i]);
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
		this.updateParameterList();
	}
}
