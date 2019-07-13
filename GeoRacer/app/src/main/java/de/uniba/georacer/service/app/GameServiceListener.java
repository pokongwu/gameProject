package de.uniba.georacer.service.app;

import android.location.Location;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by lleuschner on 21.05.19.
 */

public interface GameServiceListener {
    void updatePlayerPosition(Location location);

    void showToast(String message);

    void drawRoute(PolylineOptions routeOption);

    void drawWaypoints(List<CircleOptions> waypointOptions);

    void drawLandmarks(List<MarkerOptions> landmarkOptions);

    void clearLandmarks();
}
