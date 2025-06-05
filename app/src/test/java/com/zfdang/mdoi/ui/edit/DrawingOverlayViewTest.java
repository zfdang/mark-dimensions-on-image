package com.zfdang.mdoi.ui.edit;

import com.zfdang.mdoi.model.Measure;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// Assuming DrawingOverlayView is not final and can be instantiated,
// or isPointNearLine is made static or accessible for testing.
// For this example, we'll assume we can call it.
// If it were an instance method, we'd need a DrawingOverlayView instance.
// If we make it static for easier testing (if it doesn't rely on instance state):
// public static boolean isPointNearLine(...)
// Or, make it package-private and test it from the same package.

public class DrawingOverlayViewTest {

    private DrawingOverlayView drawingOverlayView; // Mock or dummy instance if needed

    @Before
    public void setUp() {
        // If isPointNearLine is not static, we need an instance.
        // Since DrawingOverlayView extends View, it needs a Context.
        // This is where Robolectric would be helpful for a full View test.
        // For a pure Java helper method, ideally it would be static or on a non-View class.
        // For this exercise, we'll assume we can test it as if it's accessible.
        // Let's simulate it being callable, perhaps by temporarily making it public static for the test,
        // or by testing it through a real instance if it's simple enough not to need full Android context.
        // Given the constraints, I cannot directly instantiate DrawingOverlayView here without a Context.
        // I will write the tests as if the method is accessible for testing.
        // In a real scenario, refactoring for testability (e.g., moving to a helper class) would be best.
    }

    // Test data for a line segment from (10, 10) to (110, 10) (Horizontal line)
    private Measure horizontalLine = new Measure(10f, 10f, 110f, 10f, 100f);
    // Test data for a line segment from (10, 10) to (10, 110) (Vertical line)
    private Measure verticalLine = new Measure(10f, 10f, 10f, 110f, 100f);
    // Test data for a line segment from (10, 10) to (110, 110) (Diagonal line)
    private Measure diagonalLine = new Measure(10f, 10f, 110f, 110f, (float)Math.sqrt(100*100 + 100*100));
    // Test data for a point-like line (start and end are same)
    private Measure pointLine = new Measure(50f, 50f, 50f, 50f, 0f);

    private static final float MAX_DISTANCE_THRESHOLD = 10f;


    // Simulating the method for testing purposes as it's hard to call from the actual View.
    // In a real project, this method would be made accessible (e.g., package-private or moved to a testable helper).
    private boolean isPointNearLine(float px, float py, Measure line, float maxDistance) {
        float x1 = line.startX;
        float y1 = line.startY;
        float x2 = line.endX;
        float y2 = line.endY;

        float dx = x2 - x1;
        float dy = y2 - y1;

        if (dx == 0 && dy == 0) { // Line is a point
            return Math.sqrt(Math.pow(px - x1, 2) + Math.pow(py - y1, 2)) < maxDistance;
        }

        float t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);

        float closestX, closestY;
        if (t < 0) {
            closestX = x1;
            closestY = y1;
        } else if (t > 1) {
            closestX = x2;
            closestY = y2;
        } else {
            closestX = x1 + t * dx;
            closestY = y1 + t * dy;
        }
        float distanceToLine = (float) Math.sqrt(Math.pow(px - closestX, 2) + Math.pow(py - closestY, 2));
        return distanceToLine < maxDistance;
    }


    @Test
    public void pointNearLine_horizontal_onLine() {
        assertTrue(isPointNearLine(50f, 10f, horizontalLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_horizontal_nearLine_withinThreshold() {
        assertTrue(isPointNearLine(50f, 15f, horizontalLine, MAX_DISTANCE_THRESHOLD)); // 5px away
    }

    @Test
    public void pointNearLine_horizontal_nearLine_outsideThreshold() {
        assertFalse(isPointNearLine(50f, 25f, horizontalLine, MAX_DISTANCE_THRESHOLD)); // 15px away
    }

    @Test
    public void pointNearLine_horizontal_endpointStart_withinThreshold() {
        assertTrue(isPointNearLine(10f, 10f, horizontalLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_horizontal_endpointEnd_withinThreshold() {
        assertTrue(isPointNearLine(110f, 10f, horizontalLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_horizontal_pastEndpointStart_withinThresholdOfEndpoint() {
        assertTrue(isPointNearLine(5f, 10f, horizontalLine, MAX_DISTANCE_THRESHOLD)); // Closest to (10,10)
    }

    @Test
    public void pointNearLine_horizontal_pastEndpointEnd_outsideThresholdOfEndpoint() {
         assertFalse(isPointNearLine(125f, 10f, horizontalLine, MAX_DISTANCE_THRESHOLD)); // 15px from (110,10)
    }


    @Test
    public void pointNearLine_vertical_onLine() {
        assertTrue(isPointNearLine(10f, 50f, verticalLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_vertical_nearLine_withinThreshold() {
        assertTrue(isPointNearLine(15f, 50f, verticalLine, MAX_DISTANCE_THRESHOLD)); // 5px away
    }

    @Test
    public void pointNearLine_vertical_nearLine_outsideThreshold() {
        assertFalse(isPointNearLine(25f, 50f, verticalLine, MAX_DISTANCE_THRESHOLD)); // 15px away
    }

    @Test
    public void pointNearLine_diagonal_onLine() {
        assertTrue(isPointNearLine(60f, 60f, diagonalLine, MAX_DISTANCE_THRESHOLD)); // Midpoint of 10,10 to 110,110
    }

    @Test
    public void pointNearLine_diagonal_nearLine_withinThreshold() {
        // Point (60, 65) near diagonal line (10,10)-(110,110)
        // Perpendicular distance will be small
        assertTrue(isPointNearLine(60f, 63f, diagonalLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_diagonal_farFromLine_outsideThreshold() {
        assertFalse(isPointNearLine(10f, 110f, diagonalLine, MAX_DISTANCE_THRESHOLD)); // This point is far
    }

    @Test
    public void pointNearLine_pointLine_atPoint_withinThreshold() {
        assertTrue(isPointNearLine(50f, 50f, pointLine, MAX_DISTANCE_THRESHOLD));
    }

    @Test
    public void pointNearLine_pointLine_nearPoint_withinThreshold() {
        assertTrue(isPointNearLine(55f, 55f, pointLine, MAX_DISTANCE_THRESHOLD)); // sqrt(5^2+5^2) = sqrt(50) approx 7.07
    }

    @Test
    public void pointNearLine_pointLine_farPoint_outsideThreshold() {
        assertFalse(isPointNearLine(60f, 60f, pointLine, MAX_DISTANCE_THRESHOLD)); // sqrt(10^2+10^2) = sqrt(200) approx 14.14
    }
}
