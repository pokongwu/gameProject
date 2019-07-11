package de.uniba.georacer.model.app;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import de.uniba.georacer.model.json.GeoLocation;

public class RoundState {
    private final int round;
    private Map<String, Double> guesses;
    private GeoLocation waypoint;

    public RoundState(int round) {
        this.round = round;
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

    public GeoLocation getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(LatLng currentWaypoint) {
        this.waypoint = GeoLocation.fromWGS84(currentWaypoint.latitude, currentWaypoint.longitude);
    }
}
