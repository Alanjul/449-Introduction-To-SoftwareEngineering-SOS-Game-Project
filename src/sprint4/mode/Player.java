package sprint4.mode;

public abstract class Player {
	private final char symbol;//"B" for blue and "R" for red
	private final String name;
	private int score;

	public Player(char symbol, String name)
	{
		if(symbol != 'B' && symbol != 'R')
		{
			throw new IllegalArgumentException("It must be 'B' for Blue and 'R' for red ");
		}
		this.symbol = symbol;
		this.name = name;
		this.score = 0;
	}
	public abstract Move chooseMove(Board board, Player oponent, Game4 game);
	
	public abstract char chooseLetter();
	
	//scoring  to determine the winner
	public void addScore(int points)
	{
		if(points < 0)
		{
			throw new IllegalArgumentException("Points can not be negative");
		}
		this.score += points; // add points to score
	}
	
	//Get the color of each player
	public String getColor()
	{
		return (symbol == 'B') ?"Blue": "Red";
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
	
	//ToString method to display the player name and score
	@Override 
	public String toString()
	{
		return String.format("%s Player (%s) - Score: %d",  getColor(), getName(), getScore());
	}
	
	
}
