package de.uniba.georacer.service.app;

import android.content.Context;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.uniba.georacer.model.json.Landmark;
import de.uniba.georacer.parsing.LandmarkParser;

public class LandmarkProvider {
    final List<Landmark> landmarks;

    public LandmarkProvider(Context context) {
        LandmarkParser parser = new LandmarkParser(context);
        this.landmarks = parser.getLandmarks();
    }

    public List<Landmark> getLandmarks(){
        return landmarks;
    }

    public List<MarkerOptions> getMarkerOptionsFromLandmarks() {
        final int NUMBER_OF_LANDMARKS = 4;
        return getRandomLandmarks(NUMBER_OF_LANDMARKS)
                .stream()
                .map(this::mapLandmarkToMarker)
                .collect(Collectors.toList());
    }

    //TODO check if landmark was already selected / remove from list
    private List<Landmark> getRandomLandmarks(int numberOfLandmarks) {
        Collections.shuffle(landmarks);

        return landmarks.subList(0, numberOfLandmarks);
    }

    private MarkerOptions mapLandmarkToMarker(Landmark landmark) {
        LatLng latLng = new LatLng(landmark.getPosition().getLatitude(),
                landmark.getPosition().getLongitude());
        String title = landmark.getName();

        return new MarkerOptions()
                .position(latLng)
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
    }
}
