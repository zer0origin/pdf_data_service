package com.w.callum.pdf_service_data;

import com.w.callum.pdf_service_data.model.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RectangleCollisionTest {

    @Test
    public void RectangleIsInsideBoundsOfRectangleOne(){
        Coordinate rec1 = new Coordinate(0, 200, 0, 200);
        Coordinate rec2 = new Coordinate(100, 150, 100, 150);

        boolean result1 = rec2.isColliding(rec1);
        boolean result2 = rec1.isColliding(rec2);

        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
    }

    @Test
    public void RectangleIsInsideBoundsOfRectangleTwo(){
        Coordinate rec1 = new Coordinate(100, 150, 100, 150);
        Coordinate rec2 = new Coordinate(100, 150, 100, 150);

        boolean result1 = rec2.isColliding(rec1);
        boolean result2 = rec1.isColliding(rec2);

        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
    }

    @Test
    public void RectangleIsInsideBoundsOfRectangleThree (){
        Coordinate rec1 = new Coordinate(100, 150, 100, 150);
        Coordinate rec2 = new Coordinate(100, 200, 100, 200);

        boolean result1 = rec2.isColliding(rec1);
        boolean result2 = rec1.isColliding(rec2);

        Assertions.assertTrue(result1);
        Assertions.assertTrue(result2);
    }

    @Test
    public void RectangleIsOutsideBoundsOfRectangleOne(){
        Coordinate rec1 = new Coordinate(100, 150, 0, 150);
        Coordinate rec2 = new Coordinate(151, 200, 151, 200);

        boolean result1 = rec2.isColliding(rec1);
        boolean result2 = rec1.isColliding(rec2);

        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
    }
}
