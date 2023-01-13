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

import org.jetbrains.annotations.Nullable;
import org.xdat.chart.Chart;
import org.xdat.data.ClusterSet;
import org.xdat.data.DataSheet;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

	static final long serialVersionUID = 5L;
	public static final String sessionFileExtension = ".ses";
	private String sessionName = "Untitled";
	private String sessionDirectory;

	@Nullable
	private DataSheet currentDataSheet;
	private ClusterSet currentClusterSet;
	private final List<Chart> charts = new LinkedList<>();

	public Session() {
		this.currentClusterSet = new ClusterSet();
	}

	public void saveToFile(String pathToFile) throws IOException {
		FileOutputStream fs = new FileOutputStream(pathToFile);
		ObjectOutputStream os = new ObjectOutputStream(fs);
		os.writeObject(this);
		os.close();
	}

	public static Session readFromFile(String pathToFile) throws IOException, ClassNotFoundException {
		FileInputStream fs = new FileInputStream(pathToFile);
		ObjectInputStream is = new ObjectInputStream(fs);
		Session readSession = (Session) is.readObject();
		readSession.initTransientData();
		is.close();
		return readSession;

	}

	private void initTransientData() {
		DataSheet ds = this.currentDataSheet;
		if (ds != null){
			ds.initTransientData();
		}
		this.currentClusterSet.initTransientData();
		this.charts.forEach(Chart::initTransientData);
	}

	public Collection<Chart> getCharts() {
		return Collections.unmodifiableList(this.charts);
	}

	public void addChart(Chart chart) {
		this.charts.add(chart);
	}

	public void removeChart(Chart chart) {
		this.charts.remove(chart);
	}

	public void clearAllCharts() {
		this.charts.clear();
	}

	public void removeParameter(String paramName) {
		DataSheet dataSheet = currentDataSheet;
		if (dataSheet != null) {
			dataSheet.removeParameter(paramName);
		}
	}

	@Nullable
	public DataSheet getCurrentDataSheet() {
		return currentDataSheet;
	}

	public void setCurrentDataSheet(@Nullable DataSheet currentDataSheet) {
		this.currentDataSheet = currentDataSheet;
	}

	public ClusterSet getCurrentClusterSet() {
		return currentClusterSet;
	}

	public void setCurrentClusterSet(ClusterSet currentClusterSet) {
		this.currentClusterSet = currentClusterSet;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionDirectory() {
		return sessionDirectory;
	}

	public void setSessionDirectory(String sessionDirectory) {
		this.sessionDirectory = sessionDirectory;
	}
}
