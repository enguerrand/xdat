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
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ParallelChartSidebarActionListener;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.Cluster;
import org.xdat.data.ClusterListener;
import org.xdat.data.ClusterSet;
import org.xdat.gui.buttons.ColorChoiceButton;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParallelCoordinatesChartSidebarPanel extends SidebarPanel {

	private static final Color CLUSTER_NAME_TEXT_FIELD_EDITING_COLOR = new Color(255, 205, 51);
	private static final Color CLUSTER_NAME_TEXT_FIELD_STANDARD_COLOR = new Color(230, 230, 230);
	private ParallelChartSidebarActionListener cmd;
	private ColorChoiceButton activeDesignColorButton;
	private JSlider activeDesignAlphaSlider;
	private JButton newClusterButton;
	private JScrollPane clusterScrollPane;
	private JPanel clusterPanel;
	private Map<Cluster, JSlider> clusterAlphaSliders;
	private Map<ClusterListener, Cluster> clusterListeners;

	public ParallelCoordinatesChartSidebarPanel(Main mainWindow, ChartFrame chartFrame, ChartPanel chartPanel, ParallelCoordinatesChart chart) {
		super(mainWindow, chartFrame, chartPanel, chart);
	}

	@Override
	protected void buildPanel(JPanel parentPanel) {
		// create components
		ParallelCoordinatesChart chart = (ParallelCoordinatesChart) getChart();
		parentPanel.setLayout(new BorderLayout());
		JPanel generalControlsPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
		generalControlsPanel.setPreferredSize(new Dimension(195, 140));
		parentPanel.add(generalControlsPanel, BorderLayout.NORTH);

		JPanel activeDesignColorButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel activeDesignColorLabel = new JLabel("Active Design Color");
		activeDesignColorButtonPanel.add(activeDesignColorLabel);
		this.activeDesignColorButton = new ColorChoiceButton(chart.getDefaultDesignColor(true, chart.isUseAlpha()), "Active Design Color");
		this.activeDesignColorButton.addActionListener(cmd);
		activeDesignColorButtonPanel.add(activeDesignColorButton);
		generalControlsPanel.add(activeDesignColorButtonPanel);

		JPanel activeDesignAlphaSliderLabelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel activeDesignAlphaSliderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		activeDesignAlphaSliderLabelPanel.setPreferredSize(new Dimension(180, 20));
		activeDesignAlphaSliderPanel.setPreferredSize(new Dimension(180, 30));
		generalControlsPanel.add(activeDesignAlphaSliderLabelPanel);
		generalControlsPanel.add(activeDesignAlphaSliderPanel);
		JLabel activeDesignAlphaLabel = new JLabel("Transparency");
		activeDesignAlphaSliderLabelPanel.add(activeDesignAlphaLabel);
		this.activeDesignAlphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
		this.activeDesignAlphaSlider.addChangeListener(cmd);
		this.activeDesignAlphaSlider.setName("activeDesignAlphaSlider");
		this.activeDesignAlphaSlider.setPreferredSize(new Dimension(120, 30));
		activeDesignAlphaSliderPanel.add(activeDesignAlphaSlider);

		JPanel addClusterButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		addClusterButtonPanel.setPreferredSize(new Dimension(180, 35));
		newClusterButton = new JButton("Add Cluster");
		newClusterButton.addActionListener(cmd);
		addClusterButtonPanel.add(newClusterButton);
		generalControlsPanel.add(addClusterButtonPanel);

		parentPanel.add(clusterScrollPane, BorderLayout.CENTER);
		JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		clusterScrollPane.setViewportView(contentPanel);

		this.clusterPanel = new JPanel(new GridLayout(0, 1));
		contentPanel.add(this.clusterPanel);

		this.activeDesignColorButton.setCurrentColor(chart.getDefaultDesignColor(true, chart.isUseAlpha()));
		if (chart.isUseAlpha())
			this.activeDesignAlphaSlider.setValue(chart.getDefaultDesignColor(true, chart.isUseAlpha()).getAlpha());
		else {
			this.activeDesignAlphaSlider.setValue(255);
			this.activeDesignAlphaSlider.setEnabled(false);
		}

		linkColorButtonToSlider(this.activeDesignColorButton, this.activeDesignAlphaSlider);
		this.updateClusterList(getMainWindow().getCurrentClusterSet());
		this.validate();
	}

	public ColorChoiceButton getActiveDesignColorButton() {
		return activeDesignColorButton;
	}

	public JSlider getActiveDesignAlphaSlider() {
		return activeDesignAlphaSlider;
	}

	public JSlider getClusterAlphaSlider(Cluster cluster) {
		if (this.clusterAlphaSliders.containsKey(cluster)) {
			return this.clusterAlphaSliders.get(cluster);
		}
		return null;
	}

	public void updateClusterList(ClusterSet clusterSet) {
		int clusterCount = clusterSet.getClusterCount();

		for (ClusterListener l : this.clusterListeners.keySet()) {
			this.clusterListeners.get(l).removeClusterListener(l);
		}
		this.clusterPanel.removeAll();
		this.clusterPanel.setLayout(new GridLayout(clusterCount, 1));
		this.clusterAlphaSliders.clear();

		for (int i = 0; i < clusterCount; i++) {
			final Cluster cluster = clusterSet.getCluster(i);
			JPanel wrapperPanel = new JPanel(new GridLayout(5, 1));

			final JTextField clusterNameTextField = new JTextField();
			clusterNameTextField.setEditable(true);
			clusterNameTextField.setBackground(CLUSTER_NAME_TEXT_FIELD_STANDARD_COLOR);
			clusterNameTextField.setToolTipText("Click to edit cluster name");
			clusterNameTextField.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					handleUpdateUpdatedClusterName(clusterNameTextField, cluster, event.getActionCommand());
				}
			});
			clusterNameTextField.addFocusListener(new FocusListener() {

				@Override
				public void focusLost(FocusEvent arg0) {
					clusterNameTextField.setBackground(CLUSTER_NAME_TEXT_FIELD_STANDARD_COLOR);
					clusterNameTextField.setToolTipText("Click to edit cluster name");
					handleUpdateUpdatedClusterName(clusterNameTextField, cluster, clusterNameTextField.getText());
				}

				@Override
				public void focusGained(FocusEvent arg0) {
					clusterNameTextField.setBackground(CLUSTER_NAME_TEXT_FIELD_EDITING_COLOR);
					clusterNameTextField.setToolTipText(null);
				}
			});
			// final JLabel clusterNameLabel = new JLabel();
			wrapperPanel.add(clusterNameTextField);

			JPanel clusterButtonPanel = new JPanel(new BorderLayout());
			wrapperPanel.add(clusterButtonPanel);

			final ColorChoiceButton clusterColorButton = new ColorChoiceButton(cluster.getActiveDesignColor(true), "clusterColor", 20, 25);
			clusterColorButton.addActionListener(this.cmd);
			clusterButtonPanel.add(clusterColorButton, BorderLayout.WEST);

			final JButton removeClusterButton = new JButton("Remove");
			removeClusterButton.addActionListener(this.cmd);
			clusterButtonPanel.add(removeClusterButton, BorderLayout.CENTER);

			final JButton applyClusterButton = new JButton("Apply");
			applyClusterButton.addActionListener(this.cmd);
			clusterButtonPanel.add(applyClusterButton, BorderLayout.EAST);

			JLabel clusterAlphaLabel = new JLabel("Transparency");
			wrapperPanel.add(clusterAlphaLabel);

			final JSlider clusterAlphaSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, cluster.getActiveDesignColor(true).getAlpha());
			this.clusterAlphaSliders.put(cluster, clusterAlphaSlider);
			clusterAlphaSlider.setPreferredSize(new Dimension(150, 10));
			clusterAlphaSlider.addChangeListener(cmd);
			wrapperPanel.add(clusterAlphaSlider);

			linkColorButtonToSlider(clusterColorButton, clusterAlphaSlider);

			final JCheckBox clusterActiveCheckbox = new JCheckBox("Active");
			clusterActiveCheckbox.addActionListener(cmd);
			clusterActiveCheckbox.setSelected(cluster.isActive());
			wrapperPanel.add(clusterActiveCheckbox);

			updateElementNames(cluster.getName(), clusterNameTextField, clusterColorButton, removeClusterButton, applyClusterButton, clusterAlphaSlider, clusterActiveCheckbox);

			ClusterListener listener = new ClusterListener() {
				@Override
				public void onNameChanged(Cluster source) {
					updateElementNames(source.getName(), clusterNameTextField, clusterColorButton, removeClusterButton, applyClusterButton, clusterAlphaSlider, clusterActiveCheckbox);
					revalidate();
					repaint();
				}

				@Override
				public void onColorChanged(Cluster source) {
					updateElementColor(clusterColorButton, source.getActiveDesignColor(getChart().isUseAlpha()));
					revalidate();
					repaint();
				}
			};
			cluster.addClusterListener(listener);

			this.clusterListeners.put(listener, cluster);
			this.clusterPanel.add(wrapperPanel);
		}

		this.revalidate();
		this.repaint();
	}

	private void linkColorButtonToSlider(final ColorChoiceButton colorButton, final JSlider clusterAlphaSlider) {
		clusterAlphaSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				Object source = event.getSource();
				if (source instanceof JSlider) {
					JSlider slider = (JSlider) source;
					int value = Math.min(slider.getValue(), 255);
					colorButton.setAlpha(value);
				}
			}
		});
	}

	private void updateElementNames(String name, JTextField clusterNameLabel, JButton clusterColorButton, JButton removeClusterButton, JButton applyClusterButton, JSlider clusterAlphaSlider, JCheckBox clusterActiveCheckbox) {
		if (!clusterNameLabel.getText().equals(name)) {
			clusterNameLabel.setText(name);
		}
		clusterColorButton.setName(name);
		removeClusterButton.setName(name);
		applyClusterButton.setName(name);
		clusterAlphaSlider.setName(name);
		clusterActiveCheckbox.setName(name);
	}

	private void updateElementColor(ColorChoiceButton clusterColorButton, Color color) {
		clusterColorButton.setCurrentColor(color);
	}

	private void handleUpdateUpdatedClusterName(JTextField source, Cluster cluster, String newName) {
		if (newName == null || newName.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Invalid cluster name: \"" + newName + "\"", "Error on renaming cluster", JOptionPane.ERROR_MESSAGE);
			source.setText(cluster.getName());
		}
		if (newName.equals(cluster.getName())) {
			return;
		}
		cluster.setName(newName);
	}

	@Override
	protected void initialize() {
		clusterScrollPane = new JScrollPane();
		clusterScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		clusterListeners = new HashMap<>();
		clusterAlphaSliders = new LinkedHashMap<>(10);
		this.cmd = new ParallelChartSidebarActionListener(getMainWindow(), this, (ParallelCoordinatesChartPanel) getChartPanel(), getMainWindow().getClusterFactory());
	}

	public void setAlphaSlidersEnabled(boolean enabled) {
		this.activeDesignAlphaSlider.setEnabled(enabled);
		for (JSlider slider : this.clusterAlphaSliders.values()) {
			slider.setEnabled(enabled);
		}
	}
}
