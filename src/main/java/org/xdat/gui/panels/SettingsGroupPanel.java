package org.xdat.gui.panels;

import org.xdat.settings.GeneralSettingsGroup;
import org.xdat.settings.Setting;
import org.xdat.settings.SettingsGroup;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SettingsGroupPanel extends JPanel {
    private final List<Supplier<Boolean>> applyActions;
    public SettingsGroupPanel(SettingsGroup settingsGroup) {
        this.applyActions = new ArrayList<>();
        this.setLayout(new BorderLayout());
        JPanel labelPanel = new JPanel(new GridLayout(0, 1));
        JPanel controlsPanel = new JPanel(new GridLayout(0, 1));
        add(labelPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.EAST);
        for (Setting setting : settingsGroup.getSettings()) {
            SettingComponents settingComponents = SettingsPanelFactory.from(setting);
            labelPanel.add(settingComponents.getLabel());
            controlsPanel.add(settingComponents.getControl());
            this.applyActions.add(settingComponents.getControl()::applyValue);
        }
    }

    private boolean applyAll() {
        boolean changed = false;
        for (Supplier<Boolean> applyAction : this.applyActions) {
            changed |= applyAction.get();
        }
        return changed;
    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setLayout(new BorderLayout());
        GeneralSettingsGroup generalSettingsGroup = new GeneralSettingsGroup();
        generalSettingsGroup.getSettings().forEach(s ->
                s.addListener(source -> {
                    System.out.println("Value changed on "+s.getTitle()+" to "+source.get());
                })
        );
        SettingsGroupPanel settingsGroupPanel = new SettingsGroupPanel(generalSettingsGroup);
        jFrame.add(settingsGroupPanel, BorderLayout.CENTER);
        JButton btn = new JButton("apply");
        btn.addActionListener(action -> {
            boolean changed = settingsGroupPanel.applyAll();
            System.out.println("Changed = "+changed);
        });
        jFrame.add(btn, BorderLayout.SOUTH);
        jFrame.setPreferredSize(new Dimension(600, 600));
        jFrame.pack();
        jFrame.setVisible(true);
    }
}
