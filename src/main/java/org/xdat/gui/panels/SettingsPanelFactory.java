package org.xdat.gui.panels;

import org.xdat.gui.controls.ColorChoiceButton;
import org.xdat.gui.controls.MinMaxSpinnerModel;
import org.xdat.gui.controls.RightAlignedSpinner;
import org.xdat.settings.BooleanSetting;
import org.xdat.settings.ColorSetting;
import org.xdat.settings.DoubleSetting;
import org.xdat.settings.Formatting;
import org.xdat.settings.IntegerSetting;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsType;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class SettingsPanelFactory {

    private SettingsPanelFactory() {
        throw new IllegalStateException("No instance for you!");
    }

    static <T>SettingComponents from(Setting<T> setting) {
        SettingsType type = setting.getType();
        JLabel label = new JLabel(setting.getTitle());
        switch(type){
            case BOOLEAN:
                return new SettingComponents(label, buildBooleanControl((BooleanSetting) setting));
            case INTEGER:
                return new SettingComponents(label, buildIntegerControl((IntegerSetting) setting));
            case DOUBLE:
                return new SettingComponents(label, buildDoubleControl((DoubleSetting) setting));
            case STRING:
                // FIXME
                throw new IllegalStateException("Setting type " + type + " not yet implemented!");
            case COLOR:
                return new SettingComponents(label, buildColorControl((ColorSetting) setting));
            default:
                throw new IllegalStateException("Unknown setting type "+ type);
        }
    }

    private static SettingControlPanel buildBooleanControl(BooleanSetting setting) {
        JCheckBox checkBox = new JCheckBox();
        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue() {
                return setting.set(checkBox.isSelected());
            }

            @Override
            public void setEnabled(boolean enabled) {
                checkBox.setEnabled(enabled);
            }
        };
        checkBox.setSelected(setting.get());
        outer.add(checkBox);
        return outer;
    }

    private static SettingControlPanel buildIntegerControl(IntegerSetting setting) {
        JSpinner spinner = new RightAlignedSpinner(new MinMaxSpinnerModel(setting.getMin(), setting.getMax()));
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
        SettingControlPanel outer = new SettingControlPanel(new GridLayout(1,1)) {
            @Override
            public boolean applyValue() {
                return setting.set((Integer)spinner.getValue());
            }

            @Override
            public void setEnabled(boolean enabled) {
                spinner.setEnabled(enabled);
            }
        };
        spinner.setValue(setting.get());
        outer.add(spinner);
        return outer;
    }

    private static SettingControlPanel buildDoubleControl(DoubleSetting setting) {
        JTextField textField = new JTextField();

        SettingControlPanel outer = new SettingControlPanel(new GridLayout(1,1)) {
            @Override
            public boolean applyValue() {
                String text = textField.getText();
                double previous = setting.get();
                try {
                    double d = Double.parseDouble(text);
                    setting.set(d);
                    return d != previous;
                } catch (NumberFormatException e) {
                    textField.setText(Formatting.formatDouble(previous, setting.getDigitCountSetting().get()));
                    return false;
                }
            }

            @Override
            public void setEnabled(boolean enabled) {
                textField.setEnabled(enabled);
            }
        };
        textField.setText(String.valueOf(Formatting.formatDouble(setting.get(), setting.getDigitCountSetting().get())));
        outer.add(textField);
        return outer;
    }

    private static SettingControlPanel buildColorControl(ColorSetting setting) {
        ColorChoiceButton colorChoiceButton = new ColorChoiceButton(setting.get());
        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue() {
                return setting.set(colorChoiceButton.getCurrentColor());
            }

            @Override
            public void setEnabled(boolean enabled) {
                colorChoiceButton.setEnabled(enabled);
            }
        };
        colorChoiceButton.addActionListener(actionCommand -> {
            Color newColor = JColorChooser.showDialog(outer, setting.getTitle(), colorChoiceButton.getCurrentColor());
            if (newColor != null) {
                colorChoiceButton.setCurrentColor(newColor);
            }
        });
        outer.add(colorChoiceButton);
        return outer;
    }
}
