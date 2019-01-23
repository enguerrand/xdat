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

package org.xdat.exceptions;

/**
 * This exception is thrown when trying to get an axis at an x location where no
 * axis is found.
 */
public class NoAxisFoundException extends Exception {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/**
	 * Instantiates a new NoAxisFound exception.
	 * @param x the x position where the axis was looked for
	 */
	public NoAxisFoundException(int x) {
		super("No Axis found at x position " + x);
	}

}
