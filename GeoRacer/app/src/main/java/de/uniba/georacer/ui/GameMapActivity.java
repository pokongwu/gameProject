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
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.uniba.georacer.service.app.DialogGameServiceProxy;
import de.uniba.georacer.service.app.GameService;
import de.uniba.georacer.service.app.GameServiceListener;
import de.uniba.georacer.ui.dialogs.GuessDistanceDialog;
import de.uniba.georacer.R;


public class GameMapActivity extends AppCompatActivity implements GameServiceListener, DialogGameServiceProxy, OnMapReadyCallback {
    protected GameService gameService;
    protected boolean gameServiceBound;
    private boolean isInitialZoomPerformed = false;
    private GoogleMap mMap;
    private Snackbar snackbar;
    private Marker currentPositionMarker;
    private List<Marker> visibleLandmarks;
    private List<Circle> visibleWaypoints;
    // ===== Game Service Connection =====

    private ServiceConnection gameServiceCon = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            visibleLandmarks = new ArrayList<>();
            visibleWaypoints = new ArrayList<>();
            GameService.LocalBinder binder = (GameService.LocalBinder) service;
            gameService = binder.getService();
            gameServiceBound = true;
            gameService.registerListener(GameMapActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            gameServiceBound = false;
        }
    };

    @Override
    public void updatePlayerPosition(Location location) {
        if (currentPositionMarker != null) {
            currentPositionMarker.remove();
        }

        //TODO write util class for converting Location <-> LatLng
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions currentPosMarkerOptions = new MarkerOptions()
                .position(currentPosition)
                .title(getString(R.string.my_position_title))
                //https://www.iconfinder.com/icons/2908584/gps_location_marker_pin_user_icon
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_human_2));

        currentPositionMarker = mMap.addMarker(currentPosMarkerOptions);

        if(!isInitialZoomPerformed) {
            isInitialZoomPerformed = true;
            zoomOnPlayer();
        }
    }

    @Override
    public void showToast(String message) {
        if (snackbar != null) {
            snackbar.dismiss();
        }

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("OK", view1 -> {}).
                setActionTextColor(getResources().getColor(android.R.color.holo_green_dark))
                .show();
    }

    @Override
    public void drawRoute(PolylineOptions routeOption) {
        mMap.addPolyline(routeOption);
    }

    @Override
    public void drawWaypoints(List<CircleOptions> waypointOptions) {
        visibleWaypoints.forEach(Circle::remove);
        visibleWaypoints.clear();

        for(CircleOptions waypoint : waypointOptions) {
            visibleWaypoints.add(mMap.addCircle(waypoint));
        }

        zoomOutOnLandmarks(visibleWaypoints.stream()
                .map(Circle::getCenter).collect(Collectors.toList()));
    }

    @Override
    public void drawLandmarks(List<MarkerOptions> landmarkOptions) {
        if (visibleLandmarks.size() == 0) {
            for (MarkerOptions marker : landmarkOptions) {
                Marker landmark = mMap.addMarker(marker);
                visibleLandmarks.add(landmark);
            }
            zoomOutOnLandmarks(visibleLandmarks.stream()
                    .map(Marker::getPosition).collect(Collectors.toList()));
        }
    }

    @Override
    public void closeMapView() {
        this.finish();
    }

    public void zoomOnPlayer(View view) {
        zoomOnPlayer();
    }

    public void zoomOnPlayer() {
        if(currentPositionMarker != null && currentPositionMarker.getPosition() != null) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(currentPositionMarker.getPosition().latitude,
                    currentPositionMarker.getPosition().longitude), 15f);
            mMap.animateCamera(cu);
        }
    }

    private void zoomOutOnLandmarks(List<LatLng> positions) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        positions.forEach(builder::include);
        builder.include(currentPositionMarker.getPosition());
        LatLngBounds bounds = builder.build();

        int padding = 400;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        mMap.animateCamera(cu);
    }

    @Override
    public void clearLandmarks() {
        visibleLandmarks.forEach(Marker::remove);
        visibleLandmarks.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideAppBar();
        setContentView(R.layout.game_map_view);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Bind to service
        Intent serviceIntent = new Intent(this, GameService.class);
        bindService(serviceIntent, gameServiceCon, Context.BIND_AUTO_CREATE);
    }

    private void hideAppBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (gameServiceCon != null) {
            gameService.resetState();
            unbindService(gameServiceCon);
        }
    }

    public void backToMenu(View view) {
        this.finish();
    }

    @Override
    public void onBackPressed() {
        gameService.resetState();
        backToMenu(findViewById(android.R.id.content));
    }

    /**
     * Manipulates the map once available.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Context context = this;
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMapToolbarEnabled(false);

        final int bottomPaddingForControlls = 180;
        mMap.setPadding(0, 0, 0, bottomPaddingForControlls);


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
            Intent intent = getIntent();

            gameService.startRoutingToDestination(destination, intent.getIntExtra("rounds", 3));
            mMap.setOnMapClickListener(null);
        });

        mMap.setOnMarkerClickListener(marker -> {
            if (isLandmark(marker)) {
                new GuessDistanceDialog().showDialog(context, marker, this);
            }

            return false;
        });
    }

    private boolean isLandmark(Marker marker) {
        return !marker.getTitle().equals(getString(R.string.my_position_title))
                && !marker.getTitle().equals(getString(R.string.my_destination_title));
    }

    @Override
    public void saveGuess(String landmarkId, double guess) {
        gameService.saveGuess(landmarkId, guess);
    }

    @Override
    public String getGuess(String landmarkId) {
        return gameService.getGuess(landmarkId);
    }
}
