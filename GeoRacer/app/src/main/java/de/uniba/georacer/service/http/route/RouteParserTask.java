package de.uniba.georacer.service.http.route;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * AsynTask for the RouteParsing.
 *
 * Known issues: would be cleaner with GSON.
 *
 * @author Ludwig
 * initial source: https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */
public class RouteParserTask  extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    final OnRouteServiceFinishedListener onRouteServiceFinishedListener;
    private final int rounds;

    public RouteParserTask(OnRouteServiceFinishedListener onRouteServiceFinishedListener, int rounds) {
        this.onRouteServiceFinishedListener = onRouteServiceFinishedListener;
        this.rounds = rounds;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();

            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        PolylineOptions route = null;

        for (int i = 0; i < result.size(); i++) {
            ArrayList points = new ArrayList();
            route = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);
                
                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            route.addAll(points);
            route.width(12);
            route.color(Color.RED);
            route.geodesic(true);

        }

        List<LatLng> waypoints = new WaypointExtractor().getWaypoints(route, rounds);

        onRouteServiceFinishedListener.onRouteServiceFinished(route, waypoints);
    }
}
