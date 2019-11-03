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

import org.xdat.Main;
import org.xdat.data.DataSheet;
import org.xdat.gui.tables.DataSheetTableColumnModel;
import org.xdat.gui.tables.DataTable;
import org.xdat.gui.tables.DataTableCellEditor;
import org.xdat.gui.tables.DataTableSelectionModel;

import javax.swing.DefaultCellEditor;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.GridLayout;
import java.util.List;

public class DataSheetTablePanel extends JPanel {
	static final long serialVersionUID = 1L;

	private Main mainWindow;
	private DataTable dataTable;

	public DataSheetTablePanel(Main mainWindow) {
		super();
		this.mainWindow = mainWindow;
		this.setLayout(new GridLayout(1, 1));
		initialiseDataSheetTableModel();
	}

	public void initialiseDataSheetTableModel() {
		DataSheet dataSheet = this.mainWindow.getCurrentSession().getCurrentDataSheet();
		if (dataSheet != null) {
			this.removeAll();
			DataSheetTableColumnModel cm = new DataSheetTableColumnModel(this.mainWindow, dataSheet);
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			// DefaultCellEditor cellEditor = new DefaultCellEditor(new
			// JTextField());
			DataTableCellEditor cellEditor = new DataTableCellEditor();
			TableColumn idCol = new TableColumn(0, 30, cellRenderer, cellEditor);
			idCol.setHeaderValue("#");
			idCol.setResizable(true);
			cm.addColumn(idCol);

			TableColumn[] cols = new TableColumn[dataSheet.getParameterCount() + 1];
			for (int colIndex = 1; colIndex <= dataSheet.getParameterCount(); colIndex++) {
				cols[colIndex] = new TableColumn(colIndex, 100, cellRenderer, cellEditor);
				cols[colIndex].setHeaderValue(dataSheet.getParameterName(colIndex - 1));
				cols[colIndex].setResizable(true);
				cm.addColumn(cols[colIndex]);
			}

			this.dataTable = new DataTable(dataSheet, cm, this.mainWindow);
			this.dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			this.dataTable.setShowGrid(true);
			this.dataTable.setColumnSelectionAllowed(false);
			this.dataTable.setRowSelectionAllowed(true);
			this.dataTable.getSelectionModel().addListSelectionListener(new DataTableSelectionModel(mainWindow));

			// Embed Table in ScrollPane

			JScrollPane scrollPane = new JScrollPane(this.dataTable);

			this.setLayout(new GridLayout(1, 1));
			this.add(scrollPane);
		}

	}

	public void updateRunsTableModel() {
		DataSheet dataSheet = this.mainWindow.getCurrentSession().getCurrentDataSheet();
		if (dataSheet != null) {
			DataSheetTableColumnModel cm = new DataSheetTableColumnModel(this.mainWindow, dataSheet);
			DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
			cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			TableColumn idCol = new TableColumn(0, 30, cellRenderer, new DefaultCellEditor(new JTextField()));
			idCol.setHeaderValue("#");
			idCol.setResizable(true);
			cm.addColumn(idCol);

			TableColumn[] cols = new TableColumn[dataSheet.getParameterCount() + 1];
			for (int colIndex = 1; colIndex <= dataSheet.getParameterCount(); colIndex++) {
				cols[colIndex] = new TableColumn(colIndex, 100, cellRenderer, new DefaultCellEditor(new JTextField()));
				cols[colIndex].setHeaderValue(dataSheet.getParameterName(colIndex - 1));
				cols[colIndex].setResizable(true);
				cm.addColumn(cols[colIndex]);
			}

			this.dataTable.setColumnModel(cm);
		}
	}

	public JTable getDataTable() {
		return dataTable;
	}
	
	public void setSelectedRows(List<Integer> selection){
		dataTable.clearSelection();
		for(Integer s : selection){
			dataTable.getSelectionModel().addSelectionInterval(s, s);
		}
	}
}
