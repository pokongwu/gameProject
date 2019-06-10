package de.uniba.georacer.service.route;

/* Ludwig Leuschner
 * initial source: https://github.com/bashantad/Tourfit-android/blob/master/src/com/example/tourfit/DirectionsJSONParser.java
 */

/*
 * Useful to track user
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectionsJSONParser {

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and
     * longitude
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {
        List<List<HashMap<String, String>>> routes = new ArrayList<>();

        try {
            JSONObject jRoute = jObject.getJSONObject("route");
            JSONArray jLegs = jRoute.getJSONArray("legs");
            List<HashMap<String, String>> path = new ArrayList<>();

            /** Traversing all legs */
            for (int j = 0; j < jLegs.length(); j++) {
                JSONArray jManeuvers = ((JSONObject) jLegs.get(j)).getJSONArray("maneuvers");

                /** Traversing all steps */
                for (int k = 0; k < jManeuvers.length(); k++) {
                    HashMap<String, String> latlong = new HashMap<>();
                    JSONObject maneuver = jManeuvers.getJSONObject(k);
                    JSONObject startPoint = maneuver.getJSONObject("startPoint");
                    latlong.put("lat", String.valueOf(startPoint.get("lat")));
                    latlong.put("lng", String.valueOf(startPoint.get("lng")));

                    path.add(latlong);
                }
            }
            routes.add(path);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return routes;
    }
}