package de.uniba.georacer.service.http.route;

import com.google.android.gms.maps.model.PolylineOptions;

public interface OnRouteServiceFinishedListener {
    void onRouteServiceFinished(PolylineOptions route);
}
