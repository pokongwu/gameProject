package de.uniba.georacer.model.app;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the state for each round (the guesses)
 *
 * @author Christos, Ludwig
 */
public class RoundState {
    private Map<String, Double> guesses;

    public RoundState() {
        guesses = new HashMap<>();
    }

    public String getGuess(String landmarkId) {
        if (!guesses.containsKey(landmarkId)) {
            return "";
        }

        return String.valueOf(guesses.get(landmarkId));
    }

    public void addGuess(String landmarkId, Double guess) {
        this.guesses.put(landmarkId, guess);
    }

    public Map<String, Double> getGuesses() {
        return guesses;
    }

    public boolean isRoundFinished() {
        final int NUMBER_OF_GUESSES = 4;
        return guesses.size() >= NUMBER_OF_GUESSES;
    }
}
