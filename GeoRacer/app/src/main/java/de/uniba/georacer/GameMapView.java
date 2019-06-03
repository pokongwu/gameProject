package de.uniba.georacer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.uniba.ioannidis.christos.georacer.R;


public class GameMapView extends AppCompatActivity implements GameServiceListener, OnMapReadyCallback {
    protected GameService gameService;
    protected boolean gameServiceBound;
    private GoogleMap mMap;
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


    public void backToMenu(View view) {
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
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
    }


}
