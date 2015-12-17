package org.xdat;

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

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListDataListener;

import org.xdat.actionListeners.scatter2DChartSettings.ParallelChartFrameComboModel;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.DataSheet;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.dialogs.LicenseDisplayDialog;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.mainWIndow.MainMenuBar;
import org.xdat.gui.panels.DataSheetTablePanel;

/**
 * The Main Class from which the program is started.
 * <p>
 * Is also used to store some global references that are needed by other
 * classes, such as for example references to Swing components. Most of the data
 * is stored in the {@link Session} and the {@link UserPreferences} classes
 * though. References to instances of both classes are kept in this class.
 */
public class Main extends JFrame {
	/** The version tracking unique identifier for Serialization. */
	public static final long serialVersionUID = 9;

	/** The release number used in the help -> about dialog. */
	public static final String XDAT_VERSION = "2.2";

	/**
	 * Flag to enable debug message printing with the log method for all
	 * classes.
	 */
	public static final boolean loggingEnabled = false;

	/** Flag to enable debug message printing for this class. */
	public static final boolean printLog = false;

	/** The main menu bar. */
	private MainMenuBar mainMenuBar;

	/** The panel that contains the data. */
	private transient DataSheetTablePanel dataSheetTablePanel;

	/**
	 * The current session containing all relevant info in the memory. This is
	 * also the information that gets serialized when saving a session.
	 */
	private Session currentSession;

	/** A reference to all active chart frames. */
	private Vector<ChartFrame> chartFrames = new Vector<ChartFrame>(0, 1);

	/**
	 * List Model Listeners to enable updating the GUI when chart Frames are
	 * opened or closed.
	 */
	private transient Vector<ListDataListener> listDataListener = new Vector<ListDataListener>();

	/** Combobox models that require update when chart frame list changes. */
	private transient Vector<ParallelChartFrameComboModel> comboModels = new Vector<ParallelChartFrameComboModel>(0);

	/**
	 * Instantiates a new main.
	 */
	public Main() {
		super("xdat   -   Untitled");
		if (!this.checkLicense()) {
			this.dispose();
			return;
		}
		this.currentSession = new Session();
		this.addWindowListener(new WindowClosingAdapter(true));
		this.mainMenuBar = new MainMenuBar(this);
		this.setJMenuBar(this.mainMenuBar);
		ImageIcon img = new ImageIcon(Main.class.getClassLoader().getResource("org/xdat/images/icon.png"));
		this.setIconImage(img.getImage());
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Dimension screenSize;
		try {
			screenSize = new Dimension(ge.getScreenDevices()[0].getDisplayMode().getWidth(), ge.getScreenDevices()[0].getDisplayMode().getHeight());
		} catch (Exception e) {
			try {
				screenSize = getToolkit().getScreenSize();
			} catch (HeadlessException e1) {
				screenSize = new Dimension(1200, 800);
			}
		}
		setLocation((int) (0.25 * screenSize.width), (int) (0.25 * screenSize.height));
		setSize((int) (0.5 * screenSize.width), (int) (0.5 * screenSize.height));
		setVisible(true);

	}

