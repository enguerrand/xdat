package org.xdat.settings;

import org.xdat.UserPreferences;

public class StringSetting extends Setting<String> {
    public StringSetting(String title, String hardCodedDefault, Key defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.STRING, defaultValuePreferenceKey);
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
