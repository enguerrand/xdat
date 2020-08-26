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
package org.xdat.gui.menus.parallelCoordinatesChart;

import org.xdat.Main;
import org.xdat.actionListeners.chartFrames.ParallelChartContextMenuActionListener;
import org.xdat.chart.Axis;
import org.xdat.gui.frames.ChartFrame;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class ParallelCoordinatesContextMenu extends JPopupMenu {

	public ParallelCoordinatesContextMenu(Main mainWindow, ChartFrame chartFrame, Axis axis) {
		super();
		this.add(new JLabel(" Axis settings: " + axis.getName()));
		this.add(new Separator());

		ParallelChartContextMenuActionListener cmd = new ParallelChartContextMenuActionListener(mainWindow, chartFrame, axis);

		JMenuItem setFilterAsNewRangeMenuItem = new JMenuItem("Set current filter as new range");
		if (axis.isFilterInverted() || !axis.getParameter().isNumeric())
			setFilterAsNewRangeMenuItem.setEnabled(false);
		else
			setFilterAsNewRangeMenuItem.addActionListener(cmd::setCurrentFiltersAsNewRange);
		this.add(setFilterAsNewRangeMenuItem);

		JMenuItem resetFilterMenuItem = new JMenuItem("Reset filter to axis range");
		resetFilterMenuItem.addActionListener(cmd::resetFilter);
		this.add(resetFilterMenuItem);

		JMenuItem autofitMenuItem = new JMenuItem("Autofit");
		if (!axis.getParameter().isNumeric())
			autofitMenuItem.setEnabled(false);
		else
			autofitMenuItem.addActionListener(cmd::autofit);
		this.add(autofitMenuItem);
		
		JMenuItem markValuesMenuItem = new JMenuItem("Apply color gradient");
		markValuesMenuItem.addActionListener(cmd::applyColorGradient);
		this.add(markValuesMenuItem);

		JMenuItem invertAxisMenuItem = new JMenuItem("Invert Axis                                ");
		invertAxisMenuItem.addActionListener(cmd::invertAxis);
		this.add(invertAxisMenuItem);
		
		JMenuItem moveColumnLeftMenuItem = new JMenuItem("Move left                               ");
		this.addCustomHint(moveColumnLeftMenuItem, "(Left-click & drag)");
		moveColumnLeftMenuItem.addActionListener(cmd::moveAxisLeft);
		this.add(moveColumnLeftMenuItem);

		JMenuItem moveColumnRightMenuItem = new JMenuItem("Move right                               ");
		this.addCustomHint(moveColumnRightMenuItem, "(Left-click & drag)");
		moveColumnRightMenuItem.addActionListener(cmd::moveAxisRight);
		this.add(moveColumnRightMenuItem);

		JMenuItem hideMenuItem = new JMenuItem("Hide axis                               ");
		hideMenuItem.addActionListener(cmd::hideAxis);
		this.add(hideMenuItem);

		JMenuItem addTicMenuItem = new JMenuItem("Add tic                               ");
		this.addCustomHint(addTicMenuItem, "(Alt+Mouse Whl Up)");
		if (!axis.getParameter().isNumeric())
			addTicMenuItem.setEnabled(false);
		else
			addTicMenuItem.addActionListener(cmd::addTic);
		this.add(addTicMenuItem);

		JMenuItem removeTicMenuItem = new JMenuItem("Remove tic                               ");
		this.addCustomHint(removeTicMenuItem, "(Alt+Mouse Whl Down)");
		if (!axis.getParameter().isNumeric())
			removeTicMenuItem.setEnabled(false);
		else
			removeTicMenuItem.addActionListener(cmd::removeTic);
		this.add(removeTicMenuItem);

		JMenuItem increaseDistanceThisAxisMenuItem = new JMenuItem("Increase spacing                               ");
		this.addCustomHint(increaseDistanceThisAxisMenuItem, "(Ctrl+Mouse Whl Up)");
		increaseDistanceThisAxisMenuItem.addActionListener(e -> cmd.addDistanceThisAxis(10));
		this.add(increaseDistanceThisAxisMenuItem);

		JMenuItem reduceDistanceThisAxisMenuItem = new JMenuItem("Reduce spacing                                ");
		this.addCustomHint(reduceDistanceThisAxisMenuItem, "(Ctrl+Mouse Whl Down)");
		increaseDistanceThisAxisMenuItem.addActionListener(e -> cmd.addDistanceThisAxis(-10));
		this.add(reduceDistanceThisAxisMenuItem);

		this.addSeparator();

		JMenuItem resetAllFiltersMenuItem = new JMenuItem("Reset all filters to axis range");
		resetAllFiltersMenuItem.addActionListener(cmd::resetAllFilters);
		this.add(resetAllFiltersMenuItem);

		JMenuItem increaseDistanceAllAxesMenuItem = new JMenuItem("Increase spacing all axes                               ");
		this.addCustomHint(increaseDistanceAllAxesMenuItem, "(Mouse Whl Up)");
		increaseDistanceAllAxesMenuItem.addActionListener(cmd::increaseDistanceAllAxes);
		this.add(increaseDistanceAllAxesMenuItem);

		JMenuItem reduceDistanceAllAxesMenuItem = new JMenuItem("Reduce spacing all axes                                ");
		this.addCustomHint(reduceDistanceAllAxesMenuItem, "(Mouse Whl Down)");
		reduceDistanceAllAxesMenuItem.addActionListener(cmd::reduceDistanceAllAxes);
		this.add(reduceDistanceAllAxesMenuItem);
		
		JMenuItem unmarkValuesMenuItem = new JMenuItem("Reset all color gradients");
		unmarkValuesMenuItem.addActionListener(cmd::resetColorGradient);
		this.add(unmarkValuesMenuItem);
	}

	private void addCustomHint(JMenuItem item, String hint) {
		item.setLayout(new BorderLayout());
		JLabel label = new JLabel(hint + " ");
		label.setFont(new Font("SansSerif", Font.PLAIN, 12));
		label.setForeground(new Color(140, 140, 180));
		item.add(label, BorderLayout.EAST);
	}
}
