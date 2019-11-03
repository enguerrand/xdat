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

package org.xdat.gui.tables;

import org.xdat.data.DataSheet;
import org.xdat.data.Design;

import javax.swing.table.AbstractTableModel;

public class DataSheetTableModel extends AbstractTableModel {
    private final DataSheet dataSheet;

    public DataSheetTableModel(DataSheet dataSheet) {
        this.dataSheet = dataSheet;
    }

    @Override
    public Class<?> getColumnClass(int arg0) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return dataSheet.getParameterCount();
    }

    @Override
    public int getRowCount() {
        return dataSheet.getDesignCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return dataSheet.getParameter(columnIndex).getName();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Design design = this.dataSheet.getDesign(rowIndex);
        if (columnIndex == 0) {
            return design.getId();
        } else {
            return design.getStringValue(dataSheet.getParameter(columnIndex - 1));
        }
    }

    @Override
    public void setValueAt(Object arg0, int rowIndex, int columnIndex) {
        dataSheet.setValueAt(arg0, rowIndex, columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        } else {
            return true;
        }
    }
}
