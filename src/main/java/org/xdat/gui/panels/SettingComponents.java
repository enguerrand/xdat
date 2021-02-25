package org.xdat.gui.panels;

import org.xdat.settings.Setting;

import javax.swing.JComponent;

public class SettingComponents {
    private final JComponent label;
    private final SettingControlPanel control;

    public SettingComponents(JComponent label, SettingControlPanel control) {
        this.label = label;
        this.control = control;
    }

    public JComponent getLabel() {
        return label;
    }

    public SettingControlPanel getControl() {
        return control;
    }

    public <T> void onLinkedSettingsControlUpdated(Setting<T> sourceSetting, T currentControlValue) {
        control.onLinkedSettingsControlUpdated(sourceSetting, currentControlValue);
    }

    public <T> void setEnabledWhen(EnabledCondition<T> enabledCondition) {
        control.setEnabledWhen(enabledCondition);
    }

}
