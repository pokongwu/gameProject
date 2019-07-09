package de.uniba.georacer.service.positioning;

import org.ejml.simple.SimpleMatrix;

import java.util.Comparator;
import java.util.Map;
import java.util.logging.Logger;

import de.uniba.georacer.model.json.GeoLocation;


public class PositioningHelperUTM extends PositioningHelper implements PositioningHelperI {
    public static final double THRESHOLD = 0.000000001;
    private static final int MAXTRIES = 100;
    public static final Logger LOGGER = Logger.getLogger("PositioningHelperUTM");

    public GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        GeoLocation utmResult = GeoLocation.fromWGS84(startingPosition.getLatitude(), startingPosition.getLongitude());

        double vectorLength = 100;
        int counter = 0;
        while (vectorLength > THRESHOLD || counter >= MAXTRIES) {
            counter++;

            // 1. Residuals
            double[][] residuals = getResiduals(guesses, utmResult);

            // 2. Design Matrix
            double[][] designMatrixArray = calculateDesignMatrix(guesses, utmResult);

            // 3. Correction Vector
            SimpleMatrix correctionVector = calculateCorrectionVector(residuals, designMatrixArray);
            vectorLength = Math.sqrt(Math.pow(correctionVector.get(0, 0), 2) + Math.pow(correctionVector.get(1, 0), 2));

            // 4. Repeat until sufficient precision reached
            UTM utm = new UTM(32, 'U', utmResult.getEastling() + correctionVector.get(0, 0), utmResult.getNorthling() + correctionVector.get(1, 0));
            utmResult = GeoLocation.fromUTM(utm.getEasting(), utm.getNorthing());

            LOGGER.info("lat " + utmResult.getLatitude() + " long " + utmResult.getLongitude());

        }
        LOGGER.info("Threshold reached after " + counter + " iterations.");

        return utmResult;
    }


    public double[][] calculateDesignMatrix(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)
        double[][] designMatrix = new double[guesses.keySet().size()][2];

        int i = 0;
        for (GeoLocation location : guesses.keySet()) {

            double xNumerator = startingPosition.getEastling() - location.getEastling();
            double xDivisor = Math.sqrt(Math.pow(location.getEastling() - startingPosition.getEastling(), 2) + Math.pow(location.getNorthling() - startingPosition.getNorthling(), 2));

            double yNumerator = startingPosition.getNorthling() - location.getNorthling();
            double yDivisor = Math.sqrt(Math.pow(location.getEastling() - startingPosition.getEastling(), 2) + Math.pow(location.getNorthling() - startingPosition.getNorthling(), 2));


            designMatrix[i][0] = xNumerator / xDivisor;
            designMatrix[i][1] = yNumerator / yDivisor;

            // increment counter
            i += 1;
        }

        return designMatrix;
    }


    /**
     * Use longitude-offset as x, and latitude-offset as y.
     *
     * @param guesses Map of GeoLocation and the respective guess by the user
     * @return the residuals in a list
     */
    public double[][] getResiduals(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        double[][] result = new double[guesses.keySet().size()][1];

        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            Double guess = guesses.get(location);

            double firstSummand = Math.pow(location.getNorthling() - startingPosition.getNorthling(), 2);
            double secondSummand = Math.pow(location.getEastling() - startingPosition.getEastling(), 2);
            double sqrt = Math.sqrt(firstSummand + secondSummand);


            result[i][0] = guess - sqrt;
            i++;
        }

        return result;
    }

}
