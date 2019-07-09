package de.uniba.georacer.service.http.route;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


//TODO error handling eg. to few points
public class WaypointExtractor {
    private static final int NUMBER_OF_ROUNDS = 3; //TODO get from sharedPrefs (option menu)

    public List<LatLng> getWaypoints(PolylineOptions route) {
        List<Location> routeLocations = route.getPoints().stream().map(this::mapLatLngToLocation)
                .collect(Collectors.toList());

        return extractWaypoints(routeLocations);
    }

    private List<LatLng> extractWaypoints(List<Location> routeLocations) {
        double totalDistance = getTotalDistanceFromRoute(routeLocations);
        double waypointDistance = totalDistance / NUMBER_OF_ROUNDS - 1;
        double curDistance = 0;
        Location lastPoint = new Location("");
        lastPoint.setLatitude(routeLocations.get(0).getLatitude());
        lastPoint.setLongitude(routeLocations.get(0).getLongitude());

        List<LatLng> waypoints = new ArrayList<>();
        waypoints.add(new LatLng(lastPoint.getLatitude(), lastPoint.getLongitude()));

        for (Location curLocation : routeLocations) {
            curDistance += lastPoint.distanceTo(curLocation);
            if(curDistance >= waypointDistance) {
                waypoints.add(new LatLng(curLocation.getLatitude(), curLocation.getLongitude()));
                curDistance = 0;

                if(waypoints.size() == NUMBER_OF_ROUNDS - 1) {
                    break;
                }
            }
            lastPoint = curLocation;
        }

        Location lastLocation = routeLocations.get(routeLocations.size() - 1);
        waypoints.add(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()));

        return waypoints;
    }

    //TODO distance can also be extracted from the json response
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
