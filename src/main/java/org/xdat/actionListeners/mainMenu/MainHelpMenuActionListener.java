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

import org.xdat.BuildProperties;
import org.xdat.Main;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainHelpMenuActionListener implements ActionListener {

	private final Main mainWindow;
	private final BuildProperties properties;

	public MainHelpMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;
		properties = new BuildProperties();

	}

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
		}
	}
}
