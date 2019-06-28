package de.uniba.georacer.positioning;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniba.georacer.model.GeoLocation;
import de.uniba.georacer.service.positioning.PositioningHelper;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class positioningHelperTests {
    Map<GeoLocation, Double> guesses = new HashMap<>();
    GeoLocation startingPosition = new GeoLocation(0, 0);

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

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
            assertEquals(residuals.get(0), realResiduals.get(0), 0.1);
        }
    }

}