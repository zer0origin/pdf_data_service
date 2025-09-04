package com.w.callum.pdf_service_data.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public class PostMetaResponse {
    @JsonProperty("height")
    private float height;
    @JsonProperty("width")
    private float width;
    @JsonProperty("numberOfPages")
    private int noOfPages;
    @JsonProperty("images")
    private Map<Integer, byte[]> images;

    @JsonCreator
    public PostMetaResponse() {
    }

    public PostMetaResponse(float height, float width, Map<Integer, byte[]> images) {
        this.height = height;
        this.width = width;
        this.images = images;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getNoOfPages() {
        return noOfPages;
    }

    public void setNoOfPages(int noOfPages) {
        this.noOfPages = noOfPages;
    }

    public Map<Integer, byte[]> getImages() {
        return images;
    }

    public void setImages(Map<Integer, byte[]> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PostMetaResponse{" +
                "height=" + height +
                ", width=" + width +
                ", noOfPages=" + noOfPages +
                '}';
    }
}
