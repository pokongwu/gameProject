package de.uniba.georacer.service.positioning;

import org.ejml.simple.SimpleMatrix;

import java.util.Map;

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
     * http://rosettacode.org/wiki/Haversine_formula#Java Haversine Formula to calculate approximation of distance
     * between two points on a sphere. Accurate enough for this application context (close distances)
     */
    public static double haversianDistance(GeoLocation location1, GeoLocation location2) {
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
