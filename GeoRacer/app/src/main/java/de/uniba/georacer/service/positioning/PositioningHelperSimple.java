package de.uniba.georacer.service.positioning;

import org.ejml.simple.SimpleMatrix;

import java.util.Map;
import java.util.logging.Logger;

import de.uniba.georacer.model.json.GeoLocation;


/**
 * WARNING: This class is mainly for testing purposes to analyze the algorithms working.
 */
public class PositioningHelperSimple extends PositioningHelper implements PositioningHelperI {
    public static final double THRESHOLD = 0.1;
    private static final int MAXTRIES = 100;
    public static final Logger LOGGER = Logger.getLogger("PositioningHelperSimple");

    public GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        GeoLocation result = GeoLocation.fromWGS84(startingPosition.getLatitude(), startingPosition.getLongitude());
        double vectorLength = 100;
        int counter = 0;
        while (vectorLength > THRESHOLD || counter >= MAXTRIES) {
            counter++;
            // 1. Residuals
            double[][] residuals = getResiduals(guesses, result);

            // 2. Design Matrix
            double[][] designMatrixArray = calculateDesignMatrix(guesses, result);

            // 3. Correction Vector
            SimpleMatrix correctionVector = calculateCorrectionVector(residuals, designMatrixArray);
            vectorLength = Math.sqrt(Math.pow(correctionVector.get(0, 0), 2) + Math.pow(correctionVector.get(1, 0), 2));
            // 4. Repeat until sufficient precision reached

            result = GeoLocation.fromWGS84(result.getLatitude() + correctionVector.get(0, 0), result.getLongitude() + correctionVector.get(1, 0));

        }
        LOGGER.info("Threshold reached after " + counter + " iterations.");
        return result;
    }


    public double[][] calculateDesignMatrix(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)

        double[][] designMatrix = new double[guesses.keySet().size()][2];
        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            double xNumerator = startingPosition.getLatitude() - location.getLatitude();
            double xDivisor = Math.sqrt(Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2) + Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2));

            double yNumerator = startingPosition.getLongitude() - location.getLongitude();
            double yDivisor = Math.sqrt(Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2) + Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2));

            designMatrix[i][0] = (xNumerator / xDivisor);
            designMatrix[i][1] = yNumerator / yDivisor;

            // increment counter
            i += 1;
        }

        return designMatrix;
    }


}
