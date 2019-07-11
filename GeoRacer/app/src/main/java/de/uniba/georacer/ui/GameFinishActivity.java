package de.uniba.georacer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.model.app.RoundState;
import de.uniba.georacer.model.json.GeoLocation;
import de.uniba.georacer.model.json.Landmark;
import de.uniba.georacer.service.positioning.DegradedMatrixException;
import de.uniba.georacer.service.positioning.PositioningHelper;
import de.uniba.georacer.service.positioning.PositioningHelperI;
import de.uniba.georacer.service.positioning.PositioningHelperUTM;
import de.uniba.georacer.state.GameStateManager;

public class GameFinishActivity extends AppCompatActivity {
    private GameState gameState;
    private Logger LOGGER = Logger.getLogger("GameFinishActivity");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);

        gameState = GameStateManager.getGameState();
        List<RoundState> roundStates = gameState.getRoundStates();
        List<Landmark> landmarks = gameState.getLandmarks();

        StringBuilder stringBuilder = new StringBuilder();
        PositioningHelperI positioningHelper = new PositioningHelperUTM();

        int finalScore = 0;

        int i = 0;
        List<LatLng> waypoints = gameState.getWaypoints();
        for (RoundState roundState : roundStates) {
            Map<GeoLocation, Double> guessesAndLandmarks = new HashMap<>();
            i++;
            Map<String, Double> guesses = roundState.getGuesses();
            LOGGER.info("Guesses for round " + i + ": ");
            stringBuilder.append("\nGuesses for round " + i + ":\n");
            for (Map.Entry entry : guesses.entrySet()) {
                Optional<Landmark> landmarkOptional = landmarks.stream().filter(a -> a.getName().equals(entry.getKey())).findFirst();
                if (landmarkOptional.isPresent()) {
                    Landmark landmark = landmarkOptional.get();
                    guessesAndLandmarks.put(GeoLocation.fromWGS84(landmark.getPosition().getLatitude(), landmark.getPosition().getLongitude()), (Double) entry.getValue());
                    int actualDistance = (int) PositioningHelper.haversianDistance(GeoLocation.fromWGS84(landmark.getPosition().getLatitude(), landmark.getPosition().getLongitude()), GeoLocation.fromWGS84(waypoints.get(i).latitude, waypoints.get(i).longitude));
                    String string = entry.getKey() + " guessed: " + entry.getValue() + " actual: " + actualDistance + "\n";
                    LOGGER.info(string);
                    stringBuilder.append(string);
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
                stringBuilder.append(msg);
                LOGGER.info(msg);
            }
            finalScore += positionDifference;
            String diff = "Difference calculated/actual postion: " + positionDifference;
            LOGGER.info(diff);
            stringBuilder.append(diff);
        }

        TextView scoreView = findViewById(R.id.final_score);
        scoreView.setText(Integer.toString(finalScore));
        TextView scoreAnalysis = findViewById(R.id.score_analysis);
        scoreAnalysis.setText(stringBuilder);
    }
}
