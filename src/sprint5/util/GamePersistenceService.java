package sprint5.util;

import java.time.*;
import java.util.*;

import javax.swing.*;

import sprint5.mode.GameMode.*;
import sprint5.mode.dao.*;
import sprint5.mode.move.*;
import sprint5.mode.player.*;

/** Handle database persistence */
public class GamePersistenceService {
	private final PlayerRecordDAO playerDAO;
	private final GameRecordDao gameDAO;
	private final StatsRecordDao statsDAO;

	// Constructor
	public GamePersistenceService(PlayerRecordDAO playerDAO, GameRecordDao gameDAO, StatsRecordDao statsDAO) {
		this.playerDAO = playerDAO;
		this.gameDAO = gameDAO;
		this.statsDAO = statsDAO;
	}

	// Starts Game Session
	public GameSession startGameSession(String bluePlayerName, String redPlayerName, String gameModeStr, int boardSize,
			boolean blueIsHuman, boolean redIsHuman) {
		try {
			String uniqueBluePlayerName = bluePlayerName;
			String uniqueRedPlayerName = redPlayerName;

			if (bluePlayerName.equals(redPlayerName)) {
				uniqueBluePlayerName = bluePlayerName + " (Blue)";
				uniqueRedPlayerName = redPlayerName + " (Red)";
			}

			// Load or create players with unique names
			PlayerRecord bluePlayer = loadOrCreatePlayer(uniqueBluePlayerName, blueIsHuman);
			PlayerRecord redPlayer = loadOrCreatePlayer(uniqueRedPlayerName, redIsHuman);

			// Get player IDs
			Integer bluePlayerId = bluePlayer.getPlayerId();
			Integer redPlayerId = redPlayer.getPlayerId();

			// Convert string to GameMode4 enum
			GameMode4 gameMode = GameMode4.valueOf(gameModeStr);

			// Determine player types
			PlayerType blueType = blueIsHuman ? PlayerType.HUMAN : PlayerType.COMPUTER;
			PlayerType redType = redIsHuman ? PlayerType.HUMAN : PlayerType.COMPUTER;

			// Create game record using Builder pattern
			GameRecord gameRecord = new GameRecord.Builder(bluePlayerId, redPlayerId, blueType, redType, gameMode,
					boardSize).build();

			// Save game to database
			Integer gameId = gameDAO.create(gameRecord);

			// Initialize session statistics with unique names
			PlayerStats blueStats = new PlayerStats(uniqueBluePlayerName);
			PlayerStats redStats = new PlayerStats(uniqueRedPlayerName);

			// Create and return game session
			return new GameSession(gameId, bluePlayerId, redPlayerId, blueStats, redStats, LocalDateTime.now());

		} catch (Exception e) {
			throw new RuntimeException("Failed to start game session: " + e.getMessage(), e);
		}
	}

	private PlayerRecord loadOrCreatePlayer(String playerName, boolean isHuman) {
		// Search for existing player by name
		List<PlayerRecord> existingPlayers = playerDAO.searchByPattern(playerName);

		// If exact match found, return it
		for (PlayerRecord player : existingPlayers) {
			if (player.getName().equals(playerName)) {
				return player;
			}
		}

		String playerType = isHuman ? "HUMAN" : "COMPUTER";
		PlayerRecord newPlayer = new PlayerRecord(playerName, playerName, playerType);
		playerDAO.create(newPlayer);

		return newPlayer;
	}

	// Record Move to database
	public void recordMove(Integer gameId, int moveNumber, Integer playerId, int row, int col, char letter,
			int sosFormed, int pointsScored) {
		try {
			GameMove move = new GameMove(gameId, moveNumber, playerId, row, col, letter, sosFormed, pointsScored);
			gameDAO.recordMove(move);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Warning: Failed to record move: " + e.getMessage());
		}
	}

