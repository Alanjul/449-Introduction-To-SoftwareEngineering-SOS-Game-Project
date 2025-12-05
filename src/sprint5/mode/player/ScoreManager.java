package sprint5.mode.player;

import sprint5.util.Winner;

//Score manager manages the player score in the game
public class ScoreManager {
	private int blueScore;
	private int redScore;

	// Constructor
	public ScoreManager() {
		this.blueScore = 0;
		this.redScore = 0;
	}

	// add points to blue player
	public void addBlueScore(int points) {
		if (points < 0) {
			throw new IllegalArgumentException("Points can not be negative");
		}
		this.blueScore += points;
	}

	// add points to Red player
	public void addRedScore(int points) {
		if (points < 0) {
			throw new IllegalArgumentException("Points can not be negative");
		}
		this.redScore += points;
	}

	/* Add points to a player's score by color */
	public void addScore(PlayerColor color, int points) {
		if (color == PlayerColor.BLUE) {
			addBlueScore(points);
		} else {
			addRedScore(points);
		}
	}

	// Get score
	public int getScore(PlayerColor color) {
		return color == PlayerColor.BLUE ? blueScore : redScore;
	}

	// Determine winner based on current score
	public Winner determineWinner() {
		if (blueScore > redScore) {
			return Winner.BLUE;
		} else if (redScore > blueScore) {
			return Winner.RED;
		} else {
			return Winner.DRAW;
		}
	}

	@Override
	public String toString() {
		return String.format("Blue: %d, Red: %d", blueScore, redScore);
	}

	public int getScoreDifference() {
		return blueScore - redScore;
	}

	// Reset score
	public void reset() {
		this.blueScore = 0;
		this.redScore = 0;
	}

	// Check for Tied scores
	public boolean isTied() {
		return blueScore == redScore;
	}

	// Blue winner
	public boolean isBlueWinning() {
		return blueScore > redScore;
	}
	// Red Winner

	public boolean isRedWinning() {
		return redScore > blueScore;
	}

	// getters
	public int getBlueScore() {
		return blueScore;
	}

	public int getRedScore() {
		return redScore;
	}
}
