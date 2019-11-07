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

public enum Key {
    SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_Y("scatterChart2DTicLabelFontsizeY"),
    PARALLEL_COORDINATES_AUTO_FIT_AXIS("ParallelCoordinatesAutoFitAxis"),
    LOCALE("locale"),
    SCATTER_CHART_2D_DISPLAY_MODE("ScatterChart2DDisplayMode"),
    SCATTER_CHART_2D_AUTOFIT_X("scatterChart2DAutofitX"),
    SCATTER_CHART_2D_AUTOFIT_Y("scatterChart2DAutofitY"),
    SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_X("scatterChart2DAxisTitleFontsizeX"),
    SCATTER_CHART_2D_AXIS_TITLE_FONTSIZE_Y("scatterChart2DAxisTitleFontsizeY"),
    SCATTER_CHART_2D_TIC_COUNT_X("scatterChart2DTicCountX"),
    SCATTER_CHART_2D_TIC_COUNT_Y("scatterChart2DTicCountY"),
    SCATTER_CHART_2D_TIC_LABEL_FONTSIZE_X("scatterChart2DTicLabelFontsizeX"),
    SCATTER_CHART_2D_DATA_POINT_SIZE("scatterChart2DDataPointSize"),
    SCATTER_CHART_2D_FOREGROUND_COLOR("scatterChart2DForegroundColor"),
    SCATTER_CHART_2D_BACKGROUND_COLOR("scatterChart2DBackgroundColor"),
    SCATTER_CHART_2D_ACTIVE_DESIGN_COLOR("scatterChart2DActiveDesignColor"),
    SCATTER_CHART_2D_SELECTED_DESIGN_COLOR("scatterChart2DSelectedDesignColor"),
    PARALLEL_COORDINATES_AXIS_ACTIVE("ParallelCoordinatesAxisActive"),
    PARALLEL_COORDINATES_AXIS_COLOR("ParallelCoordinatesAxisColor"),
    PARALLEL_COORDINATES_VERTICALLY_OFFSET_AXIS_LABELS("ParallelCoordinatesVerticallyOffsetAxisLabels"),
    PARALLEL_COORDINATES_LABELS_VERTICAL_DISTANCE("ParallelCoordinatesAxisLabelsVerticalDistance"),
    PARALLEL_COORDINATES_AXIS_LABEL_FONT_COLOR("ParallelCoordinatesAxisLabelFontColor"),
    PARALLEL_COORDINATES_AXIS_LABEL_FONT_SIZE("ParallelCoordinatesAxisLabelFontSize"),
    PARALLEL_COORDINATES_AXIS_TIC_COUNT("ParallelCoordinatesAxisTicCount"),
    PARALLEL_COORDINATES_AXIS_TIC_LABEL_FONT_COLOR("ParallelCoordinatesAxisTicLabelFontColor"),
    PARALLEL_COORDINATES_AXIS_TIC_LABEL_FORMAT("ParallelCoordinatesAxisTicLabelFormat"),
    PARALLEL_COORDINATES_AXIS_TIC_LABEL_DIGIT_COUNT("ParallelCoordinatesAxisTicLabelDigitCount"),
    PARALLEL_COORDINATES_AXIS_TIC_LENGTH("ParallelCoordinatesAxisTicLength"),
    PARALLEL_COORDINATES_AXIS_WIDTH("ParallelCoordinatesAxisWidth"),
    PARALLEL_COORDINATES_FILTER_COLOR("ParallelCoordinatesFilterColor"),
    PARALLEL_COORDINATES_FILTER_HEIGHT("ParallelCoordinatesFilterHeight"),
    PARALLEL_COORDINATES_FILTER_WIDTH("ParallelCoordinatesFilterWidth"),
    TIC_LABEL_FONT_SIZE("ticLabelFontSize"),
    DESIGN_LABEL_FONT_SIZE("designLabelFontSize"),
    LINE_THICKNESS("lineThickness"),
    SELECTED_DESIGN_LINE_THICKNESS("selectedDesignLineThickness"),
    SHOW_FILTERED_DESIGNS("showFilteredDesigns"),
    PARALLEL_COORDINATES_SHOW_ONLY_SELECTED_DESIGNS("parallelCoordinatesShowOnlySelectedDesigns"),
    ACTIVE_DESIGN_DEFAULT_COLOR("activeDesignDefaultColor"),
    SELECTED_DESIGN_DEFAULT_COLOR("selectedDesignDefaultColor"),
    IN_ACTIVE_DESIGN_DEFAULT_COLOR("inActiveDesignDefaultColor"),
    SHOW_DESIGN_IDS("showDesignIDs"),
    ANTI_ALIASING("antiAliasing"),
    USE_ALPHA("useAlpha"),
    DESIGN_ID_FONT_SIZE("designIDFontSize"),
    PARALLEL_CHART_BACKGROUND_COLOR("backgroundColor"),
    PARALLEL_COORDINATES_FILTER_INVERTED("ParallelCoordinatesFilterInverted"),
    PARALLEL_COORDINATES_AXIS_INVERTED("ParallelCoordinatesAxisInverted"),
    PARALLEL_COORDINATES_AXIS_DEFAULT_MIN("ParallelCoordinatesAxisDefaultMin"),
    PARALLEL_COORDINATES_AXIS_DEFAULT_MAX("ParallelCoordinatesAxisDefaultMax"),
    DIRECTORY_TO_IMPORT_FROM("dirToImportFrom"),
    LAST_FILE_BROWSING_DIRECTORY("lastFileBrowsingDirectory"),
    USER_DIR("userDir"),
    DELIMITER("delimiter"),
    TREAT_CONSECUTIVE_AS_ONE("treatConsecutiveAsOne"),
    OTHER_DELIMITER("otherDelimiter"),
    ;

    private final String id;

    Key(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}