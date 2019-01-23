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

package org.xdat.actionListeners.parallelCoordinatesDisplaySettings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.panels.AxisDisplaySettingsPanel;

/**
 * ActionListener for the combobox that allows to switch the Axis that is
 * currently being edited in an Axis Display Settings Dialog.
 * 
 * @see org.xdat.gui.panels.AxisDisplaySettingsPanel
 * @see org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog
 */
public class AxisDisplaySettingsAxisChoiceComboActionListener implements ActionListener {

	/** The chart for which the settings are edited. */
	private ParallelCoordinatesChart parallelCoordinatesChart;

	/** The panel on which the combobox is located. */
	private AxisDisplaySettingsPanel panel;

	/** The combobox that allows the user to choose from the available Axes. */
	private JComboBox combo;

	/**
	 * Instantiates a new axis display settings axis choice combo action
	 * listener.
	 * 
	 * @param chart
	 *            the chart to which the Dialog belongs.
	 * @param panel
	 *            the panel on which the combo is located.
	 * @param combo
	 *            the combobox
	 */
	public AxisDisplaySettingsAxisChoiceComboActionListener(ParallelCoordinatesChart chart, AxisDisplaySettingsPanel panel, JComboBox combo) {
		this.parallelCoordinatesChart = chart;
		this.panel = panel;
		this.combo = combo;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("comboBoxChanged")) {
			this.panel.setStates(this.parallelCoordinatesChart.getAxis(combo.getSelectedItem().toString()));
		} else {
			System.out.println("AxisDisplaySettingsAxisChoiceComboActionListener: " + e.getActionCommand());
		}

	}

}
