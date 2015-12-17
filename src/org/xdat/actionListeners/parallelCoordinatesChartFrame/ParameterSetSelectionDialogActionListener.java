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

package org.xdat.actionListeners.parallelCoordinatesChartFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.xdat.Main;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParameterSetSelectionDialog;
import org.xdat.gui.frames.ChartFrame;

/**
 * ActionListener for a {@link ParameterSetSelectionDialog}.
 */
public class ParameterSetSelectionDialogActionListener implements ActionListener {

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/** The dialog. */
	private ParameterSetSelectionDialog dialog;

	/**
	 * Instantiates a new parameter set selection dialog action listener.
	 * 
	 * @param chartFrame
	 *            the chart frame
	 * @param dialog
	 *            the dialog
	 */
	public ParameterSetSelectionDialogActionListener(ChartFrame chartFrame, ParameterSetSelectionDialog dialog) {
		this.chartFrame = chartFrame;
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
		log("Action Command = " + actionCommand);
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

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ParameterSetSelectionDialogActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
