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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

/**
 * Button to display the current color of a {@link org.xdat.data.Cluster} and
 * bring up a dialog to edit this color.
 */
public class ColorChoiceButton extends JButton {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/** The current color. */
	private Color currentColor;

	/** The edge width. */
	private int width = 16;
	
	/** The edge height. */
	private int height = 16;
	
	/**
	 * Instantiates a new color choice button with an edge length of 16 pixels
	 * 
	 * @param currentColor
	 *            the current color
	 * @param actionCommand
	 *            the action command
	 */
	public ColorChoiceButton(Color currentColor, String actionCommand) {
		this(currentColor, actionCommand, 16, 16);
	}

	/**
	 * Instantiates a new color choice button.
	 * 
	 * @param currentColor
	 *            the current color
	 * @param actionCommand
	 *            the action command
	 * @param width
	 *            the width
	 * @param height
	 *            the height
	 */
	public ColorChoiceButton(Color currentColor, String actionCommand, int width, int height) {
		super();
		this.currentColor = currentColor;
		this.width = width;
		this.height = height;
		this.setActionCommand(actionCommand);
		this.setLayout(new GridLayout(1, 1));
		this.setPreferredSize(new Dimension(this.width, this.height));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.currentColor);
		g.fillRect(0, 0, this.width, this.height);

	}

	/**
	 * Action performed.
	 * 
	 * @param e
	 *            the e
	 */
	public void actionPerformed(ActionEvent e) {

	}

	/**
	 * Sets the current color.
	 * 
	 * @param currentColor
	 *            the new current color
	 */
	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}
	
	/**
	 * Sets the alpha of the current color
	 * @param alpha
	 * 		the alpha value
	 */
	public void setAlpha(int alpha){
		this.currentColor = new Color(this.currentColor.getRed(), this.currentColor.getGreen(), this.currentColor.getBlue(), alpha);
		this.repaint();
	}
}
