package de.uniba.georacer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.model.app.Result;
import de.uniba.georacer.model.app.RoundState;
import de.uniba.georacer.model.json.GeoLocation;
import de.uniba.georacer.model.json.Landmark;
import de.uniba.georacer.service.positioning.DegradedMatrixException;
import de.uniba.georacer.service.positioning.PositioningHelper;
import de.uniba.georacer.service.positioning.PositioningHelperI;
import de.uniba.georacer.service.positioning.PositioningHelperUTM;
import de.uniba.georacer.state.GameStateManager;
import de.uniba.georacer.ui.FinishList.CustomAdapter;

public class GameFinishActivity extends AppCompatActivity {
    private GameState gameState;
    private Logger LOGGER = Logger.getLogger("GameFinishActivity");
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);
        listView = findViewById(R.id.game_finish_list);
        List<Result> results = getResults();
        ListAdapter adapter= new CustomAdapter(results,getApplicationContext());
        listView.setAdapter(adapter);
    }

    private List<Result> getResults() {
        List<Result> results = new ArrayList<>();

        gameState = GameStateManager.getGameState();
        List<RoundState> roundStates = gameState.getRoundStates();
        List<Landmark> landmarks = gameState.getLandmarks();

        PositioningHelperI positioningHelper = new PositioningHelperUTM();

        //TODO add to model
        int finalScore = 0;

        int i = 0;
        List<LatLng> waypoints = gameState.getWaypoints();
        for (RoundState roundState : roundStates) {
            Result result = new Result();
            Map<GeoLocation, Double> guessesAndLandmarks = new HashMap<>();
            Map<String, Double> guesses = roundState.getGuesses();
            LOGGER.info("Guesses for round " + (i + 1) + ": ");
            result.setRound(String.valueOf(i + 1));
            int j = 0;
            double diffSum = 0;

            for (Map.Entry entry : guesses.entrySet()) {
                j++;
                Optional<Landmark> landmarkOptional = landmarks.stream().filter(a -> a.getName().equals(entry.getKey())).findFirst();
                if (landmarkOptional.isPresent()) {
                    Landmark landmark = landmarkOptional.get();
                    guessesAndLandmarks.put(GeoLocation.fromWGS84(landmark.getPosition().getLatitude(), landmark.getPosition().getLongitude()), (Double) entry.getValue());
                    int actualDistance = (int) PositioningHelper.haversianDistance(GeoLocation.fromWGS84(landmark.getPosition().getLatitude(), landmark.getPosition().getLongitude()), GeoLocation.fromWGS84(waypoints.get(i).latitude, waypoints.get(i).longitude));
                    String string = entry.getKey() + " guessed: " + entry.getValue() + " actual: " + actualDistance + "\n";
                    LOGGER.info(string);
                    // is not dynamic, but too lazy to convert to list, sorry //Ludwig
                    Double guessedValue = Double.valueOf(entry.getValue().toString());
                    Double diff = Math.abs(guessedValue - actualDistance);
                    diffSum += diff;
                    switch (j) {
                        case 1:
                            result.setLandmarkName1(entry.getKey().toString());
                            result.setGuessedDistanceLandmark1(convertAndIgnoreDecimalPlaces(guessedValue));
                            result.setActualDistanceLandmark1(String.valueOf(actualDistance));
                            result.setDifferenceDistanceLandmark1(convertAndIgnoreDecimalPlaces(diff));
                            break;
                        case 2:
                            result.setLandmarkName2(entry.getKey().toString());
                            result.setGuessedDistanceLandmark2(convertAndIgnoreDecimalPlaces(guessedValue));
                            result.setActualDistanceLandmark2(String.valueOf(actualDistance));
                            result.setDifferenceDistanceLandmark2(convertAndIgnoreDecimalPlaces(diff));
                            break;
                        case 3:
                            result.setLandmarkName3(entry.getKey().toString());
                            result.setGuessedDistanceLandmark3(convertAndIgnoreDecimalPlaces(guessedValue));
                            result.setActualDistanceLandmark3(String.valueOf(actualDistance));
                            result.setDifferenceDistanceLandmark3(convertAndIgnoreDecimalPlaces(diff));
                            break;
                        case 4:
                            result.setLandmarkName4(entry.getKey().toString());
                            result.setGuessedDistanceLandmark4(convertAndIgnoreDecimalPlaces(guessedValue));
                            result.setActualDistanceLandmark4(String.valueOf(actualDistance));
                            result.setDifferenceDistanceLandmark4(convertAndIgnoreDecimalPlaces(diff));
                            break;
                    }
                }
            }
            // Calculate position
            GeoLocation waypoint = GeoLocation.fromWGS84(waypoints.get(i).latitude, waypoints.get(i).longitude);
            GeoLocation calculatedPosition = null;
            int positionDifference = 1000;
            try {
                calculatedPosition = positioningHelper.calculatePositionFromGuesses(guessesAndLandmarks, waypoint);
                if (calculatedPosition != null) {
                    positionDifference = (int) PositioningHelper.haversianDistance(waypoint, calculatedPosition);
                }
            } catch (DegradedMatrixException e) {
                String msg = "Guesses were too bad to calculate a position.";
                result.setWaypointGuessingError(msg);
                LOGGER.info(msg);
            }

            double avgGuessingError = diffSum / guesses.size();
            result.setAvgGuessingError(convertAndIgnoreDecimalPlaces(avgGuessingError));

            finalScore += positionDifference;
            String diffMsg = "Difference calculated/actual postion: " + positionDifference;
            result.setWaypointGuessingError(String.valueOf(positionDifference));
            LOGGER.info(diffMsg);
            results.add(result);
            i++;
        }

        TextView scoreView = findViewById(R.id.final_score);
        scoreView.setText(Integer.toString(finalScore));

        return results;

    }

    private String convertAndIgnoreDecimalPlaces(double value) {
        return String.valueOf((int) value);
    }
}
