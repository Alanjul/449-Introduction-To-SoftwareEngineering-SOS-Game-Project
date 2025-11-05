package sprint3;

public class GeneralGame extends Game {
	private char winner; // winner or draw
	private int blueScore;
	private int redScore;
	
	public GeneralGame(Board3 board)
	{
		super(board, GameMode3.GENERAL);
		blueScore = 0;
		redScore = 0;
		winner = '\0';
		
	}
	
	@Override
	public  void makeMove(int row, int col, char letter)
	{
		
		if(gameOver) return ;
		boolean currentMove = board.makeMove(row, col, letter);
		//if the move is not successful;
		if(!currentMove) return;
		
		int sosCount = board.checkSOS(row, col);
		if (sosCount  > 0)
		{
			if(currentTurn == 'B' )
			{
				blueScore += sosCount;
			}else
			{
				redScore += sosCount;
			}
			
		}else
		{
			switchTurn(); // game continue
		}
		if (board.isFull())
		{
			gameOver = true;
			if (blueScore > redScore)
			{
				winner = 'B';
			}else if (redScore > blueScore)
			{
				winner = 'R';
			}else
			{
				winner = '\0'; // draw
			}
		}
		
	}

	// reset the game
	public void reset() {
		board.resetGame();
		currentTurn = 'B';
		gameOver = false;
		winner = '\0';
		redScore = 0;
		blueScore = 0;
	}
	
	@Override
	public  char checkWinner()
	{
		return winner;
	}
	@Override
	public String getGameResult()
	{
		if(!gameOver)
		{
			return "Game still in progress";
		}
		if(winner == 'B')
		{
			return "Blue Player wins the game  with scores of " + blueScore  + " Vs " + redScore + " for Red player";
		}
		else if (winner == 'R')
		{
			return "Red Player wins the game  with scores of " + redScore  + " Vs " + blueScore + " for blue player";
		}else
		{
			return "Draw! Score for blue is " + blueScore + " for red player is" + redScore ;
		}
	}

	public char getWinner() {
		return winner;
	}

	
	public int getBlueScore() {
		return blueScore;
	}

	public int getRedScore() {
		return redScore;
	}
}
