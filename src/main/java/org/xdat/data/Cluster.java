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

import org.xdat.UserPreferences;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A group of {@link org.xdat.data.Design}s that can be displayed in a
 * userspecified different color or removed from the display altogether,
 * irrespective of the {@link org.xdat.chart.Filter} settings.
 * <p>
 * Clusters enable the user to regroup the designs in logical subsets. This
 * achieved by storing a reference to a Cluster in the Design instance. Whenever
 * the Design is asked to which Cluster it belongs, it returns this reference.
 * The Cluster then provides the information whether it is active (which
 * determines whether the design should be displayed) and, if so, in which color
 * the Design is displayed.
 */
public class Cluster implements Serializable {

	static final long serialVersionUID = 1L;
	private String name;
	private Color activeDesignColor;
	private Color activeDesignColorNoAlpha;
	private boolean active = true;
	private int lineThickness = 1;
	private int uniqueId;
	private transient List<ClusterListener> clusterListeners;
	public Cluster(String name, int uniqueId) {
		this.name = name;
		this.uniqueId = uniqueId;
		this.activeDesignColor = UserPreferences.getInstance().getParallelCoordinatesActiveDesignDefaultColor();
		this.activeDesignColorNoAlpha = new Color(this.activeDesignColor.getRed(), this.activeDesignColor.getGreen(), this.activeDesignColor.getBlue());
		this.clusterListeners = new ArrayList<>();
	}

	public Color getActiveDesignColor(boolean useAlpha) {
		return useAlpha ? activeDesignColor : activeDesignColorNoAlpha;
	}

	public void setActiveDesignColor(Color activeDesignColor) {
		this.activeDesignColor = activeDesignColor;
		this.activeDesignColorNoAlpha = new Color(this.activeDesignColor.getRed(), this.activeDesignColor.getGreen(), this.activeDesignColor.getBlue());
		this.fireColorChanged();
	}

	public int getLineThickness() {
		return lineThickness;
	}

	public void setLineThickness(int lineThickness) {
		this.lineThickness = lineThickness;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.fireNameChanged();
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Cluster duplicate() {
		Cluster duplication = new Cluster(this.name, this.uniqueId);
		duplication.setActive(this.active);
		duplication.setActiveDesignColor(this.activeDesignColor);
		duplication.setLineThickness(this.lineThickness);
		return duplication;
	}

	public boolean copySettingsTo(Cluster cluster) {
		boolean changed = !Objects.equals(cluster.name, this.name) ||
				cluster.active != this.active ||
				!Objects.equals(cluster.activeDesignColor, this.activeDesignColor) ||
				cluster.lineThickness != this.lineThickness;
		if (!changed) {
			return false;
		}
		cluster.setName(this.name);
		cluster.setActive(this.active);
		cluster.setActiveDesignColor(this.activeDesignColor);
		cluster.setLineThickness(this.lineThickness);
		return true;
	}

	public int getUniqueId() {
		return uniqueId;
	}
	
	public void addClusterListener(ClusterListener l){
		if(this.clusterListeners==null){
			this.clusterListeners = new ArrayList<>();
		}
		this.clusterListeners.add(l);
	}
	
	public void removeClusterListener(ClusterListener l){
		if(this.clusterListeners==null){
			return;
		}
		if(this.clusterListeners.contains(l)){
			this.clusterListeners.remove(l);
		}
	}
	
	private void fireNameChanged(){
		for(ClusterListener l : this.clusterListeners){
			l.onNameChanged(this);
		}
	}

	private void fireColorChanged(){
		for(ClusterListener l : this.clusterListeners){
			l.onColorChanged(this);
		}
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cluster cluster = (Cluster) o;
        return uniqueId == cluster.uniqueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uniqueId);
    }
}
