package sprint5.mode.GameMode;

import sprint5.util.Winner;

/** GameResult represents the result of a game */
public record GameResult(GameState state, String message, int bluePlayerScore, int redPlayerScore, Winner winner) {

	// Constructor
	public GameResult {
		if (state == null) {
			throw new IllegalArgumentException("State cannot be null");
		}
		if (message == null || message.trim().isEmpty()) {
			throw new IllegalArgumentException("Message cannot be null or empty");
		}
		if (winner == null) {
			throw new IllegalArgumentException("Winner cannot be null");
		}
		if (bluePlayerScore < 0 || redPlayerScore < 0) {
			throw new IllegalArgumentException("Scores cannot be negative");
		}
	}

	// Check if the game is over
	public boolean isGameOver() {
		return state != GameState.IN_PROGRESS;
	}

	// Check for draw
	public boolean isDraw() {
		return winner == Winner.DRAW;
	}

	// Check for winner
	public boolean hasWinner() {
		return winner.hasWinner();
	}

	public static GameResult inProgress(String currentPlayerName, int blueScore, int redScore) {
		String message = currentPlayerName + "'s turn";
		return new GameResult(GameState.IN_PROGRESS, message, blueScore, redScore, Winner.NONE);
	}

	// Score difference
	public int getScoreDifference() {
		return bluePlayerScore - redPlayerScore;
	}

	@Override
	public String toString() {
		return message;
	}

	public Winner getWinner() {
		return winner;
	}
}
