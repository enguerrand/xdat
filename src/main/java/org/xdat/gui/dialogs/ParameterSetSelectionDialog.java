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
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ParameterSetSelectionDialogActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.buttons.CustomButton;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

public class ParameterSetSelectionDialog extends JDialog {

	static final long serialVersionUID = 2L;
	private List<JCheckBox> checkBoxes = new LinkedList<>();

	public ParameterSetSelectionDialog(Main mainWindow, ChartFrame chartFrame) {
		super(chartFrame, "Select active parameters");
		this.setModal(true);

		// create components
		this.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel controlsPanel = new JPanel(new BorderLayout());
		JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlButtonsPanel.setPreferredSize(new Dimension(400, 50));
		JPanel checkBoxOuterPanel = new JPanel(new BorderLayout());
		JPanel checkBoxInnerPanel = new JPanel(new GridLayout(0, 1));
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

		CustomButton selectAllButton = new CustomButton("Select All", "images" + "/selectAllButtonDefault.png", "images" + "/selectAllButtonPressed.png", "Select All");
		CustomButton unselectAllButton = new CustomButton("Unselect All", "images" + "/UnSelectAllButtonDefault.png", "images" + "/UnSelectAllButtonPressed.png", "Unselect All");
		CustomButton invertSelectionButton = new CustomButton("Invert Selection", "images" + "/reverseSelectionButtonDefault.png", "images" + "/reverseSelectionButtonPressed.png", "Invert Selection");

		// JButton selectAllButton = new JButton("Select All");
		// JButton unselectAllButton = new JButton("Unselect All");
		// JButton invertSelectionButton = new JButton("Invert Selection");

		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("Ok");

		// add action listener
		ParameterSetSelectionDialogActionListener cmd = new ParameterSetSelectionDialogActionListener(chartFrame, this);
		selectAllButton.addActionListener(cmd);
		unselectAllButton.addActionListener(cmd);
		invertSelectionButton.addActionListener(cmd);
		cancelButton.addActionListener(cmd);
		okButton.addActionListener(cmd);

		// add components
		this.add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(controlsPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(checkBoxOuterPanel), BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		checkBoxOuterPanel.add(checkBoxInnerPanel, BorderLayout.NORTH);

		controlsPanel.add(controlButtonsPanel, BorderLayout.WEST);
		controlButtonsPanel.add(selectAllButton);
		controlButtonsPanel.add(unselectAllButton);
		controlButtonsPanel.add(invertSelectionButton);

		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);

		ParallelCoordinatesChart c = (ParallelCoordinatesChart) chartFrame.getChart();
		for (int i = 0; i < mainWindow.getDataSheet().getParameterCount(); i++) {
			JCheckBox cb = new JCheckBox(mainWindow.getDataSheet().getParameter(i).getName(), c.getAxis(i).isActive());
			checkBoxes.add(cb);
			checkBoxInnerPanel.add(cb);
		}

		Dimension screenSize = getToolkit().getScreenSize();
		setSize(new Dimension(350, 400));
		int yPos = (int) (0.5 * (screenSize.height - this.getSize().getHeight()));
		int xPos = (int) (0.5 * (screenSize.width - this.getSize().getWidth()));
		setLocation(xPos, yPos);

		this.setVisible(true);
	}

	public JCheckBox getCheckBox(int index) {
		return checkBoxes.get(index);
	}

	public int getCheckBoxCount() {
		return checkBoxes.size();
	}

}
