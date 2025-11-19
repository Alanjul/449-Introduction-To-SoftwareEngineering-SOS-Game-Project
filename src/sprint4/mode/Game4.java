package sprint4.mode;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import sprint3.Board3;
import sprint3.GameMode3;
import sprint4.util.BoardSearcher;

public abstract class Game4 {
	
	protected Board board;
	protected boolean gameOver;
	protected GameMode4  mode;
	protected char currentTurn;
	protected Player bluePlayer;
	protected Player redPlayer;
	protected List<LineSegment4> lineFormed;
	protected  int blueScore;
	protected int redScore;
	protected GameState state;
	
	protected Game4(Board board, Player bluePlayer, Player redPlayer, GameMode4 mode)
	{
		if(board == null || bluePlayer == null || redPlayer == null || mode == null)
		{
			throw new IllegalArgumentException("Arguments can not be null");
		}
		this.board =  board; //creates board given size
		this.currentTurn = 'B'; //Blue starts
		this.bluePlayer = bluePlayer;
		this.redPlayer = redPlayer;
		this.state = GameState.IN_PROGRESS;
		this.lineFormed = new ArrayList<>();
		this.mode = mode;
		this.gameOver = false; 
		this.redScore = 0;
		this.blueScore = 0;
	}
	
	//Get the current player based on turn
	public Player getCurrentPlayer()
	{
		return (currentTurn == 'B') ? bluePlayer: redPlayer;
	}
	
	//Get the opponent of the current player
	public Player getOpponent()
	{
		return (currentTurn == 'B') ? redPlayer : bluePlayer;
	}
	/*MakeMove method that moves on the board
	 * @Param move to make a move
	 * @return MoveResult containing success status and any SoS formed*/
	public MoveResult makeMove(Move move)
	{
		//check if the game is over and not in progress
		if(gameOver || state != GameState.IN_PROGRESS)
		{
			return new MoveResult("Game is over", false, new ArrayList<>());
		}
		//Attempt the move
		boolean success = board.makeMove(move.getRow(),move.getCol(), move.getLetter());
		if(!success)
		{
			return new MoveResult("Cell is already occupied", false, new ArrayList<>());
		}
		//Check for sos patttern
		Player currentPlayer = getCurrentPlayer(); // retrieve current player
		Color playerColor = (currentTurn == 'B') ? Color.BLUE : Color.RED;
		//Find   new  sos lines formed
		List<LineSegment4>newLines = BoardSearcher.findSOSPatterns( board, move.getRow(),
				move.getCol(), playerColor);
		
		//Add lines to game collections
		lineFormed.addAll(newLines);
		
		//Update the score
		int sosCount = newLines.size();
		if(currentTurn == 'B')
		{
			blueScore += sosCount;
			bluePlayer.addScore(sosCount);
		}else
		{
			redScore += sosCount;
			redPlayer.addScore(sosCount);
		}
		boolean switchTurns = shouldSwitchTurns(sosCount);
		
		if(switchTurns)
		{
			switchTurn();
		}
		//check if game is over
		checkGameOver();
		return new MoveResult("Move successful", true, newLines);
	}
	
	protected abstract boolean shouldSwitchTurns(int sosFormed);
	//Check  if the game  is over
	protected abstract void checkGameOver();
	
	//Switch turns between player
	public void switchTurn()
	{
		currentTurn = (currentTurn == 'B') ? 'R' : 'B';
	}
	
	//Reset the game to initial state
	public void resetGame()
	{
		board.resetGame();
		lineFormed.clear();//clear all lines
		blueScore = 0;
		redScore = 0;
		bluePlayer.resetScore();
		redPlayer.resetScore();
		currentTurn = 'B';
		state = GameState.IN_PROGRESS;//game in progress
		gameOver = false;
	}
	
	//Get the game result
	public GameResult getResult()
	{
		String message = generateResult();
		char winner = determineWinner();
		return new GameResult(state, message, blueScore, redScore, winner);
	}
	
	//Helper message to display results based on current state
	protected String generateResult()
	{
		//use switch to check for state
		switch(state)
		{
		case IN_PROGRESS:
			return getCurrentPlayer().getName() + "'s turn ";
		case BLUE_PLAYER_WINS:
			return "Blue Player wins Score : " + blueScore + " Vs red player Score: " + redScore;
		case RED_PLAYER_WINS:
			return "Red Player wins Score : " + redScore + " Vs blue player Score: " + blueScore;
		case DRAW:
			return "Game ended in draw, Blue Score: " + blueScore + " Vs red Score: " + redScore;
		default:
			return "Unknown state";
		}
	}
	//determineWinner determines who wins after comparing score
	public char determineWinner()
	{
		if(state == GameState.IN_PROGRESS)
		{
			return 'N'; // no winner
		}
		if (blueScore > redScore)
		{
			return 'B';
		}else if (redScore > blueScore)
		{
			return 'R';
		}else
		{
			return 'D' ; // draw
		}
	}
	/**
	 * @return the board
	 */
	public Board getBoard() {
		return board;
	}
	/**
	 * @return the gameOver
	 */
	public boolean isGameOver() {
		return gameOver;
	}
	/**
	 * @return the mode
	 */
	public GameMode4 getMode() {
		return mode;
	}
	/**
	 * @return the currentTurn
	 */
	public char getCurrentTurn() {
		return currentTurn;
	}
	/**
	 * @return the bluePlayer
	 */
	public Player getBluePlayer() {
		return bluePlayer;
	}
	/**
	 * @return the redPlayer
	 */
	public Player getRedPlayer() {
		return redPlayer;
	}
	/**
	 * @return the state
	 */
	public GameState getState() {
		return state;
	}

	/**
	 * @return the lineFormed
	 */
	public List<LineSegment4> getLineFormed() {
		return new ArrayList<>( lineFormed);
	}

	public int getBlueScore() {
		return blueScore;
	}
	
	public int getRedScore() {
		return redScore;
	}
	
}
