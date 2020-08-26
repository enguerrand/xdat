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
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.UiDefines;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.panels.AxisDisplaySettingsPanel;
import org.xdat.gui.panels.SettingsGroupPanel;
import org.xdat.gui.panels.PaddedPanel;
import org.xdat.settings.SettingsGroup;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;

public class ParallelCoordinatesDisplaySettingsDialog extends JDialog {

	static final long serialVersionUID = 2L;
	private Main mainWindow;
	private AxisDisplaySettingsPanel axisDisplaySettingsPanel;
	public ParallelCoordinatesDisplaySettingsDialog(Main mainWindow) {
		super(mainWindow, "Parallel Coord Settings", true);
		this.mainWindow = mainWindow;

		SettingsGroup generalSettingsGroup = this.mainWindow.getGeneralSettingsGroup();
		SettingsGroupPanel chartDisplaySettingsPanel = new SettingsGroupPanel(generalSettingsGroup);
		axisDisplaySettingsPanel = new AxisDisplaySettingsPanel(mainWindow.getParallelCoordinatesAxisSettingsGroup());
		buildDialog(chartDisplaySettingsPanel);

		this.setVisible(true);
	}

	public ParallelCoordinatesDisplaySettingsDialog(Main mainWindow, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		super(chartFrame, "Display Settings");
		this.setModal(true);
		this.mainWindow = mainWindow;

		SettingsGroup settingsGroup = chart.getChartSettingsGroup();
		SettingsGroupPanel chartDisplaySettingsPanel = new SettingsGroupPanel(settingsGroup);
		axisDisplaySettingsPanel = new AxisDisplaySettingsPanel(chart.getAxes());

		buildDialog(chartDisplaySettingsPanel);

		this.setVisible(true);
	}

	private void buildDialog(SettingsGroupPanel generalSettingsGroupPanel) {
		this.addWindowListener(new WindowClosingAdapter(false));
		this.setResizable(false);

		JTabbedPane tabbedPane = new JTabbedPane();
		this.setLayout(new BorderLayout());
		this.add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.add("General", generalSettingsGroupPanel);
		tabbedPane.add("Axis-specific", axisDisplaySettingsPanel);

		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("Ok");
		JPanel mainButtonsPanel = new PaddedPanel(UiDefines.PADDING);
		mainButtonsPanel.setLayout(new GridLayout(1, 2));
		mainButtonsPanel.add(cancelButton);
		mainButtonsPanel.add(okButton);
		okButton.addActionListener(actionEvent -> {
			generalSettingsGroupPanel.applyAll();
			axisDisplaySettingsPanel.applyAll();
			this.dispose();
		});
		cancelButton.addActionListener(actionEvent ->
				this.dispose()
		);

		this.add(mainButtonsPanel, BorderLayout.SOUTH);
		this.pack();

		int left = (int) (0.5 * this.mainWindow.getSize().width) - (int) (this.getSize().width * 0.5) + this.mainWindow.getLocation().x;
		int top = (int) (0.5 * this.mainWindow.getSize().height) - (int) (this.getSize().height * 0.5) + this.mainWindow.getLocation().y;
		this.setLocation(left, top);
	}
}
