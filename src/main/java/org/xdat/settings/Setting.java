package org.xdat.settings;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class Setting<T> {
    @Nullable
    private final String defaultValuePreferenceKey;
    private final T hardCodedDefault;
    private T currentValue;
    private final String title;
    private final SettingsType type;

    Setting(String title, T hardCodedDefault, SettingsType type, @Nullable String defaultValuePreferenceKey) {
        this.title = title;
        this.type = type;
        this.defaultValuePreferenceKey = defaultValuePreferenceKey;
        this.hardCodedDefault = hardCodedDefault;
        this.currentValue = getDefault();
    }

    public void setCurrentToDefault(){
        setDefault(currentValue);
    }

    abstract T getDefaultImpl(String key, T fallback);

    abstract void setDefaultImpl(String key, T defaultValue);

    public void setDefault(T defaultValue){
        if (this.defaultValuePreferenceKey == null) {
            return;
        }
        setDefaultImpl(this.defaultValuePreferenceKey, defaultValue);
    }

    public T getDefault() {
        if (this.defaultValuePreferenceKey == null) {
            return hardCodedDefault;
        }
        return getDefaultImpl(this.defaultValuePreferenceKey, hardCodedDefault);
    }

    public T get(){
        return currentValue;
    }

    public boolean set(T value) {
        if (Objects.equals(value, this.currentValue)) {
            return false;
        }
        this.currentValue = value;
        return true;
    }

    public String getTitle() {
        return title;
    }

    public SettingsType getType() {
        return type;
    }
}