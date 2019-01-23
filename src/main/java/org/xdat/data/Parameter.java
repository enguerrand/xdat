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

import java.awt.FontMetrics;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

import org.xdat.Main;
import org.xdat.exceptions.CorruptDataException;

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

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0003;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** Datasheet to which the parameter belongs. */
	private DataSheet dataSheet;

	/** The parameter name. */
	private String name;

	/**
	 * Specifies whether the parameter is numeric. If it is not, it is discrete.
	 */
	private boolean numeric = true;

	/** Specifies at least one design has a numeric value */
	private boolean atLeastOneNumeric = false;

	/** Specifies at least one design has a non-numeric value */
	private boolean atLeastOneNonNumeric = false;

	/** The discrete levels. Only applies for non-numeric parameters. */
	private TreeSet<String> discreteLevels = new TreeSet<String>(new ReverseStringComparator());

	/**
	 * Instantiates a new parameter.
	 * 
	 * @param name
	 *            the parameter name
	 * @param dataSheet 
	 * 			the data sheet
	 */
	public Parameter(String name, DataSheet dataSheet) {
		this.name = name;
		this.dataSheet = dataSheet;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
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
	public boolean isMixed() {
		return (this.atLeastOneNonNumeric && this.atLeastOneNumeric);
	}

	/**
	 * Finds the maximum value for this parameter in the datasheet.
	 * 
	 * @return the maximum value
	 */
	public double getMaxValue() {
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			double value = dataSheet.getDesign(i).getDoubleValue(this);
			if (value > max)
				max = value;
		}
		return max;
	}

	/**
	 * Finds the minimum value for this parameter in the datasheet.
	 * 
	 * @return the minimum value
	 */
	public double getMinValue() {
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < dataSheet.getDesignCount(); i++) {
			double value = dataSheet.getDesign(i).getDoubleValue(this);
			if (value < min)
				min = value;
		}
		return min;
	}

	/**
	 * Returns whether the parameter is numeric or discrete from the stored
	 * setting.
	 * 
	 * @return true, if the parameter is numeric
	 */
	public boolean isNumeric() {
		return numeric;
	}

	/**
	 * Checks whether the parameter is numeric or discrete by rechecking all
	 * designs.
	 * 
	 * @return true, if the parameter is numeric
	 */
	public boolean checkIfNumeric() {
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

	/**
	 * Specifies whether the parameter is numeric or dicrete. If a parameter is
	 * switched to non-numeric for the first time all existing designs are
	 * checked to identify all discrete levels.
	 * 
	 * @param numeric
	 *            specifies if the parameter is numeric
	 */
	public void setNumeric(boolean numeric) {

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
					log("getDoubleValueOf: string " + string + " not found, adding it to tree set ");
					this.discreteLevels.add(string);
				}
			}
		}
		this.numeric = numeric;
	}

	/**
	 * @param atLeastOneNumeric
	 *            specifies whether there is at least one design with a numeric
	 *            value for this design
	 */
	public void setAtLeastOneNumeric(boolean atLeastOneNumeric) {
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
	public double getDoubleValueOf(String string) {
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
		log("getDoubleValueOf: checking index of string " + string);
		while (it.hasNext()) {
			if (string.equalsIgnoreCase(it.next())) {
				return (double) index;
			}
			index++;
		}

		// String not found, add it to discrete levels
		log("getDoubleValueOf: string " + string + " not found, adding it to tree set ");
		this.discreteLevels.add(string);
		index = 0;
		it = discreteLevels.iterator();
		log("getDoubleValueOf: re-checking index of string " + string);
		while (it.hasNext()) {
			String next = it.next();
			log("getDoubleValueOf: comparing string " + string + " to string " + next);
			if (string.equalsIgnoreCase(next)) {
				log("getDoubleValueOf: match found, returning index " + index);
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
			log("getStringValueOf: Parameter " + this.name + " is numeric. Returning " + (Double.toString(value)));
			return Double.toString(value);
		} else {
			log("getStringValueOf: Parameter " + this.name + " is not numeric. ");
			log("value = " + value);
			int index = (int) value;
			log("getStringValueOf: index is " + index);
			int currentIndex = 0;
			Iterator<String> it = discreteLevels.iterator();
			while (it.hasNext()) {
				log("getStringValueOf: checking currentIndex " + currentIndex + " against index " + index);
				String next = it.next();
				if (currentIndex == index) {
					log("getStringValueOf: check positive. Returning string " + next);
					return next;
				}
				currentIndex++;
			}
			throw new CorruptDataException(this);
		}
	}

	/**
	 * Gets the discrete level count in the TreeSet that stores all discrete
	 * levels.
	 * <p>
	 * Only applies to non-numeric parameters.
	 * 
	 * @return the discrete level count
	 */
	public int getDiscreteLevelCount() {
		if (this.isNumeric()) {
			throw new RuntimeException("Parameter " + this.name + " is numeric!");
		} else {
			// log("getDiscreteLevelCount returning size "+this.discreteLevels.size());
			return this.discreteLevels.size();
		}
	}

	/**
	 * Checks the count of designs for which this parameter is on the discrete
	 * level defined by the given string argument. If the count is zero, the
	 * discrete level is removed.
	 * <p>
	 * Only applies to non-numeric parameters.
	 * 
	 * @param stringValueToCheck
	 *            the discrete level for which the occurrence count should be
	 *            returned
	 */
	public void checkOccurrenceInDiscreteLevel(String stringValueToCheck) {
		if (this.isNumeric()) {
			throw new RuntimeException("Parameter " + this.name + " is numeric!");
		} else {
			int occurrenceCount = 0;
			for (int i = 0; i < this.dataSheet.getDesignCount(); i++) {
				if (this.dataSheet.getDesign(i).getStringValue(this).equalsIgnoreCase(stringValueToCheck)) {
					occurrenceCount++;
				}
			}
			if (occurrenceCount < 1) {
				this.discreteLevels.remove(stringValueToCheck);
			}
		}
	}

	/**
	 * Reset discrete levels to an empty TreeSet.
	 */
	public void resetDiscreteLevelsAndState() {
		log("resetDiscreteLevels called");
		this.discreteLevels = new TreeSet<String>(new ReverseStringComparator());
		this.numeric = true;
		this.atLeastOneNonNumeric = false;
		this.atLeastOneNumeric = false;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (Parameter.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Comparator for the discrete levels TreeSet to sort the data
	 * alphabetically.
	 */
	class ReverseStringComparator implements Comparator<String>, Serializable {

		/** The version tracking unique identifier for Serialization. */
		static final long serialVersionUID = 0000;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(String s1, String s2) {
			return (s2.compareToIgnoreCase(s1));
		}
	}

	/**
	 * Gets the longest tic label string.
	 * 
	 * @param fm
	 *            the FontMetrics for which the string width should be
	 *            calculated
	 * @param numberFormat 
	 * 			the format being used
	 * @return the string's length
	 */
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
			log("paintComponent: minLength " + minLength);
			log("paintComponent: maxLength " + maxLength);
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
}
