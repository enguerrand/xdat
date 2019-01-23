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

package org.xdat.gui.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Locale;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.actionListeners.importSettings.FileImportSettingsDialogActionListener;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.panels.TitledSubPanel;

/**
 * Dialog to edit the settings for file import, such as delimiters and default
 * browsing location.
 */
public class FileImportSettingsDialog extends JDialog {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** Flag to enable debug message printing for this class. */
	private static final boolean printLog = false;

	/** The main window. */
	private Main mainWindow;

	/** The file browsing button group. */
	private ButtonGroup fileBrowsingButtonGroup = new ButtonGroup();

	/** The delimiter button group. */
	private ButtonGroup delimiterButtonGroup = new ButtonGroup();

	/** The number format locale button group. */
	private ButtonGroup numberFormatLocaleButtonGroup = new ButtonGroup();

	/** Text field to enter a userspecific delimiting character. */
	private JTextField otherTextField;

	/** Text field to enter a user-specific default browsing location. */
	private JTextField useThisTextField;

	/** Enables treating consecutive delimiters as one. */
	private JCheckBox treatConsecutiveAsOneCheckBox;

	/** Enables blank space as delimiting character. */
	JRadioButton spaceRadioButton = new JRadioButton("Space");

	/** Enables tabs as delimiting character */
	JRadioButton tabRadioButton = new JRadioButton("Tabstop");

	/** Enables any white space as delimiting character. */
	JRadioButton allBlanksRadioButton = new JRadioButton("Any Blank Space");

	/** Enables comma as delimiting character. */
	JRadioButton commaRadioButton = new JRadioButton("Comma");

	/** Enables semi-colon as delimiting character. */
	JRadioButton semiColonRadioButton = new JRadioButton("Semi-colon");

	/** Enables the user-specific character as delimiting character. */
	JRadioButton otherRadioButton = new JRadioButton("Other: ");

	/** Sets the default browsing location to the user's home directory. */
	JRadioButton useHomeRadioButton = new JRadioButton("Use home directory");

	/** Sets the default browsing location to the last opened directory. */
	JRadioButton useLastRadioButton = new JRadioButton("Use last opened directory");

	/** Sets the default browsing location to the user-specific directory. */
	JRadioButton useThisRadioButton = new JRadioButton("Use this directory:  ");

	/** Sets the number format locale to US English. */
	JRadioButton usLocaleRadioButton = new JRadioButton("US Number Formats (1,234.56)");

	/** Sets the number format locale to German. */
	JRadioButton germanLocaleRadioButton = new JRadioButton("German Number Formats (1.234,56)");

