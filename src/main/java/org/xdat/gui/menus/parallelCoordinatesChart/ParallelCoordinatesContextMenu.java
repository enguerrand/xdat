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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.xdat.Main;
import org.xdat.actionListeners.parallelCoordinatesChartFrame.ParallelChartContextMenuActionListener;
import org.xdat.chart.Axis;
import org.xdat.gui.frames.ChartFrame;

/**
 * A context menu for the parallel coordinates chart that is opened upon
 * right-click.
 * <p>
 * This makes it easier to edit axis-specific settings.
 */
public class ParallelCoordinatesContextMenu extends JPopupMenu {
	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/**
	 * Instantiates a new parallel coordinates context menu.
	 * 
	 * @param mainWindow
	 *            the main Window
	 * @param chartFrame
	 *            the chart frame
	 * @param axis
	 *            the axis
	 */
	public ParallelCoordinatesContextMenu(Main mainWindow, ChartFrame chartFrame, Axis axis) {
		super();
		this.add(new JLabel(" Axis settings: " + axis.getName()));
		this.add(new Separator());

		ParallelChartContextMenuActionListener cmd = new ParallelChartContextMenuActionListener(mainWindow, chartFrame, axis);
		// set filter as new range
		JMenuItem setFilterAsNewRangeMenuItem = new JMenuItem("Set current filter as new range");
		setFilterAsNewRangeMenuItem.setActionCommand("setCurrentFilterAsNewRange");
		if (axis.isFilterInverted() || !axis.getParameter().isNumeric())
			setFilterAsNewRangeMenuItem.setEnabled(false);
		else
			setFilterAsNewRangeMenuItem.addActionListener(cmd);
		this.add(setFilterAsNewRangeMenuItem);

		// reset filter
		JMenuItem resetFilterMenuItem = new JMenuItem("Reset filter to axis range");
		resetFilterMenuItem.setActionCommand("resetFilter");
		resetFilterMenuItem.addActionListener(cmd);
		this.add(resetFilterMenuItem);

		// autofit
		JMenuItem autofitMenuItem = new JMenuItem("Autofit");
		autofitMenuItem.setActionCommand("autofit");
		if (!axis.getParameter().isNumeric())
			autofitMenuItem.setEnabled(false);
		else
			autofitMenuItem.addActionListener(cmd);
		this.add(autofitMenuItem);
		

		// Color brush axis values
		JMenuItem markValuesMenuItem = new JMenuItem("Apply color gradient");
		markValuesMenuItem.setActionCommand("applyColorGradient");
		markValuesMenuItem.addActionListener(cmd);
		this.add(markValuesMenuItem);

		// invert axis
		JMenuItem invertAxisMenuItem = new JMenuItem("Invert Axis                                ");
		invertAxisMenuItem.setActionCommand("invertAxis");
		invertAxisMenuItem.addActionListener(cmd);
		this.add(invertAxisMenuItem);
		
		// move column to left
		JMenuItem moveColumnLeftMenuItem = new JMenuItem("Move left                               ");
		this.addCustomHint(moveColumnLeftMenuItem, "(Left-click & drag)");
		moveColumnLeftMenuItem.setActionCommand("moveAxisLeft");
		moveColumnLeftMenuItem.addActionListener(cmd);
		this.add(moveColumnLeftMenuItem);

		// move column to right
		JMenuItem moveColumnRightMenuItem = new JMenuItem("Move right                               ");
		moveColumnRightMenuItem.setActionCommand("moveAxisRight");
		this.addCustomHint(moveColumnRightMenuItem, "(Left-click & drag)");
		moveColumnRightMenuItem.addActionListener(cmd);
		this.add(moveColumnRightMenuItem);

		// hide axis
		JMenuItem hideMenuItem = new JMenuItem("Hide axis                               ");
		hideMenuItem.setActionCommand("hideAxis");
		hideMenuItem.addActionListener(cmd);
		this.add(hideMenuItem);

		// add tic
		JMenuItem addTicMenuItem = new JMenuItem("Add tic                               ");
		addTicMenuItem.setActionCommand("addTic");
		this.addCustomHint(addTicMenuItem, "(Alt+Mouse Whl Up)");
		if (!axis.getParameter().isNumeric())
			addTicMenuItem.setEnabled(false);
		else
			addTicMenuItem.addActionListener(cmd);
		this.add(addTicMenuItem);

		// remove tic
		JMenuItem removeTicMenuItem = new JMenuItem("Remove tic                               ");
		removeTicMenuItem.setActionCommand("removeTic");
		this.addCustomHint(removeTicMenuItem, "(Alt+Mouse Whl Down)");
		if (!axis.getParameter().isNumeric())
			removeTicMenuItem.setEnabled(false);
		else
			removeTicMenuItem.addActionListener(cmd);
		this.add(removeTicMenuItem);

		// increase distance
		JMenuItem increaseDistanceThisAxisMenuItem = new JMenuItem("Increase spacing                               ");
		increaseDistanceThisAxisMenuItem.setActionCommand("increaseDistanceThisAxis");
		this.addCustomHint(increaseDistanceThisAxisMenuItem, "(Ctrl+Mouse Whl Up)");
		increaseDistanceThisAxisMenuItem.addActionListener(cmd);
		this.add(increaseDistanceThisAxisMenuItem);

		// reduce distance
		JMenuItem reduceDistanceThisAxisMenuItem = new JMenuItem("Reduce spacing                                ");
		reduceDistanceThisAxisMenuItem.setActionCommand("reduceDistanceThisAxis");
		this.addCustomHint(reduceDistanceThisAxisMenuItem, "(Ctrl+Mouse Whl Down)");
		reduceDistanceThisAxisMenuItem.addActionListener(cmd);
		this.add(reduceDistanceThisAxisMenuItem);

		// all axes
		this.addSeparator();

		// reset filter
		JMenuItem resetAllFiltersMenuItem = new JMenuItem("Reset all filters to axis range");
		resetAllFiltersMenuItem.setActionCommand("resetAllFilters");
		resetAllFiltersMenuItem.addActionListener(cmd);
		this.add(resetAllFiltersMenuItem);

		// increase distance
		JMenuItem increaseDistanceAllAxesMenuItem = new JMenuItem("Increase spacing all axes                               ");
		increaseDistanceAllAxesMenuItem.setActionCommand("increaseDistanceAllAxes");
		this.addCustomHint(increaseDistanceAllAxesMenuItem, "(Mouse Whl Up)");
		increaseDistanceAllAxesMenuItem.addActionListener(cmd);
		this.add(increaseDistanceAllAxesMenuItem);

		// reduce distance
		JMenuItem reduceDistanceAllAxesMenuItem = new JMenuItem("Reduce spacing all axes                                ");
		reduceDistanceAllAxesMenuItem.setActionCommand("reduceDistanceAllAxes");
		this.addCustomHint(reduceDistanceAllAxesMenuItem, "(Mouse Whl Down)");
		reduceDistanceAllAxesMenuItem.addActionListener(cmd);
		this.add(reduceDistanceAllAxesMenuItem);
		
		// Undo color brushing axis values
		JMenuItem unmarkValuesMenuItem = new JMenuItem("Reset all color gradients");
		unmarkValuesMenuItem.setActionCommand("resetColorGradient");
		unmarkValuesMenuItem.addActionListener(cmd);
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
