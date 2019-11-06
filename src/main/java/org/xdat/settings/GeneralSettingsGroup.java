package org.xdat.settings;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class GeneralSettingsGroup extends SettingsGroup {

    public GeneralSettingsGroup() {
        super("General", buildSettings());
    }

    private static List<Setting> buildSettings() {
        return Arrays.asList(
                new BooleanSetting("Offset Axis Labels", true, Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS),
                new BooleanSetting("Use Anti Aliasing", true, Key.ANTI_ALIASING),
                new BooleanSetting("Use Transparency", true, Key.USE_ALPHA),
                new ColorSetting("Background Color", Color.WHITE, Key.PARALLEL_CHART_BACKGROUND_COLOR),
                new ColorSetting("Active Design Color", new Color(0, 150, 0, 128), Key.ACTIVE_DESIGN_DEFAULT_COLOR),
                new ColorSetting("Selected Design Color", Color.BLUE, Key.SELECTED_DESIGN_DEFAULT_COLOR),
                new ColorSetting("Filtered Design Color", new Color(200, 200, 200, 100), Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR),
                new ColorSetting("Use Anti Aliasing", Color.RED, Key.PARALLEL_COORDINATES_FILTER_COLOR),
                new BooleanSetting("Show filtered Designs", false, Key.ANTI_ALIASING),
                new BooleanSetting("Show Design IDs", true, Key.ANTI_ALIASING),
                new IntegerSetting("Design Label Font Size", 10, Key.DESIGN_LABEL_FONT_SIZE, 0, 100),
                new IntegerSetting("Selected Design Line Thickness", 2, Key.SELECTED_DESIGN_LINE_THICKNESS, 0, 10),
                new IntegerSetting("Design Line Thickness", 1, Key.SELECTED_DESIGN_LINE_THICKNESS, 0, 10),
                new IntegerSetting("Filter Symbol Width", 7, Key.PARALLEL_COORDINATES_FILTER_WIDTH, 1, 30),
                new IntegerSetting("Filter Symbol Height", 10, Key.PARALLEL_COORDINATES_FILTER_HEIGHT, 1, 60)
        );
    }
}
