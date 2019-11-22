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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class SettingsTransaction {
    private final List<Setting<?>> changedSettings = new ArrayList<>();
    private final Set<Runnable> handlers = new LinkedHashSet<>();

    public SettingsTransaction() {
    }

    public void execute(Collection<Function<SettingsTransaction, Boolean>> tasks) {
        boolean changed = false;
        for (Function<SettingsTransaction, Boolean> applyAction : tasks) {
            changed |= applyAction.apply(this);
        }
        if (changed) {
            runHandlers();
        }
    }

    public void handleOnce(Runnable handler) {
        handlers.add(handler);
    }

    void addChanged(Setting<?> setting){
        this.changedSettings.add(setting);
    }

    private void runHandlers(){
        if (isChanged()) {
            for (Runnable handler : handlers) {
                handler.run();
            }
        }
    }

    private boolean isChanged() {
        return !this.changedSettings.isEmpty();
    }
}
