package de.uniba.georacer;

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

import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import de.uniba.georacer.service.route.RouteService;
import de.uniba.georacer.service.route.RouteURLs;

public class GameService extends Service implements OnRouteServiceFinished {
    private GameState gameState;
    private final IBinder binder = new LocalBinder();
    private final List<GameServiceListener> listeners = new ArrayList<GameServiceListener>();


    // ===== Service Methods =====

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        gameState = new GameState();
        initLocationServices();
        return binder;
    }

    // ===== Local Binder =====

    public class LocalBinder extends Binder {
        GameService getService() {
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
                if(gameState.getStart() == null) {
                    gameState.setStart(location);
                }


                // Alert Listeners about changed player position
                for (GameServiceListener listener : listeners) {
                    listener.updatePlayerPosition(location);
                    if(gameState.getDestination() == null) {
                        listener.showToast("Tap on the map in order to set the destination");
                    } else {
                        listener.showToast("Current destination is " + gameState.getDestination().getLatitude() + ", " + gameState.getDestination().getLongitude());
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
        gameState.setDestination(destination);

        if(gameState.getStart() != null) {
            RouteService routeService = new RouteService(this);
            String routeUrl = RouteURLs.getRouteUrl(gameState.getStart(), gameState.getDestination(), getApplicationContext());
            routeService.execute(routeUrl);
        } else {
            Log.w("##", "no start position available");
        }
    }

    @Override
    public void onRouteServiceFinished(PolylineOptions route) {
        for(GameServiceListener listener : listeners) {
            listener.drawRoute(route);
        }
    }
}
