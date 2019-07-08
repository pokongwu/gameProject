package de.uniba.georacer.positioning;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniba.georacer.model.json.GeoLocation;
import de.uniba.georacer.service.positioning.PositioningHelperI;
import de.uniba.georacer.service.positioning.PositioningHelperNoOffset;
import de.uniba.georacer.service.positioning.PositioningHelperUTM;
import de.uniba.georacer.service.positioning.UTM;
import de.uniba.georacer.service.positioning.WGS84;

import static org.junit.Assert.assertEquals;

public class positioningHelperTests {
    Map<GeoLocation, Double> guessesNoOffset = new HashMap<>();
    Map<GeoLocation, Double> guessesOffset = new HashMap<>();
    Map<GeoLocation, Double> guessesOffset2 = new HashMap<>();
    GeoLocation startingPosition = GeoLocation.fromWGS84(0, 0);
    PositioningHelperI helperNoOffset = new PositioningHelperNoOffset();
    PositioningHelperI helperOffset = new PositioningHelperUTM();

    @Before
    public void init() {
        GeoLocation l1 = GeoLocation.fromWGS84(20.0, -10.0);
        GeoLocation l2 = GeoLocation.fromWGS84(30.0, 100.0);
        GeoLocation l3 = GeoLocation.fromWGS84(-40.0, -20.0);
        GeoLocation l4 = GeoLocation.fromWGS84(-20.0, 120.0);

        guessesNoOffset.put(l1, 70.0);
        guessesNoOffset.put(l2, 90.0);
        guessesNoOffset.put(l3, 30.0);
        guessesNoOffset.put(l4, 70.0);

        guessesOffset.put(GeoLocation.fromWGS84(49.8914738, 10.8865525), 200.0); // Basanese
        guessesOffset.put(GeoLocation.fromWGS84(49.890886, 10.882925), 140.0); // Dom
        guessesOffset.put(GeoLocation.fromWGS84(49.8925749, 10.8876731), 250.0); // Café Austraße
        guessesOffset.put(GeoLocation.fromWGS84(49.8931644, 10.8888355), 400.0); // Grüner Markt

        guessesOffset2.put(GeoLocation.fromWGS84(49.8914738, 10.8865525), 265.0); // Basanese
        guessesOffset2.put(GeoLocation.fromWGS84(49.890886, 10.882925), 230.0); // Dom
        guessesOffset2.put(GeoLocation.fromWGS84(49.8925749, 10.8876731), 190.0); // Café Austraße
        guessesOffset2.put(GeoLocation.fromWGS84(49.8931644, 10.8888355), 250.0); // Grüner Markt
    }

    @Test
    public void residualVectorTest() {
        // Given see init method
        PositioningHelperNoOffset helper = new PositioningHelperNoOffset();
        // When
        double[][] residuals = helper.getResiduals(guessesNoOffset, startingPosition);

        // Then (Example from slides)
        List<Double> realResiduals = new ArrayList<>();
        realResiduals.add(-14.7);
        realResiduals.add(47.6);
        realResiduals.add(-14.4);
        realResiduals.add(-51.7);
        for (int i = 0; i < residuals.length; i++) {
            assertEquals(realResiduals.get(i), residuals[i][0], 0.1);
        }
    }

    @Test
    public void testDesignMatrixNoOffset() {
        //Given see init
        //When
        double[][] actual = helperNoOffset.calculateDesignMatrix(guessesNoOffset, startingPosition);
        //Then
        double[][] result = {{-0.89, 0.45}, {-0.29, -0.96}, {0.89, 0.45}, {0.16, -0.99}};

        /*
        for (int j = 0; j < actual.length; j++) {
            System.out.println(actual[j][0] + " " + actual[j][1]);
        }
        */

        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < 2; i++) {
                assertEquals(result[j][i], actual[j][i], 0.2);
            }
        }
    }


    @Test
    public void integrationNoOffset() {
        GeoLocation result = helperNoOffset.calculatePositionFromGuesses(guessesNoOffset, startingPosition);
        assertEquals(31.99265, result.getLongitude(), 0.00001);
        assertEquals(-33.17731, result.getLatitude(), 0.00001);
    }

    @Test
    public void integrationWithOffset() {
        GeoLocation result = helperOffset.calculatePositionFromGuesses(guessesOffset, GeoLocation.fromWGS84(49.89, 10.88));
        assertEquals(10.884, result.getLongitude(), 0.001);
        assertEquals(49.892, result.getLatitude(), 0.001);
    }

    @Test
    public void integrationWithOffset2() {
        GeoLocation result = helperOffset.calculatePositionFromGuesses(guessesOffset2, GeoLocation.fromWGS84(49.9, 10.0));
        assertEquals(10.885722, result.getLongitude(), 0.001);
        assertEquals(49.893774, result.getLatitude(), 0.001);
    }


    @Test
    public void integrationWithOffsetZero() {
        GeoLocation result = helperOffset.calculatePositionFromGuesses(guessesOffset, GeoLocation.fromWGS84(49, 10));
        assertEquals(10.884, result.getLongitude(), 0.001);
        assertEquals(49.892, result.getLatitude(), 0.001);
    }

    @Test
    public void integrationWithOffsetSlightlyWorseStart() {
        GeoLocation result = helperOffset.calculatePositionFromGuesses(guessesOffset, GeoLocation.fromWGS84(49.7, 10.8));
        assertEquals(10.884, result.getLongitude(), 0.001);
        assertEquals(49.892, result.getLatitude(), 0.001);
    }

    @Test
    public void conversionTest() {
        WGS84 wgs84 = new WGS84(49.89, 10.88);
        UTM utm = new UTM(wgs84);
        assertEquals(utm.getEasting(), 635037.175, 0.1);
        assertEquals(utm.getNorthing(), 5528095.179, 0.1);
    }
}