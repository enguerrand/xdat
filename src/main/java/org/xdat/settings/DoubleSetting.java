package org.xdat.settings;

import org.jetbrains.annotations.Nullable;
import org.xdat.UserPreferences;

public class DoubleSetting extends Setting<Double> {
    public DoubleSetting(String title, double hardCodedDefault, @Nullable Key defaultValuePreferenceKey) {
        super(title, hardCodedDefault, SettingsType.DOUBLE, defaultValuePreferenceKey);
    }

    @Override
    void setDefaultImpl(Key key, Double defaultValue) {
        UserPreferences.putDouble(key, defaultValue);
    }

    @Override
    Double getDefaultImpl(Key key, Double fallback) {
        return UserPreferences.getDouble(key, fallback);
    }
}
