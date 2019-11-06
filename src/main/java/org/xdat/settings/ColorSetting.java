package org.xdat.settings;

import org.jetbrains.annotations.Nullable;
import org.xdat.UserPreferences;

import java.awt.Color;

public class ColorSetting extends Setting<Color> {
    public ColorSetting(String title, Color hardCodedDefault, @Nullable String defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.COLOR, defaultValuePreferenceKey);
    }

    @Override
    void setDefaultImpl(String key, Color defaultValue) {
        UserPreferences.putColor(key, defaultValue);
    }

    @Override
    Color getDefaultImpl(String key, Color fallback) {
        return UserPreferences.getColor(key, fallback);
    }
}
