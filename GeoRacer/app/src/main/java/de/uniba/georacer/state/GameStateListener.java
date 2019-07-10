package de.uniba.georacer.state;

import de.uniba.georacer.model.app.GameState;

public interface GameStateListener {
    public void triggertNextRound(int currentRound);

    public void triggerGameFinish(GameState gameState);
}
