package sprint5.mode.computerPlayer;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;

//ComputerPlayer depends on this strategy
public interface ComputerStrategy {

	/**Finds the best Move for the given board state
	 * @param board current board state
	 * @param game current game instance
	 * @param computerPlayer
	 * @param opponent the opponent player
	 * @param difficultyLevel
	 * @return the best move*/
	Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
			ComputerPlayer.LevelsOfDifficulty difficultyLevel);

	/**getStrategyName returns the name of the strategy*/
String getStrategyName();

}
