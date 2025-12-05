package sprint5.mode.GameMode;

import sprint5.mode.board.Board;
import sprint5.mode.player.Player;

/**GameFactory  for  creating games*/
public interface GameFactory{


	/**Creating a game instance
	 * @param Board of the game board
	 * @param bluePlayer the blue player
	 * @param redPlayer the red player
	 * @return Game4 instances*/
	Game4 createGame(Board board, Player bluePlayer, Player redPlayer);

	GameMode4 getGameMode(); //return the game mode
	}
