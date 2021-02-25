package org.xdat.gui.panels;

import org.jetbrains.annotations.Nullable;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsTransaction;

import javax.swing.JPanel;
import java.awt.LayoutManager;
import java.util.Objects;

public abstract class SettingControlPanel extends JPanel {
    @Nullable
    private EnabledCondition<?> enabledCondition = null;

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
    public abstract boolean applyValue(SettingsTransaction transaction);

    public abstract void setEnabled(boolean enabled);

    public <T> void onLinkedSettingsControlUpdated(Setting<T> sourceSetting, T currentControlValue) {
        checkEnabled(sourceSetting, currentControlValue);
    }

    private <T> void checkEnabled(Setting<T> sourceSetting, T currentValue) {
        if (this.enabledCondition == null) {
            return;
        }
        if (Objects.equals(sourceSetting.getUniqueId(), this.enabledCondition.getEnablingSetting().getUniqueId())) {
            setEnabled(Objects.equals(this.enabledCondition.getEnablingValue(), currentValue));
        }
    }

    public <T> void setEnabledWhen(EnabledCondition<T> enabledCondition) {
        this.enabledCondition = enabledCondition;
        enabledCondition.getEnablingSetting().addListener((source, transaction) -> checkEnabled(source, source.get()));
        checkEnabled(enabledCondition.getEnablingSetting(), enabledCondition.getEnablingSetting().get());
    }

}
