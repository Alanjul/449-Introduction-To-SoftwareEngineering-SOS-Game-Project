package sprint4.mode;

/**GameInterface  for  creating games*/
public interface GameInterface {
	
	
	/**Creating a game instance
	 * @param Board of the game board
	 * @param bluePlayer the blue player
	 * @param redPlayer the red player
	 * @return Game4 instances*/
	Game4 createGame(Board board, Player bluePlayer, Player redPlayer);
	
	GameMode4 getGameMode(); //return the game mode
	}
