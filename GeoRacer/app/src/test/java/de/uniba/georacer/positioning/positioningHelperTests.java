package de.uniba.georacer.positioning;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.uniba.georacer.model.GeoLocation;
import de.uniba.georacer.service.positioning.PositioningHelper;

import static org.junit.Assert.assertEquals;

public class positioningHelperTests {
    Map<GeoLocation, Double> guessesNoOffset = new HashMap<>();
    Map<GeoLocation, Double> guessesOffset = new HashMap<>();
    GeoLocation startingPosition = new GeoLocation(0, 0);
    PositioningHelper helper = new PositioningHelper();

    @Before
    public void init() {
        GeoLocation l1 = new GeoLocation(-10.0, 20.0);
        GeoLocation l2 = new GeoLocation(100.0, 30.0);
        GeoLocation l3 = new GeoLocation(-20.0, -40.0);
        GeoLocation l4 = new GeoLocation(120.0, -20.0);

        guessesNoOffset.put(l1, 70.0);
        guessesNoOffset.put(l2, 90.0);
        guessesNoOffset.put(l3, 30.0);
        guessesNoOffset.put(l4, 70.0);

        guessesOffset.put(new GeoLocation(10.8865525, 49.8914738), 70.0);
        guessesOffset.put(new GeoLocation(10.8875033, 49.8925600), 90.0);
        guessesOffset.put(new GeoLocation(10.8876731, 49.8925749), 30.0);
        guessesOffset.put(new GeoLocation(10.8888355, 49.8931644), 70.0);
    }

    @Test
    public void residualVectorTest() {
        // Given see init method
        PositioningHelper helper = new PositioningHelper();
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
        double[][] actual = helper.calculateDesignMatrix(guessesNoOffset, startingPosition);
        //Then
        double[][] result = {{0.89, 0.45}, {-0.89, 0.45}, {-0.29, -0.96}, {0.16, -0.99}};

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
    @Ignore //TODO: Calculate expected result values for design matrix
    public void testDesignMatrixOffset() {
        //Given see init
        //When
        double[][] actual = helper.calculateDesignMatrixWithOffset(guessesOffset, startingPosition);
        //Then
        double[][] result = {{0.89, 0.45}, {-0.89, 0.45}, {-0.29, -0.96}, {0.16, -0.99}};


        for (int j = 0; j < actual.length; j++) {
            System.out.println(actual[j][0] + " " + actual[j][1]);
        }


        for (int j = 0; j < result.length; j++) {
            for (int i = 0; i < 2; i++) {
                assertEquals(result[j][i], actual[j][i], 0.2);
            }
        }
    }


    @Test
    public void testOffsetDeterminer() {
        List<Double> latitudes = guessesOffset.keySet().stream().map(geo -> geo.getLatitude()).collect(Collectors.toList());
        List<Double> longitudes = guessesOffset.keySet().stream().map(geo -> geo.getLongitude()).collect(Collectors.toList());
        int latitudeOffset = helper.determineOffset(latitudes);
        int longitudeOffset = helper.determineOffset(longitudes);

        assertEquals(2, latitudeOffset);
        assertEquals(2, longitudeOffset);
    }

    @Test
    public void testOffsetDeterminer2() {
        Map<GeoLocation, Double> testLocations = new HashMap();
        testLocations.put(new GeoLocation(10.888865525, 49.88914738), 10.0);
        testLocations.put(new GeoLocation(10.888875033, 49.88925600), 10.0);
        testLocations.put(new GeoLocation(10.888876731, 49.88925749), 10.0);
        testLocations.put(new GeoLocation(10.88888355, 49.88931644), 10.0);

        List<Double> longitudes = testLocations.keySet().stream().map(geo -> geo.getLongitude()).collect(Collectors.toList());
        List<Double> latitudes = testLocations.keySet().stream().map(geo -> geo.getLatitude()).collect(Collectors.toList());
        int longitudeOffset = helper.determineOffset(longitudes);
        int latitudeOffset = helper.determineOffset(latitudes);

        assertEquals(4, longitudeOffset);
        assertEquals(3, latitudeOffset);
    }

    @Test
    public void testOffsetDeterminer3() {
        Map<GeoLocation, Double> testLocations = new HashMap();
        testLocations.put(new GeoLocation(10.8, 49.1), 10.0);
        testLocations.put(new GeoLocation(10.8, 49.2), 10.0);
        testLocations.put(new GeoLocation(10.8, 49.2), 10.0);
        testLocations.put(new GeoLocation(10.8, 49.2), 10.0);

        List<Double> longitudes = testLocations.keySet().stream().map(geo -> geo.getLongitude()).collect(Collectors.toList());
        List<Double> latitudes = testLocations.keySet().stream().map(geo -> geo.getLatitude()).collect(Collectors.toList());
        int longitudeOffset = helper.determineOffset(longitudes);
        int latitudeOffset = helper.determineOffset(latitudes);

        assertEquals(1, longitudeOffset);
        assertEquals(0, latitudeOffset);
    }

    @Test
    public void testBase1() {
        Map<GeoLocation, Double> testLocations = new HashMap();
        GeoLocation l1 = new GeoLocation(10.88881, 49.1);
        testLocations.put(l1, 10.0);
        GeoLocation l2 = new GeoLocation(10.88882, 49.2);
        testLocations.put(l2, 10.0);


        int longitudeOffset = 3;
        int latitudeOffset = 0;


        assertEquals(10.88, l1.getLongitudeBase(longitudeOffset), 0.01);
        assertEquals(49, l1.getLatitudeBase(latitudeOffset), 0.01);
        assertEquals(10.88, l2.getLongitudeBase(longitudeOffset), 0.01);
        assertEquals(49, l1.getLatitudeBase(latitudeOffset), 0.01);
    }

    @Test
    public void testBase2() {
        Map<GeoLocation, Double> testLocations = new HashMap();
        GeoLocation l1 = new GeoLocation(10, 49.1);
        testLocations.put(l1, 10.0);
        GeoLocation l2 = new GeoLocation(10, 49.2);
        testLocations.put(l2, 10.0);


        int longitudeOffset = 0;
        int latitudeOffset = 0;


        assertEquals(10, l1.getLongitudeBase(longitudeOffset), 0.01);
        assertEquals(49, l1.getLatitudeBase(latitudeOffset), 0.01);
        assertEquals(10, l2.getLongitudeBase(longitudeOffset), 0.01);
        assertEquals(49, l1.getLatitudeBase(latitudeOffset), 0.01);
    }

    @Test
    public void integrationNoOffset() {
        GeoLocation result = helper.calculatePositionFromGuesses(guessesNoOffset, startingPosition);
        assertEquals(31.99265, result.getLongitude(), 0.00001);
        assertEquals(-33.17731, result.getLatitude(), 0.00001);
    }
}