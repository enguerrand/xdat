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

package org.xdat.gui.tables;

/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/* 
 * ColorEditor.java (compiles with releases 1.3 and 1.4) is used by 
 * TableDialogEditDemo.java.
 */

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ColorEditor extends AbstractCellEditor

implements TableCellEditor, ActionListener {

	private JDialog parent;
	private Color currentColor;
	private JButton button;
	private static final String EDIT = "edit";

	public ColorEditor(JDialog parent) {
		button = new JButton();
		button.setActionCommand(EDIT);
		button.addActionListener(this);
		button.setBorderPainted(false);
		this.parent = parent;

	}

	public void actionPerformed(ActionEvent e) {
		if (EDIT.equals(e.getActionCommand())) {
			Color newColor = JColorChooser.showDialog(parent, "Pick a Color", currentColor);
			if (newColor != null)
				this.currentColor = newColor;
			button.setBackground(currentColor);
			fireEditingStopped();
		}
	}

	public Object getCellEditorValue() {
		return currentColor;
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		currentColor = (Color) value;
		return button;
	}
}
