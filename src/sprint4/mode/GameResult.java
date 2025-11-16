package sprint4.mode;

public class GameResult {
	private GameState state;
	private String message;
	private int bluePlayerScore;
	private int redPlayerScore;
	private char winner;
	
	public GameResult(GameState state, String message, int bluePlayerScore, int redPlayerScore,
			char winner)
	{
		this.winner = winner;
		this.state = state;
		this.message = message;
		this.redPlayerScore = redPlayerScore;
		this.bluePlayerScore = bluePlayerScore;
	}

	public GameState getState() {
		return state;
	}

	public String getMessage() {
		return message;
	}

	public int getBluePlayerScore() {
		return bluePlayerScore;
	}

	public int getRedPlayerScore() {
		return redPlayerScore;
	}


	public char getWinner() {
		return winner;
	}
    public boolean isGameOver()
    {
    	return state != GameState.IN_PROGRESS;
    }
    @Override
    public String toString()
    {
    	return message;
    }

}
