package sprint5.mode.dao;

import java.sql.*;
import java.time.*;
import java.util.*;

import sprint5.mode.GameMode.*;
import sprint5.mode.move.*;
import sprint5.mode.player.*;

public class GameRecordImplementation extends AbstractDAO<GameRecord> implements GameRecordDao {

	// Game SQL
	private static final String SQL_INSERT_GAME = "INSERT INTO games (blue_player_id, red_player_id, game_mode, board_size) VALUES (?, ?, ?, ?)";
	private static final String SQL_SELECT_GAME = "SELECT * FROM games WHERE game_id = ?";
	private static final String SQL_SELECT_ALL_GAMES = "SELECT * FROM games ORDER BY started_at DESC";
	private static final String SQL_UPDATE_GAME = "UPDATE games SET blue_score = ?, red_score = ?, winner_id = ?, game_status = ?, total_moves = ?, duration_seconds = ?, completed_at = ? WHERE game_id = ?";
	private static final String SQL_DELETE_GAME = "DELETE FROM games WHERE game_id = ?";
	private static final String SQL_COMPLETE_GAME = "UPDATE games SET winner_id = ?, blue_score = ?, red_score = ?, game_status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP, duration_seconds = TIMESTAMPDIFF(SECOND, started_at, CURRENT_TIMESTAMP) WHERE game_id = ?";

	// Move SQL
	private static final String SQL_INSERT_MOVE = "INSERT INTO game_moves (game_id, move_number, player_id, row_position, col_position, letter, sos_formed, points_scored) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_SELECT_MOVES = "SELECT * FROM game_moves WHERE game_id = ? ORDER BY move_number";

	// Query SQL
	private static final String SQL_RECENT_GAMES = "SELECT * FROM games ORDER BY started_at DESC LIMIT ?";
	private static final String SQL_PLAYER_GAMES = "SELECT * FROM games WHERE blue_player_id = ? OR red_player_id = ? ORDER BY started_at DESC";
	private static final String SQL_GAMES_BETWEEN = "SELECT * FROM games WHERE (blue_player_id = ? AND red_player_id = ?) OR (blue_player_id = ? AND red_player_id = ?) ORDER BY started_at DESC";
	private static final String SQL_GAMES_BY_MODE = "SELECT * FROM games WHERE game_mode = ? ORDER BY started_at DESC";
	private static final String SQL_GAMES_BY_STATUS = "SELECT * FROM games WHERE game_status = ? ORDER BY started_at DESC";
	private static final String SQL_INCOMPLETE_GAMES = "SELECT * FROM games WHERE game_status = 'IN_PROGRESS' ORDER BY started_at DESC";
	private static final String SQL_GAMES_BY_DATE_RANGE = "SELECT * FROM games WHERE started_at BETWEEN ? AND ? ORDER BY started_at DESC";
	private static final String SQL_COUNT_GAMES = "SELECT COUNT(*) FROM games";
	private static final String SQL_COUNT_BY_MODE = "SELECT COUNT(*) FROM games WHERE game_mode = ?";

	@Override
	protected GameRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
		// Convert String to GameMode4 enum
		GameMode4 gameMode = GameMode4.valueOf(rs.getString("game_mode"));

		// Convert String to GameStatus enum
		GameStatus status = GameStatus.valueOf(rs.getString("game_status"));

