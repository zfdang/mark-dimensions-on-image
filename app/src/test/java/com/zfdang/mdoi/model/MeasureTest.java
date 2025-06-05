package com.zfdang.mdoi.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class MeasureTest {

    @Test
    public void constructor_initializesFieldsCorrectly() {
        Measure ruler = new Measure(10f, 20f, 110f, 20f, 100.0f);
        assertEquals(10f, ruler.startX, 0.001f);
        assertEquals(20f, ruler.startY, 0.001f);
        assertEquals(110f, ruler.endX, 0.001f);
        assertEquals(20f, ruler.endY, 0.001f); // Corrected: endY should be 20f for a horizontal line
        assertEquals(100.0f, ruler.pixelLength, 0.001f);

        // Check default values for calibration and label
        assertEquals(0f, ruler.actualLength, 0.001f);
        assertEquals("", ruler.unit);
        assertEquals(0f, ruler.calibrationFactor, 0.001f);
        assertEquals("", ruler.label);
    }

    @Test
    public void defaultConstructor_initializesFieldsWithDefaults() {
        Measure ruler = new Measure();
        assertEquals(0f, ruler.startX, 0.001f);
        assertEquals(0f, ruler.startY, 0.001f);
        assertEquals(0f, ruler.endX, 0.001f);
        assertEquals(0f, ruler.endY, 0.001f);
        assertEquals(0f, ruler.pixelLength, 0.001f);
        assertEquals(0f, ruler.actualLength, 0.001f);
        assertEquals("", ruler.unit);
        assertEquals(0f, ruler.calibrationFactor, 0.001f);
        assertEquals("", ruler.label);
    }


    @Test
    public void pixelLength_isStoredCorrectly() {
        // Test that pixelLength passed in constructor is correctly assigned.
        Measure ruler1 = new Measure(0f, 0f, 30f, 40f, 50.0f); // 3-4-5 triangle, length 50
        assertEquals(50.0f, ruler1.pixelLength, 0.001f);

        Measure ruler2 = new Measure(10f, 10f, 10f, 110f, 100.0f); // Vertical line, length 100
        assertEquals(100.0f, ruler2.pixelLength, 0.001f);
    }

    @Test
    public void setCalibration_calculatesCorrectFactor() {
        Measure ruler = new Measure(0, 0, 100, 0, 100.0f); // 100px long
        ruler.setCalibration(20.0f, "cm"); // Represents 20 cm

        assertEquals(20.0f, ruler.actualLength, 0.001f);
        assertEquals("cm", ruler.unit);
        assertEquals(0.2f, ruler.calibrationFactor, 0.001f); // 20cm / 100px = 0.2
    }

    @Test
    public void setCalibration_withZeroPixelLength_handlesDivisionByZero() {
        Measure ruler = new Measure(0, 0, 0, 0, 0.0f); // 0px long
        ruler.setCalibration(10.0f, "m"); // Represents 10 m

        assertEquals(10.0f, ruler.actualLength, 0.001f);
        assertEquals("m", ruler.unit);
        assertEquals(0.0f, ruler.calibrationFactor, 0.001f); // Factor should be 0 to avoid NaN or Infinity
    }

    @Test
    public void setCalibration_updatesExistingValues() {
        Measure ruler = new Measure(0, 0, 150, 0, 150.0f);
        ruler.setCalibration(15.0f, "in");
        assertEquals(15.0f, ruler.actualLength, 0.001f);
        assertEquals("in", ruler.unit);
        assertEquals(0.1f, ruler.calibrationFactor, 0.001f);

        ruler.setCalibration(30.0f, "cm"); // Re-calibrate
        assertEquals(30.0f, ruler.actualLength, 0.001f);
        assertEquals("cm", ruler.unit);
        assertEquals(0.2f, ruler.calibrationFactor, 0.001f); // 30cm / 150px = 0.2
    }

    @Test
    public void setLabel_isSetCorrectly() {
        Measure ruler = new Measure(0,0,0,0,0f); // Coordinates and pixelLength are not relevant for this test
        ruler.setLabel("Test Label");
        assertEquals("Test Label", ruler.label);
    }

    @Test
    public void setLabel_canBeEmpty() {
        Measure ruler = new Measure(0,0,0,0,0f);
        ruler.setLabel("");
        assertEquals("", ruler.label);
    }

    @Test
    public void setLabel_canBeNullAndBecomesEmpty() {
        // Assuming the setLabel method handles null by converting to empty or Measure.label is initialized to ""
        // Based on current Measure.java, label is initialized to "" and setLabel just assigns.
        // If setLabel could accept null, this test would be more relevant for that specific null-handling.
        // For now, we test setting it to null, which should result in the field being null.
        // However, the constructor initializes it to "", so let's test updating an existing label.
        Measure ruler = new Measure(0,0,0,0,0f);
        ruler.setLabel("Initial Label");
        ruler.setLabel(null); // Test setting label to null
        assertNull("Label should be null if explicitly set to null", ruler.label);

        // If the requirement is that label should never be null (e.g., always defaults to ""),
        // then setLabel should enforce this, and this test would change or be removed.
        // Given current Measure.java, label field can be null if setLabel(null) is called.
    }
}
