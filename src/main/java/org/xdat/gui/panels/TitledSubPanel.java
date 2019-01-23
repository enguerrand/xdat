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

package org.xdat.gui.panels;

import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * A generic class with some display settings. Used in other classes rather than
 * JPanel in order to be able to change their looks all at once.
 */
public class TitledSubPanel extends JPanel {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0001;

	/**
	 * Instantiates a new titled sub panel.
	 * 
	 * @param title
	 *            the title
	 */
	public TitledSubPanel(String title) {
		super();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
		titledBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 16));
		EmptyBorder emptyBorder = (new EmptyBorder(5, 5, 5, 5));

		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(emptyBorder, titledBorder), emptyBorder));

	}
}