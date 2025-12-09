package sprint5.mode.dao;

import java.time.*;
import java.util.*;

public class StatsRecord {
	private Integer statsId;
	private final Integer playerId;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int gamesDrawn;
	private int totalSosFormed;
	private int totalPointsScored;
	private LocalDateTime lastUpdated;

	// constructor
	public StatsRecord(Integer playerId) {
		this(null, playerId, 0, 0, 0, 0, 0, 0, LocalDateTime.now());
	}

	public StatsRecord(Integer statsId, Integer playerId, int gamesPlayed, int gamesWon, int gamesLost, int gamesDrawn,
			int totalSosFormed, int totalPointsScored, LocalDateTime lastUpdated) {
		validatePlayerId(playerId);
		validateStats(gamesPlayed, gamesWon, gamesLost, gamesDrawn, totalSosFormed, totalPointsScored);

		this.statsId = statsId;
		this.playerId = playerId;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.gamesDrawn = gamesDrawn;
		this.totalSosFormed = totalSosFormed;
		this.totalPointsScored = totalPointsScored;
		this.lastUpdated = lastUpdated != null ? lastUpdated : LocalDateTime.now();
	}

	// validate
	private void validatePlayerId(Integer playerId) {
		if (playerId == null) {
			throw new IllegalArgumentException("Player ID cannot be null");
		}
	}

	private void validateStats(int played, int won, int lost, int drawn, int sos, int points) {
		if (played < 0 || won < 0 || lost < 0 || drawn < 0 || sos < 0 || points < 0) {
			throw new IllegalArgumentException("Stats cannot be negative");
		}
		if (played != (won + lost + drawn)) {
			throw new IllegalArgumentException(
					String.format("Games played (%d) must equal won + lost + drawn (%d)", played, won + lost + drawn));
		}

	}

	// Getters
	public Integer getStatsId() {
		return statsId;
	}

	public Integer getPlayerId() {
		return playerId;
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

	public int getTotalSosFormed() {
		return totalSosFormed;
	}

	public int getTotalPointsScored() {
		return totalPointsScored;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	// Setter
	public void setStatsId(Integer statsId) {
		if (this.statsId != null) {
			throw new IllegalStateException("Stats ID already set");
		}
		this.statsId = statsId;
	}

	// Update
	public void recordWin() {
		this.gamesWon++;
		this.gamesPlayed++;
		this.lastUpdated = LocalDateTime.now();
	}

	public void recordLoss() {
		this.gamesLost++;
		this.gamesPlayed++;
		this.lastUpdated = LocalDateTime.now();
	}

	public void recordDraw() {
		this.gamesDrawn++;
		this.gamesPlayed++;
		this.lastUpdated = LocalDateTime.now();
	}

	public void addSosFormed(int sosCount) {
		if (sosCount < 0)
			throw new IllegalArgumentException("SOS count cannot be negative");
		this.totalSosFormed += sosCount;
		this.lastUpdated = LocalDateTime.now();
	}

	public void addPointsScored(int points) {
		if (points < 0)
			throw new IllegalArgumentException("Points cannot be negative");
		this.totalPointsScored += points;
		this.lastUpdated = LocalDateTime.now();
	}

	public double getWinRate() {
		return gamesPlayed == 0 ? 0.0 : (double) gamesWon / gamesPlayed * 100.0;
	}

	public double getAvgSosPerGame() {
		return gamesPlayed == 0 ? 0.0 : (double) totalSosFormed / gamesPlayed;
	}

	public double getAvgPointsPerGame() {
		return gamesPlayed == 0 ? 0.0 : (double) totalPointsScored / gamesPlayed;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof StatsRecord))
			return false;
		StatsRecord that = (StatsRecord) o;
		return Objects.equals(statsId, that.statsId) && Objects.equals(playerId, that.playerId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(statsId, playerId);
	}

	@Override
	public String toString() {
		return String.format("StatsRecord[played=%d, won=%d, lost=%d, drawn=%d, winRate=%.1f%%]", gamesPlayed, gamesWon,
				gamesLost, gamesDrawn, getWinRate());
	}

}
