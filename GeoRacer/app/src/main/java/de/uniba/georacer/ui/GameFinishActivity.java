package de.uniba.georacer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.model.app.RoundState;
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
        int i = 1;
        for(RoundState roundState : roundStates){
            Map<String, Double> guesses = roundState.getGuesses();
            LOGGER.info("Guesses for round " + i + ": ");

        }
        PositioningHelperI positioningHelper = new PositioningHelperUTM();
    }
}
