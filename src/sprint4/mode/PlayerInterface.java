package sprint4.mode;

/**Interface class for creating player*/
public interface PlayerInterface {
	
	/**
	 * Create the player instances
	 * @param symbol for player symbol
	 * @param preferredLetter for human player
	 * @param difficultyLevel for computer
	 * @return player instance*/
	Player createPlayer(char symbol, char preferredLetter, ComputerPlayer.LevelsOfDifficulty difficultyLevel);
	
	//Check if it is human player
	boolean isHumanPlayer();
	

}
