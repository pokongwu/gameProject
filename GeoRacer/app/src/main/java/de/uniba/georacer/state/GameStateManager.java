package de.uniba.georacer.state;

import android.location.Location;

import de.uniba.georacer.model.app.GameState;

public class GameStateManager {
    private final GameState gameState;
    private final GameStateListener gameStateListener;

    public GameStateManager(GameStateListener gameStateListener) {
        this.gameState = new GameState();
        this.gameStateListener = gameStateListener;
    }

    public boolean isStartPositionSet() {
        return gameState.getStart() != null;
    }

    public void setStart(Location start) {
        gameState.setStart(start);
    }

    public Location getStartPosition() {
        return gameState.getStart();
    }

    public Location getDestination() {
        if(gameState.getDestination() == null) {
            return null;
        }

        Location _destination = new Location("");
        _destination.setLatitude(gameState.getDestination().getLatitude());
        _destination.setLongitude(gameState.getDestination().getLongitude());

        return _destination;
    }

    public void setDestination(Location destination) {
        gameState.setDestination(destination);
    }

    public void saveGuess(String landmarkId, Double guess) {
        gameState.saveGuess(landmarkId, guess);

        if(gameState.isRoundFinished()) {
            gameStateListener.triggertNextRound(gameState.getCurrentRound());
        }
    }
}
