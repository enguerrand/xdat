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
package org.xdat.actionListeners.licenseDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.gui.dialogs.LicenseDisplayDialog;

/**
 * ActionListener for {@link org.xdat.gui.dialogs.LicenseDisplayDialog}.
 */
public class LicenseDisplayDialogActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	private static final boolean printLog = false;

	/** The license display dialog. */
	private LicenseDisplayDialog dialog;

	/** The {@link org.xdat.UserPreferences}. */
	private UserPreferences preferences;

	/**
	 * Instantiates a new license display dialog action listener.
	 * 
	 * @param dialog
	 *            the dialog
	 * @param preferences
	 *            the user preferences
	 */
	public LicenseDisplayDialogActionListener(LicenseDisplayDialog dialog, UserPreferences preferences) {
		log("constructor called.");
		this.preferences = preferences;
		this.dialog = dialog;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Accept License Agreement")) {
			this.preferences.setLicenseAccepted(true);
			dialog.dispose();
		} else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (LicenseDisplayDialogActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
