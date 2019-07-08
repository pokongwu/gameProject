package de.uniba.georacer.service.positioning;

import org.ejml.simple.SimpleMatrix;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.uniba.georacer.model.json.GeoLocation;

public abstract class PositioningHelper {
    protected SimpleMatrix calculateCorrectionVector(double[][] residuals, double[][] designMatrixArray) {
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

    /**
     * Use longitude-offset as x, and latitude-offset as y.
     *
     * @param guesses Map of GeoLocation and the respective guess by the user
     * @return the residuals in a list
     */
    public double[][] getResiduals(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) {
        //guesses = new TreeMap<>(guesses);
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
     * http://rosettacode.org/wiki/Haversine_formula#Java Haversine Formula to calculate approximation of distance
     * between two points on a sphere. Accurate enough for this application context (close distances)
     */
    public static double distFrom(GeoLocation location1, GeoLocation location2) {
        double earthRadius = 6371000; // Returns Meters
        double dLat = Math.toRadians(location2.getLatitude() - location1.getLatitude());
        double dLng = Math.toRadians(location2.getLongitude() - location1.getLongitude());
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(location1.getLatitude())) * Math.cos(Math.toRadians(location2.getLatitude()));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }
}
