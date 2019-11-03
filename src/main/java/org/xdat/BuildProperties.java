package org.xdat;

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

import java.io.IOException;
import java.util.Properties;

public class BuildProperties extends Properties{

    public static final String BUILD_DATE = "build.date";
    public static final String VERSION = "version";

    public BuildProperties() {
        try {
            load(getClass().getClassLoader().getResourceAsStream("build.properties"));
        } catch (IOException e1) {
            //ignore
        }
    }

    public String getVersion(){
        return getProperty(VERSION, "Unknown");
    }

    public String getBuildDate(){
        return getProperty(BUILD_DATE, "Unknown");
    }
}