	// Complete game
	public void completeGame(GameSession session, Winner winner, int blueScore, int redScore,
			PlayerStats blueSessionStats, PlayerStats redSessionStats) {
		try {
			// Calculate duration
			Duration duration = Duration.between(session.getStartTime(), LocalDateTime.now());
			int durationSeconds = (int) duration.getSeconds();

			// Determine winner ID
			Integer winnerId = determineWinnerId(winner, session.getBluePlayerId(), session.getRedPlayerId());

			// Complete game record
			gameDAO.completeGame(session.getGameId(), winnerId, blueScore, redScore);

			// Update player statistics
			updatePlayerStatistics(session.getBluePlayerId(), blueSessionStats, winner == Winner.BLUE,
					winner == Winner.DRAW);

			updatePlayerStatistics(session.getRedPlayerId(), redSessionStats, winner == Winner.RED,
					winner == Winner.DRAW);

			// Update last played timestamp
			playerDAO.updateLastPlayed(session.getBluePlayerId());
			playerDAO.updateLastPlayed(session.getRedPlayerId());

		} catch (Exception e) {
			throw new RuntimeException("Failed to complete game: " + e.getMessage(), e);
		}
	}

	// Update player statistics in database
	private void updatePlayerStatistics(Integer playerId, PlayerStats sessionStats, boolean won, boolean draw) {
		try {
			int gamesWon = won ? 1 : 0;
			int gamesLost = (!won && !draw) ? 1 : 0;
			int gamesDrawn = draw ? 1 : 0;

			statsDAO.updateAfterGame(playerId, gamesWon, gamesLost, gamesDrawn, sessionStats.getTotalSOSFormed(),
					sessionStats.getTotalPointsScored());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Warning: Failed to update statistics: " + e.getMessage());
		}
	}

	// Create Stats for a Player
	public StatsRecord loadOrCreateStats(Integer playerId) {
		return statsDAO.findByPlayerId(playerId).orElseGet(() -> {
			StatsRecord newStats = new StatsRecord(null, playerId, 0, 0, 0, 0, 0, 0, LocalDateTime.now());
			statsDAO.create(newStats);
			return newStats;
		});
	}

	private PlayerStats convertToPlayerStats(PlayerStats stats, String playerName) {
		return new PlayerStats(playerName, stats.getGamesPlayed(), stats.getGamesWon(), stats.getGamesLost(),
				stats.getGamesDrawn(), stats.getTotalSOSFormed(), stats.getTotalPointsScored());
	}

	// Helper method to determine the winner
	private Integer determineWinnerId(Winner winner, Integer bluePlayerId, Integer redPlayerId) {
		if (winner == null) {
			return null;
		}

		switch (winner) {
		case BLUE:
			return bluePlayerId;
		case RED:
			return redPlayerId;
		case DRAW:
		case NONE:
		default:
			return null;
		}
	}

	// Game Session helper class
	public static class GameSession {
		private final Integer gameId;
		private final Integer bluePlayerId;
		private final Integer redPlayerId;
		private final PlayerStats blueSessionStats;
		private final PlayerStats redSessionStats;
		private final LocalDateTime startTime;

		public GameSession(Integer gameId, Integer bluePlayerId, Integer redPlayerId, PlayerStats blueSessionStats,
				PlayerStats redSessionStats, LocalDateTime startTime) {
			this.gameId = gameId;
			this.bluePlayerId = bluePlayerId;
			this.redPlayerId = redPlayerId;
			this.blueSessionStats = blueSessionStats;
			this.redSessionStats = redSessionStats;
			this.startTime = startTime;
		}

		public Integer getGameId() {
			return gameId;
		}

		public Integer getBluePlayerId() {
			return bluePlayerId;
		}

		public Integer getRedPlayerId() {
			return redPlayerId;
		}

		public PlayerStats getBlueSessionStats() {
			return blueSessionStats;
		}

		public PlayerStats getRedSessionStats() {
			return redSessionStats;
		}

		public LocalDateTime getStartTime() {
			return startTime;
		}
	}
}
