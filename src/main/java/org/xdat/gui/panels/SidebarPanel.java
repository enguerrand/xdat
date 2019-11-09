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
import org.xdat.chart.Chart;
import org.xdat.gui.controls.CustomButton;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class SidebarPanel extends JPanel {

	private Main mainWindow;
	private ChartFrame chartFrame;
	private ChartPanel chartPanel;
	private JPanel contentPanel;
	private JPanel headerPanel;
    private Chart chart;

	SidebarPanel(Main mainWindow, ChartFrame chartFrame, ChartPanel chartPanel, Chart chart) {
		this.mainWindow = mainWindow;
		this.chartFrame = chartFrame;
		this.chartPanel = chartPanel;
		this.chart = chart;
		this.headerPanel = new JPanel();
		this.contentPanel = new JPanel();
		initialize();
		updateView(true);
	}

	private void updateView(boolean maximized) {
		buildHeaderPanel(maximized);
		this.setLayout(new BorderLayout());
		this.add(headerPanel, BorderLayout.NORTH);
		contentPanel.removeAll();
		if(maximized){
			buildPanel(this.contentPanel);
			this.add(contentPanel, BorderLayout.CENTER);
		}
		this.chartFrame.validate();
		this.chartFrame.repaint();
	}

	private void buildHeaderPanel(final boolean maximized) {
		this.headerPanel.removeAll();
		this.headerPanel.setLayout(new BorderLayout());
		CustomButton toggleButton;
		if(maximized){
			toggleButton = new CustomButton("Collapse", "images" + "/minimize.png", "images" + "/minimize_pressed.png", "Collapse");
		}
		else{
			toggleButton = new CustomButton("Expand", "images" + "/maximize.png", "images" + "/maximize_pressed.png", "Expand");
		}
		this.headerPanel.add(toggleButton, BorderLayout.WEST);
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateView(!maximized);
			}
		});
	}

	public Main getMainWindow() {
		return mainWindow;
	}

	public Chart getChart() {
		return chart;
	}
	
	public ChartPanel getChartPanel() {
		return chartPanel;
	}
	
	public ChartFrame getChartFrame() {
		return chartFrame;
	}
	
	protected abstract void buildPanel(JPanel contentPanel);

	protected abstract void initialize();

}
