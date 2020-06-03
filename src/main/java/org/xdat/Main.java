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

import org.xdat.actionListeners.scatter2DChartSettings.ParallelChartFrameComboModel;
import org.xdat.chart.Chart;
import org.xdat.chart.ParallelCoordinatesChart;
import org.xdat.data.ClusterFactory;
import org.xdat.data.ClusterSet;
import org.xdat.data.DataSheet;
import org.xdat.data.DatasheetListener;
import org.xdat.exceptions.NoParametersDefinedException;
import org.xdat.gui.WindowClosingAdapter;
import org.xdat.gui.frames.ChartFrame;
import org.xdat.gui.menus.mainWindow.MainMenuBar;
import org.xdat.gui.panels.DataSheetTablePanel;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsGroup;
import org.xdat.settings.SettingsGroupFactory;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

public class Main extends JFrame {
	public static final long serialVersionUID = 10L;
	private MainMenuBar mainMenuBar;
	private Session currentSession;
	private List<ChartFrame> chartFrames = new LinkedList<>();
	private final BuildProperties buildProperties;
	private final ClusterFactory clusterFactory = new ClusterFactory();
	private final DatasheetListener datasheetListener;
	private final List<DatasheetListener> subListeners = new CopyOnWriteArrayList<>();
	private DataSheetTablePanel dataSheetTablePanel;
	private final List<ParallelChartFrameComboModel> comboModels = new LinkedList<>();
	private final SettingsGroup generalSettingsGroup;
	private final SettingsGroup parallelCoordinatesAxisSettingsGroup;

	private static final List<String> LOOK_AND_FEEL_ORDER_OF_PREF = Arrays.asList(
			"com.sun.java.swing.plaf.gtk.GTKLookAndFeel",
			"com.sun.java.swing.plaf.windows.WindowsLookAndFeel",
			"javax.swing.plaf.nimbus.NimbusLookAndFeel"
	);

	public Main() {
		super("xdat   -   Untitled");
		generalSettingsGroup = SettingsGroupFactory.buildGeneralParallelCoordinatesChartSettingsGroup();
		for (Setting<?> value : generalSettingsGroup.getSettings().values()) {
			value.addListener((source, transaction) -> source.setCurrentToDefault());
		}
		parallelCoordinatesAxisSettingsGroup = SettingsGroupFactory.buildParallelCoordinatesChartAxisSettingsGroup();
		for (Setting<?> value : parallelCoordinatesAxisSettingsGroup.getSettings().values()) {
			value.addListener((source, transaction) -> source.setCurrentToDefault());
		}

		this.buildProperties = new BuildProperties();
		this.datasheetListener = new DatasheetListener() {
			@Override
			public void onClustersChanged() {
				repaintAllChartFrames();
				fireSubListeners(DatasheetListener::onClustersChanged);
			}

			@Override
			public void onDataPanelUpdateRequired() {
				updateDataPanel();
				fireSubListeners(DatasheetListener::onDataPanelUpdateRequired);
			}

			@Override
			public void onDataChanged(boolean[] autoFitRequired, boolean[] filterResetRequired, boolean[] applyFiltersRequired) {
				final ProgressMonitor progressMonitor = new ProgressMonitor(Main.this, "", "Rebuilding charts", 0, getDataSheet().getParameterCount() - 1);
				progressMonitor.setMillisToPopup(0);

				for (int i = 0; i < getDataSheet().getParameterCount(); i++) {
					final int progress = i;
					if (progressMonitor.isCanceled()) {
						break;
					}
					SwingUtilities.invokeLater(() ->
							progressMonitor.setProgress(progress));
					if (autoFitRequired[i]) {
						autofitAxisAllChartFrames(i);
					}
					if (filterResetRequired[i]) {
						resetFiltersOnAxisAllChartFrames(i);
					}
					if (applyFiltersRequired[i]) {
						refilterAllChartFrames(i);
					}
				}

				progressMonitor.close();

				repaintAllChartFrames();

				fireSubListeners(l -> l.onDataChanged(autoFitRequired, filterResetRequired, applyFiltersRequired));
			}
		};
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
		DataSheet currentDataSheet = this.currentSession.getCurrentDataSheet();
		if(currentDataSheet != null) {
			currentDataSheet.removeListener(this.datasheetListener);
		}
		this.currentSession.setCurrentDataSheet(dataSheet);
		if (dataSheet == null) {
			this.remove(this.dataSheetTablePanel);
		} else {
			dataSheet.addListener(this.datasheetListener);
		}
		this.initialiseDataPanel();
		this.repaint();
		this.rebuildAllChartFrames();
	}

