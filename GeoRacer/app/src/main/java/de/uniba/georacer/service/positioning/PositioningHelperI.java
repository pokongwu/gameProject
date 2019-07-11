package de.uniba.georacer.service.positioning;

import java.util.Map;

import de.uniba.georacer.model.json.GeoLocation;

public interface PositioningHelperI {

    /**
     * IMPORTANT: The starting position HAS to be relatively close the the location of the landmarks and the player.
     * The simplifications of the algorithm cause it to be numerically instable for starting positions that are too
     * far away. Use for instance one of the landmarks as starting position (maybe min lat/lon?)
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
