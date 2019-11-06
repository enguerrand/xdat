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

import org.xdat.BuildProperties;
import org.xdat.Main;
import org.xdat.gui.dialogs.LicenseDisplayDialog;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.KeyEvent;

class MainHelpMenu extends JMenu {



	MainHelpMenu(Main mainWindow) {
		super("Help");
		BuildProperties properties = new BuildProperties();
		this.setMnemonic(KeyEvent.VK_H);
		JMenuItem mi;

		mi = new JMenuItem("About", 'a');
		mi.setMnemonic(KeyEvent.VK_A);
		mi.addActionListener(actionEvent ->
				onAbout(mainWindow, properties)
		);
		this.add(mi);

		JMenuItem itemLicense = new JMenuItem("License");
		itemLicense.addActionListener(actionEvent ->
				onLicense(mainWindow)
		);
		itemLicense.setMnemonic(KeyEvent.VK_L);
		add(itemLicense);
	}

	private void onAbout(Main mainWindow, BuildProperties properties) {
		final String version = properties.getVersion();
		final String buildDate = properties.getBuildDate();
		JOptionPane.showMessageDialog(mainWindow,
				"xdat "+version+"\n" +
						"Built on "+buildDate +"\n" +
						"Created by: Enguerrand de Rochefort\n" +
						"Licensed under: GNU General Public License, Version 3\n\n" +
						"Visit www.xdat.org to get help."
				,
				"About xdat", JOptionPane.INFORMATION_MESSAGE);
	}

	private void onLicense(JFrame mainWindow) {
		LicenseDisplayDialog d = new LicenseDisplayDialog(mainWindow);
		d.setVisible(true);
	}
}
