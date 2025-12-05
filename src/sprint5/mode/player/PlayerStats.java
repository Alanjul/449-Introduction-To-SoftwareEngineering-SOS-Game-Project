package sprint5.mode.player;

public class PlayerStats {
	private String playerName;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int gamesDrawn;
	private int totalSOSFormed;
	private int totalPointsScored;

	public PlayerStats() {
		this("Unknown", 0, 0, 0, 0, 0, 0);
	}

	public PlayerStats(String playerName) {
		this(playerName, 0, 0, 0, 0, 0, 0);
	}

	public PlayerStats(String playerName, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn,
			int totalSOSFormed, int totalPointScored) {
		validateStats(gamesPlayed, gamesWon, gamesLost, gamesDrawn);
		this.playerName = playerName;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.gamesDrawn = gamesDrawn;
		this.totalSOSFormed = totalSOSFormed;
		this.totalPointsScored = totalPointsScored;
	}

	// Validate
	private void validateStats(int played, int won, int lost, int drawn) {
		if (played < 0 || won < 0 || lost < 0 || drawn < 0) {
			throw new IllegalArgumentException("Statistics can not be negative");
		}

		if (won + lost + drawn != played) {
			throw new IllegalArgumentException(
					String.format("Games played (%d) must equal won + lost + drawn (%d + %d + %d = %d)", played, won,
							lost, drawn, won + lost + drawn));
		}
	}

	// record win
	public void recordWin() {
		gamesPlayed++;
		gamesWon++;
	}

	// record loss
	public void recordLoss() {
		gamesPlayed++;
		gamesLost++;
	}

	// record draw
	public void recordDraw() {
		gamesPlayed++;
		gamesDrawn++;
	}

	// record sos formed
	public void recordSOSFormed(int sosCount) {
		if (sosCount < 0) {
			throw new IllegalArgumentException("SOS count cannot be negative");
		}
		this.totalSOSFormed += sosCount;
	}

	// records points scored
	public void recordPointsScored(int points) {
		if (points < 0) {
			throw new IllegalArgumentException("Points cannot be negative");
		}
		this.totalPointsScored += points;
	}

	// Reset stats
	public void resetStats() {
		gamesPlayed = 0;
		gamesWon = 0;
		gamesLost = 0;
		gamesDrawn = 0;
		totalSOSFormed = 0;
		totalPointsScored = 0;
	}

	// win percentage
	public double getWinPercentage() {
		if (gamesPlayed == 0) {
			return 0.0;
		}
		return (gamesWon * 100.0) / gamesPlayed;
	}

	// loss percentage
	public double getLossPercentage() {
		if (gamesPlayed == 0) {
			return 0.0;
		}
		return (gamesLost * 100.0) / gamesPlayed;
	}

	// draw percentage
	public double getDrawPercentage() {
		if (gamesPlayed == 0) {
			return 0.0;
		}
		return (gamesDrawn * 100.0) / gamesPlayed;
	}

	// average SOS formed
	public double getAverageSOSPerGame() {
		if (gamesPlayed == 0) {
			return 0.0;
		}
		return (double) totalSOSFormed / gamesPlayed;
	}

	// points per game
	public double getAveragePointsPerGame() {
		if (gamesPlayed == 0) {
			return 0.0;
		}
		return (double) totalPointsScored / gamesPlayed;
	}

	public void setPlayerName(String playerName) {
		if (playerName == null || playerName.trim().isEmpty()) {
			throw new IllegalArgumentException("Player name cannot be null or empty");
		}
		this.playerName = playerName;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}

	public int getGamesWon() {
		return gamesWon;
	}

	public int getGamesLost() {
		return gamesLost;
	}

	public int getGamesDrawn() {
		return gamesDrawn;
	}

	public int getTotalSOSFormed() {
		return totalSOSFormed;
	}

	public int getTotalPointsScored() {
		return totalPointsScored;
	}

	/**
	 * Get summary of statistics.
	 * @return formatted statistics string
	 */
	public String getSummary() {
		return String.format(
				"Player: %s\n" + "Games Played: %d (W: %d, L: %d, D: %d)\n" + "Win Rate: %.1f%%\n"
						+ "Total SOS Formed: %d (Avg: %.2f per game)\n" + "Total Points: %d (Avg: %.2f per game)",
				playerName, gamesPlayed, gamesWon, gamesLost, gamesDrawn, getWinPercentage(), totalSOSFormed,
				getAverageSOSPerGame(), totalPointsScored, getAveragePointsPerGame());
	}

	/**
	 * generate statistics string.
	 * @return compact stats string
	 */
	@Override
	public String toString() {
		return String.format("PlayerStats[%s: %dW-%dL-%dD (%.1f%%), SOS:%d, Pts:%d]", playerName, gamesWon, gamesLost,
				gamesDrawn, getWinPercentage(), totalSOSFormed, totalPointsScored);
	}

	/**
	 * Check equality based on all statistics.
	 *
	 * @param obj the object to compare
	 * @return true if all statistics match
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PlayerStats other)) {
			return false;
		}

		return gamesPlayed == other.gamesPlayed && gamesWon == other.gamesWon && gamesLost == other.gamesLost
				&& gamesDrawn == other.gamesDrawn && totalSOSFormed == other.totalSOSFormed
				&& totalPointsScored == other.totalPointsScored && playerName.equals(other.playerName);
	}

	/**
	 * Generate hash code based on statistics.
	 *
	 * @return hash code
	 */
	@Override
	public int hashCode() {
		int result = playerName.hashCode();
		result = 31 * result + gamesPlayed;
		result = 31 * result + gamesWon;
		result = 31 * result + gamesLost;
		result = 31 * result + gamesDrawn;
		result = 31 * result + totalSOSFormed;
		result = 31 * result + totalPointsScored;
		return result;
	}

	public PlayerStats copy() {
		return new PlayerStats(playerName, gamesPlayed, gamesWon, gamesLost, gamesDrawn, totalSOSFormed,
				totalPointsScored);
	}
}
