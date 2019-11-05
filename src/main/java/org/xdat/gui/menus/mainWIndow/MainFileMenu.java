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
import org.xdat.actionListeners.mainMenu.MainFileMenuActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.KeyEvent;

public class MainFileMenu extends JMenu {

	public MainFileMenu(Main mainWindow) {
		super("File");
		this.setMnemonic(KeyEvent.VK_F);
		JMenuItem mi;
		MainFileMenuActionListener cmd = new MainFileMenuActionListener(mainWindow);
		// Save and Load Sessions
		mi = new JMenuItem("Load Session", 'l');
		mi.setMnemonic(KeyEvent.VK_L);
		mi.addActionListener(cmd);
		this.add(mi);
		mi = new JMenuItem("Save Session", 's');
		mi.setMnemonic(KeyEvent.VK_S);
		mi.addActionListener(cmd);
		this.add(mi);
		mi = new JMenuItem("Save Session As...", 'a');
		mi.setMnemonic(KeyEvent.VK_A);
		mi.addActionListener(cmd);
		this.add(mi);
		// Exit
		mi = new JMenuItem("Exit", 'x');
		mi.setMnemonic(KeyEvent.VK_X);
		mi.addActionListener(cmd);
		this.add(mi);
	}
}
