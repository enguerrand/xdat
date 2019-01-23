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

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.parallelCoordinatesChart.ParallelCoordinatesChartFrameOptionsMenu;

/**
 * ActionListener that is used for a
 * {@link ParallelCoordinatesChartFrameOptionsMenu}.
 */
public class ChartFrameOptionsMenuActionListener implements ActionListener {

	/** The main window. */
	private Main mainWindow;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The chart. */
	private ParallelCoordinatesChart chart;

	/** The chart frame. */
	private ChartFrame chartFrame;

	/**
	 * Instantiates a new chart frame options menu action listener.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param chart
	 *            the chart
	 * @param chartFrame
	 *            the chart frame
	 */
	public ChartFrameOptionsMenuActionListener(Main mainWindow, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.chart = chart;
		this.chartFrame = chartFrame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		log("constructor called");
		if (e.getActionCommand().equals("Display Settings")) {
			new ParallelCoordinatesDisplaySettingsDialog(this.mainWindow, this.chart, this.chartFrame);
		}

		else if (e.getActionCommand().equals("Reset to Default")) {
			this.chart.resetDisplaySettingsToDefault();
			this.chartFrame.repaint();
		} else if (e.getActionCommand().equals("Export to png")) {
			String filepath;
			JFileChooser chooser = new JFileChooser();
			if (UserPreferences.getInstance().getCurrentDir() != null)
				chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
			int returnVal = chooser.showSaveDialog(this.chartFrame);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filepath = chooser.getSelectedFile().getAbsolutePath();
				String filename = chooser.getSelectedFile().getName();
				if (!filename.endsWith(".png")) {
					filepath = filepath + ".png";
				}
				UserPreferences.getInstance().setLastFile(filepath);
				try {
					BufferedImage bi = new BufferedImage(chartFrame.getChartPanel().getWidth(), chartFrame.getChartPanel().getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics g = bi.createGraphics();
					chartFrame.getChartPanel().paint(g);
					g.dispose();
					ImageIO.write(bi, "png", new File(filepath));
				} catch (IOException exc) {
					JOptionPane.showMessageDialog(this.chartFrame, "IOException on saving image: " + exc.getMessage(), "Export to Image", JOptionPane.OK_OPTION);
				}
			}
		}

		else {
			System.out.println(e.getActionCommand());
		}
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (ChartFrameOptionsMenuActionListener.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}
}
