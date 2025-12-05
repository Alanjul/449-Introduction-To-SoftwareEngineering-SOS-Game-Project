package sprint5.mode.player;
import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.computerPlayer.ComputerPlayer;
import sprint5.mode.humanplayer.HumanPlayer;
import sprint5.mode.move.Move;

public abstract class Player {
	private final char symbol;//"B" for blue and "R" for red
	private final String name;
	private static final char BLUE_SYMBOL = 'B';
	private static final char RED_SYMBOL = 'R';
	private int score;

	public Player(char symbol, String name)
	{

		validateSymbol(symbol);
		validateName(name);
		this.symbol = symbol;
		this.name = name;
		this.score = 0;
	}

	// Validate player symbol
	private void validateSymbol(char symbol) {
		if (symbol != BLUE_SYMBOL && symbol != RED_SYMBOL) {
			throw new IllegalArgumentException(
					String.format("It must be '%c' for Blue and '%c for red, got: ", BLUE_SYMBOL, RED_SYMBOL, symbol));
		}
	}
   //Set the player's score to specific value
	/**@param score
	 * */
	public void setScore(int score)
	{
		if(score < 0)
		{
			throw new IllegalArgumentException(
					String.format("Score can not be negative, we got: %d ", score));
		}
		this.score = score;
	}
	// validate name
	// Validate player symbol
	private void validateName(String name) {
		if (name == null && name.trim().isEmpty()) {
			throw new IllegalArgumentException("Player name cannot be null or empty");
		}
	}
	public abstract Move chooseMove(Board board, Player oponent, Game4 game);

	public abstract char chooseLetter();

	//scoring  to determine the winner
	public void addScore(int points)
	{
		if(points < 0)
		{
			throw new IllegalArgumentException(
					String.format("Points can not be negative, got: %d",
							points));
		}
		this.score += points; // add points to score
	}

	//Get the color of each player
	public String getColor()
	{
		return (symbol == BLUE_SYMBOL) ?"Blue": "Red";
	}
	// checking if it is human player
	public boolean isHumanPlayer()
	{
		return this instanceof HumanPlayer;
	}
	//computer player
	public boolean isComputer()
	{
		return this instanceof ComputerPlayer;
	}
	//rest score for the player
	public void resetScore()
	{
		this.score = 0;
	}
	/**
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(this == obj) {
			return true;
		}
		if(!(obj instanceof Player other)) {
			return false;
		}
		return symbol == other.symbol && name.equals(other.name);
	}
	//Generate hash code based on symbol and name
	@Override
	public int hashCode()
	{
	   int result = symbol;
	   result = 31 * result + name.hashCode();
	   return result;
	}
	//ToString method to display the player name and score
	@Override
	public String toString()
	{
		return String.format("%s Player (%s) - Score: %d",
				getColor(), getName(), getScore());
	}


}
