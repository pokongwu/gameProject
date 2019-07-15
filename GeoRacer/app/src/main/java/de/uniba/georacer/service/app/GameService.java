package de.uniba.georacer.service.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.service.http.route.OnRouteServiceFinishedListener;
import de.uniba.georacer.service.http.route.RouteService;
import de.uniba.georacer.service.http.route.RouteURLs;
import de.uniba.georacer.state.GameStateListener;
import de.uniba.georacer.state.GameStateManager;
import de.uniba.georacer.ui.GameFinishActivity;

/**
 * Running Service for the app
 *
 * @author Christos, Ludwig, Pio
 */
public class GameService extends Service
        implements OnRouteServiceFinishedListener, GameStateListener, LocationListener {
    private GameStateManager gameStateManager;
    private LandmarkProvider landmarkProvider;
    private final IBinder binder = new LocalBinder();
    private final List<GameServiceListener> listeners = new ArrayList<GameServiceListener>();
    private Location lastKnownLocation;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        gameStateManager = new GameStateManager(this);
        landmarkProvider = new LandmarkProvider(getApplicationContext());
        gameStateManager.setLandmarks(landmarkProvider.getLandmarks());
        initLocationServices();
        return binder;
    }

    public void resetState() {
        gameStateManager.reset();
    }

    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

    public void registerListener(GameServiceListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(GameServiceListener listener) {
        listeners.remove(listener);
    }

    private void initLocationServices() throws SecurityException {
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = this;

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                5,
                locationListener);
    }

    @Override
    public void onLocationChanged(Location location) {
        lastKnownLocation = location;

        for (GameServiceListener listener : listeners) {
            listener.updatePlayerPosition(location);
            if (gameStateManager.getDestination() == null) {
                listener.showToast("Tap on the map in order to set the destination");
            }
        }

        if(isUserNextToTheWaypoint(location)) {
            retrieveRandomLandmarks();
        }
    }

    private boolean isUserNextToTheWaypoint(Location loacation) {
        Location currentWaypoint = gameStateManager.getCurrentWaypoint();
        if(currentWaypoint == null) {
            return false;
        }

        return currentWaypoint.distanceTo(loacation) <= 45;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void startRoutingToDestination(Location destination, int rounds) {
        if (!gameStateManager.isStartPositionSet()) {
            gameStateManager.setStart(lastKnownLocation);
        }

        gameStateManager.setDestination(destination);
        gameStateManager.setNumberOfRounds(rounds);

        if (gameStateManager.isStartPositionSet()) {
            RouteService routeService = new RouteService(this,rounds);
            String routeUrl = RouteURLs.getRouteUrl(gameStateManager.getStartPosition(),
                    gameStateManager.getDestination(), getApplicationContext());
            routeService.execute(routeUrl);
        } else {
            Log.w("##", "no start position available");
        }
    }

    @Override
    public void onRouteServiceFinished(PolylineOptions routeOptions, List<LatLng> waypoints) {
        if(routeOptions == null) {
            for (GameServiceListener listener : listeners) {
                listener.showToast("Can't find a suitable route, please pick a new destination.");
            }
        }

        gameStateManager.setWaypoints(waypoints);
        List<CircleOptions> waypointOptions =
                new WaypointsOptionGenerator(this,
                        waypoints,
                        gameStateManager.getCurrentRound()).getWaypointOptions();

        for (GameServiceListener listener : listeners) {
            listener.drawRoute(routeOptions);
            listener.drawWaypoints(waypointOptions);
            listener.showToast("Please walk to the next waypoint.");
        }

        if(isUserNextToTheWaypoint(lastKnownLocation)) {
            retrieveRandomLandmarks();
        }
    }

    @Override
    public void triggertNextRound(int currentRound) {
        List<CircleOptions> waypointOptions =
                new WaypointsOptionGenerator(this,
                        gameStateManager.getWaypoints(),
                        gameStateManager.getCurrentRound()).getWaypointOptions();

        for (GameServiceListener listener : listeners) {
            listener.showToast("Please walk to the next waypoint.");
            listener.clearLandmarks();
            listener.drawWaypoints(waypointOptions);
        }
    }

    @Override
    public void triggerGameFinish(GameState gameState) {
        Intent openFinishActivity = new Intent(this, GameFinishActivity.class);
        openFinishActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(openFinishActivity);

        for (GameServiceListener listener : listeners) {
            listener.closeMapView();
        }
    }

    public void retrieveRandomLandmarks() {
        List<MarkerOptions> markers = landmarkProvider.getMarkerOptionsFromLandmarks();

        for (GameServiceListener listener : listeners) {
            listener.drawLandmarks(markers);
            listener.showToast("Tap on the marker and guess the distance.");
        }
    }

    public String getGuess(String landmarkId) {
        return gameStateManager.getGuess(landmarkId);
    }

    public void saveGuess(String landmarkId, Double guess) {
        gameStateManager.saveGuess(landmarkId, guess);
    }
}
