package sprint5.mode.computerPlayer;
import java.util.List;
import java.util.Random;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.board.Board.Cell;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;
import sprint5.util.BoardSearcher;

public class ComputerPlayer extends Player {

	public enum LevelsOfDifficulty{

		RANDOM("Random", "Make random moves", 50),
		EASY ("Easy", "Forms SOS whenever possible", 300),
		MEDIUM("Medium", "Use Monte Carlo Tree search with 400 simulation", 8000),
		HARD("Hard", "Use Monte Carlo Tree search with 600 simulation", 12000),
		EXPERT("Expert", "Monte Carlo Tree with 800 simulation", 25000);

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
	private final ComputerStrategy strategy;

	//Constructor to build computer player
	public  ComputerPlayer(char symbol, LevelsOfDifficulty difficulty, ComputerStrategy strategy)
	{
		super(symbol, "Computer Player (" + difficulty.getName() + ")");
		this.difficulty = difficulty;
		this.strategy = strategy;
	}
	@Override
	public Move chooseMove(Board board, Player opponent, Game4 game)
	{
		return strategy.findBestMove(board, game, this, opponent, difficulty);
	}
	@Override
	public  char chooseLetter()
	{
		return Math.random()< 0.5 ? 'S' : 'O';
	}
	public LevelsOfDifficulty getDifficulty() {
        return difficulty;
    }

}
