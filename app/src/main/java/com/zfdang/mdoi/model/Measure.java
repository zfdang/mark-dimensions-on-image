package com.zfdang.mdoi.model;

public class Measure {
    public float startX, startY, endX, endY;
    public int length;

    public Measure(float sx, float sy, float ex, float ey, int l) {
        startX = sx;
        startY = sy;
        endX = ex;
        endY = ey;
        length = l;
    }
}
