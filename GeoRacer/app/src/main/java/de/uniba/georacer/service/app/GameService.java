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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.uniba.georacer.state.GameStateListener;
import de.uniba.georacer.state.GameStateManager;
import de.uniba.georacer.service.http.route.OnRouteServiceFinishedListener;
import de.uniba.georacer.model.json.Landmark;
import de.uniba.georacer.parsing.LandmarkProvider;
import de.uniba.georacer.service.http.route.RouteService;
import de.uniba.georacer.service.http.route.RouteURLs;

public class GameService extends Service implements OnRouteServiceFinishedListener, GameStateListener {
    private GameStateManager gameStateManager;
    private LandmarkProvider landmarkProvider;
    private final IBinder binder = new LocalBinder();
    private final List<GameServiceListener> listeners = new ArrayList<GameServiceListener>();


    // ===== Service Methods =====

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        gameStateManager = new GameStateManager(this);
        landmarkProvider = new LandmarkProvider(getApplicationContext());
        initLocationServices();
        return binder;
    }

    // ===== Local Binder =====

    public class LocalBinder extends Binder {
        public GameService getService() {
            return GameService.this;
        }
    }

    // ===== Game Service Listener Methods =====

    public void registerListener(GameServiceListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(GameServiceListener listener) {
        listeners.remove(listener);
    }


    // ===== Event Handling =====
    private void initLocationServices() throws SecurityException {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                System.out.println("on location changed..");

                // TODO init on another place or should user define the start location?
                if (!gameStateManager.isStartPositionSet()) {
                    gameStateManager.setStart(location);
                }


                // Alert Listeners about changed player position
                for (GameServiceListener listener : listeners) {
                    listener.updatePlayerPosition(location);
                    if (gameStateManager.getDestination() == null) {
                        listener.showToast("Tap on the map in order to set the destination");
                    } else {
                        listener.showToast("Current destination is " + gameStateManager.getDestination().getLatitude() + ", " + gameStateManager.getDestination().getLongitude());
                    }
                }
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
        };

        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //    return;
        //}
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, locationListener);
    }

    public void startRoutingToDestination(Location destination) {
        gameStateManager.setDestination(destination);

        if (gameStateManager.isStartPositionSet()) {
            RouteService routeService = new RouteService(this);
            String routeUrl = RouteURLs.getRouteUrl(gameStateManager.getStartPosition(), gameStateManager.getDestination(), getApplicationContext());
            routeService.execute(routeUrl);
        } else {
            Log.w("##", "no start position available");
        }
    }

    @Override
    public void onRouteServiceFinished(PolylineOptions route) {
        for (GameServiceListener listener : listeners) {
            listener.drawRoute(route);
        }
    }

    @Override
    public void triggertNextRound(int currentRound) {
        //TODO trigger Next Round in mapview
        System.out.println("trigger next round!");
        retrieveRandomLandmarks();
    }

    public void retrieveRandomLandmarks() {
        final int NUMBER_OF_LANDMARKS = 4;
        List<MarkerOptions> markers = landmarkProvider.getRandomLandmarks(NUMBER_OF_LANDMARKS)
                .stream()
                .map(this::mapLandmarkToMarker)
                .collect(Collectors.toList());

        for (GameServiceListener listener : listeners) {
            listener.drawLandmarks(markers);
            listener.showToast("Tap on the marker and guess the distance!");
        }
    }

    private MarkerOptions mapLandmarkToMarker(Landmark landmark) {
        LatLng latLng = new LatLng(landmark.getPosition().getLatitude(),
                landmark.getPosition().getLongitude());
        String title = landmark.getName();

        return new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
    }

    public void saveGuess(String landmarkId, Double guess) {
        gameStateManager.saveGuess(landmarkId, guess);
    }

    public void showSnackbar(String message) {
        for (GameServiceListener listener : listeners) {
            listener.showToast(message);
        }
    }
}
