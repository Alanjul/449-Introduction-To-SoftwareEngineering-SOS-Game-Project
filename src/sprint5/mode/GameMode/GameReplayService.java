package sprint5.mode.GameMode;

import java.time.*;
import java.util.*;
import java.util.stream.*;

import javax.swing.*;

import sprint5.mode.dao.*;
import sprint5.mode.move.*;

public class GameReplayService {
	private final GameRecordDao gameDAO;
	private final PlayerRecordDAO playerDAO;

	public GameReplayService() {
		this.gameDAO = new GameRecordImplementation();
		this.playerDAO = new PlayerRecordImplementation();
	}

	/**
	 * Constructor.
	 */
	public GameReplayService(GameRecordDao gameDAO, PlayerRecordDAO playerDAO) {
		this.gameDAO = gameDAO;
		this.playerDAO = playerDAO;
	}

	/**
	 * Load a game replay by game ID.
	 * 
	 * @param gameId the game ID
	 * @return Optional containing GameReplay if found
	 */
	public Optional<GameReplay> loadReplay(Integer gameId) {
		// Get game
		try {
			Optional<GameRecord> gameOpt = gameDAO.findById(gameId);
			if (!gameOpt.isPresent()) {
				return Optional.empty();
			}
			GameRecord game = gameOpt.get();
			if (!game.isCompleted()) {
				return Optional.empty();
			}
			// Get players
			Optional<PlayerRecord> blueOpt = playerDAO.findById(game.getBluePlayerId());
			Optional<PlayerRecord> redOpt = playerDAO.findById(game.getRedPlayerId());

			if (!blueOpt.isPresent() || !redOpt.isPresent()) {
				return Optional.empty();
			}

			// Get moves
			List<GameMove> moves = gameDAO.getGameMoves(gameId);

			// Create replay
			GameReplay replay = new GameReplay(game, blueOpt.get(), redOpt.get(), moves);

			return Optional.of(replay);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			return Optional.empty();
		}
	}

