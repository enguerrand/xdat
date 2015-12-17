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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.JButton;

import org.xdat.Main;

/**
 * Custom Button that can be represented by to images. One for the default state
 * and one for the pressed state.
 */
public class CustomButton extends JButton {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0003;

	/** Image that represents the button. */
	private Image imgDefault;

	/** Image that represents the button when pressed. */
	private Image imgPressed;

	/**
	 * Instantiates a new custom button.
	 * 
	 * @param toolTip
	 *            the message that is displayed when hovering over the button
	 *            with the mouse.
	 * @param pathToDefaultImage
	 *            the path to image representing the unpressed state of the
	 *            button.
	 * @param pathToPressedImage
	 *            the path to image representing the pressed state of the
	 *            button.
	 * @param actionCommand
	 *            the action command
	 */
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.getModel().isPressed()) {
			g.drawImage(this.imgPressed, 0, 0, this);
		} else {
			g.drawImage(this.imgDefault, 0, 0, this);
		}

	}

	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the action event
	 */
	public void actionPerformed(ActionEvent e) {

	}
}
