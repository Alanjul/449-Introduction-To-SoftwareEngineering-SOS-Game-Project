package sprint4.mode;

//Implements PlayerFactory abstraction
//Uses computerStrategy  injection
public class ComputerPlayerFactory implements PlayerInterface {

	private final ComputerStrategy strategy;

	public ComputerPlayerFactory(ComputerStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public Player createPlayer(char symbol, char preferredLetter, ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		return new ComputerPlayer(symbol, difficultyLevel, strategy);
	}

	// Check if it is human player
	@Override
	public boolean isHumanPlayer() {
		return false;
	}

}
