package de.uniba.georacer.state;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.model.json.Landmark;

public class GameStateManager {
    private static GameState gameState;
    private final GameStateListener gameStateListener;

    public GameStateManager(GameStateListener gameStateListener) {
        gameState = new GameState();
        this.gameStateListener = gameStateListener;
    }

    public static GameState getGameState() {
        return gameState;
    }

    public int getCurrentRound() {
        return gameState.getCurrentRound();
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
        if (gameState.getDestination() == null) {
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

    public void setWaypoints(List<LatLng> waypoints) {
        gameState.setWaypoints(waypoints);
    }

    public Location getCurrentWaypoint() {
        LatLng waypointLatLng = gameState.getCurrentWaypoint();
        if (waypointLatLng == null) {
            return null;
        }

        Location waypoint = new Location("");
        waypoint.setLatitude(waypointLatLng.latitude);
        waypoint.setLongitude(waypointLatLng.longitude);

        return waypoint;
    }

    public List<LatLng> getWaypoints() {
        return gameState.getWaypoints();
    }

    public String getGuess(String landmarkId) {
        return gameState.getGuess(landmarkId);
    }

    public void saveGuess(String landmarkId, Double guess) {
        gameState.saveGuess(landmarkId, guess);

        if (gameState.isRoundFinished()) {
            if (gameState.isGameFinished()) {
                gameStateListener.triggerGameFinish(gameState);
            } else {
                gameState.startNewRound();
                gameStateListener.triggertNextRound(gameState.getCurrentRound());
            }
        }
    }

    public void setNumberOfRounds(int rounds) {
        gameState.setNumberOfRounds(rounds);
    }

    public void reset() {
        gameState.setCurrentRound(0);
    }

    public void setLandmarks(List<Landmark> landmarks) {
        gameState.setLandmarks(landmarks);
    }
}
