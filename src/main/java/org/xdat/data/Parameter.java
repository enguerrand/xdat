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

import org.xdat.exceptions.CorruptDataException;

import java.awt.FontMetrics;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * A Parameter represents a parameter of a {@link org.xdat.data.DataSheet}.
 * <p>
 * It is used to store information about the type of data stored in a column of
 * the DataSheet.
 * <p>
 * Two data types are supported: Numeric and Discrete.
 * <p>
 * Numeric parameters are used for columns that only contain numbers. Discrete
 * parameters are used for all columns that contain at least one non-numeric
 * value. All values are stored in a TreeSet and sorted in alphabetical order.
 * This makes it possible to also treat information on parameters that are not
 * quantifiable, such as different shapes of an object or similar.
 * 
 */
public class Parameter implements Serializable {
	static final long serialVersionUID = 4L;
    private DataSheet dataSheet;
	private String name;
	private boolean numeric = true;
	private boolean atLeastOneNumeric = false;
	private boolean atLeastOneNonNumeric = false;
	private TreeSet<String> discreteLevels = new TreeSet<>(new ReverseStringComparator());
    private int ticLabelDigitCount = 3;
	public Parameter(String name, DataSheet dataSheet) {
		this.name = name;
		this.dataSheet = dataSheet;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Checks whether the parameter is mixed. A parameter is mixed if at least
	 * one design has a numeric value and at least one design has a non-numeric
	 * value.
	 * 
	 * @return true, if the parameter is mixed
	 */
	boolean isMixed() {
		return (this.atLeastOneNonNumeric && this.atLeastOneNumeric);
	}

	public boolean isNumeric() {
		return numeric;
	}

	boolean checkIfNumeric() {
		this.numeric = true;
		for (int i = 0; i < this.dataSheet.getDesignCount(); i++) {
			try {
				String string = this.dataSheet.getDesign(i).getStringValue(this);
				NumberParser.parseNumber(string.toString());
				this.atLeastOneNumeric = true;
			} catch (ParseException e) {
				this.setNumeric(false);
				return false;
			}
		}
		return true;
	}

	void setNumeric(boolean numeric) {

		if (this.numeric && !numeric) {
			this.atLeastOneNonNumeric = true;
			for (int i = 0; i < this.dataSheet.getDesignCount(); i++) {
				String string = this.dataSheet.getDesign(i).getStringValue(this);
				int index = 0;
				Iterator<String> it = discreteLevels.iterator();
				boolean stringFound = false;
				while (it.hasNext()) {
					if (string.equalsIgnoreCase(it.next())) {
						stringFound = true;
						break;
					}
					index++;
				}

				if (!stringFound) {
					// String not found, add it to discrete levels
					this.discreteLevels.add(string);
				}
			}
		}
		this.numeric = numeric;
	}

	void setAtLeastOneNumeric(boolean atLeastOneNumeric) {
		this.atLeastOneNumeric = atLeastOneNumeric;
	}

	/**
	 * Gets a numeric representation of a string value for this parameter.
	 * <p>
	 * If the parameter is numeric, an attempt is made to parse the string as a
	 * Double. If this attempt leads to a ParseException, the parameter is not
	 * considered numeric anymore, but is transformed into a discrete parameter.
	 * <p>
	 * If the parameter is not numeric, the string is looked up in the TreeSet
	 * discreteLevels that should contain all discrete values (that is Strings)
	 * that were found in the data sheet for this parameter. If the value is not
	 * found it is added as a new discrete level for this parameter. The treeSet
	 * is then searched again in order to get the correct index of the new
	 * discrete level.
	 * <p>
	 * If this second search does not yield the result, something unexpected has
	 * gone wrong and a CorruptDataException is thrown.
	 * 
	 * @param string
	 *            the string
	 * @return the numeric representation of the given string
	 */
	double getDoubleValueOf(String string) {
		if (this.numeric) {
			try {
				double value = NumberParser.parseNumber(string);
				this.atLeastOneNumeric = true;
				return value;
			} catch (ParseException e1) {
				this.setNumeric(false);
				this.atLeastOneNonNumeric = true;
			}
		}

		int index = 0;
		Iterator<String> it = discreteLevels.iterator();
		while (it.hasNext()) {
			if (string.equalsIgnoreCase(it.next())) {
				return (double) index;
			}
			index++;
		}

		// String not found, add it to discrete levels
		this.discreteLevels.add(string);
		index = 0;
		it = discreteLevels.iterator();
		while (it.hasNext()) {
			String next = it.next();
			if (string.equalsIgnoreCase(next)) {
				return (double) index;
			}
			index++;
		}
		throw new CorruptDataException(this);
	}

	/**
	 * Gets the string representation of a given double value for this
	 * parameter.
	 * <p>
	 * If the parameter is numeric, the provided value is simply converted to a
	 * String and returned.
	 * <p>
	 * If it is discrete, the double value is casted to an Integer value and
	 * this value is used as an index to look up the corresponding discrete
	 * value string in the TreeSet discreteLevels.
	 * <p>
	 * If no value is found for the given index the data is assumed to be
	 * corrupt and a CorruptDataException is thrown.
	 * 
	 * @param value
	 *            the numeric value
	 * @return the string representation of the given double value for this
	 *         parameter.
	 */
	public String getStringValueOf(double value) {
		if (this.numeric) {
			return Double.toString(value);
		} else {
			int index = (int) value;
			int currentIndex = 0;
			Iterator<String> it = discreteLevels.iterator();
			while (it.hasNext()) {
				String next = it.next();
				if (currentIndex == index) {
					return next;
				}
				currentIndex++;
			}
			throw new CorruptDataException(this);
		}
	}

	public int getDiscreteLevelCount() {
		if (this.isNumeric()) {
			throw new RuntimeException("Parameter " + this.name + " is numeric!");
		} else {
			// log("getDiscreteLevelCount returning size "+this.discreteLevels.size());
			return this.discreteLevels.size();
		}
	}

	void updateDiscreteLevels(){
	    if (isNumeric()) {
	        return;
        }
	    this.discreteLevels.clear();
	    this.discreteLevels.addAll(this.dataSheet.getDesigns()
                .stream()
                .map(d -> d.getStringValue(this))
                .collect(Collectors.toSet()));
    }


    public void setTicLabelDigitCount(int value) {
        this.ticLabelDigitCount = value;
    }

    public int getTicLabelDigitCount(){
        return this.ticLabelDigitCount;
    }

    public String getTicLabelFormat() {
        int digitCount = getTicLabelDigitCount();
        return "%"+(digitCount+1)+"."+digitCount+"f";
    }

	void resetDiscreteLevelsAndState() {
		this.discreteLevels = new TreeSet<>(new ReverseStringComparator());
		this.numeric = true;
		this.atLeastOneNonNumeric = false;
		this.atLeastOneNumeric = false;
	}

	public int getLongestTicLabelStringLength(FontMetrics fm, String numberFormat) {
		if (this.isNumeric()) {
			double minValue = Double.POSITIVE_INFINITY;
			double maxValue = Double.NEGATIVE_INFINITY;

			for (int i = 0; i < dataSheet.getDesignCount(); i++) {
				if (dataSheet.getDesign(i).getDoubleValue(this) > maxValue)
					maxValue = dataSheet.getDesign(i).getDoubleValue(this);
				if (dataSheet.getDesign(i).getDoubleValue(this) < minValue)
					minValue = dataSheet.getDesign(i).getDoubleValue(this);
			}
			int minLength = fm.stringWidth(String.format(numberFormat, minValue));
			int maxLength = fm.stringWidth(String.format(numberFormat, maxValue));
			return Math.max(minLength, maxLength);
		} else {
			int length = 0;
			for (int i = 0; i < this.getDiscreteLevelCount(); i++) {
				int tempLength = fm.stringWidth(this.getStringValueOf(i));
				if (tempLength > length)
					length = tempLength;
			}
			return length;
		}
	}

	class ReverseStringComparator implements Comparator<String>, Serializable {

		static final long serialVersionUID = 0L;
		public int compare(String s1, String s2) {
			return (s2.compareToIgnoreCase(s1));
		}
	}

}
