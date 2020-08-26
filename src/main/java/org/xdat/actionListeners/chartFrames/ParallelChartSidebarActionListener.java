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

package org.xdat.actionListeners.chartFrames;

import org.xdat.Main;
import org.xdat.UserPreferences;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.Cluster;
import org.xdat.data.ClusterFactory;
import org.xdat.data.ClusterSet;
import org.xdat.data.DataSheet;
import org.xdat.gui.controls.ColorChoiceButton;
import org.xdat.gui.panels.ParallelCoordinatesChartPanel;
import org.xdat.gui.panels.ParallelCoordinatesChartSidebarPanel;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JSlider;
import java.awt.Color;

public class ParallelChartSidebarActionListener {
	private final ParallelCoordinatesChartSidebarPanel sidePanel;
	private final Main mainWindow;
	private final ParallelCoordinatesChartPanel chartPanel;
	private final ClusterFactory clusterFactory;
	private Color activeDesignColor;

	public ParallelChartSidebarActionListener(Main mainWindow, ParallelCoordinatesChartSidebarPanel panel, ParallelCoordinatesChartPanel chartPanel, ClusterFactory clusterFactory) {
		this.mainWindow = mainWindow;
		this.sidePanel = panel;
		this.chartPanel = chartPanel;
		this.activeDesignColor = ((ParallelCoordinatesChart) chartPanel.getChart()).getDefaultDesignColor(true,  chartPanel.getChart().isUseAlpha());
		this.clusterFactory = clusterFactory;
	}

	public void changeClusterColor(ColorChoiceButton source, Cluster cluster) {
		Color newColor = JColorChooser.showDialog(sidePanel.getChartFrame(), "Cluster Color", cluster.getActiveDesignColor(true));
		if (newColor != null) {
			if (newColor.getAlpha() == 255) {
				int alpha = sidePanel.getClusterAlphaSlider(cluster).getValue();
				newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alpha);
			}

			cluster.setActiveDesignColor(newColor);
			source.setCurrentColor(newColor);
			sidePanel.getClusterAlphaSlider(cluster).setValue(newColor.getAlpha());
			chartPanel.repaint();
		}
	}

	public void toggleClusterActive(JCheckBox source, Cluster cluster) {
		cluster.setActive(source.isSelected());
		this.mainWindow.fireClustersChanged();
	}

	public void applySettings(ParallelCoordinatesChart chart, Cluster cluster) {
		DataSheet dataSheet = chart.getDataSheet();
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			if (dataSheet.getDesign(i).isActive(chart)) {
				dataSheet.getDesign(i).setCluster(cluster);
			}
		}
		this.chartPanel.setPreferredSize(this.chartPanel.getPreferredSize());

		for (int i = 0; i < this.mainWindow.getChartFrameCount(); i++) {
			this.mainWindow.getChartFrame(i).validate();
			this.mainWindow.getChartFrame(i).repaint();
		}
	}

	public void removeCluster(Cluster cluster) {
		ClusterSet clusterSet = mainWindow.getCurrentClusterSet();
		clusterSet.removeCluster(cluster);

		this.sidePanel.updateClusterList(clusterSet);
		this.chartPanel.revalidate();
		this.chartPanel.repaint();
	}

	public void addCluster() {
		ClusterSet clusterSet = mainWindow.getCurrentClusterSet();
		clusterSet.newCluster(clusterFactory);
		this.sidePanel.updateClusterList(clusterSet);
	}

	public void changeActiveDesignColor(ParallelCoordinatesChart chart) {
		Color newColor = JColorChooser.showDialog(sidePanel.getChartFrame(), "Background Color", this.activeDesignColor);
		if (newColor != null) {
			if (newColor.getAlpha() == 255) {
				int alphaValue = sidePanel.getActiveDesignAlphaSlider().getValue();
				newColor = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), alphaValue);
			}

			this.activeDesignColor = newColor;
			chart.setActiveDesignColor(newColor);
			this.sidePanel.getActiveDesignColorButton().setCurrentColor(this.activeDesignColor);
			this.sidePanel.getActiveDesignAlphaSlider().setValue(newColor.getAlpha());
			chartPanel.repaint();
		}
	}

	public void setClusterAlpha(JSlider slider, Cluster cluster) {
		int value = slider.getValue();
		if(cluster != null){
			Color newColor = new Color(cluster.getActiveDesignColor(true).getRed(), cluster.getActiveDesignColor(true).getGreen(), cluster.getActiveDesignColor(true).getBlue(), value);
			cluster.setActiveDesignColor(newColor);
			chartPanel.repaint();
		}
	}

	public void setActiveDesignColorAlpha(JSlider slider, ParallelCoordinatesChart chart) {
		int value = slider.getValue();
		Color newColor = new Color(activeDesignColor.getRed(), activeDesignColor.getGreen(), activeDesignColor.getBlue(), value);
		this.activeDesignColor = newColor;
		chart.setActiveDesignColor(newColor);
		this.sidePanel.getActiveDesignColorButton().setCurrentColor(this.activeDesignColor);
		this.sidePanel.getActiveDesignAlphaSlider().setValue(slider.getValue());
		chartPanel.repaint();
		Color oldDefaultColor = UserPreferences.getInstance().getParallelCoordinatesActiveDesignDefaultColor();
		UserPreferences.getInstance().setParallelCoordinatesActiveDesignDefaultColor(new Color(oldDefaultColor.getRed(), oldDefaultColor.getGreen(), oldDefaultColor.getBlue(), value));
	}

	public Color getActiveDesignColor() {
		return activeDesignColor;
	}
}
