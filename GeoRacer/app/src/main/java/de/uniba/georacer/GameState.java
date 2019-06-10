package de.uniba.georacer;

import android.location.Location;

public class GameState {
    private int round;
    private Location start;
    private Location destination;

    public GameState() {
        round = 0;
    }


    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public Location getStart() {
        if(start == null) {
            return null;
        }
        Location _start = new Location("");
        _start.setLatitude(start.getLatitude());
        _start.setLongitude(start.getLongitude());
        return _start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getDestination() {
        if(destination == null) {
            return null;
        }
        Location _destination = new Location("");
        _destination.setLatitude(destination.getLatitude());
        _destination.setLongitude(destination.getLongitude());
        return _destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }
}
