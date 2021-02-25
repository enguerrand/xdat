package org.xdat.settings;

import org.xdat.UserPreferences;

import java.util.List;

public class MultipleChoiceSetting extends Setting<String> {
    private final List<String> options;

    public MultipleChoiceSetting(String title, String hardCodedDefault, Key defaultValuePreferenceKey, List<String> options) {
        super(title, hardCodedDefault, SettingsType.MULTIPLE_CHOICE, defaultValuePreferenceKey);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    void setDefaultImpl(Key key, String defaultValue) {
        UserPreferences.putString(key, defaultValue);
    }

    @Override
    String getDefaultImpl(Key key, String fallback) {
        return UserPreferences.getString(key, fallback);
    }
}
