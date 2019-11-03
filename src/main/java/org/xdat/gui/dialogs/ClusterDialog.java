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

package org.xdat.gui.dialogs;

import org.xdat.Main;
import org.xdat.actionListeners.clusterDialog.ClusterDialogActionListener;
import org.xdat.data.DataSheet;
import org.xdat.gui.tables.ClusterTableModel;
import org.xdat.gui.tables.ColorEditor;
import org.xdat.gui.tables.ColorRenderer;
import org.xdat.gui.tables.GenericTableColumnModel;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * A dialog to edit {@link org.xdat.data.Cluster}s.
 */
public class ClusterDialog extends JDialog {

	static final long serialVersionUID = 1L;
	private DataSheet dataSheet;
	private JTable clusterTable;

	public ClusterDialog(JFrame parent, Main mainWindow, DataSheet dataSheet) {
		super(parent, "Data Clustering");
		this.setModal(true);
		this.dataSheet = dataSheet;
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// create components
		this.setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel controlsPanel = new JPanel(new BorderLayout());
		JPanel controlButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		controlButtonsPanel.setPreferredSize(new Dimension(300, 50));
		JPanel tablePanel = new JPanel(new BorderLayout());
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));

		JButton addButton = new JButton("Add");
		JButton removeButton = new JButton("Remove");
		JButton cancelButton = new JButton("Cancel");
		JButton okButton = new JButton("Ok");

		GenericTableColumnModel cm = new GenericTableColumnModel();
		TableColumn nameCol = new TableColumn(0, 100);
		nameCol.setHeaderValue("Cluster");
		nameCol.setResizable(true);
		cm.addColumn(nameCol);

		ColorRenderer colorRenderer = new ColorRenderer(false);
		TableColumn colorCol = new TableColumn(1, 50, colorRenderer, new ColorEditor(this));
		colorCol.setHeaderValue("Color");
		colorCol.setResizable(true);
		cm.addColumn(colorCol);

		TableColumn lineThicknessCol = new TableColumn(2, 30);
		lineThicknessCol.setHeaderValue("Line Thickness");
		lineThicknessCol.setResizable(true);
		cm.addColumn(lineThicknessCol);

		TableColumn activeCol = new TableColumn(3, 30);
		activeCol.setHeaderValue("Active");
		activeCol.setResizable(true);
		cm.addColumn(activeCol);

		ClusterTableModel tableModel = this.dataSheet.getClusterSet().createTableModel(mainWindow.getClusterFactory());
		this.clusterTable = new JTable(tableModel, cm);
		JScrollPane scrollPane = new JScrollPane(this.clusterTable);

		// add action listener
		ClusterDialogActionListener cmd = new ClusterDialogActionListener(mainWindow, this, tableModel);
		addButton.addActionListener(cmd);
		removeButton.addActionListener(cmd);
		cancelButton.addActionListener(cmd);
		okButton.addActionListener(cmd);

		// add components
		this.add(mainPanel, BorderLayout.CENTER);
		mainPanel.add(controlsPanel, BorderLayout.NORTH);
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

		controlsPanel.add(controlButtonsPanel, BorderLayout.WEST);
		controlButtonsPanel.add(addButton);
		controlButtonsPanel.add(removeButton);
		tablePanel.add(scrollPane, BorderLayout.CENTER);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);

		Dimension parentSize = parent.getSize();
		setSize(new Dimension(250, 400));
		int xPos = Math.max(parent.getX()+(int) (0.5 * (parentSize.width - 250)), 0);
		int yPos = Math.max(parent.getY()+((int) (0.5 * (parentSize.height - 400))), 0);
		setLocation(xPos, yPos);
		this.setVisible(true);
	}

	public JTable getClusterTable() {
		return clusterTable;
	}

	public DataSheet getDataSheet() {
		return dataSheet;
	}

}
