/*
 *  Copyright 2019, Enguerrand de Rochefort
 *
 * This file is part of xdat.
 *
 * xdat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xdat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xdat.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.xdat.settings;

import java.awt.Color;

public class SettingsGroupFactory {
    public static SettingsGroup buildGeneralParallelCoordinatesChartSettingsGroup() {
        return SettingsGroup.newBuilder()
                .addSetting(new BooleanSetting("Offset Axis Labels", true, Key.PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS))
                .addSetting(new IntegerSetting("Axis Label vertical Distance", 10, Key.PARALLEL_COORDINATES_LABELS_VERTICAL_DISTANCE, 0, 100))
                .addSetting(new BooleanSetting("Use Anti Aliasing", true, Key.ANTI_ALIASING))
                .addSetting(new BooleanSetting("Use Transparency", true, Key.USE_ALPHA))
                .addSetting(new ColorSetting("Background Color", Color.WHITE, Key.PARALLEL_CHART_BACKGROUND_COLOR))
                .addSetting(new ColorSetting("Active Design Color", new Color(0, 150, 0, 128), Key.ACTIVE_DESIGN_DEFAULT_COLOR))
                .addSetting(new ColorSetting("Selected Design Color", Color.BLUE, Key.SELECTED_DESIGN_DEFAULT_COLOR))
                .addSetting(new ColorSetting("Filtered Design Color", new Color(200, 200, 200, 100), Key.IN_ACTIVE_DESIGN_DEFAULT_COLOR))
                .addSetting(new ColorSetting("Filter Color", Color.RED, Key.PARALLEL_COORDINATES_FILTER_COLOR))
                .addSetting(new BooleanSetting("Use Anti Aliasing", true, Key.ANTI_ALIASING))
                .addSetting(new BooleanSetting("Show filtered Designs", false, Key.SHOW_FILTERED_DESIGNS))
                .addSetting(new BooleanSetting("Show only selected Designs", false, Key.PARALLEL_COORDINATES_SHOW_ONLY_SELECTED_DESIGNS))
                .addSetting(new BooleanSetting("Show Design IDs", true, Key.SHOW_DESIGN_IDS))
                .addSetting(new IntegerSetting("Design Label Font Size", 10, Key.DESIGN_LABEL_FONT_SIZE, 0, 100))
                .addSetting(new IntegerSetting("Selected Design Line Thickness", 2, Key.SELECTED_DESIGN_LINE_THICKNESS, 0, 10))
                .addSetting(new IntegerSetting("Design Line Thickness", 1, Key.LINE_THICKNESS, 0, 10))
                .addSetting(new IntegerSetting("Filter Symbol Width", 7, Key.PARALLEL_COORDINATES_FILTER_WIDTH, 1, 30))
                .addSetting(new IntegerSetting("Filter Symbol Height", 10, Key.PARALLEL_COORDINATES_FILTER_HEIGHT, 1, 60))
                .build();
    }

    public static SettingsGroup buildParallelCoordinatesChartAxisSettingsGroup() {
        IntegerSetting digitCountSetting = new IntegerSetting("Tic Label Digit Count", 3, Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_DIGIT_COUNT, 0, 20);
        return SettingsGroup.newBuilder()
                .addSetting(new BooleanSetting("Active", true, Key.PARALLEL_COORDINATES_AXIS_ACTIVE))
                .addSetting(new ColorSetting("Axis Color", Color.BLACK, Key.PARALLEL_COORDINATES_AXIS_COLOR))
                .addSetting(new ColorSetting("Axis Label Color", Color.BLACK, Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR))
                .addSetting(new IntegerSetting("Axis Label Fontsize", 20, Key.PARALLEL_COORDINATES_AXIS_LABEL_FONT_SIZE, 0, 100))
                .addSetting(new IntegerSetting("Axis Spacing", 200, Key.PARALLEL_COORDINATES_AXIS_WIDTH, 0, 1000))
                .addSetting(new IntegerSetting("Tic Size", 4, Key.PARALLEL_COORDINATES_AXIS_TIC_LENGTH, 0, 100))
                .addSetting(new IntegerSetting("Number of Tics", 11, Key.PARALLEL_COORDINATES_AXIS_TIC_COUNT, 0, 1000))
                .addSetting(new ColorSetting("Tic Label Color", Color.BLACK, Key.PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR))
                .addSetting(new IntegerSetting("Tic Label Fontsize", 10, Key.TIC_LABEL_FONT_SIZE, 0, 100))
                .addSetting(digitCountSetting)
                .addSetting(new BooleanSetting("Invert Filter", false, Key.PARALLEL_COORDINATES_FILTER_INVERTED))
                .addSetting(new BooleanSetting("Invert Axis", false, Key.PARALLEL_COORDINATES_AXIS_INVERTED))
                .addSetting(new BooleanSetting("Autofit Axis", true, Key.PARALLEL_COORDINATES_AUTO_FIT_AXIS))
                .addSetting(new DoubleSetting("Min", 0, Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MIN, digitCountSetting))
                .addSetting(new DoubleSetting("Max", 1, Key.PARALLEL_COORDINATES_AXIS_DEFAULT_MAX, digitCountSetting))
                .build();
    }
}