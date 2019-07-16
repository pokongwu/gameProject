package de.uniba.georacer.service.positioning;

import java.util.Map;

import de.uniba.georacer.model.json.GeoLocation;

/**
 * @author Christos
 */
public interface PositioningHelperI {

    /**
     * IMPORTANT: If the landmarks provided are to a certain degree colinear to each other, the outcome will heavily depend
     * on the starting position. The solution is to avoid colinearity when calling this method. This has been determined to
     * be out of scope for this project. Additionally, to avoid this problem with "almost colinear" landmarks, use a reasonable
     * starting position like one of the landmarks to circumvent numerical instability introduced by using a simplified algorithm.
     *
     * Provide this method with a map of @see {@link de.uniba.georacer.model.json.GeoLocation} and guesses from the player and a starting position.
     * It will the return an approximation of the location based on the guesses.
     *
     * @param guesses map of locations and doubles representing guessed distance
     * @param startingPosition starting position for calculations, use a sensible location (a landmark or Bamberg city center)
     * @return Approximation of the GeoLocation based on the guesses and known landmark locations
     */
    GeoLocation calculatePositionFromGuesses(Map<GeoLocation, Double> guesses, GeoLocation startingPosition) throws DegradedMatrixException;

    double[][] calculateDesignMatrix(Map<GeoLocation, Double> guessesNoOffset, GeoLocation startingPosition);
}
