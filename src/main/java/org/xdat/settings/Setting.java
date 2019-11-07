package org.xdat.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Setting<T> {
    private final Key key;
    private final T hardCodedDefault;
    private T currentValue;
    private final String title;
    private final SettingsType type;
    private final List<SettingsListener<T>> listeners = new ArrayList<>();

    Setting(String title, T hardCodedDefault, SettingsType type, Key key) {
        this.title = title;
        this.type = type;
        this.key = key;
        this.hardCodedDefault = hardCodedDefault;
        this.currentValue = getDefault();
    }

    public void addListener(SettingsListener<T> l) {
        this.listeners.add(l);
    }

    public void setCurrentToDefault(){
        setDefault(currentValue);
    }

    abstract T getDefaultImpl(Key key, T fallback);

    abstract void setDefaultImpl(Key key, T defaultValue);

    public void setDefault(T defaultValue){
        if (this.key == null) {
            return;
        }
        setDefaultImpl(this.key, defaultValue);
    }

    public T getDefault() {
        if (this.key == null) {
            return hardCodedDefault;
        }
        return getDefaultImpl(this.key, hardCodedDefault);
    }

    public T get(){
        return currentValue;
    }

    public boolean set(T value) {
        if (Objects.equals(value, this.currentValue)) {
            return false;
        }
        this.currentValue = value;
        this.listeners.forEach(l -> l.onValueChanged(this));
        return true;
    }

    public String getTitle() {
        return title;
    }

    public SettingsType getType() {
        return type;
    }

    public Key getKey(){
        return key;
    }
}