package org.xdat.settings;

import org.jetbrains.annotations.Nullable;
import org.xdat.gui.panels.EnabledCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public abstract class Setting<T> implements Serializable {
    static final long serialVersionUID = 1L;
    private final String uniqueId = UUID.randomUUID().toString();
    private final Key key;
    private final T hardCodedDefault;
    private T currentValue;
    private final String title;
    private final SettingsType type;
    private transient List<SettingsListener<T>> listeners;
    @Nullable
    private EnabledCondition<?, T> enabledCondition;

    Setting(String title, T hardCodedDefault, SettingsType type, Key key) {
        this.title = title;
        this.type = type;
        this.key = key;
        this.hardCodedDefault = hardCodedDefault;
        this.currentValue = getDefault();
        initTransientData();
    }

    public String getUniqueId() {
        return uniqueId;
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
        return set(value, null);
    }

    public boolean set(T value, @Nullable SettingsTransaction transaction) {
        if (Objects.equals(value, this.currentValue)) {
            return false;
        }
        this.currentValue = value;
        if (transaction != null) {
            transaction.addChanged(this);
        }
        this.listeners.forEach(l -> l.onValueChanged(this, transaction));
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

    public void initTransientData() {
        this.listeners = new ArrayList<>();
    }

    public boolean resetToDefault(@Nullable SettingsTransaction t) {
        return set(getDefault(), t);
    }

    public void setEnabledCondition(@Nullable EnabledCondition<?, T> enabledCondition) {
        this.enabledCondition = enabledCondition;
    }

    public Optional<EnabledCondition<?, T>> getEnabledCondition() {
        return Optional.ofNullable(enabledCondition);
    }
}