package com.w.callum.pdf_service_data.model;

public record Coordinate(double x1, double x2, double y1, double y2) {
    public boolean isColliding(Coordinate rec2){
        Coordinate rec1 = this;
        boolean checkX = rec1.x1 <= rec2.x2 && rec1.x2 >= rec2.x1;
        boolean checkY = rec1.y1 <= rec2.y2 && rec1.y2 >= rec2.y1;

        return checkX && checkY;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x1=" + x1 +
                ", x2=" + x2 +
                ", y1=" + y1 +
                ", y2=" + y2 +
                '}';
    }
}
