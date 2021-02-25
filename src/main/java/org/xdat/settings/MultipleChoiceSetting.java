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

package org.xdat.settings;

import org.xdat.UserPreferences;

import java.util.List;

public class MultipleChoiceSetting extends Setting<String> {
    private final List<String> options;

    public MultipleChoiceSetting(String title, String hardCodedDefault, Key defaultValuePreferenceKey, List<String> options) {
        super(title, hardCodedDefault, SettingsType.MULTIPLE_CHOICE, defaultValuePreferenceKey);
        this.options = options;
    }

    public List<String> getOptions() {
        return options;
    }

    @Override
    void setDefaultImpl(Key key, String defaultValue) {
        UserPreferences.putString(key, defaultValue);
    }

    @Override
    String getDefaultImpl(Key key, String fallback) {
        return UserPreferences.getString(key, fallback);
    }
}
