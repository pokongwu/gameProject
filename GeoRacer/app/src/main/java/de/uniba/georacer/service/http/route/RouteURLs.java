package de.uniba.georacer.service.http.route;

import android.content.Context;
import android.location.Location;

import de.uniba.georacer.R;

/**
 * Building the URL for the RouteService.
 *
 * @author Ludwig
 * initial source: https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */
public class RouteURLs {
    public static String getRouteUrl(Location start, Location destination, Context context) {
        String fullShape = "true";
        String directionsUrl = context.getString(R.string.mapquest_api_route_uri) +
                "key=%s&from=%s,%s&to=%s,%s&routeType=%s&fullShape=%s";

        return String.format(directionsUrl,
                context.getString(R.string.mapquest_api_key),
                start.getLatitude(),
                start.getLongitude(),
                destination.getLatitude(),
                destination.getLongitude(),
                "pedestrian",
                fullShape);
    }
}
