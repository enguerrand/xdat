/*
 *  Copyright 2022, Enguerrand de Rochefort
 *
 * This file is part of logrifle.
 *
 * logrifle is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * logrifle is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with logrifle.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.xdat.gui.panels;

import org.xdat.data.DataSheet;
import org.xdat.data.Parameter;

public class DisabledValueSupplierDatasheetAxisLimit implements DisabledValueSupplier<Double> {
    public static final long serialVersionUID = 1L;
    public enum AxisLimitType {
        MIN,
        MAX,
    }
    private final DataSheet dataSheet;
    private final Parameter parameter;
    private final AxisLimitType limitType;

    public DisabledValueSupplierDatasheetAxisLimit(DataSheet dataSheet, Parameter parameter, AxisLimitType limitType) {
        this.dataSheet = dataSheet;
        this.parameter = parameter;
        this.limitType = limitType;
    }

    @Override
    public Double getDisabledValue() {
        switch (this.limitType) {
            case MIN: return dataSheet.getMinValueOf(parameter);
            case MAX: return dataSheet.getMaxValueOf(parameter);
            default: throw new IllegalStateException("Missing implementation for limit type " + limitType);
        }
    }
}
