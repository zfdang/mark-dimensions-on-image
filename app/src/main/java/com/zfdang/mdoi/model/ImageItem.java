package com.zfdang.mdoi.model;

public class ImageItem {
    private int imageId;
    private final String description;
    private final String date;

    public ImageItem(int imageId, String description, String date) {
        this.imageId = imageId;
        this.description = description;
        this.date = date;
    }

    public int getImageId() {
        return imageId;
    }

    public String getDescription() { return description; }
    public String getDate() { return date; }
}