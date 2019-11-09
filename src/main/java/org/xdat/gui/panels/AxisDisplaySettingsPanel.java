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

package org.xdat.gui.panels;

import org.xdat.chart.Axis;
import org.xdat.gui.UiDefines;
import org.xdat.settings.BooleanSetting;
import org.xdat.settings.Key;
import org.xdat.settings.SettingsGroup;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class AxisDisplaySettingsPanel extends JPanel {

	private TitledSubPanel contentPanel = new TitledSubPanel("");
	private JComboBox<Axis> axisChoiceCombo;
	private final Map<SettingsGroup, SettingsGroupPanel> axisSettingPanels = new HashMap<>();
	private Runnable applyAll;
	private JPanel settingsPanelWrapper = new JPanel();

	private AxisDisplaySettingsPanel() {
		this.setLayout(new BorderLayout());
		this.add(contentPanel, BorderLayout.CENTER);
		this.contentPanel.setLayout(new BorderLayout());
		this.contentPanel.add(settingsPanelWrapper, BorderLayout.CENTER);
		this.settingsPanelWrapper.setLayout(new BorderLayout());
	}

	public AxisDisplaySettingsPanel(SettingsGroup settings){
		this();
		SettingsGroupPanel p = new SettingsGroupPanel(settings);
		this.axisSettingPanels.put(settings, p);
		setCurrentSettings(settings);
		this.applyAll = p::applyAll;
	}

	public AxisDisplaySettingsPanel(List<Axis> axes){
		this();
		Vector<Axis> v = new Vector<>(0, 1);
		for (Axis axis : axes) {
			if (axis.isActive()) {
				v.add(axis);
				SettingsGroup settings = axis.getSettings();
				SettingsGroupPanel settingsGroupPanel = new SettingsGroupPanel(settings);
				getMinTextField(settingsGroupPanel);
				getMaxTextField(settingsGroupPanel);
				BooleanSetting autoFitSetting = settings.getBooleanSetting(Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS);
				autoFitSetting.addListener(src -> {
					setAxisRangeFieldsEnabled(!src.get(), settingsGroupPanel);
				});
				setAxisRangeFieldsEnabled(!autoFitSetting.get(), settingsGroupPanel);
				axisSettingPanels.put(settings, settingsGroupPanel);
			}
		}
		axisChoiceCombo = new JComboBox<>(v);
		axisChoiceCombo.addActionListener(actionEvent -> {
			Axis selectedItem = (Axis) axisChoiceCombo.getSelectedItem();
			if (selectedItem != null) {
				setCurrentSettings(selectedItem.getSettings());
			}
		});

		this.applyAll = () -> {
			for (SettingsGroupPanel panel : axisSettingPanels.values()) {
				panel.applyAll();
			}
		};

		axisChoiceCombo.setSelectedIndex(0);
		axisChoiceCombo.setPreferredSize(new Dimension(100, 25));
		JPanel axisComboWrapper = new PaddedPanel(UiDefines.PADDING);
		axisComboWrapper.setLayout(new GridLayout(1, 1));
		axisComboWrapper.add(axisChoiceCombo);
		this.add(axisComboWrapper, BorderLayout.NORTH);
        Axis first = axes.get(0);
        if(first != null) {
            setCurrentSettings(first.getSettings());
        }
	}


	private void setCurrentSettings(SettingsGroup settings) {
        this.settingsPanelWrapper.removeAll();
		SettingsGroupPanel settingsGroupPanel = this.axisSettingPanels.get(settings);
		this.settingsPanelWrapper.add(settingsGroupPanel, BorderLayout.CENTER);
		this.revalidate();
		this.repaint();
	}

	private JPanel getMaxTextField(SettingsGroupPanel settingsGroupPanel) {
		return settingsGroupPanel.getComponents(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX).getControl();
	}

	private JPanel getMinTextField(SettingsGroupPanel settingsGroupPanel) {
		return settingsGroupPanel.getComponents(Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN).getControl();
	}

	public void applyAll() {
		applyAll.run();
	}

	public void setAxisRangeFieldsEnabled(boolean fieldsEnabled, SettingsGroupPanel settingsGroupPanel) {
		getMinTextField(settingsGroupPanel).setEnabled(fieldsEnabled);
		getMaxTextField(settingsGroupPanel).setEnabled(fieldsEnabled);
	}
}