	/**
	 * To be called when a new datasheet is loaded into the panel to update the
	 * GUI.
	 */
	public void initialiseDataPanel() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				log("updateDataPanel called");
				if (dataSheetTablePanel == null) {
					log("updateDataPanel: dataSheetTablePanel null");
					dataSheetTablePanel = new DataSheetTablePanel(Main.this);
					setContentPane(dataSheetTablePanel);
					// this.repaint();
					dataSheetTablePanel.revalidate();
				} else if (currentSession.getCurrentDataSheet() != null) {
					log("updateDataPanel: DataSheet non-null");
					dataSheetTablePanel.initialiseDataSheetTableModel();
					dataSheetTablePanel.revalidate();
				} else {
					setContentPane(new JPanel());
					repaint();
				}
			}
		});
	}

	/**
	 * To be called when the data in the panel has changed to update the GUI.
	 */
	public void updateDataPanel() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
		log("updateDataPanel called");
		if (dataSheetTablePanel == null) {
			log("updateDataPanel: dataSheetTablePanel null");
			dataSheetTablePanel = new DataSheetTablePanel(Main.this);
			setContentPane(dataSheetTablePanel);
			// this.repaint();
			dataSheetTablePanel.revalidate();
		} else if (currentSession.getCurrentDataSheet() != null) {
			log("updateDataPanel: DataSheet non-null");
			dataSheetTablePanel.updateRunsTableModel();
			dataSheetTablePanel.revalidate();
		} else {
			setContentPane(new JPanel());
			repaint();
		}
	}
		});
	}

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the command line arguments (not used)
	 */
	public static void main(String[] args) {
		new Main();
	}

	/**
	 * Gets the data sheet.
	 * 
	 * @return the data sheet
	 */
	public DataSheet getDataSheet() {
		return this.currentSession.getCurrentDataSheet();
	}

	/**
	 * Sets the data sheet.
	 * 
	 * @param dataSheet
	 *            the new data sheet
	 */
	public void setDataSheet(DataSheet dataSheet) {
		this.currentSession.setCurrentDataSheet(dataSheet);
		if (dataSheet == null) {
			this.remove(this.dataSheetTablePanel);
		}
		this.initialiseDataPanel();
		this.repaint();
	}

	/**
	 * Gets the version string to be shown in the help->about dialog.
	 * 
	 * @return the version string
	 */
	public static String getVersionString() {
		return XDAT_VERSION;
	}

	/**
	 * Gets the current session.
	 * 
	 * @return the current session
	 */
	public Session getCurrentSession() {
		return currentSession;
	}

	/**
	 * Sets the current session.
	 * 
	 * @param currentSession
	 *            the new current session
	 */
	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	/**
	 * Adds a chart frame to the Vector with references to all chart frames.
	 * 
	 * @param chartFrame
	 *            the chart frame
	 */
	public void addChartFrame(ChartFrame chartFrame) {
		this.chartFrames.add(chartFrame);
		this.addChartToComboboxes(chartFrame);
	}

	/**
	 * Removes a chart frame from the Vector with references to all chart
	 * frames.
	 * 
	 * @param chartFrame
	 *            the chart frame
	 */
	public void removeChartFrame(ChartFrame chartFrame) {
		this.chartFrames.remove(chartFrame);
		this.currentSession.removeChart(chartFrame.getChart());
		this.removeChartFromComboboxes(chartFrame);
	}

	/**
	 * Gets a chart frame to the Vector with references to all chart frames.
	 * 
	 * @param index
	 *            the index
	 * @return the chart frame
	 */
	public ChartFrame getChartFrame(int index) {
		return this.chartFrames.get(index);
	}

	/**
	 * Gets a chart frame to the Vector with references to all chart frames by
	 * chart title.
	 * 
	 * @param title
	 *            the title
	 * @return the chart frame
	 */
	public ChartFrame getChartFrame(String title) {
		for (int i = 0; i < this.getChartFrameCount(); i++) {
			if (title.equals(this.getChartFrame(i).getTitle()))
				return this.getChartFrame(i);
		}
		throw new RuntimeException("No chart found with title " + title);
	}

	/**
	 * Gets the chart frame count.
	 * 
	 * @return the number of active chart frames
	 */
	public int getChartFrameCount() {
		return this.chartFrames.size();
	}

	/**
	 * Gets a unique id for the next chart
	 * @param chartClass 
	 * 				the chart's class
	 * 
	 * @return a unique id for the next chart
	 */
	public int getUniqueChartId(Class chartClass) {
		int id = 0;
		for (int i = 0; i < this.getChartFrameCount(); i++) {

			if (chartClass.equals(this.chartFrames.get(i).getChart().getClass())) {
				id = this.chartFrames.get(i).getChart().getID();
			}
		}
		return ++id;
	}

	/**
	 * Dispose all chart frames.
	 */
	public void disposeAllChartFrames() {
		for (int i = this.chartFrames.size() - 1; i >= 0; i--) {
			this.removeChartFromComboboxes(this.chartFrames.get(i));
			this.chartFrames.get(i).dispose();
		}
		this.chartFrames.removeAllElements();
		this.currentSession.clearAllCharts();
	}

	/**
	 * Rebuild all chart frames when the data has changed.
	 */
	public void rebuildAllChartFrames() {
		log("rebuildAllChartFrames:------------------------------------- ");
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Chart[] charts = new Chart[currentSession.getChartCount()];
				log("rebuildAllChartFrames: currently " + charts.length + " charts active. ");
				for (int i = 0; i < charts.length; i++) {
					log("rebuildAllChartFrames: reading chart " + i);
					charts[i] = (currentSession.getChart(i));
				}
				disposeAllChartFrames();
				log("rebuildAllChartFrames: still " + charts.length + " charts active. ");
				for (int i = 0; i < charts.length; i++) {
					log("rebuildAllChartFrames: creating chart " + i);

					try {
						ChartFrame newFrame = new ChartFrame(Main.this, charts[i]);
						chartFrames.add(newFrame);
						currentSession.addChart(charts[i]);
					} catch (NoParametersDefinedException e) {
						JOptionPane.showMessageDialog(Main.this, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
					}
				}
				log("rebuildAllChartFrames: done. Session has now " + currentSession.getChartCount() + " active charts.");
			}
		});
	}

	/**
	 * Repaint all chart frames when the data has changed.
	 */
	public void repaintAllChartFrames() {
		repaintAllChartFrames(new ArrayList<ChartFrame>());
	}

	/**
	 * Repaint all chart frames when the data has changed, but exclude some
	 * @param exclusionList the charts not to be repainted
	 */
	public void repaintAllChartFrames(final List<ChartFrame> exclusionList) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				log("repaintAllChartFrames:------------------------------------- ");
				for (ChartFrame cf : chartFrames) {
					if (!exclusionList.contains(cf)) {
						cf.repaint();
					}
				}
			}
		});
	}

	/**
	 * Reapply filters for a given column.
	 * 
	 * @param columnIndex
	 *            the index of the column for which to reapply all filters
	 */
	public void refilterAllChartFrames(int columnIndex) {
		log("refilterAllChartFrames:------------------------------------- ");
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(columnIndex).applyFilters();
			}
		}
	}

	/**
	 * Autofit a specified axis for all chart frames .
	 * 
	 * @param axisIndex
	 *            the index of the axis to autofit
	 */
	public void autofitAxisAllChartFrames(int axisIndex) {
		log("autofitAxisAllChartFrames:------------------------------------- ");
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).autofit();
			}
		}
	}

	/**
	 * Resets filters for a specified axis for all chart frames .
	 * 
	 * @param axisIndex
	 *            the index of the axis to autofit
	 */
	public void resetFiltersOnAxisAllChartFrames(int axisIndex) {
		log("resetFiltersOnAxisAllChartFrames:------------------------------------- ");
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).resetFilters();
			}
		}
	}

	/**
	 * Load session.
	 * 
	 * @param pathToFile
	 *            the path to the session file
	 */
	public void loadSession(String pathToFile) {
		try {
			this.disposeAllChartFrames();
			this.currentSession = Session.readFromFile(this, pathToFile);

			this.setTitle("xdat   -   " + pathToFile);

			log("loadSession called. " + this.getChartFrameCount() + " chart frames to load.");

			ChartFrame[] chartFrames = new ChartFrame[this.currentSession.getChartCount()];
			for (int i = 0; i < chartFrames.length; i++) {
				try {
					new ChartFrame(this, this.currentSession.getChart(i));

				} catch (NoParametersDefinedException e) {
					JOptionPane.showMessageDialog(this, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
				}
			}
			if (this.currentSession.getCurrentDataSheet() != null) {
				this.getMainMenuBar().setItemsRequiringDataSheetEnabled(true);
			} else {
				this.initialiseDataPanel();
				this.getMainMenuBar().setItemsRequiringDataSheetEnabled(false);
			}

		} catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(this, "The file " + pathToFile + " is not a proper xdat version " + Main.XDAT_VERSION + " Session file", "Load Session", JOptionPane.OK_OPTION);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, "The file " + pathToFile + " is not a proper xdat version " + Main.XDAT_VERSION + " Session file", "Load Session", JOptionPane.OK_OPTION);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error on loading session: " + e.getMessage(), "Load Session", JOptionPane.OK_OPTION);
		}

	}

	/**
	 * Save session.
	 * 
	 * @param pathToFile
	 *            the path where the session should be saved.
	 */
	public void saveSessionAs(String pathToFile) {
		for (int i = 0; i < this.currentSession.getChartCount(); i++) {
			log("saving session with chart " + i);
		}
		try {
			this.currentSession.saveToFile(pathToFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IOException on saving session: " + e.getMessage(), "Save Session", JOptionPane.OK_OPTION);
		}
	}

	private boolean checkLicense() {
		if (!UserPreferences.getInstance().isLicenseAccepted()) {
			new LicenseDisplayDialog(UserPreferences.getInstance());
		}

		return UserPreferences.getInstance().isLicenseAccepted();
	}

	/**
	 * Checks if is debug message printing is enabled.
	 * 
	 * @return true, if debug message printing is enabled
	 */
	public static boolean isLoggingEnabled() {
		return loggingEnabled;
	}

	/**
	 * Gets the main menu bar.
	 * 
	 * @return the main menu bar
	 */
	public MainMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	/**
	 * Prints debug information to stdout when printLog is set to true.
	 * 
	 * @param message
	 *            the message
	 */
	private void log(String message) {
		if (Main.printLog && Main.isLoggingEnabled()) {
			System.out.println(this.getClass().getName() + "." + message);
		}
	}

	/**
	 * Gets the Data Sheet Table Panel.
	 * 
	 * @return the Data Sheet Table Panel
	 */
	public DataSheetTablePanel getDataSheetTablePanel() {
		return dataSheetTablePanel;
	}

	/**
	 * Informs all combobox models that a new parallel chart was added.
	 * 
	 * @param chartFrame
	 *            the chart frame to be added
	 */
	public void addChartToComboboxes(ChartFrame chartFrame) {
		for (int i = 0; i < this.comboModels.size(); i++) {
			if (chartFrame.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
				comboModels.get(i).addElement(chartFrame.getChart().getTitle());
		}
	}
	}

	/**
	 * Informs all combobox models that a parallel chart was closed.
	 * 
	 * @param chartFrame
	 *            the chart frame to be removed
	 */
	public void removeChartFromComboboxes(ChartFrame chartFrame) {
		for (int i = 0; i < this.comboModels.size(); i++) {
			if (chartFrame.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
				comboModels.get(i).removeElement(chartFrame.getChart().getTitle());
		}
	}
	}

	/**
	 * Registers a comboBoxModel for update notification
	 * 
	 * @param comboModel
	 *            the model to be registered
	 */
	public void registerComboModel(ParallelChartFrameComboModel comboModel) {
		this.comboModels.add(comboModel);
	}

	/**
	 * Unregisters a comboBoxModel for update notification
	 * 
	 * @param comboModel
	 *            the model to be unregistered
	 */
	public void unRegisterComboModel(ParallelChartFrameComboModel comboModel) {
		this.comboModels.remove(comboModel);
	}
}
