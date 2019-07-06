package de.uniba.georacer.service.http.route;

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
            JSONObject jShape = jRoute.getJSONObject("shape");
            List<HashMap<String, String>> path = new ArrayList<>();

            JSONArray jShapePoints = jShape.getJSONArray("shapePoints");

            for(int j = 0; j < jShapePoints.length(); j = j + 2) {
                HashMap<String, String> latlong = new HashMap<>();
                latlong.put("lat", String.valueOf(jShapePoints.get(j)));
                latlong.put("lng", String.valueOf(jShapePoints.get(j+1)));

                path.add(latlong);
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