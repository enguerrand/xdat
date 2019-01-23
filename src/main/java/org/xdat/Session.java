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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import org.xdat.chart.Chart;
import org.xdat.data.DataSheet;

/**
 * Stores all relevant information of a session.
 * <p>
 * References to the {@link org.xdat.data.DataSheet} and all active
 * {@link org.xdat.chart.ParallelCoordinatesChart}s are stored in session class
 * instances. When the user saves his Session, this information is serialized
 * and can be retrieved at a later time.
 * <p>
 * Care has been taken to avoid serializing swing objects. Therefore, no such
 * objects should be referenced from within this class, only the information
 * needed to reconstruct the chart frames.
 */
public class Session implements Serializable {

	/** The version tracking unique identifier for Serialization. */
	static final long serialVersionUID = 0004;

	/** Flag to enable debug message printing for this class. */
	static final boolean printLog = false;

	/** The when storing session files this file extension will be used. */
	public static final String sessionFileExtension = ".ses";

	/** The session name. */
	private String sessionName = "Untitled";

	/** The directory where the session was saved. */
	private String sessionDirectory;

	/** The current data sheet. */
	private DataSheet currentDataSheet;

	/** All active charts. */
	private Vector<Chart> charts = new Vector<Chart>(0, 1);

	/**
	 * Instantiates a new session.
	 */
	public Session() {
		log("constructor called.");

	}

	/**
	 * Serializes the session to a file.
	 * 
	 * @param pathToFile
	 *            the path to file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void saveToFile(String pathToFile) throws IOException {
		FileOutputStream fs = new FileOutputStream(pathToFile);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(this);
		os.close();
	}

	/**
	 * Read from file.
	 * 
	 * @param mainWindow
	 *            the main window
	 * @param pathToFile
	 *            the path where the file is saved
	 * @return the session instance
	 * @throws InvalidClassException
	 *             thrown when the selected file is not a valid serialized
	 *             session
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             thrown when the class was not found.
	 */
	public static Session readFromFile(Main mainWindow, String pathToFile) throws java.io.InvalidClassException, java.io.IOException, ClassNotFoundException {
		FileInputStream fs = new FileInputStream(pathToFile);
		ObjectInputStream is = new ObjectInputStream(fs);
		Session readSession = (Session) is.readObject();
		is.close();
		return readSession;

	}

	/**
	 * Gets a chart from this session.
	 * 
	 * @param index
	 *            the index of the chart
	 * @return the chart
	 */
	public Chart getChart(int index) {
		return this.charts.get(index);
	}

	/**
	 * Gets the number of charts
	 * 
	 * @return the chart count
	 */
	public int getChartCount() {
		return this.charts.size();
	}

	/**
	 * Adds a chart to the session.
	 * 
	 * @param chart
	 *            the chart
	 */
	public void addChart(Chart chart) {
		this.charts.add(chart);
	}

	/**
	 * Removes a chart from the session.
	 * 
	 * @param chart
	 *            the chart
	 * @return true, if chart was successfully removed
	 */
	public boolean removeChart(Chart chart) {
		return this.charts.remove(chart);
	}

	/**
	 * Clear all charts.
	 */
	public void clearAllCharts() {
		this.charts.removeAllElements();
	}

	/**
	 * Gets the current data sheet.
	 * 
	 * @return the current data sheet
	 */
	public DataSheet getCurrentDataSheet() {
		return currentDataSheet;
	}

	/**
	 * Sets the current data sheet.
	 * 
	 * @param currentDataSheet
	 *            the new current data sheet
	 */
	public void setCurrentDataSheet(DataSheet currentDataSheet) {
		this.currentDataSheet = currentDataSheet;
	}

	/**
	 * Gets the session name.
	 * 
	 * @return the session name
	 */
	public String getSessionName() {
		return sessionName;
	}

	/**
	 * Sets the session name.
	 * 
	 * @param sessionName
	 *            the new session name
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	/**
	 * Gets the directory where the session was saved.
	 * 
	 * @return the session directory
	 */
	public String getSessionDirectory() {
		return sessionDirectory;
	}

	/**
	 * Sets the directory where the session is saved.
	 * 
	 * @param sessionDirectory
	 *            the new session directory
	 */
	public void setSessionDirectory(String sessionDirectory) {
		this.sessionDirectory = sessionDirectory;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (Session.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

}
