package de.uniba.georacer.service.positioning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.uniba.georacer.model.GeoLocation;

public class PositioningHelper {

    public GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        GeoLocation resultLocation = new GeoLocation(0.0, 0.0);

        // 1. Residuals
        List<Double> residuals = getResiduals(guesses, startingPosition);

        // 2. Design Matrix
        double[][] designMatrix = calculateDesignMatrix(guesses, startingPosition);

        // 3. Correction Vector
        // 4. Repeat until sufficient precision reached

        return resultLocation;
    }

    public double[][] calculateDesignMatrix(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)
        double[][] designMatrix = new double[2][guesses.keySet().size()];
        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            double xDenominator = startingPosition.getLatitude() - location.getLatitude();
            double xDivisor = Math.sqrt(Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2) + Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2));

            double yDenominator = startingPosition.getLongitude() - location.getLongitude();
            double yDivisor = Math.sqrt(Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2) + Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2));

            designMatrix[0][i] = (xDenominator / xDivisor);
            designMatrix[1][i] = yDenominator / yDivisor;

            // increment counter
            i += 1;
        }

        return designMatrix;
    }

    private double[][] calculateDesignMatrixWithOffset(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        // Derivative for x:  x - x^i / sqrt((x^i - x)^2 + (y^i - y)^2)
        // 2-Dimensional (x and y values)
        double[][] designMatrix = new double[2][guesses.keySet().size()];

        int i = 0;
        for (GeoLocation location : guesses.keySet()) {
            double xDenominator = startingPosition.getLatitudeOffset() - location.getLatitudeOffset();
            double xDivisor = Math.sqrt(Math.pow(location.getLatitudeOffset() - startingPosition.getLatitudeOffset(), 2) + Math.pow(location.getLongitudeOffset() - startingPosition.getLongitudeOffset(), 2));

            double yDenominator = startingPosition.getLongitudeOffset() - location.getLongitudeOffset();
            double yDivisor = Math.sqrt(Math.pow(location.getLatitudeOffset() - startingPosition.getLatitudeOffset(), 2) + Math.pow(location.getLongitudeOffset() - startingPosition.getLongitudeOffset(), 2));

            designMatrix[i][0] = xDenominator / xDivisor;
            designMatrix[i][1] = yDenominator / yDivisor;

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
    public List<Double> getResiduals(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        List<Double> result = new ArrayList<>();

        for (GeoLocation location : guesses.keySet()) {
            Double guess = guesses.get(location);
            double firstSummand = Math.pow(location.getLongitude() - startingPosition.getLongitude(), 2);
            double secondSummand = Math.pow(location.getLatitude() - startingPosition.getLatitude(), 2);
            double sqrt = Math.sqrt(firstSummand + secondSummand);
            result.add(guess - sqrt);
        }

        return result;
    }


}
