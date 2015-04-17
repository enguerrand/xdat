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

package org.xdat.gui.buttons;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Spinner model that allows the user to specify minimum and maximum input
 * values, for example to define a range for an {@link org.xdat.chart.Axis} .
 * <p>
 * This spinnermodel is designed to be used with a pair of spinners.
 * <p>
 * One spinner lets the user specify the maximum value of a range and the other
 * spinner lets him specify the minimum value. Both spinners are limited within
 * the range provided in the constructor arguments min and max.
 * <p>
 * However, in order to function correctly, each spinner also needs a reference
 * to its counterpart in order to make sure that the min value spinner does not
 * allow the user to choose a value that is larger than the one set in the max
 * value spinner and vice versa. Through this setting, the spinner also
 * understands whether it is a lower bound or an upper bound spinner, because it
 * is only provided with a reference to the opposite spinner.
 */
public class MinMaxSpinnerModel extends SpinnerNumberModel {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/** The minimum value. */
	private int min;

	/** The maximum value. */
	private int max;

	/**
	 * Reference to the upper bound spinner. Only used by the lower bound
	 * spinner
	 */
	private JSpinner upperBoundSpinner;

	/**
	 * Reference to the lower bound spinner. Only used by the upper bound
	 * spinner
	 */
	private JSpinner lowerBoundSpinner;

	/**
	 * Instantiates a new min max spinner model.
	 * 
	 * @param min
	 *            the min
	 * @param max
	 *            the max
	 */
	public MinMaxSpinnerModel(int min, int max) {
		this.min = min;
		this.max = max;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.SpinnerNumberModel#setValue(java.lang.Object)
	 */
	public void setValue(Object value) {
		int integerValue = (Integer) value;

		int upperBound = this.max;
		int lowerBound = this.min;
		if (this.upperBoundSpinner != null) // reference to upper bound spinner
											// available => this class instance
											// is a lower bound spinner
		{
			upperBound = Math.min((Integer) this.upperBoundSpinner.getValue(), this.max);
		}

		if (this.lowerBoundSpinner != null) // reference to lower bound spinner
											// available => this class instance
											// is a upper bound spinner
		{
			lowerBound = Math.max((Integer) this.lowerBoundSpinner.getValue(), this.min);
		}
		if (integerValue > upperBound) {
			super.setValue(upperBound);
		} else if (integerValue < lowerBound) {
			super.setValue(lowerBound);
		} else {
			super.setValue(integerValue);
		}

	}

	/**
	 * Sets the lower bound spinner. Only used for upper bound spinners.
	 * 
	 * @param lowerBoundSpinner
	 *            the new lower bound spinner
	 */
	public void setLowerBoundSpinner(JSpinner lowerBoundSpinner) {
		this.lowerBoundSpinner = lowerBoundSpinner;
	}

	/**
	 * Sets the upper bound spinner. Only used for lower bound spinners.
	 * 
	 * @param upperBoundSpinner
	 *            the new upper bound spinner
	 */
	public void setUpperBoundSpinner(JSpinner upperBoundSpinner) {
		this.upperBoundSpinner = upperBoundSpinner;
	}

}
