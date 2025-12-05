package sprint5.mode.GameMode;

import sprint5.mode.board.Board;
import sprint5.mode.player.ScoreManager;

public interface GameOverStrategy {
	
	//Check if the game is over and determine the resulting state
	 GameState checkGameOver(Board board, ScoreManager scoreManager);
	 
	 //Determine if  turns should switch after the move
	 boolean shouldSwitchTurns(int sosFormed);

}
