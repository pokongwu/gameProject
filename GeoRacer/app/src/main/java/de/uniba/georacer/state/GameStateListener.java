package de.uniba.georacer.state;

import de.uniba.georacer.model.app.GameState;

public interface GameStateListener {
    void triggertNextRound(int currentRound);

    void triggerGameFinish(GameState gameState);
}
