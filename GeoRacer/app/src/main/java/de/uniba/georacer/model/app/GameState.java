package de.uniba.georacer.model.app;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static int roundCounter = 0;

    private int currentRound;
    private Location start;
    private Location destination;
    private List<RoundState> roundStates;

    public GameState() {
        roundStates = new ArrayList<>();
        startNewRound();
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int currentRound) {
        this.currentRound = currentRound;
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


    public void startNewRound() {
        roundCounter++;
        currentRound = roundCounter;
        roundStates.add(new RoundState(roundCounter));
    }
}
