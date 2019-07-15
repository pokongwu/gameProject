package de.uniba.georacer.service.app;

/**
 * Proxy between the alert dialog for the guesses and the GameService
 *
 * @author Ludwig
 */
public interface DialogGameServiceProxy {
    void saveGuess(String landmarkId, double guess);

    String getGuess(String landmarkId);
}
