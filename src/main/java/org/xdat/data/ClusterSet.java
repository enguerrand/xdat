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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
        Cluster newCluster = factory.newCluster(this.clusters);
        this.clusters.add(newCluster);
        this.dataSheet.onClustersUpdated(Collections.emptyList(), Collections.singletonList(newCluster), Collections.emptyList());
    }

	public Cluster getCluster(int i) {
		return this.clusters.get(i);
	}

	public Cluster getCluster(String clusterName) {
        for (Cluster cluster : this.clusters) {
            if (cluster.getName().equals(clusterName)) {
                return cluster;
            }
        }
		throw new IllegalArgumentException("Could not find cluster " + clusterName);
	}

	public Optional<Cluster> findClusterById(int id) {
        return this.clusters.stream()
                .filter(c -> c.getUniqueId() == id)
                .findAny();
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
                Cluster removed = this.clusters.remove(i);
                this.dataSheet.onClustersUpdated(Collections.emptyList(), Collections.emptyList(), Collections.singletonList(removed));
                return;
            }
        }
        throw new IllegalArgumentException("Could not find cluster " + clusterName);
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public void applyClustersBuffer(List<Cluster> clustersBuffer) {
        List<Cluster> removedClusters = new ArrayList<>();
        List<Cluster> changedClusters = new ArrayList<>();
        List<Cluster> addedClusters = new ArrayList<>();
        Iterator<Cluster> iterator = this.clusters.iterator();
        while(iterator.hasNext()){
            Cluster next = iterator.next();
            if(!clustersBuffer.contains(next)) {
                removedClusters.add(next);
                iterator.remove();
            }
        }

        for (Cluster bufferedCluster : clustersBuffer) {
            Cluster existing = this.clusters.stream()
                    .filter(b -> b.equals(bufferedCluster))
                    .findAny()
                    .orElse(null);
            if (existing == null) {
                Cluster added = bufferedCluster.duplicate();
                this.clusters.add(added);
                addedClusters.add(added);
            } else {
                boolean changed = bufferedCluster.copySettingsTo(existing);
                if (changed) {
                    changedClusters.add(existing);
                }
            }
        }
        this.dataSheet.onClustersUpdated(changedClusters, addedClusters, removedClusters);
    }

    public void initTransientData() {
        for (Cluster cluster : this.clusters) {
            cluster.initTransientData();
        }
    }
}
