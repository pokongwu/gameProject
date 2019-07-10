package de.uniba.georacer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import de.uniba.georacer.R;
import de.uniba.georacer.model.app.GameState;
import de.uniba.georacer.state.GameStateManager;

public class GameFinishActivity extends AppCompatActivity {
    private GameState gameState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finish);


        gameState = GameStateManager.getGameState();
    }
}
