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

package org.xdat.data;

import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClusterSet implements Serializable, TableModel {

	static final long serialVersionUID = 1L;
	private DataSheet dataSheet;
	private List<Cluster> clusters = new LinkedList<Cluster>();

	private List<Cluster> clustersBuffer = new LinkedList<>();

	private transient List<TableModelListener> listeners = new ArrayList<>();

	private int uniqueIdentificationNumberCounter = 0;

	public ClusterSet(DataSheet dataSheet) {
		this.dataSheet = dataSheet;
	}

	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
			case (0):
				return String.class;
			case (1):
				return Color.class;
			case (2):
				return Integer.class;
			case (3):
				return Boolean.class;
		}
		return null;
	}

	public int getColumnCount() {
		return 4;
	}

	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case (0):
				return "Cluster";
			case (1):
				return "Color";
			case (2):
				return "Active";
		}
		return null;
	}

	public int getRowCount() {
		return this.clustersBuffer.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case (0):
				return this.clustersBuffer.get(rowIndex).getName();
			case (1):
				return this.clustersBuffer.get(rowIndex).getActiveDesignColor(true);
			case (2):
				return this.clustersBuffer.get(rowIndex).getLineThickness();
			case (3):
				return this.clustersBuffer.get(rowIndex).isActive();
		}
		return null;
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	public void setValueAt(Object arg0, int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case (0): {
				if (this.isNameUnique(arg0.toString(), rowIndex)) {
					this.clustersBuffer.get(rowIndex).setName(arg0.toString());
				} else
					JOptionPane.showMessageDialog(null, "This name is not unique. Please choose a different name.", "Rename Cluster", JOptionPane.INFORMATION_MESSAGE);
				break;
			}
			case (1): {
				this.clustersBuffer.get(rowIndex).setActiveDesignColor((Color) arg0);
				break;
			}
			case (2): {
				try {
					int thickness = Integer.parseInt(arg0.toString());
					if (thickness < 0 || thickness > 10) {
						throw new NumberFormatException();
					}
					this.clustersBuffer.get(rowIndex).setLineThickness(thickness);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Invalid input. Values must be integers between 0 and 10.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
				}
				break;
			}
			case (3): {
				this.clustersBuffer.get(rowIndex).setActive(Boolean.parseBoolean(arg0.toString()));
				break;
			}
		}
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		if (this.listeners == null) // rebuild after deserialization
			this.listeners = new ArrayList<>();
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		if(listeners==null)
			return;
		listeners.remove(l);
	}

	public void fireTableChanged() {
		if(listeners == null)
			return;
		TableModelEvent e = new TableModelEvent(this);
		for (int i = 0, n = listeners.size(); i < n; i++) {
			listeners.get(i).tableChanged(e);
		}
	}

	public void addClusterToBuffer() {
		String newClusterName = this.getUniqueClusterName();
		Cluster newCluster = new Cluster(newClusterName, uniqueIdentificationNumberCounter++);
		this.clustersBuffer.add(newCluster);
		this.fireTableChanged();
	}

	public void removeClusterFromBuffer(int i) {
		this.clustersBuffer.remove(i);
		this.fireTableChanged();
	}

	public Cluster getCluster(int i) {
		return this.clusters.get(i);
	}

	public Cluster getCluster(String clusterName) {
		for (int i = 0; i < this.clusters.size(); i++) {
			if (this.clusters.get(i).getName().equals(clusterName))
				return this.clusters.get(i);
		}
		throw new IllegalArgumentException("Could not find cluster " + clusterName);
	}

	public int getClusterCount() {
		return this.clusters.size();
	}

	public void removeClusterFromBuffer(String clusterName) {
		for (int i = 0; i < this.clustersBuffer.size(); i++) {
			if (this.clustersBuffer.get(i).getName().equals(clusterName)) {
				this.clustersBuffer.remove(i);
				this.fireTableChanged();
				return;
			}
		}
		throw new IllegalArgumentException("Could not find cluster " + clusterName);
	}

	private String getUniqueClusterName() {
		String name = "Cluster 1";
		int id = 1;
		while (!isNameUnique(name))
			name = "Cluster " + (id++);
		return name;
	}

	private boolean isNameUnique(String name) {
		boolean unique = true;
		for (int i = 0; i < this.clustersBuffer.size(); i++) {
			if (name.equals(this.clustersBuffer.get(i).getName())) {
				unique = false;
				break;
			}
		}
		return unique;
	}

	/**
	 * Checks if a given name is a unique Cluster name but does not check
	 * against the name of Cluster with index exception.
	 * <p>
	 * This is needed when the user edits the Cluster name. In this case, the
	 * entered name must be checked against the names of all other Clusters, but
	 * not against the current name of this Cluster. Otherwise reentering the
	 * same name as the Cluster had before would produce an error message.
	 * 
	 * @param name
	 *            the name
	 * @param exception
	 *            the exception
	 * @return true, if is name unique
	 */
	private boolean isNameUnique(String name, int exception) // clustersBuffer.get(exception)
																// won't be
																// evaluated
	{
		boolean unique = true;
		for (int i = 0; i < this.clustersBuffer.size(); i++) {
			if (name.equals(this.clustersBuffer.get(i).getName()) && i != exception) {
				unique = false;
				break;
			}
		}
		return unique;
	}

	public void applyChanges() {

		for (int i = this.clusters.size() - 1; i >= 0; i--) {
			boolean clusterRemoved = true;

			for (int j = this.clustersBuffer.size() - 1; j >= 0; j--) {
				if (this.clusters.get(i).getUniqueIdentificationNumber() == this.clustersBuffer.get(j).getUniqueIdentificationNumber()) {
					clusterRemoved = false;
					this.clustersBuffer.remove(j).copySettingsTo(this.clusters.get(i));
					break;
				}
			}
			if (clusterRemoved) {
				Cluster c = this.clusters.remove(i);
				for (int j = 0; j < this.dataSheet.getDesignCount(); j++) {
					if (c.equals(this.dataSheet.getDesign(j).getCluster())) {
						this.dataSheet.getDesign(j).setCluster(null);
					}
				}
			}
		}
		for (int i = 0; i < this.clustersBuffer.size(); i++) {
			this.clusters.add(this.clustersBuffer.get(i).duplicate());
		}
	}

	public void createBuffer() {
		clustersBuffer.clear();
		for (int i = 0; i < this.clusters.size(); i++) {
			this.clustersBuffer.add(this.clusters.get(i).duplicate());
		}
	}

}
