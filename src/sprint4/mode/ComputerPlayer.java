package sprint4.mode;
import java.util.List;
import java.util.Random;

import sprint4.Util.BoardSearcher;
import sprint4.mode.Board;
import sprint4.mode.Board.Cell;

public class ComputerPlayer extends Player {

	public enum LevelsOfDifficulty{
		RANDOM("Random", "Make random moves", 50),
		EASY ("Easy", "Forms SOS whenever possible", 150),
		MEDIUM("Medium", "Use Monte Carlo Tree search with 400 simulation", 400),
		HARD("Hard", "Use Monte Carlo Tree search with 600 simulation", 600),
		EXPERT("Expert", "Monte Carlo Tree with 800 simulation", 800);
		
		private String name;
		private String description;
		private int numberOfSimulation;
		
		LevelsOfDifficulty(String name,  String description, int  numberOfSimulation)
		{
			this.name = name;
			this.description = description;
			this.numberOfSimulation = numberOfSimulation;
			
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * @return the numberOfSimulation
		 */
		public int getNumberOfSimulation() {
			return numberOfSimulation;
		}

	}
	private LevelsOfDifficulty difficulty;
	private final Random random;
	private Game4 currentGame;
	private final ComputerStrategy strategy;
	
	//Constructor to build computer player
	public  ComputerPlayer(char symbol, LevelsOfDifficulty difficulty, ComputerStrategy strategy)
	{
		super(symbol, "Computer Player (" + difficulty.getName() + ")");
		this.difficulty = difficulty;
		this.random = new Random();
		this.strategy = strategy;
	}
	
	@Override
	public Move chooseMove(Board board, Player opponent, Game4 game)
	{
		List<Cell>emptyCells = board.getEmptyCells();//get empty cells
		if(emptyCells.isEmpty())
		{
			return null;
		}
		//using switch to switch difficulty levels
		return switch(difficulty)
		{
		case RANDOM  -> makeRandomMove(board, emptyCells);
		case EASY -> makeEasyMove(board, emptyCells);
		case MEDIUM, HARD, EXPERT ->  strategy.findBestMove(board, game, this, opponent, difficulty);
		
		};
	}
	
	
	//helper to try to from sos
	private Move makeEasyMove(Board board, List<Board4.Cell>emptyCells)
	{
		//Best O and S moves
		Move bestS = BoardSearcher.findBestMove(board, 'S');
		Move bestO = BoardSearcher.findBestMove(board, 'O');
		int countS =  bestS != null ? BoardSearcher.countSOS(board, bestS.getRow(), bestS.getCol(), 'S') : 0;
		int countO = bestO != null ? BoardSearcher.countSOS(board, bestO.getRow(), bestO.getCol(), 'O') : 0;
		
		if(countS > 0 || countO > 0)
		{
			return (countS >= countO) ? bestS : bestO;
		}
		
		return makeRandomMove(board, emptyCells);
	}
	@Override
	public  char chooseLetter()
	{
		return random.nextBoolean() ? 'S' : 'O';
	}

	//helper method to make random move on empty cell
	private Move makeRandomMove(Board board, List<Board.Cell> emptyCells)
	{
		Cell cell = emptyCells.get(random.nextInt(emptyCells.size()));
		char letter = random.nextBoolean() ? 'S': 'O';
		
		return new Move(cell.row(), cell.col(), letter);
	}
	
	public LevelsOfDifficulty getDifficulty() {
        return difficulty;
    }

	/**
	 * @return the currentGame
	 */
	public Game4 getCurrentGame() {
		return currentGame;
	}

	/**
	 * @param currentGame the currentGame to set
	 */
	public void setCurrentGame(Game4 currentGame) {
		this.currentGame = currentGame;
	}
	
	
}
