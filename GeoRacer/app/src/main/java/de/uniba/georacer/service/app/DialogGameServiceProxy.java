package de.uniba.georacer.service.app;

public interface DialogGameServiceProxy {
    void saveGuess(String landmarkId, double guess);
    String getGuess(String landmarkId);
}
