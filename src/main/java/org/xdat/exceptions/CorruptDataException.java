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

import org.xdat.data.Parameter;

/**
 * This exception is thrown when the data of a discrete parameter is corrupt.
 * <p>
 * 
 * @see org.xdat.data.Parameter
 */
public class CorruptDataException extends RuntimeException {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0000;

	/**
	 * Instantiates a new corrupt data exception.
	 * 
	 * @param corruptParameter
	 *            the corrupt parameter
	 */
	public CorruptDataException(Parameter corruptParameter) {
		super("The data of parameter \"" + corruptParameter.getName() + "\" seems to be corrupt!");
	}

}
