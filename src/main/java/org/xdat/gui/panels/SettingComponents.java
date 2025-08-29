package org.xdat.gui.panels;

import org.xdat.settings.Setting;

import javax.swing.JComponent;

public class SettingComponents<K> {
    private final JComponent label;
    private final SettingControlPanel<K> control;

    public SettingComponents(JComponent label, SettingControlPanel<K> control) {
        this.label = label;
        this.control = control;
    }

    public JComponent getLabel() {
        return label;
    }

    public SettingControlPanel<K> getControl() {
        return control;
    }

    public <T> void onLinkedSettingsControlUpdated(Setting<T> sourceSetting, T currentControlValue) {
        control.onLinkedSettingsControlUpdated(sourceSetting, currentControlValue);
    }

    public <T> void setEnabledWhen(EnabledCondition<T, K> enabledCondition) {
        control.setEnabledWhen(enabledCondition);
    }

}
