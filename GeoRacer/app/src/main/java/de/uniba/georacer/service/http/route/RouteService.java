package de.uniba.georacer.service.http.route;

/* Ludwig Leuschner
 * initial source: https://www.journaldev.com/13373/android-google-map-drawing-route-two-points
 */

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RouteService extends AsyncTask {
    private final OnRouteServiceFinishedListener onRouteServiceFinishedListener;
    private final int rounds;

    public RouteService(OnRouteServiceFinishedListener onRouteServiceFinishedListener, int rounds) {
        this.onRouteServiceFinishedListener = onRouteServiceFinishedListener;
        this.rounds = rounds;
    }

    @Override
    protected String doInBackground(Object... url) {
        String data = "";

        try {
            data = downloadUrl((String) url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);

        RouteParserTask parserTask = new RouteParserTask(onRouteServiceFinishedListener,rounds);
        parserTask.execute((String) result);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            if(iStream != null) {
                iStream.close();
            }

            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return data;
    }
}
