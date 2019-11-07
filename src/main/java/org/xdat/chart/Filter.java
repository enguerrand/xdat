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

package org.xdat.chart;

import org.xdat.data.DataSheet;
import org.xdat.data.Design;
import org.xdat.data.Parameter;

import java.io.Serializable;

/**
 * Provides the possibility to filter the Designs on a
 * {@link org.xdat.chart.ParallelCoordinatesChart}.
 * <p>
 * Filtered Designs are not displayed on he Chart anymore, or are displayed in a
 * different color than the unfiltered Designs.
 * <p>
 * Each filter is a small triangular draggable symbol on an Axis. Each Axis has
 * two filters, an upper Filter and a lower Filter. The upper Filter is used to
 * filter out all Designs that have a larger value than the Filter value for the
 * Parameter represented by the Filter's Axis. Accordingly, the lower Filter is
 * used to filter out all Designs that have a lower value than the Filter value
 * for the Parameter represented by the Filter's Axis.
 * <p>
 * The behavior described above changes when
 * <ul>
 * <li>the Filter's Axis is inverted: In this case the lower Filter filters out
 * large values and the upper Filter filters out small values.
 * <li>the Filter is inverted. In this case all designs with values between the
 * two Filters become inactive.
 * </ul>
 */
public class Filter implements Serializable {
	static final long serialVersionUID = 2L;
	public static final int UPPER_FILTER = 0;
	public static final int LOWER_FILTER = 1;
	public static final double FILTER_TOLERANCE = 0.00001;
	private int filterType;
	private Axis axis;
	private int xPos;
	private double value;
	public Filter(DataSheet dataSheet, Axis axis, int filterType) {
		this.axis = axis;
		this.filterType = filterType;
		this.reset(dataSheet);
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(double value, DataSheet dataSheet) {
		this.value = value;
		apply(dataSheet);
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int pos) {
		this.xPos = pos;
	}

	public int getYPos(DataSheet dataSheet) {
		if (this.axis.getTicCount() == 1)
			return this.getAxis().getChart().getAxisTopPos() + (int) (this.getAxis().getChart().getAxisHeight() * 0.5);
		else {
			double upperLimit = this.axis.getMax();
			double lowerLimit = this.axis.getMin();
			if (value > upperLimit)
				value = upperLimit;
			else if (value < lowerLimit)
				value = lowerLimit;
			double valueRange = upperLimit - lowerLimit;

			int topPos = this.axis.getChart().getAxisTopPos() - 1;
			int bottomPos = topPos + this.getAxis().getChart().getAxisHeight() + 1;
			double posRange = bottomPos - topPos;
			double ratio;
			int yPos;
			if (axis.isAxisInverted()) {
				ratio = (value - lowerLimit) / valueRange;
				yPos = topPos + (int) (posRange * ratio);
			} else {
				ratio = (value - lowerLimit) / valueRange;
				yPos = bottomPos - (int) (posRange * ratio);
			}
			return yPos;
		}
	}

	public void setYPos(int pos, DataSheet dataSheet) {
		double upperLimit = this.axis.getMax();
		// log("getValue: upperLimit of filter "+filterType+": "+upperLimit);
		double lowerLimit = this.axis.getMin();
		// log("getValue: lowerLimit of filter "+filterType+": "+lowerLimit);
		double valueRange = upperLimit - lowerLimit;
		int topPos = this.axis.getChart().getAxisTopPos() - 1;
		int bottomPos = topPos + this.getAxis().getChart().getAxisHeight() + 1;
		double posRange = bottomPos - topPos;
		double ratio;
		if (axis.isAxisInverted())
			ratio = (pos - topPos) / posRange;
		else
			ratio = (bottomPos - pos) / posRange;
		this.value = lowerLimit + valueRange * ratio;
		apply(dataSheet);
	}

	public int getHighestPos(DataSheet dataSheet) {
		int pos;
		if (this.getFilterType() == Filter.UPPER_FILTER) {
			pos = this.getAxis().getChart().getAxisTopPos();
		} else if (this.getFilterType() == Filter.LOWER_FILTER) {
			pos = this.getAxis().getUpperFilter().getYPos(dataSheet);
		} else {
			pos = this.getAxis().getChart().getAxisTopPos();
		}
		return pos - 1;
	}

	public int getLowestPos(DataSheet dataSheet) {
		int pos;
		if (this.getFilterType() == Filter.UPPER_FILTER) {
			pos = this.getAxis().getLowerFilter().getYPos(dataSheet);
		} else if (this.getFilterType() == Filter.LOWER_FILTER) {
			pos = this.getAxis().getChart().getAxisTopPos() + this.getAxis().getChart().getAxisHeight();
		} else {
			pos = this.getAxis().getChart().getAxisTopPos() + this.getAxis().getChart().getAxisHeight();
		}
		return pos + 1;
	}

	public int getFilterType() {
		return filterType;
	}

	public Axis getAxis() {
		return axis;
	}

	/**
	 * Applies the Filter to all designs.
	 * <p>
	 * This method has to be called every time this Filter value is modified.
	 * This allows to check the Filtering state of a Design with respect to a
	 * Filter only when there is actually a reason for checking rather then upon
	 * every repaint.
	 * <p>
	 * This implementation was chosen because the check if a design is filtered
	 * would become to CPU expensive otherwise because each Filter would have to
	 * be checked for each design every time the Chart is repainted.
	 * <p>
	 *
     * @param dataSheet
     */
	public void apply(DataSheet dataSheet) {
		Parameter param = this.axis.getParameter();
		double tolerance = this.getAxis().getRange() * FILTER_TOLERANCE;
		if (tolerance <= 0) {
			tolerance = FILTER_TOLERANCE;
		}
		double value = this.getValue();

		for (Design design : dataSheet.getDesigns()) {
			if (this.filterType == UPPER_FILTER && axis.isFilterInverted() && axis.isAxisInverted()) {
				if (design.getDoubleValue(param) - tolerance > value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == LOWER_FILTER && axis.isFilterInverted() && axis.isAxisInverted()) {
				if (design.getDoubleValue(param) + tolerance < value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == UPPER_FILTER && axis.isAxisInverted()) {
				if (design.getDoubleValue(param) + tolerance < value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == LOWER_FILTER && axis.isAxisInverted()) {
				if (design.getDoubleValue(param) - tolerance > value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == UPPER_FILTER && axis.isFilterInverted()) {
				if (design.getDoubleValue(param) + tolerance < value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == LOWER_FILTER && axis.isFilterInverted()) {
				if (design.getDoubleValue(param) - tolerance > value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == UPPER_FILTER) {
				if (design.getDoubleValue(param) - tolerance > value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			} else if (this.filterType == LOWER_FILTER) {
				if (design.getDoubleValue(param) + tolerance < value)
					design.setActive(this, false);
				else
					design.setActive(this, true);
			}
		}
	}

	public void reset(DataSheet dataSheet) {
		if (this.filterType == UPPER_FILTER && this.axis.isAxisInverted()) {
			this.setValue(this.axis.getMax(), dataSheet);
		} else if (this.filterType == UPPER_FILTER) {
			double value = this.axis.getMax();
			this.setValue(value, dataSheet);
		} else if (this.filterType == LOWER_FILTER && this.axis.isAxisInverted()) {
			this.setValue(this.axis.getMax(), dataSheet);
		} else {
			this.setValue(this.axis.getMin(), dataSheet);
		}
	}

}
