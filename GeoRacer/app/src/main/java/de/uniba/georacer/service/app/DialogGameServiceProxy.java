package de.uniba.georacer.service.app;

public interface DialogGameServiceProxy {
    public void saveGuess(String landmarkId, double guess);
    public String getGuess(String landmarkId);
}
