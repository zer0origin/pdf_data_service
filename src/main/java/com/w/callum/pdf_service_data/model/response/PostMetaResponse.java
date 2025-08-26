package com.w.callum.pdf_service_data.model.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostMetaResponse {
    @JsonProperty("height")
    private float height;
    @JsonProperty("width")
    private float width;
    @JsonProperty("noOfPages")
    private int noOfPages;
    @JsonProperty("images")
    private List<String> images;

    @JsonCreator
    public PostMetaResponse() {
    }

    public PostMetaResponse(float height, float width, int noOfPages, List<String> images) {
        this.height = height;
        this.width = width;
        this.noOfPages = noOfPages;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
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