	/**
	 * Get all completed games available for replay.
	 * 
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getAllReplays() {
		try {
			List<GameRecord> games = gameDAO.findAll();
			return loadMultipleReplays(games);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting all replays" + e.getMessage());
			return new ArrayList<>();
		}

	}

	/**
	 * Get recent games available for replay.
	 * 
	 * @param limit number of games to retrieve
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getRecentReplays(int limit) {
		try {
			List<GameRecord> games = gameDAO.getRecentGames(limit);

			return loadMultipleReplays(games);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting recent replays: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Get replays for a specific player.
	 * 
	 * @param playerId the player ID
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getPlayerReplays(Integer playerId) {
		try {
			List<GameRecord> games = gameDAO.getPlayerGames(playerId);
			return loadMultipleReplays(games);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting player replays: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Get replays between two players.
	 * 
	 * @param player1Id first player ID
	 * @param player2Id second player ID
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getReplaysBetweenPlayers(Integer player1Id, Integer player2Id) {
		try {
			List<GameRecord> games = gameDAO.getGamesBetweenPlayers(player1Id, player2Id);
			return loadMultipleReplays(
					games.stream().filter(GameRecord::isCompleted).collect(java.util.stream.Collectors.toList()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting replays between players " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Get replays by game mode.
	 * 
	 * @param gameMode "SIMPLE" or "GENERAL"
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getReplaysByMode(String gameMode) {
		try {
			List<GameRecord> games = gameDAO.getGamesByMode(gameMode);
			return loadMultipleReplays(
					games.stream().filter(GameRecord::isCompleted).collect(java.util.stream.Collectors.toList()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting replay mode: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Get replays within a date
	 * 
	 * @param start start date
	 * @param end   end date
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> getReplaysByDateRange(LocalDateTime start, LocalDateTime end) {
		try {
			List<GameRecord> games = gameDAO.getGamesByDateRange(start, end);
			return loadMultipleReplays(
					games.stream().filter(GameRecord::isCompleted).collect(java.util.stream.Collectors.toList()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error getting replays by date: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	/**
	 * Search replays by player name.
	 * 
	 * @param playerName player name pattern
	 * @return list of GameReplay objects
	 */
	public List<GameReplay> searchReplaysByPlayer(String playerName) {
		try {
			List<PlayerRecord> players = playerDAO.searchByPattern(playerName);
			if (players.isEmpty()) {
				return new ArrayList<>();
			}
			List<GameReplay> allReplays = new ArrayList<>();
			for (PlayerRecord player : players) {
				allReplays.addAll(getPlayerReplays(player.getPlayerId()));
			}

			// remove duplicate
			return allReplays.stream().distinct().sorted((r1, r2) -> r2.getCreatedAt().compareTo(r1.getCreatedAt()))
					.collect(Collectors.toList());

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error searching replays: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	// Replay statistics for a game
	public ReplayStatistics getReplayStatistics(Integer gameId) {
		try {
			Optional<GameReplay> replay = loadReplay(gameId);
			if (replay.isEmpty()) {
				return null;
			}

			return new ReplayStatistics(replay.get());

		} catch (Exception e) {
			System.err.println("Error getting replay statistics: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Check if a game can be replayed.
	 * 
	 * @param gameId the game ID
	 * @return true if game is completed and has moves
	 */
	public boolean canReplay(Integer gameId) {
		try {
			Optional<GameRecord> game = gameDAO.findById(gameId);
			return game.isPresent() && game.get().isCompleted();
		} catch (Exception e) {
			return false;
		}
	}

	// Get the number of available replays
	public int getTotalReplayCount() {
		List<GameRecord> completedGames = gameDAO.getGamesByStatus("COMPLETED");
		return completedGames.size();

	}

	/**
	 * Get replay counts by mode
	 */
	public ReplayModeCounts getReplayCountsByMode() {
		try {
			int simpleCount = gameDAO.getGameCountByMode("SIMPLE");
			int generalCount = gameDAO.getGameCountByMode("GENERAL");
			return new ReplayModeCounts(simpleCount, generalCount);
		} catch (Exception e) {
			return new ReplayModeCounts(0, 0);
		}
	}

	/**
	 * Helper method to load multiple replays.
	 */
	private List<GameReplay> loadMultipleReplays(List<GameRecord> games) {
		List<GameReplay> replays = new ArrayList<>();

		for (GameRecord game : games) {
			// Only include completed games
			if (!game.isCompleted()) {
				continue;
			}

			Optional<GameReplay> replay = loadReplay(game.getGameId());
			replay.ifPresent(replays::add);
		}

		return replays;
	}

	public int getMoveCount(Integer gameId) {
		try {
			List<GameMove> moves = gameDAO.getGameMoves(gameId);
			return moves.size();
		} catch (Exception e) {
			return 0;
		}
	}

	public boolean deleteReplay(Integer gameId) {
		try {
			return gameDAO.delete(gameId);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error deleting replay: " + e.getMessage());
			return false;
		}
	}

	// Inner
	public static class ReplayStatistics {
		private final GameReplay replay;

		public ReplayStatistics(GameReplay replay) {
			this.replay = replay;
		}

		public int getTotalMoves() {
			return replay.getTotalMoves();
		}

		public int getBlueScore() {
			return replay.getBlueScore();
		}

		public int getRedScore() {
			return replay.getRedScore();
		}

		public String getWinner() {
			return replay.getWinnerName();
		}

		public long getDurationSeconds() {
			return replay.getDurationSeconds();
		}

		public double getAverageMoveTime() {
			if (replay.getTotalMoves() == 0)
				return 0.0;
			return (double) replay.getDurationSeconds() / replay.getTotalMoves();
		}

		public int getTotalSOSFormed() {
			return replay.getMoves().stream().mapToInt(GameMove::getSosFormed).sum();
		}

		public String getFormattedDuration() {
			long seconds = getDurationSeconds();
			long minutes = seconds / 60;
			long secs = seconds % 60;
			return String.format("%d:%02d", minutes, secs);
		}

		@Override
		public String toString() {
			return String.format("Moves: %d | Duration: %s | Score: %d-%d | Winner: %s | Total SOS: %d",
					getTotalMoves(), getFormattedDuration(), getBlueScore(), getRedScore(), getWinner(),
					getTotalSOSFormed());
		}
	}

	public static class ReplayModeCounts {
		private final int simpleCount;
		private final int generalCount;

		public ReplayModeCounts(int simpleCount, int generalCount) {
			this.simpleCount = simpleCount;
			this.generalCount = generalCount;
		}

		public int getSimpleCount() {
			return simpleCount;
		}

		public int getGeneralCount() {
			return generalCount;
		}

		public int getTotalCount() {
			return simpleCount + generalCount;
		}

		@Override
		public String toString() {
			return String.format("Simple: %d | General: %d | Total: %d", simpleCount, generalCount, getTotalCount());
		}
	}

}
