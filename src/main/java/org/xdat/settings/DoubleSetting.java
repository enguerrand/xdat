package org.xdat.settings;

import org.xdat.UserPreferences;

public class DoubleSetting extends Setting<Double> {
    private final IntegerSetting digitCountSetting;

    public DoubleSetting(String title, double hardCodedDefault, Key defaultValuePreferenceKey, IntegerSetting digitCountSetting) {
        super(title, hardCodedDefault, SettingsType.DOUBLE, defaultValuePreferenceKey);
        this.digitCountSetting = digitCountSetting;
    }

    @Override
    void setDefaultImpl(Key key, Double defaultValue) {
        UserPreferences.putDouble(key, defaultValue);
    }

    @Override
    Double getDefaultImpl(Key key, Double fallback) {
        return UserPreferences.getDouble(key, fallback);
    }

    public IntegerSetting getDigitCountSetting() {
        return digitCountSetting;
    }
}
