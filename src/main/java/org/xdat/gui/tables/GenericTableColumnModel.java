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

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * A column model for the JTable that displays the
 * {@link org.xdat.data.DataSheet}.
 */
public class GenericTableColumnModel extends DefaultTableColumnModel {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0002;

	/**
	 * Instantiates a new generic table column model.
	 */
	public GenericTableColumnModel() {
		super();
		this.setColumnMargin(1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableColumnModel#getColumnSelectionAllowed()
	 */
	public boolean getColumnSelectionAllowed() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.DefaultTableColumnModel#moveColumn(int, int)
	 */
	public void moveColumn(int arg0, int arg1) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.DefaultTableColumnModel#removeColumn(javax.swing.table
	 * .TableColumn)
	 */
	public void removeColumn(TableColumn arg0) {
	}
}