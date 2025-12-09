package sprint5.mode.computerPlayer;

import java.util.*;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;
import sprint5.util.BoardSearcher;

public class EasyStrategy implements ComputerStrategy {
	private final Random random;
	private static final double SCORING_PROBABILITY = 0.10;

	public EasyStrategy() {
		this.random = new Random();

	}

	@Override
	public Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
			ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		List<Board.Cell> emptyCells = board.getEmptyCells();

		if (emptyCells.isEmpty()) {
			return null;
		}
		if (random.nextDouble() < SCORING_PROBABILITY) {
			Move scoringMove = findScoringMove(board);
			if (scoringMove != null) {
				return scoringMove;
			}
		}

		// Otherwise make a random move
		return makeRandomMove(emptyCells);
	}

	private Move findScoringMove(Board board) {

		List<Board.Cell> emptyCells = board.getEmptyCells();

		// Shuffle
		Collections.shuffle(emptyCells, random);

		for (Board.Cell cell : emptyCells) {
			int countS = BoardSearcher.countSOS(board, cell.row(), cell.col(), 'S');
			if (countS > 0) {
				return new Move(cell.row(), cell.col(), 'S');
			}

			// Try 'O'
			int countO = BoardSearcher.countSOS(board, cell.row(), cell.col(), 'O');
			if (countO > 0) {
				return new Move(cell.row(), cell.col(), 'O');
			}
		}
		return null;
	}

	/**
	 * Make a random move on an empty cell
	 */
	private Move makeRandomMove(List<Board.Cell> emptyCells) {
		Board.Cell cell = emptyCells.get(random.nextInt(emptyCells.size()));
		char letter = random.nextBoolean() ? 'S' : 'O';
		return new Move(cell.row(), cell.col(), letter);
	}

	@Override
	public String getStrategyName() {
		return "Easy Strategy (Forms SOS)";
	}

}
