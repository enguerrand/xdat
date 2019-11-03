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
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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
	public static final long serialVersionUID = 10L;
	public static final boolean printLog = false;
	private MainMenuBar mainMenuBar;
	private transient DataSheetTablePanel dataSheetTablePanel;
	private Session currentSession;
	private Vector<ChartFrame> chartFrames = new Vector<ChartFrame>(0, 1);
	private transient Vector<ListDataListener> listDataListener = new Vector<ListDataListener>();
	private transient Vector<ParallelChartFrameComboModel> comboModels = new Vector<ParallelChartFrameComboModel>(0);
	private final BuildProperties buildProperties;

	private static final List<String> LOOK_AND_FEEL_ORDER_OF_PREF = Arrays.asList(
			"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
			"javax.swing.plaf.nimbus.NimbusLookAndFeel"
	);

	public Main() {
		super("xdat   -   Untitled");
		this.buildProperties = new BuildProperties();
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

	public void initialiseDataPanel() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				if (dataSheetTablePanel == null) {
					dataSheetTablePanel = new DataSheetTablePanel(Main.this);
					setContentPane(dataSheetTablePanel);
					// this.repaint();
					dataSheetTablePanel.revalidate();
				} else if (currentSession.getCurrentDataSheet() != null) {
					dataSheetTablePanel.initialiseDataSheetTableModel();
					dataSheetTablePanel.revalidate();
				} else {
					setContentPane(new JPanel());
					repaint();
				}
			}
		});
	}

	public void updateDataPanel() {
		SwingUtilities.invokeLater(() -> {
			if (dataSheetTablePanel == null) {
				dataSheetTablePanel = new DataSheetTablePanel(Main.this);
				setContentPane(dataSheetTablePanel);
				dataSheetTablePanel.revalidate();
			} else if (currentSession.getCurrentDataSheet() != null) {
				dataSheetTablePanel.updateRunsTableModel();
				dataSheetTablePanel.revalidate();
			} else {
				setContentPane(new JPanel());
				repaint();
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
		setLookAndFeel();
		new Main();
	}

	private static void setLookAndFeel() {
		for (String lnf : LOOK_AND_FEEL_ORDER_OF_PREF) {
			try {
				UIManager.setLookAndFeel(lnf);
				break;
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
			}
		}
	}

	public DataSheet getDataSheet() {
		return this.currentSession.getCurrentDataSheet();
	}

	public void setDataSheet(DataSheet dataSheet) {
		this.currentSession.setCurrentDataSheet(dataSheet);
		if (dataSheet == null) {
			this.remove(this.dataSheetTablePanel);
		}
		this.initialiseDataPanel();
		this.repaint();
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public void addChartFrame(ChartFrame chartFrame) {
		this.chartFrames.add(chartFrame);
		this.addChartToComboboxes(chartFrame);
	}

	public void removeChartFrame(ChartFrame chartFrame) {
		this.chartFrames.remove(chartFrame);
		this.currentSession.removeChart(chartFrame.getChart());
		this.removeChartFromComboboxes(chartFrame);
	}

	public ChartFrame getChartFrame(int index) {
		return this.chartFrames.get(index);
	}

	public ChartFrame getChartFrame(String title) {
		for (int i = 0; i < this.getChartFrameCount(); i++) {
			if (title.equals(this.getChartFrame(i).getTitle()))
				return this.getChartFrame(i);
		}
		throw new RuntimeException("No chart found with title " + title);
	}

	public int getChartFrameCount() {
		return this.chartFrames.size();
	}

	public int getUniqueChartId(Class chartClass) {
		int id = 0;
		for (int i = 0; i < this.getChartFrameCount(); i++) {

			if (chartClass.equals(this.chartFrames.get(i).getChart().getClass())) {
				id = this.chartFrames.get(i).getChart().getID();
			}
		}
		return ++id;
	}

	public void disposeAllChartFrames() {
		for (int i = this.chartFrames.size() - 1; i >= 0; i--) {
			this.removeChartFromComboboxes(this.chartFrames.get(i));
			this.chartFrames.get(i).dispose();
		}
		this.chartFrames.removeAllElements();
		this.currentSession.clearAllCharts();
	}

	public void rebuildAllChartFrames() {
		SwingUtilities.invokeLater(() -> {
			Chart[] charts = new Chart[currentSession.getChartCount()];
			for (int i = 0; i < charts.length; i++) {
				charts[i] = (currentSession.getChart(i));
			}
			disposeAllChartFrames();
			for (int i = 0; i < charts.length; i++) {

				try {
					ChartFrame newFrame = new ChartFrame(Main.this, charts[i]);
					chartFrames.add(newFrame);
					currentSession.addChart(charts[i]);
				} catch (NoParametersDefinedException e) {
					JOptionPane.showMessageDialog(Main.this, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void repaintAllChartFrames() {
		repaintAllChartFrames(new ArrayList<ChartFrame>());
	}

	private void repaintAllChartFrames(final List<ChartFrame> exclusionList) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				for (ChartFrame cf : chartFrames) {
					if (!exclusionList.contains(cf)) {
						cf.repaint();
					}
				}
			}
		});
	}

	public void refilterAllChartFrames(int columnIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(columnIndex).applyFilters();
			}
		}
	}

	public void autofitAxisAllChartFrames(int axisIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).autofit();
			}
		}
	}

	public void resetFiltersOnAxisAllChartFrames(int axisIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).resetFilters();
			}
		}
	}

	public void loadSession(String pathToFile) {
		try {
			this.disposeAllChartFrames();
			this.currentSession = Session.readFromFile(this, pathToFile);

			this.setTitle("xdat   -   " + pathToFile);

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

		} catch (InvalidClassException | ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, "The file " + pathToFile + " is not a proper xdat version " + buildProperties.getVersion() + " Session file", "Load Session", JOptionPane.OK_OPTION);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error on loading session: " + e.getMessage(), "Load Session", JOptionPane.OK_OPTION);
		}

	}

	public void saveSessionAs(String pathToFile) {
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

	public static boolean isLoggingEnabled() {
		return false;
	}

	public MainMenuBar getMainMenuBar() {
		return mainMenuBar;
	}

	public DataSheetTablePanel getDataSheetTablePanel() {
		return dataSheetTablePanel;
	}

	public void addChartToComboboxes(ChartFrame chartFrame) {
		for (int i = 0; i < this.comboModels.size(); i++) {
			if (chartFrame.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
				comboModels.get(i).addElement(chartFrame.getChart().getTitle());
			}
		}
	}

	public void removeChartFromComboboxes(ChartFrame chartFrame) {
		for (int i = 0; i < this.comboModels.size(); i++) {
			if (chartFrame.getChart().getClass().equals(ParallelCoordinatesChart.class)) {
				comboModels.get(i).removeElement(chartFrame.getChart().getTitle());
			}
		}
	}

	public void registerComboModel(ParallelChartFrameComboModel comboModel) {
		this.comboModels.add(comboModel);
	}

	public void unRegisterComboModel(ParallelChartFrameComboModel comboModel) {
		this.comboModels.remove(comboModel);
	}
}
