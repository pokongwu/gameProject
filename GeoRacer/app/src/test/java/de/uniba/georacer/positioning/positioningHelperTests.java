package de.uniba.georacer.positioning;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniba.georacer.model.GeoLocation;
import de.uniba.georacer.service.positioning.PositioningHelper;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class positioningHelperTests {
    Map<GeoLocation, Double> guesses = new HashMap<>();
    GeoLocation startingPosition = new GeoLocation(0, 0);
    PositioningHelper helper = new PositioningHelper();

    @Before
    public void init() {
        GeoLocation l1 = new GeoLocation(-10, 20);
        GeoLocation l2 = new GeoLocation(100, 30);
        GeoLocation l3 = new GeoLocation(-20, -40);
        GeoLocation l4 = new GeoLocation(120, -20);

        guesses.put(l1, 70.0);
        guesses.put(l2, 90.0);
        guesses.put(l3, 30.0);
        guesses.put(l4, 70.0);
    }

    //TODO: Ordering varies, since map is used internally
    @Test
    public void residualVectorTest() {
        // Given see init method
        PositioningHelper helper = new PositioningHelper();
        // When
        List<Double> residuals = helper.getResiduals(guesses, startingPosition);
        // Then (Example from slides)
        List<Double> realResiduals = new ArrayList<>();
        realResiduals.add(47.6);
        realResiduals.add(-14.4);
        realResiduals.add(-14.7);
        realResiduals.add(-51.7);

        for (int i = 0; i < residuals.size(); i++) {
            assertEquals(realResiduals.get(i), residuals.get(i), 0.1);
        }

    }

    @Test
    public void testDesignMatrix() {
        //Given see init
        //When
        double[][] actual = helper.calculateDesignMatrix(guesses, startingPosition);
        //Then
        double[][] result = {{-0.89, -0.29, 0.89, 0.16}, {0.45, -0.96, 0.45, -0.99}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < result[0].length; j++) {
                assertEquals(result[i][j], actual[i][j], 0.2);
            }
        }

    }

}