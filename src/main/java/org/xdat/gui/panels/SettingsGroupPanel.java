package org.xdat.gui.panels;

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

public class SettingsGroupPanel extends JPanel {
    private final List<Supplier<Boolean>> applyActions;
    private SettingsGroup settingsGroup;
    private final Map<Key, SettingComponents> map = new LinkedHashMap<>();

    public SettingsGroupPanel(SettingsGroup settingsGroup) {
        this.settingsGroup = settingsGroup;
        this.applyActions = new ArrayList<>();
        this.setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
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
