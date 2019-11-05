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

import org.xdat.chart.Chart;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;

public abstract class ChartPanel extends JPanel {

	private Chart chart;
	private int marginTop = 20;
	private int marginBottom = 80;
	private int marginLeft = 80;
	private int marginRight = 20;
	ChartPanel(Chart chart) {
		this.chart = chart;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(chart.getBackGroundColor());
		this.drawPlotFieldBackground(g);
	}

	private void drawPlotFieldBackground(Graphics g) {
		g.setColor(this.chart.getBackGroundColor());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public Dimension getPreferredSize() {
		int width = marginLeft + marginRight + chart.getWidth();
		int height = marginTop + marginBottom + chart.getHeight();
		Dimension preferredSize = new Dimension(width, height);

		return preferredSize;
	}

	int getMarginLeft() {
		return marginLeft;
	}

	public Chart getChart() {
		return this.chart;
	}
}
