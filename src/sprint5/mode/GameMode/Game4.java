package sprint5.mode.GameMode;

import java.awt.Color;
import java.util.*;
import sprint5.mode.board.Board;
import sprint5.mode.computerPlayer.ComputerPlayer;
import sprint5.mode.move.*;
import sprint5.mode.player.*;
import sprint5.util.*;


public class Game4 {

	private Board board;
	private final PatternDetector patternDetector;
    private final GameOverStrategy gameOverStrategy;
	private GameMode4  mode;
	private Player bluePlayer;
	private Player redPlayer;
	
	//managers
	private final ScoreManager scoreManager;
    private final TurnManager turnManager;
    //Game state
	protected List<LineSegment4> linesFormed;
	protected GameState state;

	protected Game4(Board board, Player bluePlayer, Player redPlayer, GameMode4 mode,
            PatternDetector patternDetector, GameOverStrategy gameOverStrategy)
	{
		if(board == null || bluePlayer == null || redPlayer == null || 
	            mode == null || patternDetector == null || gameOverStrategy == null)
		{
			throw new IllegalArgumentException("Arguments can not be null");
		}
		//initialize
		this.board = board;
        this.bluePlayer = bluePlayer;
        this.redPlayer = redPlayer;
        this.mode = mode;
        this.patternDetector = patternDetector;
        this.gameOverStrategy = gameOverStrategy;
		
		//Initialize manager
        this.scoreManager = new ScoreManager();
        this.turnManager = new TurnManager(); // Blue starts
        
        //Initialize state
        this.state = GameState.IN_PROGRESS;
        this.linesFormed = new ArrayList<>();
	}

	//Get the current player based on turn
	public Player getCurrentPlayer()
	{
		return turnManager.isBluesTurn() ? bluePlayer : redPlayer;
	}

	//Get the opponent of the current player
	public Player getOpponent(ComputerPlayer computer)
	{
		return turnManager.isBluesTurn() ? redPlayer : bluePlayer;
	}
	
	//Player color
	public PlayerColor getCurrentColor() {
		return turnManager.getCurrentTurn();
    }
	
	/*MakeMove method that moves on the board
	 * @Param move to make a move
	 * @return MoveResult containing success status and any SoS formed*/
	public MoveResult makeMove(Move move)
	{
		//check if the game is ove
		 if (isGameOver()) {
	            return new MoveResult("Game is over", false, new ArrayList<>());
	        }
		//Attempt the move
		boolean success = board.makeMove(move.getRow(),move.getCol(), move.getLetter());
		if(!success)
		{
			return new MoveResult("Cell is already occupied", false, new ArrayList<>());
		}
		//Check for sos patttern
		 PlayerColor currentColor = getCurrentColor(); 
		 List<LineSegment4> newLines = patternDetector.findPatterns(
		            board, 
		            move.getRow(), 
		            move.getCol(), 
		            currentColor.getAwtColor()
		        );
		//Add lines to game collections
		linesFormed.addAll(newLines);

		//Update the score
		int sosCount = newLines.size();
		if (sosCount > 0) {
            scoreManager.addScore(currentColor, sosCount);
            getCurrentPlayer().addScore(sosCount);
        }
		
		//Check if turns should switch
		 boolean switchTurns = gameOverStrategy.shouldSwitchTurns(sosCount);
	        if (switchTurns) {
	            turnManager.switchTurn();
	        }
		//check if game is over
		checkGameOver();
		return new MoveResult("Move successful", true, newLines);
	}

	
	//Check  if the game  is over
	private  void checkGameOver()
	{
		GameState newState = gameOverStrategy.checkGameOver(board, scoreManager);
        this.state = newState;
	}

	//Reset the game to initial state
	public void resetGame()
	{
		board.resetGame();
        linesFormed.clear();
        scoreManager.reset();
        turnManager.reset();
        bluePlayer.resetScore();
        redPlayer.resetScore();
        state = GameState.IN_PROGRESS;
	}

	//Get the game result
	public GameResult getResult()
	{
		String message = generateResultMessage();
        Winner winner = determineWinner();
        return new GameResult(
            state, 
            message, 
            scoreManager.getBlueScore(), 
            scoreManager.getRedScore(), 
            winner
        );
	}

	
	//determineWinner determines who wins after comparing score
	private Winner determineWinner()
	{
		if (state == GameState.IN_PROGRESS) {
            return Winner.NONE;
        }
        
        return Winner.fromScores(
            scoreManager.getBlueScore(),
            scoreManager.getRedScore(),
            true
        );
	}
	
	private String generateResultMessage() {
        if (state == GameState.IN_PROGRESS) {
            return String.format("%s's turn in %s mode - Blue: %d, Red: %d",
                getCurrentPlayer().getName(),
                mode.getName(),
                scoreManager.getBlueScore(),
                scoreManager.getRedScore()
            );
        }
        return switch (state) {
        case BLUE_PLAYER_WINS -> String.format(
            "Blue Player wins! Final score: Blue %d - Red %d",
            scoreManager.getBlueScore(), scoreManager.getRedScore()
        );
        case RED_PLAYER_WINS -> String.format(
            "Red Player wins! Final score: Blue %d - Red %d",
            scoreManager.getBlueScore(), scoreManager.getRedScore()
        );
        case DRAW -> String.format(
            "Draw! Final score: Blue %d - Red %d",
            scoreManager.getBlueScore(), scoreManager.getRedScore()
        );
        default -> "Unknown state";
    };
        
	}
	
	
	//Getters
	 public List<LineSegment4> getLinesFormed() {
	        return Collections.unmodifiableList(new ArrayList<>(linesFormed));
	    }
	public ScoreManager getScoreManager() {
        return scoreManager;
    }
	
	public TurnManager getTurnManager() {
        return turnManager;
    }
	public int getRedScore() {
        return scoreManager.getRedScore();
    }
	public int getBlueScore() {
        return scoreManager.getBlueScore();
    }
	public Board getBoard() {
        return board;
    }
    
    public boolean isGameOver() {
        return state != GameState.IN_PROGRESS;
    }
    
    public GameMode4 getMode() {
        return mode;
    }
    
    public PlayerColor getCurrentTurn() {
        return turnManager.getCurrentTurn();
    }
    public Player getBluePlayer() {
        return bluePlayer;
    }
    
    public Player getRedPlayer() {
        return redPlayer;
    }
    
    public GameState getState() {
        return state;
    }
}
