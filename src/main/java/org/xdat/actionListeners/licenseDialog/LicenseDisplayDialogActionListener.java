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

import org.xdat.UserPreferences;
import org.xdat.gui.dialogs.LicenseDisplayDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LicenseDisplayDialogActionListener implements ActionListener {
	private LicenseDisplayDialog dialog;
	private UserPreferences preferences;
	public LicenseDisplayDialogActionListener(LicenseDisplayDialog dialog, UserPreferences preferences) {
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
}
