package de.uniba.georacer.service.positioning;

import java.util.Map;

import de.uniba.georacer.model.GeoLocation;

public interface PositioningHelperI {

    GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guessesNoOffset, GeoLocation startingPosition);

    double[][] calculateDesignMatrix(Map<GeoLocation, Double> guessesNoOffset, GeoLocation startingPosition);
}
