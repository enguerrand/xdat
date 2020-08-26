package org.xdat.settings;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SettingsGroup implements Serializable {

    static final long serialVersionUID = 1L;

    private final Map<Key, Setting<?>> settingsMap;

    private SettingsGroup(Collection<Setting<?>> settings) {
        LinkedHashMap<Key, Setting<?>> m = new LinkedHashMap<>();
        for (Setting setting : settings) {
            m.put(setting.getKey(), setting);
        }
        this.settingsMap = Collections.unmodifiableMap(m);
    }

    public Map<Key, Setting<?>> getSettings() {
        return settingsMap;
    }

    public Setting<?> getSetting(Key key) {
        Setting<?> setting = settingsMap.get(key);
        if (setting == null) {
            throw new IllegalArgumentException("No setting stored under key "+key);
        }
        return setting;
    }

    public BooleanSetting getBooleanSetting(Key key) {
        return (BooleanSetting) getSetting(key);
    }

    public ColorSetting getColorSetting(Key key) {
        return (ColorSetting) getSetting(key);
    }

    public DoubleSetting getDoubleSetting(Key key) {
        return (DoubleSetting) getSetting(key);
    }

    public IntegerSetting getIntegerSetting(Key key) {
        return (IntegerSetting) getSetting(key);
    }

    public StringSetting getStringSetting(Key key) {
        return (StringSetting) getSetting(key);
    }

    public boolean getBoolean(Key key) {
        return getBooleanSetting(key).get();
    }

    public Color getColor(Key key) {
        return getColorSetting(key).get();
    }

    public Integer getInteger(Key key) {
        return getIntegerSetting(key).get();
    }

    public Double getDouble(Key key) {
        return getDoubleSetting(key).get();
    }

    public String getString(Key key) {
        return getStringSetting(key).get();
    }

    public static Builder newBuilder(){
        return new Builder();
    }

    public void resetToDefault() {
        SettingsTransaction transaction = new SettingsTransaction();

        transaction.execute(this.settingsMap.values().stream()
                .map(s ->
                    (Function<SettingsTransaction, Boolean>) s::resetToDefault
                )
                .collect(Collectors.toList())
        );
    }

    public static class Builder {
        private boolean built = false;
        private List<Setting<?>> settings = new ArrayList<>();
        private Builder() {
        }

        public <T> Builder addSetting(Setting<T> setting) {
            throwIfBuilt();
            settings.add(setting);
            return this;
        }

        public SettingsGroup build(){
            throwIfBuilt();
            built = true;
            return new SettingsGroup(this.settings);
        }

        private void throwIfBuilt() {
            if (built) {
                throw new IllegalStateException("already built!");
            }
        }
    }

    public void initTransientData(){
        settingsMap.values().forEach(Setting::initTransientData);
    }
}