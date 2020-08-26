package org.xdat.settings;

import org.xdat.UserPreferences;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String title, boolean hardCodedDefault, Key defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.BOOLEAN, defaultValuePreferenceKey);
    }

    @Override
    void setDefaultImpl(Key key, Boolean defaultValue) {
        UserPreferences.putBoolean(key, defaultValue);
    }

    @Override
    Boolean getDefaultImpl(Key key, Boolean fallback) {
        return UserPreferences.getBoolean(key, fallback);
    }
}