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

package org.xdat.gui.buttons;

import org.xdat.Main;

import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

public class CustomButton extends JButton {

	private Image imgDefault;
	private Image imgPressed;

	public CustomButton(String toolTip, String pathToDefaultImage, String pathToPressedImage, String actionCommand) {
		super();
		this.setToolTipText(toolTip);
		URL urlDefault = Main.class.getResource(pathToDefaultImage);
		URL urlPressed = Main.class.getResource(pathToPressedImage);

		this.imgDefault = null;
		this.imgPressed = null;
		Toolkit tk = Toolkit.getDefaultToolkit();
		try {
			MediaTracker m = new MediaTracker(this);
			this.imgDefault = tk.getImage(urlDefault);
			m.addImage(this.imgDefault, 0);
			this.imgPressed = tk.getImage(urlPressed);
			m.addImage(this.imgPressed, 0);
			m.waitForAll();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// }
		this.setActionCommand(actionCommand);
		this.setLayout(new GridLayout(1, 1));
		this.setPreferredSize(new Dimension(imgDefault.getWidth(this), imgDefault.getHeight(this)));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.getModel().isPressed()) {
			g.drawImage(this.imgPressed, 0, 0, this);
		} else {
			g.drawImage(this.imgDefault, 0, 0, this);
		}

	}
}
