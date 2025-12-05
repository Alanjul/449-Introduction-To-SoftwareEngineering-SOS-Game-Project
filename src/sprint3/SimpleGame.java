package sprint3;

public class SimpleGame extends Game {

	private char winner = '\0'; //B for Blue player and R for red player,  '\0' no winner yet
	public SimpleGame(Board3 board)
	{
		super(board, GameMode3.SIMPLE);
	}

	@Override
	public void makeMove(int row, int col, char letter)
	{
		if(gameOver) {
			return;
		}
		boolean currentMove =  board.makeMove(row, col, letter);

		//already filled
		if(!currentMove) {
			return;
		}
		int sosCount = board.checkSOS(row, col); //checking is SOS is formed
		if (sosCount > 0)
		{
			winner = currentTurn;
			gameOver = true;
			return ;//bleak out of method
		}
		//check if the board is full with no winner
		if (board.isFull())
		{
			gameOver = true;
			winner = '\0';//draw no winner
			return ;
		}
		switchTurn(); // if no win and the board is not full
	}
	@Override
	public char checkWinner()
	{
		return winner;
	}
	//reset the game
	public void reset()
	{
		board.resetGame();
		currentTurn = 'B';
		gameOver = false;
		winner = '\0';
	}

	@Override
	public String getGameResult()
	{
		if(!gameOver)
		{
			return "Game in progress";
		}
		if(winner == 'B')
		{
			return "Blue Player wins";
		}else if(winner == 'R')
		{
			return "Red player wins";
		} else if (isGameOver())
		{
			return "Draw";
		}
		return "";
	}

	public char getWinner() {
		return winner;
	}

	public void setWinner(char winner) {
		this.winner = winner;
		setGameOver(true);
	}

}

