package org.xdat.settings;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class SettingsGroup {
    private final String title;
    private final Map<Key, Setting> settingsMap;

    public SettingsGroup(String title, Collection<Setting> settings) {
        this.title = title;
        LinkedHashMap<Key, Setting> m = new LinkedHashMap<>();
        for (Setting setting : settings) {
            m.put(setting.getKey(), setting);
        }
        this.settingsMap = Collections.unmodifiableMap(m);
    }

    public String getTitle() {
        return title;
    }

    public Map<Key, Setting> getSettings() {
        return settingsMap;
    }

    public Setting getSetting(Key key) {
        Setting setting = settingsMap.get(key);
        if (setting == null) {
            throw new IllegalArgumentException("No setting stored under key "+key);
        }
        return setting;
    }
}