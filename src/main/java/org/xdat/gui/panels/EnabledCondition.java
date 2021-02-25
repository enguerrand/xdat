/*
 *  Copyright 2021, Enguerrand de Rochefort
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

package org.xdat.gui.panels;

import org.xdat.settings.Setting;

public class EnabledCondition<T> {
    private final Setting<T> enablingSetting;
    private final T enablingValue;

    public EnabledCondition(Setting<T> enablingSetting, T enablingValue) {
        this.enablingSetting = enablingSetting;
        this.enablingValue = enablingValue;
    }

    public Setting<T> getEnablingSetting() {
        return enablingSetting;
    }

    public T getEnablingValue() {
        return enablingValue;
    }
}
