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

package org.xdat.actionListeners.importSettings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonModel;
import javax.swing.JFileChooser;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.gui.dialogs.FileImportSettingsDialog;

/**
 * ActionListener for a {@link FileImportSettingsDialog}.
 */
public class FileImportSettingsDialogActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The main window. */
	Main mainWindow;

	/** The dialog. */
	private FileImportSettingsDialog dialog;

	/**
	 * Instantiates a new file import settings dialog action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param dialog
	 *            the dialog
	 */
	public FileImportSettingsDialogActionListener(Main mainWindow, FileImportSettingsDialog dialog) {
		log("constructor called.");
		this.mainWindow = mainWindow;
		this.dialog = dialog;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand == "...") {
			String filepath;
			JFileChooser chooser = new JFileChooser();
			if (UserPreferences.getInstance().getCurrentDir() != null) {
				chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
			}
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = chooser.showOpenDialog(this.mainWindow);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = chooser.getSelectedFile().getAbsolutePath();
				dialog.getUseThisTextField().setText(filepath);
			}

		} else if (actionCommand == "Ok") {
			ButtonModel fileBrowsingSelectedModel = this.dialog.getFileBrowsingButtonGroup().getSelection();
			if (fileBrowsingSelectedModel.equals(dialog.getUseHomeRadioButton().getModel())) {
				UserPreferences.getInstance().setDirToImportFrom(UserPreferences.IMPORT_FROM_HOMEDIR);
			} else if (fileBrowsingSelectedModel.equals(dialog.getUseLastRadioButton().getModel())) {
				UserPreferences.getInstance().setDirToImportFrom(UserPreferences.IMPORT_FROM_LASTDIR);
			} else if (fileBrowsingSelectedModel.equals(dialog.getUseThisRadioButton().getModel())) {
				UserPreferences.getInstance().setDirToImportFrom(UserPreferences.IMPORT_FROM_USERDIR);
			}
			UserPreferences.getInstance().setUserDir(dialog.getUseThisTextField().getText());

			ButtonModel delimiterSelectedModel = this.dialog.getDelimiterButtonGroup().getSelection();
			if (delimiterSelectedModel.equals(dialog.getSpaceRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter(" ");
			} else if (delimiterSelectedModel.equals(dialog.getTabRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter("\\t");
			} else if (delimiterSelectedModel.equals(dialog.getAllBlanksRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter("\\s");
			} else if (delimiterSelectedModel.equals(dialog.getCommaRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter(",");
			} else if (delimiterSelectedModel.equals(dialog.getSemiColonRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter(";");
			} else if (delimiterSelectedModel.equals(dialog.getOtherRadioButton().getModel())) {
				UserPreferences.getInstance().setDelimiter(this.dialog.getOtherTextField().getText());
				UserPreferences.getInstance().setOtherDelimiter(this.dialog.getOtherTextField().getText());
			}
			UserPreferences.getInstance().setTreatConsecutiveAsOne(this.dialog.getTreatConsecutiveAsOneCheckBox().isSelected());

			// NumberFormat locale
			ButtonModel localeButtonModel = this.dialog.getNumberFormatLocaleButtonGroup().getSelection();
			if (localeButtonModel.equals(dialog.getGermanLocaleRadioButton().getModel())) {
				UserPreferences.getInstance().setLocale(UserPreferences.LOCALE_DE);
			} else {
				UserPreferences.getInstance().setLocale(UserPreferences.LOCALE_US);
			}
			this.dialog.dispose();
		} else if (actionCommand == "Cancel") {
			this.dialog.dispose();
		} else {
			log(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (FileImportSettingsDialogActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
