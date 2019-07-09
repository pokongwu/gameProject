package de.uniba.georacer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import de.uniba.georacer.service.app.GameService;
import de.uniba.georacer.service.app.GameServiceListener;
import de.uniba.georacer.ui.dialogs.GuessDistanceDialog;
import de.uniba.georacer.R;


public class GameMapView extends AppCompatActivity implements GameServiceListener, OnMapReadyCallback {
    protected GameService gameService;
    protected boolean gameServiceBound;
    private GoogleMap mMap;
    private Snackbar snackbar;
    private Marker currentPositionMarker;
    List<Marker> currentVisibleLandmarks;
    // ===== Game Service Connection =====

    private ServiceConnection gameServiceCon = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            currentVisibleLandmarks = new ArrayList<>();
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameServiceBound = true;
            gameService.registerListener(GameMapView.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            gameServiceBound = false;
        }
    };

    @Override
    public void updatePlayerPosition(Location location) {
        if(currentPositionMarker != null) {
            currentPositionMarker.remove();
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(),
                        location.getLongitude()), 15f));

        //TODO write util class for converting Location <-> LatLng
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions currentPosMarkerOptions = new MarkerOptions()
                .position(currentPosition)
                .title(getString(R.string.my_position_title))
                //https://www.freeiconspng.com/img/1673
                //https://www.iconfinder.com/icons/2908584/gps_location_marker_pin_user_icon
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_2));

        currentPositionMarker = mMap.addMarker(currentPosMarkerOptions);
    }

    @Override
    public void showToast(String message) {
        if(snackbar != null) {
            snackbar.dismiss();
        }

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                .show();

    }

    @Override
    public void drawRoute(PolylineOptions route, List<LatLng> waypoints) {
        mMap.addPolyline(route);
        for(LatLng waypoint : waypoints) {
            mMap.addCircle(new CircleOptions()
                    .center(waypoint).radius(50).strokeColor(getColor(R.color.waypointStroke)));
        }

        gameService.retrieveRandomLandmarks();
    }

    @Override
    public void drawLandmarks(List<MarkerOptions> markers) {
        clearLandmarks();

        for(MarkerOptions marker : markers) {
            Marker landmark = mMap.addMarker(marker);
            currentVisibleLandmarks.add(landmark);
        }
    }

    @Override
    public void clearLandmarks() {
        currentVisibleLandmarks.forEach(Marker::remove);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_map_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Bind to service
        Intent serviceIntent = new Intent(this, GameService.class);
        bindService(serviceIntent, gameServiceCon, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gameServiceCon != null) {
            unbindService(gameServiceCon);
        }
    }

    public void backToMenu(View view) {
        this.finish();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Context context = this;
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false);


        mMap.setOnMapClickListener(latLng -> {
            Location destination = new Location("map tap");
            destination.setLatitude(latLng.latitude);
            destination.setLongitude(latLng.longitude);

            //https://www.iconfinder.com/icons/1806295/destination_finish_flag_location_map_marker_icon
            MarkerOptions destinationPosMarker = new MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.my_destination_title))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_destination));
            mMap.addMarker(destinationPosMarker);

            gameService.startRoutingToDestination(destination);
            mMap.setOnMapClickListener(null);
        });

        mMap.setOnMarkerClickListener(marker -> {
            if(isLandmark(marker)) {
                new GuessDistanceDialog().showDialog(context, marker, gameService);
            }

            return false;
        });
    }

    private boolean isLandmark(Marker marker) {
        return !marker.getTitle().equals(getString(R.string.my_position_title))
                && !marker.getTitle().equals(getString(R.string.my_destination_title));
    }

}