		// Build GameRecord using Builder pattern
		return new GameRecord.Builder(rs.getInt("blue_player_id"), rs.getInt("red_player_id"), PlayerType.HUMAN, // Default,
																													// will
																													// be
																													// updated
																													// if
																													// needed
				PlayerType.HUMAN, // Default, will be updated if needed
				gameMode, rs.getInt("board_size")).gameId(rs.getInt("game_id"))
				.scores(rs.getInt("blue_score"), rs.getInt("red_score"))
				.winner(rs.getObject("winner_id", Integer.class)).status(status).totalMoves(rs.getInt("total_moves"))
				.durationSeconds(rs.getObject("duration_seconds", Long.class))
				.startedAt(rs.getTimestamp("started_at").toLocalDateTime())
				.completedAt(rs.getTimestamp("completed_at") != null ? rs.getTimestamp("completed_at").toLocalDateTime()
						: null)
				.build();
	}

	@Override
	protected String getTableName() {
		return "games";
	}

	@Override
	public Integer create(GameRecord game) {
		// Convert enum to string for database
		String gameModeStr = game.getGameMode().name();

		Integer gameId = executeInsert(SQL_INSERT_GAME, game.getBluePlayerId(), game.getRedPlayerId(), gameModeStr,
				game.getBoardSize());
		return gameId;
	}

	@Override
	public Optional<GameRecord> findById(Integer gameId) {
		List<GameRecord> results = executeQuery(SQL_SELECT_GAME, gameId);
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	@Override
	public List<GameRecord> findAll() {
		return executeQuery(SQL_SELECT_ALL_GAMES);
	}

	@Override
	public boolean update(GameRecord game) {
		// Convert enum to string for database
		String statusStr = game.getGameStatus().name();

		return executeUpdate(SQL_UPDATE_GAME, game.getBlueScore(), game.getRedScore(), game.getWinnerId(), statusStr,
				game.getTotalMoves(), game.getDurationSeconds(),
				game.getCompletedAt() != null ? Timestamp.valueOf(game.getCompletedAt()) : null, game.getGameId()) > 0;
	}

	@Override
	public boolean delete(Integer gameId) {
		return executeUpdate(SQL_DELETE_GAME, gameId) > 0;
	}

	@Override
	public Integer recordMove(GameMove move) {
		Integer moveId = executeInsert(SQL_INSERT_MOVE, move.getGameId(), move.getMoveNumber(), move.getPlayerId(),
				move.getRowPosition(), move.getColPosition(), String.valueOf(move.getLetter()), move.getSosFormed(),
				move.getPointsScored());
		move.setMoveId(moveId);
		return moveId;
	}

	@Override
	public List<GameMove> getGameMoves(Integer gameId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_MOVES)) {

			stmt.setInt(1, gameId);
			return executeMoveQuery(stmt);

		} catch (SQLException e) {
			String msg = String.format("Error getting moves for game: %d", gameId);
			LOGGER.log(java.util.logging.Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public void completeGame(Integer gameId, Integer winnerId, int blueScore, int redScore) {
		executeUpdate(SQL_COMPLETE_GAME, winnerId, blueScore, redScore, gameId);
	}

	@Override
	public List<GameRecord> getRecentGames(int limit) {
		return executeQuery(SQL_RECENT_GAMES, limit);
	}

	@Override
	public List<GameRecord> getPlayerGames(Integer playerId) {
		return executeQuery(SQL_PLAYER_GAMES, playerId, playerId);
	}

	@Override
	public List<GameRecord> getGamesBetweenPlayers(Integer player1Id, Integer player2Id) {
		return executeQuery(SQL_GAMES_BETWEEN, player1Id, player2Id, player2Id, player1Id);
	}

	@Override
	public List<GameRecord> getGamesByMode(String gameMode) {
		return executeQuery(SQL_GAMES_BY_MODE, gameMode);
	}

	@Override
	public List<GameRecord> getGamesByStatus(String gameStatus) {
		return executeQuery(SQL_GAMES_BY_STATUS, gameStatus);
	}

	@Override
	public List<GameRecord> getIncompleteGames() {
		return executeQuery(SQL_INCOMPLETE_GAMES);
	}

	@Override
	public List<GameRecord> getGamesByDateRange(LocalDateTime start, LocalDateTime end) {
		return executeQuery(SQL_GAMES_BY_DATE_RANGE, Timestamp.valueOf(start), Timestamp.valueOf(end));
	}

	@Override
	public int getTotalGameCount() {
		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL_COUNT_GAMES)) {

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			String msg = "Error getting total game count";
			LOGGER.log(java.util.logging.Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
		return 0;
	}

	@Override
	public int getGameCountByMode(String gameMode) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_COUNT_BY_MODE)) {

			stmt.setString(1, gameMode);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			String msg = String.format("Error getting game count for mode: %s", gameMode);
			LOGGER.log(java.util.logging.Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
		return 0;
	}

	private List<GameMove> executeMoveQuery(PreparedStatement stmt) throws SQLException {
		List<GameMove> moves = new ArrayList<>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				moves.add(mapResultSetToMove(rs));
			}
		}
		return moves;
	}

	private GameMove mapResultSetToMove(ResultSet rs) throws SQLException {
		return new GameMove(rs.getInt("move_id"), rs.getInt("game_id"), rs.getInt("move_number"),
				rs.getInt("player_id"), rs.getInt("row_position"), rs.getInt("col_position"),
				rs.getString("letter").charAt(0), rs.getInt("sos_formed"), rs.getInt("points_scored"),
				rs.getTimestamp("move_timestamp").toLocalDateTime());
	}
}