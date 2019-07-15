package de.uniba.georacer.service.http.route;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Extracts the waypoints from the RouteResponse
 *
 * Known issues: a little bit much redundancy in this class
 *
 * @author Ludwig
 */
public class WaypointExtractor {
    public List<LatLng> getWaypoints(PolylineOptions route, int rounds) {
        List<Location> routeLocations = route.getPoints().stream().map(this::mapLatLngToLocation)
                .collect(Collectors.toList());

        return extractWaypoints(routeLocations, rounds);
    }

    private List<LatLng> extractWaypoints(List<Location> routeLocations, int rounds) {
        double totalDistance = getTotalDistanceFromRoute(routeLocations);
        double waypointDistance = totalDistance / rounds - 1;
        double curDistance = 0;
        Location lastPoint = new Location("");
        lastPoint.setLatitude(routeLocations.get(0).getLatitude());
        lastPoint.setLongitude(routeLocations.get(0).getLongitude());

        List<LatLng> waypoints = new ArrayList<>();
        if(rounds > 1) {
            waypoints.add(new LatLng(lastPoint.getLatitude(), lastPoint.getLongitude()));

            for (Location curLocation : routeLocations) {
                if (waypoints.size() == rounds - 1) {
                    break;
                }

                curDistance += lastPoint.distanceTo(curLocation);
                if (curDistance >= waypointDistance) {
                    waypoints.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
                    curDistance = 0;
                }
                lastPoint = curLocation;
            }
        }

        Location lastLocation = routeLocations.get(routeLocations.size() - 1);
        waypoints.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));

        return waypoints;
    }

    // distance can also be extracted from the json response, this approach was faster to implement..
    private double getTotalDistanceFromRoute(List<Location> routeLocations) {
        double distance = 0;
        Location lastPoint = new Location("");
        lastPoint.setLatitude(routeLocations.get(0).getLatitude());
        lastPoint.setLongitude(routeLocations.get(0).getLongitude());

        for (Location curLocation : routeLocations) {
            distance += lastPoint.distanceTo(curLocation);
            lastPoint = curLocation;
        }

        return distance;
    }

    private Location mapLatLngToLocation(LatLng latLng) {
        Location position = new Location("");
        position.setLatitude(latLng.latitude);
        position.setLongitude(latLng.longitude);

        return position;
    }
}
