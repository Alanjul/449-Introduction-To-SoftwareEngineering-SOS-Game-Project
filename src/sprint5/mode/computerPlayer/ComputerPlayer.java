package sprint5.mode.computerPlayer;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;

import sprint5.mode.move.Move;
import sprint5.mode.player.Player;

public class ComputerPlayer extends Player {

	public enum LevelsOfDifficulty {

		RANDOM("Random", "Make random moves"), EASY("Easy", "Forms SOS whenever possible"),
		MEDIUM("Medium", "Use Monte Carlo Tree search with  simulation", 10000),
		HARD("Hard", "Use Monte Carlo Tree search with  simulation", 40000),
		EXPERT("Expert", "Monte Carlo Tree with 100000 simulation", 100000);

		private String name;
		private String description;
		private int numberOfSimulation;

		LevelsOfDifficulty(String name, String description) {
			this(name, description, 0);
		}

		LevelsOfDifficulty(String name, String description, int numberOfSimulation) {
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

	// Constructor to build computer player
	public ComputerPlayer(char symbol, LevelsOfDifficulty difficulty, ComputerStrategy strategy) {
		super(symbol, "Computer Player (" + difficulty.getName() + ")");
		this.difficulty = difficulty;
		this.strategy = strategy;
	}

	@Override
	public Move chooseMove(Board board, Player opponent, Game4 game) {
		return strategy.findBestMove(board, game, this, opponent, difficulty);
	}

	@Override
	public char chooseLetter() {
		return Math.random() < 0.5 ? 'S' : 'O';
	}

	public LevelsOfDifficulty getDifficulty() {
		return difficulty;
	}

}
