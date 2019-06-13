package de.uniba.georacer.service.route;

/* Ludwig Leuschner
 * initial source: https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */

import android.content.Context;
import android.location.Location;

import de.uniba.ioannidis.christos.georacer.R;

public class RouteURLs {
    public static String getDirectionsUrl(Location start, Location destination, Context context) {
        StringBuilder directionsUrl = new StringBuilder();

        directionsUrl.append(context.getString(R.string.mapquest_api_base_uri));
        directionsUrl.append("key=")
                .append(context.getString(R.string.mapquest_api_key));

        directionsUrl.append("&");

        directionsUrl.append("from=")
                .append(start.getLatitude())
                .append(",")
                .append(start.getLongitude());

        directionsUrl.append("&");

        directionsUrl.append("to=")
                .append(destination.getLatitude())
                .append(",")
                .append(destination.getLongitude());

        directionsUrl.append("&");

        directionsUrl.append("routeType=" + "pedestrian");

        return directionsUrl.toString();
    }
}
