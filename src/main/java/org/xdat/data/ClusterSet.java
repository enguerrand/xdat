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

import org.xdat.gui.tables.ClusterTableModel;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ClusterSet implements Serializable {

	static final long serialVersionUID = 1L;
	private DataSheet dataSheet;
	private List<Cluster> clusters = new LinkedList<>();

    public ClusterSet() {
    }

    public ClusterSet(DataSheet dataSheet) {
        this();
		this.dataSheet = dataSheet;
	}

	public void newCluster(ClusterFactory factory){
        this.clusters.add(factory.newCluster(this.clusters));
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

    public ClusterTableModel createTableModel(ClusterFactory factory) {
        return new ClusterTableModel(clusters, factory);
    }

    public void removeCluster(String clusterName) {
        for (int i = 0; i < this.clusters.size(); i++) {
            if (this.clusters.get(i).getName().equals(clusterName)) {
                this.clusters.remove(i);
                return;
            }
        }
        throw new IllegalArgumentException("Could not find cluster " + clusterName);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }
}
