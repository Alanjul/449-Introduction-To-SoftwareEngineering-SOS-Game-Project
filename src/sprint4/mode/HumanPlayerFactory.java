package sprint4.mode;

public class HumanPlayerFactory  implements PlayerInterface{

	@Override
	public Player createPlayer(char symbol, char preferredLetter, ComputerPlayer.LevelsOfDifficulty difficultyLeve)
	{
		String name =  (symbol =='B')? "Blue Player": "Red Player";
		return new HumanPlayer(symbol, name, preferredLetter);
	}
	
	
	@Override
	public boolean isHumanPlayer()
	{
		return true;
	}
}
