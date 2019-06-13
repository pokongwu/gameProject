package de.uniba.georacer;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import de.uniba.ioannidis.christos.georacer.R;


public class GameMapView extends AppCompatActivity implements GameServiceListener, OnMapReadyCallback {
    protected GameService gameService;
    protected boolean gameServiceBound;
    private GoogleMap mMap;
    private MarkerOptions currentPosMarker;
    private MarkerOptions destinationPosMarker;
    // ===== Game Service Connection =====

    private ServiceConnection gameServiceCon = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
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
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(),
                        location.getLongitude()), 15f));

        //TODO write util class for converting Location <-> LatLng
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        currentPosMarker = new MarkerOptions().position(currentPosition).title("You are here");
        mMap.addMarker(currentPosMarker);
    }

    @Override
    public void showToast(String message) {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                .show();

    }

    @Override
    public void drawRoute(PolylineOptions route) {
        mMap.clear();
        mMap.addPolyline(route);
        mMap.addMarker(currentPosMarker);
        mMap.addMarker(destinationPosMarker);
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng bbg = new LatLng(49.889371, 10.887191);
        mMap.addMarker(new MarkerOptions().position(bbg).title("Klosterbräu"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(bbg));

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Location destination = new Location("map tap");
                destination.setLatitude(latLng.latitude);
                destination.setLongitude(latLng.longitude);

                destinationPosMarker = new MarkerOptions().position(latLng).title("Your destination");
                mMap.addMarker(destinationPosMarker);

                gameService.startRoutingToDestination(destination);
            }
        });
    }


}
