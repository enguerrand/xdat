/*
 *  Copyright 2014, Enguerrand de Rochefort
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

package org.xdat.actionListeners.parallelCoordinatesDisplaySettings;

import org.xdat.UserPreferences;
import org.xdat.gui.dialogs.ParallelCoordinatesDisplaySettingsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DefaultDisplaySettingsDialogActionListener implements ActionListener {

	private ParallelCoordinatesDisplaySettingsDialog dialog;

	public DefaultDisplaySettingsDialogActionListener(ParallelCoordinatesDisplaySettingsDialog dialog) {
		this.dialog = dialog;

	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		if (actionCommand.equals("Ok")) {
			UserPreferences.getInstance().setParallelCoordinatesVerticallyOffsetAxisLabels(this.dialog.getChartDisplaySettingsPanel().getAxisLabelVerticalOffsetCheckbox().isSelected());
			UserPreferences.getInstance().setAntiAliasing(this.dialog.getChartDisplaySettingsPanel().getAntiAliasingCheckbox().isSelected());
			UserPreferences.getInstance().setUseAlpha(this.dialog.getChartDisplaySettingsPanel().getAlphaCheckbox().isSelected());
			UserPreferences.getInstance().setParallelCoordinatesDefaultBackgroundColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getBackGroundColor());
			UserPreferences.getInstance().setParallelCoordinatesActiveDesignDefaultColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getActiveDesignColor());
			UserPreferences.getInstance().setParallelCoordinatesSelectedDesignDefaultColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getSelectedDesignColor());
			UserPreferences.getInstance().setParallelCoordinatesInactiveDesignDefaultColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getFilteredDesignColor());
			UserPreferences.getInstance().setParallelCoordinatesFilterColor(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().getFilterColor());
			UserPreferences.getInstance().setParallelCoordinatesShowOnlySelectedDesigns(this.dialog.getChartDisplaySettingsPanel().getShowOnlySelectedDesignsCheckBox().isSelected());
			UserPreferences.getInstance().setParallelCoordinatesShowDesignIDs(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().isShowDesignIDs());
			UserPreferences.getInstance().setParallelCoordinatesShowFilteredDesigns(this.dialog.getChartDisplaySettingsPanel().getChartDisplaySettingsActionListener().isShowFilteredDesigns());
			UserPreferences.getInstance().setParallelCoordinatesDesignLabelFontSize((Integer) this.dialog.getChartDisplaySettingsPanel().getDesignLabelFontSizeSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesLineThickness((Integer) this.dialog.getChartDisplaySettingsPanel().getDesignLineThicknessSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesSelectedDesignLineThickness((Integer) this.dialog.getChartDisplaySettingsPanel().getSelectedDesignLineThicknessSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesFilterWidth((Integer) this.dialog.getChartDisplaySettingsPanel().getFilterWidthSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesFilterHeight((Integer) this.dialog.getChartDisplaySettingsPanel().getFilterHeightSpinner().getValue());

			UserPreferences.getInstance().setParallelCoordinatesAxisColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getAxisColor());
			UserPreferences.getInstance().setParallelCoordinatesAxisLabelFontColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getAxisLabelColor());
			UserPreferences.getInstance().setParallelCoordinatesAxisTicLabelFontColor(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().getTicLabelColor());
			UserPreferences.getInstance().setFilterInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertFilter());
			UserPreferences.getInstance().setParallelCoordinatesAxisInverted(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isInvertAxis());
			UserPreferences.getInstance().setParallelCoordinatesAutoFitAxis(this.dialog.getAxisDisplaySettingsPanel().getAxisDisplaySettingsActionListener().isAutoFitAxis());
			UserPreferences.getInstance().setParallelCoordinatesAxisDefaultMin(this.dialog.getAxisDisplaySettingsPanel().getAxisMin());
			UserPreferences.getInstance().setParallelCoordinatesAxisDefaultMax(this.dialog.getAxisDisplaySettingsPanel().getAxisMax());

			UserPreferences.getInstance().setParallelCoordinatesAxisWidth((Integer) this.dialog.getAxisDisplaySettingsPanel().getAxisWidthSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesAxisLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getAxisLabelFontSizeSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesAxisTicLength((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicSizeSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesAxisTicCount((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicCountSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesAxisTicLabelDigitCount((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicLabelDigitCountSpinner().getValue());
			UserPreferences.getInstance().setParallelCoordinatesAxisTicLabelFontSize((Integer) this.dialog.getAxisDisplaySettingsPanel().getTicLabelFontSizeSpinner().getValue());
			this.dialog.dispose();
		} else if (actionCommand.equals("Cancel")) {
			this.dialog.dispose();
		}
	}
}
