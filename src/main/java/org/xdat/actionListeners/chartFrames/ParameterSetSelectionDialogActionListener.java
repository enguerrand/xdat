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

package org.xdat.actionListeners.chartFrames;

import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParameterSetSelectionDialog;
import org.xdat.gui.frames.ChartFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ParameterSetSelectionDialogActionListener implements ActionListener {

	private ChartFrame chartFrame;
	private ParameterSetSelectionDialog dialog;

	public ParameterSetSelectionDialogActionListener(ChartFrame chartFrame, ParameterSetSelectionDialog dialog) {
		this.chartFrame = chartFrame;
		this.dialog = dialog;

	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Select All")) {
			for (int i = 0; i < dialog.getCheckBoxCount(); i++) {
				dialog.getCheckBox(i).setSelected(true);
			}
		} else if (actionCommand.equals("Unselect All")) {
			for (int i = 0; i < dialog.getCheckBoxCount(); i++) {
				dialog.getCheckBox(i).setSelected(false);
			}
		} else if (actionCommand.equals("Invert Selection")) {
			for (int i = 0; i < dialog.getCheckBoxCount(); i++) {
				dialog.getCheckBox(i).setSelected(!dialog.getCheckBox(i).isSelected());
			}
		} else if (actionCommand.equals("Cancel")) {
			dialog.dispose();
		} else if (actionCommand.equals("Ok")) {

			ParallelCoordinatesChart c = (ParallelCoordinatesChart) chartFrame.getChart();
			for (int i = 0; i < dialog.getCheckBoxCount(); i++) {
				c.getAxis(i).setActive(dialog.getCheckBox(i).isSelected());
			}
			dialog.dispose();
		}
		this.chartFrame.getChartPanel().setSize(this.chartFrame.getChartPanel().getPreferredSize());
		this.chartFrame.validate();
		this.chartFrame.repaint();
	}
}
