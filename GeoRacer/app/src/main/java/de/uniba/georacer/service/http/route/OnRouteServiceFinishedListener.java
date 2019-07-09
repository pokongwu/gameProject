package de.uniba.georacer.service.http.route;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public interface OnRouteServiceFinishedListener {
    void onRouteServiceFinished(PolylineOptions route, List<LatLng> waypoints);
}
