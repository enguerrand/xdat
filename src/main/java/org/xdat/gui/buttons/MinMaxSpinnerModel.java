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

import javax.swing.SpinnerNumberModel;

public class MinMaxSpinnerModel extends SpinnerNumberModel {
	private final int min;
	private final int max;

	public MinMaxSpinnerModel(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public void setValue(Object value) {
		int integerValue = (Integer) value;
		int upperBound = this.max;
		super.setValue(Math.min(this.max, Math.max(integerValue, this.min)));
	}
}
