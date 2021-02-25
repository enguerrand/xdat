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
        return from(setting, true);
    }

    static <T>SettingComponents from(Setting<T> setting) {
        return from(setting, false);
    }

    private static <T>SettingComponents from(Setting<T> setting, boolean direct) {
        SettingsType type = setting.getType();
        JLabel label = new JLabel(setting.getTitle());
        switch(type){
            case BOOLEAN:
                return new SettingComponents(label, buildBooleanControl((BooleanSetting) setting, direct));
            case INTEGER:
                return new SettingComponents(label, buildIntegerControl((IntegerSetting) setting, direct));
            case DOUBLE:
                return new SettingComponents(label, buildDoubleControl((DoubleSetting) setting, direct));
            case STRING:
                // FIXME
                throw new IllegalStateException("Setting type " + type + " not yet implemented!");
            case COLOR:
                return new SettingComponents(label, buildColorControl((ColorSetting) setting, direct));
            case MULTIPLE_CHOICE:
                return new SettingComponents(label, buildMultipleChoiceControl((MultipleChoiceSetting) setting, direct));
            default:
                throw new IllegalStateException("Unknown setting type "+ type);
        }
    }

    private static SettingControlPanel buildBooleanControl(BooleanSetting setting, boolean direct) {
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
        if (direct) {
            checkBox.addActionListener(actionEvent -> outer.applyValue(null));
        }
        return outer;
    }

    private static SettingControlPanel buildIntegerControl(IntegerSetting setting, boolean direct) {
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
        if (direct) {
            spinner.addChangeListener(actionEvent -> outer.applyValue(null));
        }
        return outer;
    }

    private static SettingControlPanel buildDoubleControl(DoubleSetting setting, boolean direct) {
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
        if (direct) {
            textField.addActionListener(actionEvent -> outer.applyValue(null));
        }
        return outer;
    }

    private static SettingControlPanel buildColorControl(ColorSetting setting, boolean direct) {
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
                if (direct) {
                    outer.applyValue(null);
                }
            }
        });
        outer.add(colorChoiceButton);
        return outer;
    }

    private static SettingControlPanel buildMultipleChoiceControl(MultipleChoiceSetting setting, boolean direct) {
        List<String> options = setting.getOptions();
        JComboBox<String> comboBox = new JComboBox<>(options.toArray(new String[0]));
        comboBox.setSelectedItem(setting.get());

        SettingControlPanel outer = new SettingControlPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            public boolean applyValue(SettingsTransaction transaction) {
                return setting.set((String)comboBox.getSelectedItem(), transaction);
            }

            @Override
            public void setEnabled(boolean enabled) {
                comboBox.setEnabled(enabled);
            }
        };
        outer.add(comboBox);
        if (direct) {
            comboBox.addActionListener(actionEvent -> outer.applyValue(null));
        }
        return outer;
    }
}
