package de.uniba.georacer.model.app;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public static int NUMBER_OF_ROUNDS = 3;
    private static int roundCounter = 0;

    private int currentRound;
    private Location start;
    private Location destination;
    private List<RoundState> roundStates;
    private List<LatLng> waypoints;

    public GameState() {
        roundStates = new ArrayList<>();
        startNewRound();
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

    // TODO defensive copy
    public List<RoundState> getRoundStates() {
        return roundStates;
    }

    private RoundState getCurrentRoundState() {
        return this.roundStates.get(currentRound - 1);
    }

    public void setWaypoints(List<LatLng> waypoints) {
        this.waypoints = waypoints;
    }

    public LatLng getCurrentWaypoint() {
        if (this.waypoints == null) {
            return null;
        }

        return this.waypoints.get(currentRound - 1);
    }

    public List<LatLng> getWaypoints() {
        return this.waypoints;
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
        final int NUMBER_OF_ROUNDS = GameState.NUMBER_OF_ROUNDS;
        return roundCounter >= NUMBER_OF_ROUNDS;
    }


    public void startNewRound() {
        roundCounter++;
        currentRound = roundCounter;
        roundStates.add(new RoundState(roundCounter));
    }

    public void setNumberOfRounds(int rounds) {
        this.NUMBER_OF_ROUNDS = rounds;
    }
}
