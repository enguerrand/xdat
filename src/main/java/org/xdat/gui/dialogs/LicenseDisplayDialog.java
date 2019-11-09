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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class LicenseDisplayDialog extends JDialog {
	static final long serialVersionUID = 1L;

	public LicenseDisplayDialog(Window parent) {
		super(parent);
		this.setTitle("GNU GENERAL PUBLIC LICENSE");
		JTextArea licenseTextArea = new JTextArea(this.getLicenseText());
		licenseTextArea.setLineWrap(true);
		licenseTextArea.setWrapStyleWord(true);
		licenseTextArea.setMargin(new Insets(5, 5, 5, 5));

		this.setModal(true);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel licenseDisplayPanel = new JPanel();
		licenseDisplayPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(licenseTextArea);
		licenseDisplayPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton okButton = new JButton("Ok");
		okButton.addActionListener(actionEvent -> {
			setVisible(false);
			dispose();
		});
		buttonsPanel.add(okButton);

		licenseDisplayPanel.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(licenseDisplayPanel);
		this.setSize(500, (int) (getToolkit().getScreenSize().height * 0.6));
		int left = (int) (getToolkit().getScreenSize().width * 0.5 - this.getWidth() * 0.5);
		int top = (int) (getToolkit().getScreenSize().height * 0.5 - this.getHeight() * 0.5);
		setLocation(left, top);
		this.validate();
	}

	private String getLicenseText() {
        InputStream inputStream = this.getClass().getResourceAsStream("LICENSE.txt");
		BufferedReader in = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        return in.lines().collect(Collectors.joining("\n"));
	}
}
