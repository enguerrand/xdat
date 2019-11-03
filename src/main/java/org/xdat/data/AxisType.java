package org.xdat.data;

public enum AxisType {
    X("X-Axis"),
    Y("Y-Axis");

    private String label;

    AxisType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
