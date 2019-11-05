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
import org.xdat.Session;
import org.xdat.UserPreferences;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFileMenuActionListener implements ActionListener {

	private Main mainWindow;
	public MainFileMenuActionListener(Main mainWindow) {
		this.mainWindow = mainWindow;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Load Session")) {
			String filepath;
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.ses files", "ses");
			chooser.setFileFilter(filter);
			chooser.addChoosableFileFilter(filter);
			if (UserPreferences.getInstance().getCurrentDir() != null) {
				chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
			}
			int returnVal = chooser.showOpenDialog(mainWindow);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = chooser.getSelectedFile().getAbsolutePath();
				UserPreferences.getInstance().setLastFile(chooser.getSelectedFile().getAbsolutePath());
				this.mainWindow.loadSession(filepath);
				this.mainWindow.initialiseDataPanel();
			}

		} else if (e.getActionCommand().equals("Save Session As...")) {
			saveSessionAs();

		} else if (e.getActionCommand().equals("Save Session")) {
			Session session = mainWindow.getCurrentSession();
			if (session.getSessionDirectory() == null || session.getSessionName() == null) {
				saveSessionAs();
			} else {
				String filepath = session.getSessionDirectory() + System.getProperty("file.separator") + session.getSessionName() + Session.sessionFileExtension;
				this.mainWindow.saveSessionAs(filepath);
			}
		} else if (e.getActionCommand().equals("Exit")) {
			mainWindow.setVisible(false);
			mainWindow.dispose();
			System.exit(0);
		} else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Save session as.
	 */
	private void saveSessionAs() {
		Session session = mainWindow.getCurrentSession();
		String filepath;
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.ses files", "ses");
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filter);
		if (UserPreferences.getInstance().getCurrentDir() != null) {
			chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
		}
		int returnVal = chooser.showSaveDialog(mainWindow);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filepath = chooser.getSelectedFile().getAbsolutePath();
			String filename = chooser.getSelectedFile().getName();
			session.setSessionDirectory(chooser.getSelectedFile().getParent());
			if (filename.endsWith(Session.sessionFileExtension)) {
				session.setSessionName(filename.substring(0, filename.length() - 4));
				this.mainWindow.saveSessionAs(filepath);
			} else {
				session.setSessionName(filename);
				this.mainWindow.saveSessionAs(filepath + Session.sessionFileExtension);
			}
			UserPreferences.getInstance().setLastFile(filepath);
			this.mainWindow.setTitle("xdat   -   " + filepath + Session.sessionFileExtension);
		}
	}
}
