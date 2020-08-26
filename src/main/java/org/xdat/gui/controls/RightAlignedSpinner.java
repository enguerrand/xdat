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

package org.xdat.gui.controls;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;

public class RightAlignedSpinner extends JSpinner {
    public RightAlignedSpinner(SpinnerModel spinnerModel) {
        super(spinnerModel);
        align();
    }

    private void align() {
        ((JSpinner.DefaultEditor)getEditor()).getTextField().setHorizontalAlignment(JTextField.RIGHT);
    }
}
