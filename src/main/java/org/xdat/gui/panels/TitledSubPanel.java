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

import org.xdat.gui.UiDefines;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Font;

public class TitledSubPanel extends JPanel {

	public TitledSubPanel(String title) {
		super();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(title);
		titledBorder.setTitleFont(new Font("SansSerif", Font.BOLD, 16));
		EmptyBorder emptyBorder = (new EmptyBorder(UiDefines.PADDING, UiDefines.PADDING, UiDefines.PADDING, UiDefines.PADDING));

		this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createCompoundBorder(emptyBorder, titledBorder), emptyBorder));

	}
}