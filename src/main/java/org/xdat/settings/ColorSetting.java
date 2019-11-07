package org.xdat.settings;

import org.xdat.UserPreferences;

import java.awt.Color;

public class ColorSetting extends Setting<Color> {
    public ColorSetting(String title, Color hardCodedDefault, Key defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.COLOR, defaultValuePreferenceKey);
    }

    @Override
    void setDefaultImpl(Key key, Color defaultValue) {
        UserPreferences.putColor(key, defaultValue);
    }

    @Override
    Color getDefaultImpl(Key key, Color fallback) {
        return UserPreferences.getColor(key, fallback);
    }
}
