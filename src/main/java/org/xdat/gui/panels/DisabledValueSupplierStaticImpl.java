/*
 *  Copyright 2022, Enguerrand de Rochefort
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

public class DisabledValueSupplierStaticImpl<T> implements DisabledValueSupplier<T> {
    public static final long serialVersionUID = 1L;
    private final T staticValue;

    public DisabledValueSupplierStaticImpl(T staticValue) {
        this.staticValue = staticValue;
    }

    @Override
    public T getDisabledValue() {
        return staticValue;
    }
}
