package org.xdat.gui.panels;

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
}
