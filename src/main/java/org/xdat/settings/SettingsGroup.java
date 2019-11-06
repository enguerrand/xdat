package org.xdat.settings;

import java.util.Collections;
import java.util.List;

public class SettingsGroup {
    private final String title;
    private final List<Setting> settings;

    public SettingsGroup(String title, List<Setting> settings) {
        this.title = title;
        this.settings = settings;
    }

    public String getTitle() {
        return title;
    }

    public List<Setting> getSettings() {
        return Collections.unmodifiableList(settings);
    }
}