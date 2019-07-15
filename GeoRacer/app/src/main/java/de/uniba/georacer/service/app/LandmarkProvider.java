package de.uniba.georacer.service.app;

import android.content.Context;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.uniba.georacer.model.json.Landmark;
import de.uniba.georacer.parsing.LandmarkParser;

/**
 * Retrieves initial the landmarks from the LandmarkParser.
 * After that provides the requested landmarks. The provided landmarks get removed from the list.
 *
 * Support for multi-player: If the game is played on two different phones, the action for the route
 * should be triggered at the same MINUTE eg. player1 at 14:23:08 and player2 at 14:23:18. If they
 * do so, both player will receive the SAME landmarks, in order to compare results at the end!
 *
 * @author Ludwig, Pio
 */
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

    private List<Landmark> getRandomLandmarks(int numberOfLandmarks) {
        Random seed = getSeed();
        Collections.shuffle(landmarks, seed);

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

    /**
     * @return a Random-Object with the current time, except minute, as seed.
     * If two (or more) players start the game at the same minute,
     * they will receive the same landmarks in order to compare results
     */
    private Random getSeed() {
        Calendar currentTime = GregorianCalendar.getInstance();
        Calendar seedTime = GregorianCalendar.getInstance();

        seedTime.clear();
        seedTime.set(
                currentTime.get(Calendar.YEAR),
                currentTime.get(Calendar.MONTH),
                currentTime.get(Calendar.DATE),
                currentTime.get(Calendar.HOUR_OF_DAY),
                currentTime.get(Calendar.MINUTE));

        return new Random(seedTime.getTimeInMillis());
    }
}
