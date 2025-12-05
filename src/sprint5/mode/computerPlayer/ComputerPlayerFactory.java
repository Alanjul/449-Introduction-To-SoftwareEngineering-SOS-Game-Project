package sprint5.mode.computerPlayer;

import sprint5.mode.humanplayer.PlayerFactory;
import sprint5.mode.player.Player;

//Implements PlayerFactory abstraction
public class ComputerPlayerFactory implements PlayerFactory {

	private final ComputerStrategy strategy;

	public ComputerPlayerFactory(ComputerStrategy strategy) {
		this.strategy = strategy;
	}

	@Override
	public Player createPlayer(char symbol, char preferredLetter, ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		ComputerStrategy strategy = StrategyFactory.createStrategy(difficultyLevel);
		return new ComputerPlayer(symbol, difficultyLevel, strategy);
	}

	// Check if it is human player
	@Override
	public boolean isHumanPlayer() {
		return false;
	}

}
