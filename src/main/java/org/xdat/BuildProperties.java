package org.xdat;

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
