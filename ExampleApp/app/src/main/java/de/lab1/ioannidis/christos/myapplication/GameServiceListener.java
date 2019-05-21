package de.lab1.ioannidis.christos.myapplication;

import android.location.Location;

/**
 * Created by lleuschner on 21.05.19.
 */

public interface GameServiceListener {

    void updatePlayerPosition(Location location);
}
