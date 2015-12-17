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

package org.xdat;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Contains generic functions that can be used by other classes.
 */
public class GFuncs {

	/**
	 * Generates a random Integer between 0 and max, using seed seed
	 * 
	 * @param max
	 *            the maximum value for the random
	 * @param seed
	 *            the seed
	 * @return the random Integer
	 */
	public static int randomNr(int max, int seed) { // creates a random number
													// between 1 and max
		byte[] value = new byte[1];
		Random r = new Random();
		value[0] = (byte) (seed + r.nextInt());
		SecureRandom rs = new SecureRandom(value);
		int result;
		try {
			result = 1 + Math.abs(rs.nextInt()) % max;
		} catch (ArithmeticException e) {
			System.out.println("Misc_randNr: !!!WARNING!!!: " + e.toString() + " Returning 1 instead of random number. SecureRandom result rs was: " + rs.nextInt());
			result = 1;
		}
		return result;
	}

	/**
	 * Waits for a user-specified number of milliseconds.
	 * 
	 * @param ms
	 *            the number of milliseconds to wait
	 */
	public static void wait_ms(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
}
