package org.xdat.gui.panels;

import javax.swing.JPanel;
import java.awt.LayoutManager;

public abstract class SettingControlPanel extends JPanel {
    public SettingControlPanel(LayoutManager layoutManager, boolean b) {
        super(layoutManager, b);
    }

    public SettingControlPanel(LayoutManager layoutManager) {
        super(layoutManager);
    }

    public SettingControlPanel(boolean b) {
        super(b);
    }

    public SettingControlPanel() {
    }

    /**
     * @return whether this operation changed the value
     */
    public abstract boolean applyValue();

    public abstract void setEnabled(boolean enabled);
}
