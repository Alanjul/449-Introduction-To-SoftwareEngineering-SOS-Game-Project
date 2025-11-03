package sprint3;

public abstract class Game {
	protected Board3 board;
	protected boolean gameOver;
	private GameMode3  mode;
	protected char currentTurn ;
	
	public Game(Board3 board, GameMode3 mode)
	{
		this.board = board; //instantiate the board
		this.currentTurn = 'B';//start the game
		this.mode = mode;
		this.gameOver = false; //not started
	}
	
	
	//check if the game is over
	public boolean isGameOver()
	{
		return gameOver;
	}
	//switch the turn
	public void switchTurn()
	{
		currentTurn = (currentTurn == 'B' ? 'R' : 'B');
	}
	
	
	public char getCurrentTurn() {
		return currentTurn;
	}


	public void setCurrentTurn(char currentTurn) {
		this.currentTurn = currentTurn;
	}


	public Board3 getBoard() {
		return board;
	}


	public void setBoard(Board3 board) {
		this.board = board;
	}


	public GameMode3 getMode() {
		return mode;
	}


	public void setMode(GameMode3 mode) {
		this.mode = mode;
	}


	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}


	public abstract void makeMove(int row, int col, char letter);
	public abstract char checkWinner();
	
	public abstract String getGameResult();


}
