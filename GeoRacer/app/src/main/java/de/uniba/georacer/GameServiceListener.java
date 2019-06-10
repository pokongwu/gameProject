package de.uniba.georacer;

import android.location.Location;

import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by lleuschner on 21.05.19.
 */

public interface GameServiceListener {
    void updatePlayerPosition(Location location);

    void showToast(String message);

    void drawRoute(PolylineOptions route);
}
