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

package org.xdat.actionListeners.mainMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.xdat.BuildProperties;
import org.xdat.Main;
import org.xdat.gui.menus.mainWIndow.MainHelpMenu;

/**
 * ActionListener for the {@link MainHelpMenu}.<br>
 * If a textfile with the name build-date.txt is found in the root directory of
 * the package the text will be read from the file and printed as the build date
 * in the about dialog.
 */
public class MainHelpMenuActionListener implements ActionListener {

	/**
	 * The main window.
	 */
	private Main mainWindow;

	/**
	 * Flag to enable debug message printing for this class.
	 */
	static final boolean printLog = false;
	private final BuildProperties properties;

	/**
	 * Instantiates a new main help menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 */
	public MainHelpMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;
		properties = new BuildProperties();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("About")) {
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
		} else {
			System.out.println(e.getActionCommand());
		}
	}
}
