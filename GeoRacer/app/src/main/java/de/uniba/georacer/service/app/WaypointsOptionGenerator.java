package de.uniba.georacer.service.app;

import android.content.Context;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.stream.Collectors;

import de.uniba.georacer.R;

/**
 * Generates the options for the waypoints, including the correct stroke color.
 *
 * @author Ludwig
 */
public class WaypointsOptionGenerator {
    private static final int WAYPOINT_RADIUS = 50;

    private final Context context;
    private final List<LatLng> waypoints;
    private final int currentRound;

    private int waypointCounter;

    public WaypointsOptionGenerator(Context context, List<LatLng> waypoints, int currentRound) {
        this.waypoints = waypoints;
        this.currentRound = currentRound;
        this.context = context;
        this.waypointCounter = 0;
    }

    public List<CircleOptions> getWaypointOptions() {
        return waypoints.stream().map(this::mapLatLngToWaypointOption)
                .collect(Collectors.toList());
    }

    private CircleOptions mapLatLngToWaypointOption(LatLng waypoint) {
        int strokeColor = ++waypointCounter < currentRound ?
                context.getColor(R.color.waypointStrokeVisited) :
                context.getColor(R.color.waypointStrokeUnvisited);

        return new CircleOptions()
                .center(waypoint).radius(WAYPOINT_RADIUS).strokeColor(strokeColor);
    }
}
