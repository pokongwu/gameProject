package de.uniba.georacer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.model.app.RoundState;
import de.uniba.georacer.model.json.GeoLocation;
import de.uniba.georacer.model.json.Landmark;
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
        int i = 1;
        for (RoundState roundState : roundStates) {
            Map<String, Double> guesses = roundState.getGuesses();
            LOGGER.info("Guesses for round " + i + ": ");
            for (Map.Entry entry : guesses.entrySet()) {
                Landmark landmark = landmarks.stream().filter(a -> a.getName() == entry.getKey()).findFirst().get();
                double actualDistance = PositioningHelper.haversianDistance(GeoLocation.fromWGS84(landmark.getPosition().getLatitude(), landmark.getPosition().getLongitude()), GeoLocation.fromWGS84(roundState.getWaypoint().getLatitude(), roundState.getWaypoint().getLongitude()));
                LOGGER.info(entry.getKey() + " guessed distance: " + entry.getValue() + " actual distance: " + actualDistance);
            }
        }
        PositioningHelperI positioningHelper = new PositioningHelperUTM();
    }
}