	public ClusterSet getCurrentClusterSet() {
        return this.currentSession.getCurrentClusterSet();
    }

    public void setCurrentClusterSet(ClusterSet clusterSet) {
        this.currentSession.setCurrentClusterSet(clusterSet);
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

	public void removeParameter(String paramName) {
		getCurrentSession().removeParameter(paramName);
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
		this.chartFrames.clear();
		this.currentSession.clearAllCharts();
	}

	private void rebuildAllChartFrames() {
		SwingUtilities.invokeLater(() -> {
			List<Chart> chartsSnapshot = new ArrayList<>(currentSession.getCharts());
			disposeAllChartFrames();
			for (Chart chart : chartsSnapshot) {
				try {
					ChartFrame newFrame = new ChartFrame(Main.this, chart);
					chartFrames.add(newFrame);
					currentSession.addChart(chart);
				} catch (NoParametersDefinedException e) {
					JOptionPane.showMessageDialog(Main.this, "Cannot create chart when no parameters are defined.", "No parameters defined!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	public void repaintAllChartFrames() {
		repaintAllChartFrames(new ArrayList<>());
	}

	private void repaintAllChartFrames(final List<ChartFrame> exclusionList) {
		SwingUtilities.invokeLater(() -> {
			for (ChartFrame cf : chartFrames) {
				if (!exclusionList.contains(cf)) {
					cf.repaint();
				}
			}
		});
	}

	public void refilterAllChartFrames(int columnIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(columnIndex).applyFilters(getDataSheet());
			}
		}
	}

	public void autofitAxisAllChartFrames(int axisIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).autofit(getDataSheet());
			}
		}
	}

	public void resetFiltersOnAxisAllChartFrames(int axisIndex) {
		for (int i = 0; i < this.chartFrames.size(); i++) {
			Chart c = this.chartFrames.get(i).getChart();
			if (c.getClass().equals(ParallelCoordinatesChart.class)) {
				((ParallelCoordinatesChart) c).getAxis(axisIndex).resetFilters(getDataSheet());
			}
		}
	}

	public void loadSession(String pathToFile) {
		try {
			this.disposeAllChartFrames();
			this.currentSession = Session.readFromFile(pathToFile);

			this.setTitle("xdat   -   " + pathToFile);

			for (Chart chart : getCurrentSession().getCharts()) {
				try {
					new ChartFrame(this, chart);
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
			JOptionPane.showMessageDialog(this, "Error on loading session: " + e.getMessage(), "Load Session", JOptionPane.ERROR_MESSAGE);
		}

	}

	public void saveSessionAs(String pathToFile) {
		try {
			this.currentSession.saveToFile(pathToFile);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "IOException on saving session: " + e.getMessage(), "Save Session", JOptionPane.ERROR_MESSAGE);
		}
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

	public ClusterFactory getClusterFactory() {
		return clusterFactory;
	}

	public SettingsGroup getGeneralSettingsGroup() {
		return generalSettingsGroup;
	}

	public SettingsGroup getParallelCoordinatesAxisSettingsGroup() {
		return parallelCoordinatesAxisSettingsGroup;
	}

	public void addDataSheetSubListener(DatasheetListener listener) {
		this.subListeners.add(listener);
	}

	public void removeDataSheetSubListener(DatasheetListener listener) {
		this.subListeners.remove(listener);
	}

	private void fireSubListeners(Consumer<DatasheetListener> action) {
		for (DatasheetListener l : this.subListeners) {
			action.accept(l);
		}
	}

	public void fireClustersChanged() {
		this.datasheetListener.onClustersChanged();
	}

	public void fireDataPanelUpdateRequired() {
		this.datasheetListener.onDataPanelUpdateRequired();
	}

	public void fireOnDataChanged(boolean[] axisAutofitRequired, boolean[] axisResetFilterRequired, boolean[] axisApplyFiltersRequired) {
		this.datasheetListener.onDataChanged(axisAutofitRequired, axisResetFilterRequired, axisApplyFiltersRequired);
	}
}