	/**
	 * Instantiates a new file import settings dialog.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @throws HeadlessException
	 *             the headless exception
	 */
	public FileImportSettingsDialog(Main mainWindow) throws HeadlessException {
		super(mainWindow, "File Import Settings", true);

		log("constructor called");
		this.addWindowListener(new WindowClosingAdapter(false));
		this.setResizable(false);
		this.mainWindow = mainWindow;

		// create components
		JPanel contentPanel = new JPanel();
		TitledSubPanel fileBrowsingSettingsPanel = new TitledSubPanel("File Browsing default Location");
		TitledSubPanel delimiterPanel = new TitledSubPanel("Delimiters");
		TitledSubPanel localePanel = new TitledSubPanel("Number Format Locale");
		TitledSubPanel buttonsPanel = new TitledSubPanel("");
		JPanel useThisPathPanel = new JPanel();

		fileBrowsingButtonGroup.add(useHomeRadioButton);
		fileBrowsingButtonGroup.add(useLastRadioButton);
		fileBrowsingButtonGroup.add(useThisRadioButton);
		if (UserPreferences.getInstance().getDirToImportFrom() == (UserPreferences.IMPORT_FROM_HOMEDIR)) {
			fileBrowsingButtonGroup.setSelected(useHomeRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDirToImportFrom() == (UserPreferences.IMPORT_FROM_LASTDIR)) {
			fileBrowsingButtonGroup.setSelected(useLastRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDirToImportFrom() == (UserPreferences.IMPORT_FROM_USERDIR)) {
			fileBrowsingButtonGroup.setSelected(useThisRadioButton.getModel(), true);
		}

		numberFormatLocaleButtonGroup.add(usLocaleRadioButton);
		numberFormatLocaleButtonGroup.add(germanLocaleRadioButton);
		if (UserPreferences.getInstance().getLocale() == Locale.GERMANY) {
			numberFormatLocaleButtonGroup.setSelected(germanLocaleRadioButton.getModel(), true);
		} else {
			numberFormatLocaleButtonGroup.setSelected(usLocaleRadioButton.getModel(), true);
		}

		this.useThisTextField = new JTextField();
		this.useThisTextField.setText(UserPreferences.getInstance().getUserDir());
		this.useThisTextField.setPreferredSize(new Dimension(250, 25));
		JButton useThisBrowseButton = new JButton("...");

		JPanel otherPanel = new JPanel();
		this.otherTextField = new JTextField();
		this.otherTextField.setText(UserPreferences.getInstance().getOtherDelimiter());
		delimiterButtonGroup.add(spaceRadioButton);
		delimiterButtonGroup.add(tabRadioButton);
		delimiterButtonGroup.add(allBlanksRadioButton);
		delimiterButtonGroup.add(commaRadioButton);
		delimiterButtonGroup.add(semiColonRadioButton);
		delimiterButtonGroup.add(otherRadioButton);
		if (UserPreferences.getInstance().getDelimiter().equals(" ")) {
			delimiterButtonGroup.setSelected(spaceRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDelimiter().equals("\\t")) {
			delimiterButtonGroup.setSelected(tabRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDelimiter().equals(",")) {
			delimiterButtonGroup.setSelected(commaRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDelimiter().equals(";")) {
			delimiterButtonGroup.setSelected(semiColonRadioButton.getModel(), true);
		} else if (UserPreferences.getInstance().getDelimiter().equals(this.otherTextField.getText())) {
			delimiterButtonGroup.setSelected(otherRadioButton.getModel(), true);
		} else {
			delimiterButtonGroup.setSelected(allBlanksRadioButton.getModel(), true);
		}
		this.treatConsecutiveAsOneCheckBox = new JCheckBox("Treat consecutive delimiters as one");

		this.treatConsecutiveAsOneCheckBox.setSelected(UserPreferences.getInstance().isTreatConsecutiveAsOne());
		JPanel cancelButtonPanel = new JPanel();
		JPanel okButtonPanel = new JPanel();
		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("Ok");

		// set Layouts
		this.setLayout(new BorderLayout());
		contentPanel.setLayout(new GridLayout(0, 1));
		fileBrowsingSettingsPanel.setLayout(new GridLayout(3, 1));
		useThisPathPanel.setLayout(new BorderLayout());
		delimiterPanel.setLayout(new GridLayout(4, 2));
		otherPanel.setLayout(new GridLayout(1, 2));
		localePanel.setLayout(new GridLayout(0, 1));
		cancelButtonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButtonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		buttonsPanel.setLayout(new GridLayout(1, 2));

		// add components
		this.add(contentPanel, BorderLayout.CENTER);
		contentPanel.add(fileBrowsingSettingsPanel);
		contentPanel.add(delimiterPanel);
		contentPanel.add(localePanel);
		this.add(buttonsPanel, BorderLayout.SOUTH);

		// file browsing settings
		fileBrowsingSettingsPanel.add(useHomeRadioButton);
		fileBrowsingSettingsPanel.add(useLastRadioButton);
		useThisPathPanel.add(useThisRadioButton, BorderLayout.WEST);
		useThisPathPanel.add(useThisTextField, BorderLayout.CENTER);
		useThisPathPanel.add(useThisBrowseButton, BorderLayout.EAST);
		fileBrowsingSettingsPanel.add(useThisPathPanel);

		// delimiter settings
		delimiterPanel.add(spaceRadioButton);
		delimiterPanel.add(semiColonRadioButton);
		delimiterPanel.add(tabRadioButton);
		delimiterPanel.add(otherPanel);
		otherPanel.add(otherRadioButton);
		otherPanel.add(otherTextField);
		delimiterPanel.add(allBlanksRadioButton);
		delimiterPanel.add(new JPanel());
		delimiterPanel.add(commaRadioButton);
		delimiterPanel.add(treatConsecutiveAsOneCheckBox);

		// locale settings
		localePanel.add(usLocaleRadioButton);
		localePanel.add(germanLocaleRadioButton);

		// buttons panel
		buttonsPanel.add(cancelButtonPanel);
		buttonsPanel.add(okButtonPanel);
		cancelButtonPanel.add(cancelButton);
		okButtonPanel.add(okButton);

		// add action listener
		FileImportSettingsDialogActionListener cmd = new FileImportSettingsDialogActionListener(mainWindow, this);
		useThisBrowseButton.addActionListener(cmd);
		cancelButton.addActionListener(cmd);
		okButton.addActionListener(cmd);

		// pack
		this.pack();

		// set location and make visible
		int left = (int) (0.5 * this.mainWindow.getSize().width) - (int) (this.getSize().width * 0.5) + this.mainWindow.getLocation().x;
		int top = (int) (0.5 * this.mainWindow.getSize().height) - (int) (this.getSize().height * 0.5) + this.mainWindow.getLocation().y;
		this.setLocation(left, top);
		this.setVisible(true);

	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private static final void log(String message) {
		if (FileImportSettingsDialog.printLog && Main.isLoggingEnabled()) {
			System.out.println("ChartFrameSettingsDialog" + message);
		}
	}

	/**
	 * Gets the delimiter button group.
	 * 
	 * @return the delimiter button group
	 */
	public ButtonGroup getDelimiterButtonGroup() {
		return delimiterButtonGroup;
	}

	/**
	 * Gets the file browsing button group.
	 * 
	 * @return the file browsing button group
	 */
	public ButtonGroup getFileBrowsingButtonGroup() {
		return fileBrowsingButtonGroup;
	}

	/**
	 * Gets the number format locale button group.
	 * 
	 * @return the number format locale button group
	 */
	public ButtonGroup getNumberFormatLocaleButtonGroup() {
		return numberFormatLocaleButtonGroup;
	}

	/**
	 * Gets the other text field.
	 * 
	 * @return the other text field
	 */
	public JTextField getOtherTextField() {
		return otherTextField;
	}

	/**
	 * Gets the text field that specifies the user-specific file-browsing
	 * location.
	 * 
	 * @return the use this text field
	 */
	public JTextField getUseThisTextField() {
		return useThisTextField;
	}

	/**
	 * Gets the treat consecutive as one check box.
	 * 
	 * @return the treat consecutive as one check box
	 */
	public JCheckBox getTreatConsecutiveAsOneCheckBox() {
		return treatConsecutiveAsOneCheckBox;
	}

	/**
	 * Gets the all blanks radio button.
	 * 
	 * @return the all blanks radio button
	 */
	public JRadioButton getAllBlanksRadioButton() {
		return allBlanksRadioButton;
	}

	/**
	 * Gets the other delimiting character radio button.
	 * 
	 * @return the other radio button
	 */
	public JRadioButton getOtherRadioButton() {
		return otherRadioButton;
	}

	/**
	 * Gets the semi colon radio button.
	 * 
	 * @return the semi colon radio button
	 */
	public JRadioButton getSemiColonRadioButton() {
		return semiColonRadioButton;
	}

	/**
	 * Gets the space radio button.
	 * 
	 * @return the space radio button
	 */
	public JRadioButton getSpaceRadioButton() {
		return spaceRadioButton;
	}

	/**
	 * Gets the tab radio button.
	 * 
	 * @return the tab radio button
	 */
	public JRadioButton getTabRadioButton() {
		return tabRadioButton;
	}

	/**
	 * Gets the use home directory radio button.
	 * 
	 * @return the use home radio button
	 */
	public JRadioButton getUseHomeRadioButton() {
		return useHomeRadioButton;
	}

	/**
	 * Gets the use last directory radio button.
	 * 
	 * @return the use last radio button
	 */
	public JRadioButton getUseLastRadioButton() {
		return useLastRadioButton;
	}

	/**
	 * Gets the use this directory radio button.
	 * 
	 * @return the use this radio button
	 */
	public JRadioButton getUseThisRadioButton() {
		return useThisRadioButton;
	}

	/**
	 * Gets the comma radio button.
	 * 
	 * @return the comma radio button
	 */
	public JRadioButton getCommaRadioButton() {
		return commaRadioButton;
	}

	/**
	 * Gets the us locale radio button.
	 * 
	 * @return the us locale radio button
	 */
	public JRadioButton getUsLocaleRadioButton() {
		return usLocaleRadioButton;
	}

	/**
	 * Gets the german locale radio button.
	 * 
	 * @return the german locale radio button
	 */
	public JRadioButton getGermanLocaleRadioButton() {
		return germanLocaleRadioButton;
	}

}
