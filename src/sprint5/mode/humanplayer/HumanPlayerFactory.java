package sprint5.mode.humanplayer;

import sprint5.mode.computerPlayer.ComputerPlayer;
import sprint5.mode.player.Player;

public class HumanPlayerFactory  implements PlayerFactory{

	@Override
	public Player createPlayer(char symbol, char preferredLetter, ComputerPlayer.LevelsOfDifficulty difficultyLeve)
	{
		String name =  determinePlayerName(symbol);
		return new HumanPlayer(symbol, name, preferredLetter);
	}


	@Override
	public boolean isHumanPlayer()
	{
		return true;
	}
//Determine player name
	private String determinePlayerName(char symbol) {
        return switch (symbol) {
            case 'B' -> "Blue Player";
            case 'R' -> "Red Player";
            default -> "Player " + symbol;
        };
    }
}
