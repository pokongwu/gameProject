package de.uniba.georacer;

import android.location.Location;

/**
 * Created by lleuschner on 21.05.19.
 */

public interface GameServiceListener {

    void updatePlayerPosition(Location location);
}
