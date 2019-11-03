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
package org.xdat.data;

import org.xdat.UserPreferences;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.regex.Pattern;


public class NumberParser {

	public static float parseNumber(String string) throws ParseException {
		NumberFormat nf = NumberFormat.getInstance(UserPreferences.getInstance().getLocale());
		ParsePosition parsePosition = new ParsePosition(0);
		Number number = nf.parse(string, parsePosition);
		if (number == null) {
			throw new ParseException("Failed to parse: " + string, 0);
		} else if (parsePosition.getIndex() < string.length()) {
			throw new ParseException("Could not parse whole string", parsePosition.getErrorIndex());
		} else if ((Pattern.matches(".*\\..{0,2}?\\..*", string) || Pattern.matches(".*,.{0,2}?,.*", string))) {
			throw new ParseException(" Recognized wrong distance of digit grouping symbols ", parsePosition.getErrorIndex());
		} else {
			return number.floatValue();
		}
	}
}
