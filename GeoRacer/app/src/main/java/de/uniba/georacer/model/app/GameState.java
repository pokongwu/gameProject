package de.uniba.georacer.model.app;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniba.georacer.model.json.Landmark;

public class GameState {
    private static int roundCounter = 0;

    private int numberOfRounds;
    private int currentRound;
    private Location start;
    private Location destination;
    private List<RoundState> roundStates;
    private List<LatLng> waypoints;
    private List<Landmark> landmarks;

    public GameState() {
        roundStates = new ArrayList<>();
        startNewRound();
    }

    public List<LatLng> getWaypoints() {
        return waypoints;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
        roundCounter = currentRound;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public List<RoundState> getRoundStates() {
        return Collections.unmodifiableList(this.roundStates);
    }

    private RoundState getCurrentRoundState() {
        return this.roundStates.get(currentRound - 1);
    }

    public void setWaypoints(List<LatLng> waypoints) {
        for (int i = 0; i < roundStates.size(); i++) {
            roundStates.get(i).setWaypoint(waypoints.get(i));
        }
        this.waypoints = waypoints;
    }

    public LatLng getCurrentWaypoint() {
        if (this.waypoints == null) {
            return null;
        }

        return this.waypoints.get(currentRound - 1);
    }

    public String getGuess(String landmarkId) {
        RoundState curRoundState = getCurrentRoundState();
        return curRoundState.getGuess(landmarkId);
    }

    public void saveGuess(String landmarkId, Double guess) {
        RoundState curRoundState = getCurrentRoundState();
        curRoundState.addGuess(landmarkId, guess);
    }

    public boolean isRoundFinished() {
        RoundState curRoundState = getCurrentRoundState();
        return curRoundState.isRoundFinished();
    }

    public boolean isGameFinished() {
        return roundCounter >= numberOfRounds;
    }


    public void startNewRound() {
        roundCounter++;
        currentRound = roundCounter;
        roundStates.add(new RoundState());
    }

    public void setNumberOfRounds(int rounds) {
        this.numberOfRounds = rounds;
    }

    public void setLandmarks(List<Landmark> landmarks) {
        this.landmarks = landmarks;
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }
}
