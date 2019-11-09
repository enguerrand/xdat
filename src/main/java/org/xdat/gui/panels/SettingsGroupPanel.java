/*
 *  Copyright 2019, Enguerrand de Rochefort
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

import org.xdat.gui.UiDefines;
import org.xdat.settings.Key;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsGroup;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SettingsGroupPanel extends PaddedPanel {
    private final List<Supplier<Boolean>> applyActions;
    private SettingsGroup settingsGroup;
    private final Map<Key, SettingComponents> map = new LinkedHashMap<>();

    public SettingsGroupPanel(SettingsGroup settingsGroup) {
        super(UiDefines.PADDING);
        this.settingsGroup = settingsGroup;
        this.applyActions = new ArrayList<>();
        this.setLayout(new BorderLayout());
        JPanel labelPanel = new PaddedPanel(0, UiDefines.PADDING, 0, 0);
        labelPanel.setLayout(new GridLayout(0, 1));
        JPanel controlsPanel = new PaddedPanel(0, 0, 0, UiDefines.PADDING);
        controlsPanel.setLayout(new GridLayout(0, 1));
        add(labelPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);
        for (Setting<?> setting : settingsGroup.getSettings().values()) {
            SettingComponents settingComponents = SettingsPanelFactory.from(setting);
            this.map.put(setting.getKey(), settingComponents);
            labelPanel.add(settingComponents.getLabel());
            controlsPanel.add(settingComponents.getControl());
            this.applyActions.add(settingComponents.getControl()::applyValue);
        }
    }

    public boolean applyAll() {
        boolean changed = false;
        for (Supplier<Boolean> applyAction : this.applyActions) {
            changed |= applyAction.get();
        }
        return changed;
    }

    public void applyAllAsDefault() {
        for (Setting setting : this.settingsGroup.getSettings().values()) {
            setting.setCurrentToDefault();
        }
    }

    public SettingComponents getComponents(Key key) {
        return map.get(key);
    }
}
