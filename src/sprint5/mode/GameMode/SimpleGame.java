package sprint5.mode.GameMode;

import sprint5.mode.board.Board;
import sprint5.mode.player.Player;
import sprint5.mode.player.ScoreManager;

public class SimpleGame  implements GameOverStrategy{


	//Check if simple Game is over
	@Override
    public GameState checkGameOver(Board board, ScoreManager scoreManager) {
       
        if (scoreManager.getBlueScore() > 0) {
            return GameState.BLUE_PLAYER_WINS;
        }
        
        if (scoreManager.getRedScore() > 0) {
            return GameState.RED_PLAYER_WINS;
        }
        
      
        if (board.isFull()) {
            return GameState.DRAW;
        }
        
        // Game continues
        return GameState.IN_PROGRESS;
    }
	@Override
    public boolean shouldSwitchTurns(int sosFormed) {
        return true; // Always switch in Simple mode
    
	}

}
