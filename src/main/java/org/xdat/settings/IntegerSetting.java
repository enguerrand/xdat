package org.xdat.settings;

import org.jetbrains.annotations.Nullable;
import org.xdat.UserPreferences;

public class IntegerSetting extends Setting<Integer> {
    private final int min;
    private final int max;
    public IntegerSetting(String title, int hardCodedDefault, @Nullable String defaultValuePreferenceKey, int min, int max) {
        super(title, hardCodedDefault, SettingsType.INTEGER, defaultValuePreferenceKey);
        this.min = min;
        this.max = max;
    }

    @Override
    void setDefaultImpl(String key, Integer defaultValue) {
        UserPreferences.putInt(key, defaultValue);
    }

    @Override
    Integer getDefaultImpl(String key, Integer fallback) {
        return UserPreferences.getInt(key, fallback);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
