package sprint5.mode.GameMode;

import sprint5.mode.board.Board;
import sprint5.mode.player.Player;
import sprint5.mode.player.ScoreManager;

//GeneralGame class implements game over logic for general mode
public class GeneralGame implements GameOverStrategy {



	//Check if the game is over
	@Override
    public GameState checkGameOver(Board board, ScoreManager scoreManager) {
        // Game continues if board not full
        if (!board.isFull()) {
            return GameState.IN_PROGRESS;
        }
        
        // Board is full - determine winner by score
        if (scoreManager.isBlueWinning()) {
            return GameState.BLUE_PLAYER_WINS;
        } else if (scoreManager.isRedWinning()) {
            return GameState.RED_PLAYER_WINS;
        } else {
            return GameState.DRAW;
        }
    }

	@Override
    public boolean shouldSwitchTurns(int sosFormed) {
        return sosFormed == 0; // Only switch if no SOS formed
    }

}
