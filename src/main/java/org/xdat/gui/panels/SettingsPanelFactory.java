package org.xdat.gui.panels;

import org.jetbrains.annotations.Nullable;
import org.xdat.gui.controls.ColorChoiceButton;
import org.xdat.gui.controls.MinMaxSpinnerModel;
import org.xdat.gui.controls.RightAlignedSpinner;
import org.xdat.settings.BooleanSetting;
import org.xdat.settings.ColorSetting;
import org.xdat.settings.DoubleSetting;
import org.xdat.settings.Formatting;
import org.xdat.settings.IntegerSetting;
import org.xdat.settings.MultipleChoiceSetting;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsTransaction;
import org.xdat.settings.SettingsType;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;

public class SettingsPanelFactory {

    private SettingsPanelFactory() {
        throw new IllegalStateException("No instance for you!");
    }

    public static <T>SettingComponents standaloneFrom(Setting<T> setting) {
        return from(setting, null);
    }

    static <T>SettingComponents from(Setting<T> setting, @Nullable SettingsControlListener settingsControlListener) {
        SettingsType type = setting.getType();
        JLabel label = new JLabel(setting.getTitle());
        switch(type){
            case BOOLEAN:
                return new SettingComponents(label, buildBooleanControl((BooleanSetting) setting, settingsControlListener));
            case INTEGER:
                return new SettingComponents(label, buildIntegerControl((IntegerSetting) setting, settingsControlListener));
            case DOUBLE:
                return new SettingComponents(label, buildDoubleControl((DoubleSetting) setting, settingsControlListener));
            case STRING:
                // FIXME
                throw new IllegalStateException("Setting type " + type + " not yet implemented!");
            case COLOR:
                return new SettingComponents(label, buildColorControl((ColorSetting) setting, settingsControlListener));
            case MULTIPLE_CHOICE:
                return new SettingComponents(label, buildMultipleChoiceControl((MultipleChoiceSetting) setting, settingsControlListener));
            default:
                throw new IllegalStateException("Unknown setting type "+ type);
        }
    }

    private static SettingControlPanel buildBooleanControl(BooleanSetting setting, @Nullable SettingsControlListener settingsControlListener) {
        JCheckBox checkBox = new JCheckBox();
        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue(@Nullable SettingsTransaction transaction) {
                return setting.set(checkBox.isSelected(), transaction);
            }

            @Override
            public void setEnabled(boolean enabled) {
                checkBox.setEnabled(enabled);
            }
        };
        checkBox.setSelected(setting.get());
        outer.add(checkBox);
        checkBox.addActionListener(actionEvent -> {
            if (settingsControlListener == null) {
                outer.applyValue(null);
            } else {
                settingsControlListener.onLinkedSettingsControlUpdated(setting, checkBox.isSelected());
            }
        });
        return outer;
    }

    private static SettingControlPanel buildIntegerControl(IntegerSetting setting, @Nullable SettingsControlListener settingsControlListener) {
        JSpinner spinner = new RightAlignedSpinner(new MinMaxSpinnerModel(setting.getMin(), setting.getMax()));
        ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
        SettingControlPanel outer = new SettingControlPanel(new GridLayout(1,1)) {
            @Override
            public boolean applyValue(SettingsTransaction transaction) {
                return setting.set((Integer)spinner.getValue(), transaction);
            }

            @Override
            public void setEnabled(boolean enabled) {
                spinner.setEnabled(enabled);
            }
        };
        spinner.setValue(setting.get());
        outer.add(spinner);
        spinner.addChangeListener(actionEvent -> {
            if (settingsControlListener == null) {
                outer.applyValue(null);
            } else {
                settingsControlListener.onLinkedSettingsControlUpdated(setting, (Integer) spinner.getValue());
            }
        });
        return outer;
    }

    private static SettingControlPanel buildDoubleControl(DoubleSetting setting, @Nullable SettingsControlListener settingsControlListener) {
        JTextField textField = new JTextField();

        SettingControlPanel outer = new SettingControlPanel(new GridLayout(1,1)) {
            @Override
            public boolean applyValue(SettingsTransaction transaction) {
                String text = textField.getText();
                double previous = setting.get();
                try {
                    double d = Double.parseDouble(text);
                    setting.set(d, transaction);
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
        textField.addActionListener(actionEvent -> {
            if (settingsControlListener == null) {
                outer.applyValue(null);
            } else {
                try {
                    double d = Double.parseDouble(textField.getText());
                    settingsControlListener.onLinkedSettingsControlUpdated(setting, d);
                } catch (NumberFormatException ignored) {
                }
            }
        });
        return outer;
    }

    private static SettingControlPanel buildColorControl(ColorSetting setting, @Nullable SettingsControlListener settingsControlListener) {
        ColorChoiceButton colorChoiceButton = new ColorChoiceButton(setting.get());
        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue(SettingsTransaction transaction) {
                return setting.set(colorChoiceButton.getCurrentColor(), transaction);
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
                if (settingsControlListener == null) {
                    outer.applyValue(null);
                } else {
                    settingsControlListener.onLinkedSettingsControlUpdated(setting, newColor);
                }
            }
        });
        outer.add(colorChoiceButton);
        return outer;
    }

    private static SettingControlPanel buildMultipleChoiceControl(MultipleChoiceSetting setting, @Nullable SettingsControlListener settingsControlListener) {
        List<String> options = setting.getOptions();
        JComboBox<String> comboBox = new JComboBox<>(options.toArray(new String[0]));
        comboBox.setSelectedItem(setting.get());

        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue(SettingsTransaction transaction) {
                @Nullable String selectedItem = (String) comboBox.getSelectedItem();
                if (selectedItem != null) {
                    return setting.set(selectedItem, transaction);
                } else {
                    return false;
                }
            }

            @Override
            public void setEnabled(boolean enabled) {
                comboBox.setEnabled(enabled);
            }
        };
        outer.add(comboBox);
        comboBox.addActionListener(actionEvent -> {
            if (settingsControlListener == null) {
                outer.applyValue(null);
            } else {
                @Nullable String selectedItem = (String) comboBox.getSelectedItem();
                if (selectedItem != null) {
                    settingsControlListener.onLinkedSettingsControlUpdated(setting, selectedItem);
                }
            }
        });
        return outer;
    }
}
