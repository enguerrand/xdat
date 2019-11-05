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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;
import org.xdat.gui.frames.ChartFrame;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChartFrameOptionsMenuActionListener implements ActionListener {

	private Main mainWindow;
	private ParallelCoordinatesChart chart;
	private ChartFrame chartFrame;
	public ChartFrameOptionsMenuActionListener(Main mainWindow, ParallelCoordinatesChart chart, ChartFrame chartFrame) {
		this.mainWindow = mainWindow;
		this.chart = chart;
		this.chartFrame = chartFrame;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Display Settings")) {
			new ParallelCoordinatesDisplaySettingsDialog(this.mainWindow, this.chart, this.chartFrame);
		} else if (e.getActionCommand().equals("Reset to Default")) {
			this.chart.resetDisplaySettingsToDefault(this.mainWindow.getDataSheet());
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
		} else {
			System.out.println(e.getActionCommand());
		}
	}
}
