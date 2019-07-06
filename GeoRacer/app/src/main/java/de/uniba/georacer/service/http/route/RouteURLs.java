package de.uniba.georacer.service.http.route;

/* Ludwig Leuschner
 * initial source: https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */

import android.content.Context;
import android.location.Location;

import de.uniba.ioannidis.christos.georacer.R;

public class RouteURLs {
    public static String getRouteUrl(Location start, Location destination, Context context) {
        String fullShape = "true";
        String directionsUrl = context.getString(R.string.mapquest_api_route_uri) +
                "key=%s&from=%s,%s&to=%s,%s&routeType=%s&fullShape=%s";
        String uri = String.format(directionsUrl,
                context.getString(R.string.mapquest_api_key),
                start.getLatitude(),
                start.getLongitude(),
                destination.getLatitude(),
                destination.getLongitude(),
                "pedestrian",
                fullShape);

        return uri;
    }
}
