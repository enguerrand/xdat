package org.xdat.settings;

import org.jetbrains.annotations.Nullable;
import org.xdat.UserPreferences;

public class StringSetting extends Setting<String> {
    public StringSetting(String title, String hardCodedDefault, @Nullable String defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.STRING, defaultValuePreferenceKey);
    }

    @Override
    void setDefaultImpl(String key, String defaultValue) {
        UserPreferences.putString(key, defaultValue);
    }

    @Override
    String getDefaultImpl(String key, String fallback) {
        return UserPreferences.getString(key, fallback);
    }
}
