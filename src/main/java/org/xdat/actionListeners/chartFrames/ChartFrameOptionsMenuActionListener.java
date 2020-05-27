/*
 *  Copyright 2019, Enguerrand de Rochefort
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

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.gui.frames.ChartFrame;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChartFrameOptionsMenuActionListener {
    private Main mainWindow;
    private ChartFrame chartFrame;

    protected ChartFrameOptionsMenuActionListener(Main mainWindow, ChartFrame chartFrame) {
        this.mainWindow = mainWindow;
        this.chartFrame = chartFrame;
    }

    protected Main getMainWindow() {
        return mainWindow;
    }

    protected ChartFrame getChartFrame() {
        return chartFrame;
    }

    public void exportToPng(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        if (UserPreferences.getInstance().getCurrentDir() != null)
            chooser.setCurrentDirectory(new File(UserPreferences.getInstance().getCurrentDir()));
        int returnVal = chooser.showSaveDialog(this.chartFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = chooser.getSelectedFile().getAbsolutePath();
            if (!filepath.endsWith(".png")) {
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
                JOptionPane.showMessageDialog(this.chartFrame, "IOException on saving image: " + exc.getMessage(), "Export to Image", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
