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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.actionListeners.importSettings.FileImportSettingsDialogActionListener;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.panels.TitledSubPanel;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.util.Locale;

public class FileImportSettingsDialog extends JDialog {

	static final long serialVersionUID = 1L;
	private Main mainWindow;
	private ButtonGroup fileBrowsingButtonGroup = new ButtonGroup();
	private ButtonGroup delimiterButtonGroup = new ButtonGroup();
	private ButtonGroup numberFormatLocaleButtonGroup = new ButtonGroup();
	private JTextField otherTextField;
	private JTextField useThisTextField;
	private JCheckBox treatConsecutiveAsOneCheckBox;
	private JRadioButton spaceRadioButton = new JRadioButton("Space");
	private JRadioButton tabRadioButton = new JRadioButton("Tabstop");
	private JRadioButton allBlanksRadioButton = new JRadioButton("Any Blank Space");
	private JRadioButton commaRadioButton = new JRadioButton("Comma");
	private JRadioButton semiColonRadioButton = new JRadioButton("Semi-colon");
	private JRadioButton otherRadioButton = new JRadioButton("Other: ");
	private JRadioButton useHomeRadioButton = new JRadioButton("Use home directory");
	private JRadioButton useLastRadioButton = new JRadioButton("Use last opened directory");
	private JRadioButton useThisRadioButton = new JRadioButton("Use this directory:  ");
	private JRadioButton usLocaleRadioButton = new JRadioButton("US Number Formats (1,234.56)");
	private JRadioButton germanLocaleRadioButton = new JRadioButton("German Number Formats (1.234,56)");

	public FileImportSettingsDialog(Main mainWindow) throws HeadlessException {
		super(mainWindow, "File Import Settings", true);
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
		useThisBrowseButton.addActionListener(cmd::onBrowse);
		cancelButton.addActionListener(cmd::onCancel);
		okButton.addActionListener(cmd::onOk);

		// set location and make visible
		this.pack();
		int left = (int) (0.5 * this.mainWindow.getSize().width) - (int) (this.getSize().width * 0.5) + this.mainWindow.getLocation().x;
		int top = (int) (0.5 * this.mainWindow.getSize().height) - (int) (this.getSize().height * 0.5) + this.mainWindow.getLocation().y;
		this.setLocation(left, top);
		this.setVisible(true);

	}

	public ButtonGroup getDelimiterButtonGroup() {
		return delimiterButtonGroup;
	}

	public ButtonGroup getFileBrowsingButtonGroup() {
		return fileBrowsingButtonGroup;
	}

	public ButtonGroup getNumberFormatLocaleButtonGroup() {
		return numberFormatLocaleButtonGroup;
	}

	public JTextField getOtherTextField() {
		return otherTextField;
	}

	public JTextField getUseThisTextField() {
		return useThisTextField;
	}

	public JCheckBox getTreatConsecutiveAsOneCheckBox() {
		return treatConsecutiveAsOneCheckBox;
	}

	public JRadioButton getAllBlanksRadioButton() {
		return allBlanksRadioButton;
	}

	public JRadioButton getOtherRadioButton() {
		return otherRadioButton;
	}

	public JRadioButton getSemiColonRadioButton() {
		return semiColonRadioButton;
	}

	public JRadioButton getSpaceRadioButton() {
		return spaceRadioButton;
	}

	public JRadioButton getTabRadioButton() {
		return tabRadioButton;
	}

	public JRadioButton getUseHomeRadioButton() {
		return useHomeRadioButton;
	}

	public JRadioButton getUseLastRadioButton() {
		return useLastRadioButton;
	}

	public JRadioButton getUseThisRadioButton() {
		return useThisRadioButton;
	}

	public JRadioButton getCommaRadioButton() {
		return commaRadioButton;
	}

	public JRadioButton getGermanLocaleRadioButton() {
		return germanLocaleRadioButton;
	}

}
