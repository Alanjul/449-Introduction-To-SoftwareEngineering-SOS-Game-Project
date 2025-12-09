package sprint5.mode.computerPlayer;

import java.util.List;
import java.util.Random;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;

public class RandomStrategy implements ComputerStrategy {
	private final Random random;

	public RandomStrategy() {
		this.random = new Random();
	}

	@Override
	public Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
			ComputerPlayer.LevelsOfDifficulty difficultyLevel) {
		List<Board.Cell> emptyCells = board.getEmptyCells();

		if (emptyCells.isEmpty()) {
			return null;
		}

		// Select random cell and random letter
		Board.Cell cell = emptyCells.get(random.nextInt(emptyCells.size()));
		char letter = random.nextBoolean() ? 'S' : 'O';

		return new Move(cell.row(), cell.col(), letter);
	}

	@Override
	public String getStrategyName() {
		return "Random Move Strategy";
	}

}
