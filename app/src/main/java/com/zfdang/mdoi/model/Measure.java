package com.zfdang.mdoi.model;

public class Measure {
    public float startX, startY, endX, endY;
    public float pixelLength;
    public float actualLength;
    public String unit;
    public float calibrationFactor; // Stores actualLength / pixelLength
    public String label; // Text label for the ruler

    // Default constructor for deserialization
    public Measure() {
        // Initialize with default values, though Gson might overwrite them
        this.startX = 0;
        this.startY = 0;
        this.endX = 0;
        this.endY = 0;
        this.pixelLength = 0;
        this.actualLength = 0;
        this.unit = "";
        this.calibrationFactor = 0;
        this.label = "";
    }

    public Measure(float sx, float sy, float ex, float ey, float pLength) {
        this.startX = sx;
        this.startY = sy;
        this.endX = ex;
        this.endY = ey;
        this.pixelLength = pLength;
        this.actualLength = 0; // Default actual length
        this.unit = ""; // Default unit
        this.calibrationFactor = 0; // Default calibration factor
        this.label = ""; // Default empty label
    }

    public void setCalibration(float actualLength, String unit) {
        this.actualLength = actualLength;
        this.unit = unit;
        if (this.pixelLength > 0) {
            this.calibrationFactor = actualLength / this.pixelLength;
        } else {
            this.calibrationFactor = 0;
        }
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
