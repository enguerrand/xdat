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

import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

public class ColorChoiceButton extends JButton {

	private Color currentColor;
	private final int width;
	private final int height;
	
	public ColorChoiceButton(Color currentColor) {
		this(currentColor, null, 16, 16);
	}

	public ColorChoiceButton(Color currentColor, @Nullable String actionCommand) {
		this(currentColor, actionCommand, 16, 16);
	}

	public ColorChoiceButton(Color currentColor, @Nullable String actionCommand, int width, int height) {
		super();
		this.currentColor = currentColor;
		this.width = width;
		this.height = height;
		if (actionCommand != null) {
			this.setActionCommand(actionCommand);
		}
		this.setLayout(new GridLayout(1, 1));
		this.setPreferredSize(new Dimension(this.width, this.height));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(this.currentColor);
		g.fillRect(0, 0, this.width, this.height);

	}

	public void setCurrentColor(Color currentColor) {
		this.currentColor = currentColor;
	}

	public Color getCurrentColor() {
		return currentColor;
	}

	public void setAlpha(int alpha){
		this.currentColor = new Color(this.currentColor.getRed(), this.currentColor.getGreen(), this.currentColor.getBlue(), alpha);
		this.repaint();
	}
}
