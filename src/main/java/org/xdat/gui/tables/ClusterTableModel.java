package org.xdat.gui.tables;

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

import org.xdat.data.Cluster;
import org.xdat.data.ClusterFactory;
import org.xdat.data.DataSheet;

import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ClusterTableModel extends AbstractTableModel {
    private final List<Cluster> clustersBuffer = new LinkedList<>();
    private final ClusterFactory factory;

    public ClusterTableModel(List<Cluster> clusters, ClusterFactory factory) {
        this.factory = factory;
        this.clustersBuffer.addAll(clusters.stream()
                .map(Cluster::duplicate)
                .collect(Collectors.toList()));
    }

    @Override
    public int getRowCount() {
        return clustersBuffer.size();
    }

    @Override
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

    @Override
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

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case (0):
                return clustersBuffer.get(rowIndex).getName();
            case (1):
                return clustersBuffer.get(rowIndex).getActiveDesignColor(true);
            case (2):
                return clustersBuffer.get(rowIndex).getLineThickness();
            case (3):
                return clustersBuffer.get(rowIndex).isActive();
        }
        return null;
    }

    @Override
    public void setValueAt(Object arg0, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case (0): {
                if (isNameUnique(arg0.toString(), rowIndex)) {
                    clustersBuffer.get(rowIndex).setName(arg0.toString());
                } else
                    JOptionPane.showMessageDialog(null, "This name is not unique. Please choose a different name.", "Rename Cluster", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            case (1): {
                clustersBuffer.get(rowIndex).setActiveDesignColor((Color) arg0);
                break;
            }
            case (2): {
                try {
                    int thickness = Integer.parseInt(arg0.toString());
                    if (thickness < 0 || thickness > 10) {
                        throw new NumberFormatException();
                    }
                    clustersBuffer.get(rowIndex).setLineThickness(thickness);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Values must be integers between 0 and 10.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
                break;
            }
            case (3): {
                clustersBuffer.get(rowIndex).setActive(Boolean.parseBoolean(arg0.toString()));
                break;
            }
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public void addCluster() {
        Cluster newCluster = factory.newCluster(this.clustersBuffer);
        this.clustersBuffer.add(newCluster);
        int index = this.clustersBuffer.size() - 1;
        fireTableRowsInserted(index, index);
    }

    public void removeCluster(int i) {
        this.clustersBuffer.remove(i);
        fireTableRowsDeleted(i, i);
    }

    private boolean isNameUnique(String name, int exception){
        boolean unique = true;
        for (int i = 0; i < this.clustersBuffer.size(); i++) {
            if (name.equals(this.clustersBuffer.get(i).getName()) && i != exception) {
                unique = false;
                break;
            }
        }
        return unique;
    }

    public void applyBuffer(List<Cluster> clusters, DataSheet dataSheet) {
        for (int i = clusters.size() - 1; i >= 0; i--) {
            boolean clusterRemoved = true;

            for (int j = this.clustersBuffer.size() - 1; j >= 0; j--) {
                if (clusters.get(i).getUniqueId() == this.clustersBuffer.get(j).getUniqueId()) {
                    clusterRemoved = false;
                    this.clustersBuffer.remove(j).copySettingsTo(clusters.get(i));
                    break;
                }
            }
            if (clusterRemoved) {
                Cluster c = clusters.remove(i);
                for (int j = 0; j < dataSheet.getDesignCount(); j++) {
                    if (c.equals(dataSheet.getDesign(j).getCluster())) {
                        dataSheet.getDesign(j).setCluster(null);
                    }
                }
            }
        }
        for (int i = 0; i < this.clustersBuffer.size(); i++) {
            clusters.add(this.clustersBuffer.get(i).duplicate());
        }
    }
}
