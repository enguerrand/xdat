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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.gui.dialogs.FileImportSettingsDialog;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainOptionsMenuActionListener implements ActionListener {

	private Main mainWindow;

	public MainOptionsMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Import Settings")) {
			new FileImportSettingsDialog(this.mainWindow);
		} else if (e.getActionCommand().equals("Parallel Coordinate Settings")) {
			new ParallelCoordinatesDisplaySettingsDialog(this.mainWindow);
		} else if (e.getActionCommand().equals("Reset to Default")) {
			int userDecision = JOptionPane.showConfirmDialog(mainWindow, "This resets all settings to default! Are you sure?", "Reset to Default", JOptionPane.OK_CANCEL_OPTION);
			if (userDecision == JOptionPane.OK_OPTION) {
				UserPreferences.getInstance().resetToDefault();
			}
		}
	}
}
