package de.uniba.georacer.service.positioning;

import org.ejml.simple.SimpleMatrix;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import de.uniba.georacer.model.json.GeoLocation;

public class PositioningHelper {
    public static final double THRESHOLD = 0.1;
    private static final int MAXTRIES = 100;
    private static int offsetAfterDecimalPoint = 3;
    public static final Logger LOGGER = Logger.getLogger("PositioningHelper");

    public GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        GeoLocation result = new GeoLocation(startingPosition.getLongitude(), startingPosition.getLatitude());
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
            result.setLatitude(result.getLatitude() + correctionVector.get(0, 0));
            result.setLongitude(result.getLongitude() + correctionVector.get(1, 0));

        }
        LOGGER.info("Threshold reached after " + counter + " iterations.");
        return result;
    }

    private SimpleMatrix calculateCorrectionVector(double[][] residuals, double[][] designMatrixArray) {
        SimpleMatrix designMatrix = new SimpleMatrix(designMatrixArray);
        SimpleMatrix residualsVector = new SimpleMatrix(residuals);
        SimpleMatrix designMatrixTransposed = designMatrix.transpose();
        // (A^T*A)^-1
        SimpleMatrix firstEquationMatrix = designMatrixTransposed.mult(designMatrix).invert();
        // A^T*v
        SimpleMatrix secondEquationMatrix = designMatrixTransposed.mult(residualsVector);
        // (A^T*A)^-1 *A^T*v
        SimpleMatrix finalEquationMatrix = firstEquationMatrix.mult(secondEquationMatrix);

        return finalEquationMatrix;
    }

    public double[][] calculateDesignMatrix(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)
        guesses = new TreeMap<>(guesses);
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

    public double[][] calculateDesignMatrixWithOffset(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)
        double[][] designMatrix = new double[guesses.keySet().size()][2];

        List<Double> latitudes = guesses.keySet().stream().map(geo -> geo.getLatitude()).collect(Collectors.toList());
        List<Double> longitudes = guesses.keySet().stream().map(geo -> geo.getLongitude()).collect(Collectors.toList());
        int latitudeOffset = offsetAfterDecimalPoint = determineOffset(latitudes);
        int longitudeOffset = offsetAfterDecimalPoint = determineOffset(longitudes);

        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            double xNumerator = startingPosition.getLatitudeOffset(offsetAfterDecimalPoint) - location.getLatitudeOffset(offsetAfterDecimalPoint);
            double xDivisor = Math.sqrt(Math.pow(location.getLatitudeOffset(offsetAfterDecimalPoint) - startingPosition.getLatitudeOffset(offsetAfterDecimalPoint), 2) + Math.pow(location.getLongitudeOffset(offsetAfterDecimalPoint) - startingPosition.getLongitudeOffset(offsetAfterDecimalPoint), 2));

            double yNumerator = startingPosition.getLongitudeOffset(offsetAfterDecimalPoint) - location.getLongitudeOffset(offsetAfterDecimalPoint);
            double yDivisor = Math.sqrt(Math.pow(location.getLatitudeOffset(offsetAfterDecimalPoint) - startingPosition.getLatitudeOffset(offsetAfterDecimalPoint), 2) + Math.pow(location.getLongitudeOffset(offsetAfterDecimalPoint) - startingPosition.getLongitudeOffset(offsetAfterDecimalPoint), 2));

            designMatrix[i][0] = xNumerator / xDivisor;
            designMatrix[i][1] = yNumerator / yDivisor;

            // increment counter
            i += 1;
        }

        return designMatrix;
    }

    /**
     * Give it a list of doubles, it will determine the offset you can use. This is based on how many digits they have in common.
     * For example 10.881, 10.889, 10.89 will give you an offset of 1, indicating you can split it like so: 10.8 + xy * 10^-1.
     * 0 represents no identical digits after the decimal point.
     *
     * @param values List of doubles
     * @return returns the 10 potency for the offset
     */
    public int determineOffset(List<Double> values) {
        String[] previousValue = String.valueOf(values.get(0)).split("\\.");
        int result = 0;
        // Outer Loop iterates over digits
        for (int i = 0; i < previousValue[1].length(); i++) { // check 10 digits
            // Inner Loop iterates over all locations
            for (int j = 1; j < values.size(); j++) {
                String[] value = String.valueOf(values.get(j)).split("\\.");
                if (value[1].charAt(i) == (previousValue[1].charAt(i))) {
                    if (j == values.size() - 1) { // If all values same at i position, and we have looked at all positions, increase result
                        result = i + 1; // i+1 because we don't want indices, but offset indicators e.g. 10^-3
                    } else {
                        continue;
                    }
                } else {
                    return result;
                }
                previousValue = value;
            }
        }
        return result;
    }


    /**
     * Use longitude-offset as x, and latitude-offset as y.
     *
     * @param guesses Map of GeoLocation and the respective guess by the user
     * @return the residuals in a list
     */
    public double[][] getResiduals(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        guesses = new TreeMap<>(guesses);
        double[][] result = new double[guesses.keySet().size()][1];

        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            Double guess = guesses.get(location);
            double firstSummand = Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2);
            double secondSummand = Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2);
            double sqrt = Math.sqrt(firstSummand + secondSummand);
            result[i][0] = guess - sqrt;
            i++;
        }

        return result;
    }


}
