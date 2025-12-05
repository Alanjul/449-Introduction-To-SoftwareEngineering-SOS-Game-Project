package sprint5.mode.dao;

import java.sql.*;
import java.time.*;
import java.util.*;

import sprint5.mode.GameMode.*;
import sprint5.mode.move.*;

/**
 * GameRecordDAOImplementation implement GameRecordDao Interface.
 */
public class GameRecordImplementation implements GameRecordDao {

	// Game SQL
	private static final String SQL_INSERT_GAME = "INSERT INTO games (blue_player_id, red_player_id, game_mode, board_size) VALUES (?, ?, ?, ?)";

	private static final String SQL_SELECT_GAME = "SELECT * FROM games WHERE game_id = ?";

	private static final String SQL_SELECT_ALL_GAMES = "SELECT * FROM games ORDER BY started_at DESC";

	private static final String SQL_UPDATE_GAME = "UPDATE games SET blue_score = ?, red_score = ?, winner_id = ?, "
			+ "game_status = ?, total_moves = ?, duration_seconds = ?, completed_at = ? " + "WHERE game_id = ?";

	private static final String SQL_DELETE_GAME = "DELETE FROM games WHERE game_id = ?";

	private static final String SQL_COMPLETE_GAME = "UPDATE games SET winner_id = ?, blue_score = ?, red_score = ?, "
			+ "game_status = 'COMPLETED', completed_at = CURRENT_TIMESTAMP, "
			+ "duration_seconds = TIMESTAMPDIFF(SECOND, started_at, CURRENT_TIMESTAMP) " + "WHERE game_id = ?";

	// Move SQL
	private static final String SQL_INSERT_MOVE = "INSERT INTO game_moves (game_id, move_number, player_id, row_position, "
			+ "col_position, letter, sos_formed, points_scored) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	private static final String SQL_SELECT_MOVES = "SELECT * FROM game_moves WHERE game_id = ? ORDER BY move_number";

	// Query SQL
	private static final String SQL_RECENT_GAMES = "SELECT * FROM games ORDER BY started_at DESC LIMIT ?";

	private static final String SQL_PLAYER_GAMES = "SELECT * FROM games WHERE blue_player_id = ? OR red_player_id = ? "
			+ "ORDER BY started_at DESC";

	private static final String SQL_GAMES_BETWEEN = "SELECT * FROM games WHERE (blue_player_id = ? AND red_player_id = ?) "
			+ "OR (blue_player_id = ? AND red_player_id = ?) ORDER BY started_at DESC";

	private static final String SQL_GAMES_BY_MODE = "SELECT * FROM games WHERE game_mode = ? ORDER BY started_at DESC";

	private static final String SQL_GAMES_BY_STATUS = "SELECT * FROM games WHERE game_status = ? ORDER BY started_at DESC";

	private static final String SQL_INCOMPLETE_GAMES = "SELECT * FROM games WHERE game_status = 'IN_PROGRESS' ORDER BY started_at DESC";

	private static final String SQL_GAMES_BY_DATE_RANGE = "SELECT * FROM games WHERE started_at BETWEEN ? AND ? ORDER BY started_at DESC";

	private static final String SQL_COUNT_GAMES = "SELECT COUNT(*) FROM games";

	private static final String SQL_COUNT_BY_MODE = "SELECT COUNT(*) FROM games WHERE game_mode = ?";

	@Override
	public Integer create(GameRecord game) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_GAME, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, game.getBluePlayerId());
			stmt.setInt(2, game.getRedPlayerId());
			stmt.setString(3, game.getGameMode());
			stmt.setInt(4, game.getBoardSize());

			stmt.executeUpdate();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					int gameId = keys.getInt(1);
					game.setGameId(gameId);
					return gameId;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error creating game", e);
		}
		return null;
	}

	@Override
	public Optional<GameRecord> findById(Integer gameId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_GAME)) {

			stmt.setInt(1, gameId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToGame(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error finding game: " + gameId, e);
		}
		return Optional.empty();
	}

	@Override
	public List<GameRecord> findAll() {
		return executeGameQuery(SQL_SELECT_ALL_GAMES);
	}

	@Override
	public boolean update(GameRecord game) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_GAME)) {

			stmt.setInt(1, game.getBlueScore());
			stmt.setInt(2, game.getRedScore());
			stmt.setObject(3, game.getWinnerId());
			stmt.setString(4, game.getGameStatus());
			stmt.setInt(5, game.getTotalMoves());
			stmt.setObject(6, game.getDurationSeconds());
			stmt.setTimestamp(7, game.getCompletedAt() != null ? Timestamp.valueOf(game.getCompletedAt()) : null);
			stmt.setInt(8, game.getGameId());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error updating game", e);
		}
	}

	@Override
	public boolean delete(Integer gameId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_GAME)) {

			stmt.setInt(1, gameId);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error deleting game", e);
		}
	}

	@Override
	public Integer recordMove(GameMove move) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_MOVE, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, move.getGameId());
			stmt.setInt(2, move.getMoveNumber());
			stmt.setInt(3, move.getPlayerId());
			stmt.setInt(4, move.getRowPosition());
			stmt.setInt(5, move.getColPosition());
			stmt.setString(6, String.valueOf(move.getLetter()));
			stmt.setInt(7, move.getSosFormed());
			stmt.setInt(8, move.getPointsScored());

			stmt.executeUpdate();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					int moveId = keys.getInt(1);
					move.setMoveId(moveId);
					return moveId;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error recording move", e);
		}
		return null;
	}

	@Override
	public List<GameMove> getGameMoves(Integer gameId) {
		List<GameMove> moves = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_MOVES)) {

			stmt.setInt(1, gameId);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					moves.add(mapResultSetToMove(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error getting moves", e);
		}
		return moves;
	}

	@Override
	public void completeGame(Integer gameId, Integer winnerId, int blueScore, int redScore) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_COMPLETE_GAME)) {

			stmt.setObject(1, winnerId);
			stmt.setInt(2, blueScore);
			stmt.setInt(3, redScore);
			stmt.setInt(4, gameId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error completing game", e);
		}
	}

	@Override
	public List<GameRecord> getRecentGames(int limit) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_RECENT_GAMES)) {

			stmt.setInt(1, limit);
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting recent games", e);
		}
	}

	@Override
	public List<GameRecord> getPlayerGames(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_PLAYER_GAMES)) {

			stmt.setInt(1, playerId);
			stmt.setInt(2, playerId);
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting player games", e);
		}
	}

	@Override
	public List<GameRecord> getGamesBetweenPlayers(Integer player1Id, Integer player2Id) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GAMES_BETWEEN)) {

			stmt.setInt(1, player1Id);
			stmt.setInt(2, player2Id);
			stmt.setInt(3, player2Id);
			stmt.setInt(4, player1Id);
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting games between players", e);
		}
	}

	@Override
	public List<GameRecord> getGamesByMode(String gameMode) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GAMES_BY_MODE)) {

			stmt.setString(1, gameMode);
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting games by mode", e);
		}
	}

	@Override
	public List<GameRecord> getGamesByStatus(String gameStatus) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GAMES_BY_STATUS)) {

			stmt.setString(1, gameStatus);
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting games by status", e);
		}
	}

	@Override
	public List<GameRecord> getIncompleteGames() {
		return executeGameQuery(SQL_INCOMPLETE_GAMES);
	}

	@Override
	public List<GameRecord> getGamesByDateRange(LocalDateTime start, LocalDateTime end) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GAMES_BY_DATE_RANGE)) {

			stmt.setTimestamp(1, Timestamp.valueOf(start));
			stmt.setTimestamp(2, Timestamp.valueOf(end));
			return executeGameQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting games by date range", e);
		}
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
			throw new RuntimeException("Error getting game count", e);
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
			throw new RuntimeException("Error getting game count by mode", e);
		}
		return 0;
	}

	private List<GameRecord> executeGameQuery(String sql) {
		List<GameRecord> games = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				games.add(mapResultSetToGame(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error executing game query", e);
		}
		return games;
	}

	private List<GameRecord> executeGameQuery(PreparedStatement stmt) throws SQLException {
		List<GameRecord> games = new ArrayList<>();

		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				games.add(mapResultSetToGame(rs));
			}
		}
		return games;
	}

	private GameRecord mapResultSetToGame(ResultSet rs) throws SQLException {
		return new GameRecord(
				rs.getInt("game_id"), 
				rs.getInt("blue_player_id"),
				rs.getInt("red_player_id"),
				rs.getString("game_mode"), 
				rs.getInt("board_size"), 
				rs.getInt("blue_score"), 
				rs.getInt("red_score"),
				rs.getObject("winner_id", Integer.class),
				rs.getString("game_status"), 
				rs.getInt("total_moves"),
				rs.getObject("duration_seconds", Long.class), 
				rs.getTimestamp("started_at").toLocalDateTime(),
				rs.getTimestamp("completed_at") != null ? rs.getTimestamp("completed_at").toLocalDateTime() : null);
	}

	private GameMove mapResultSetToMove(ResultSet rs) throws SQLException {
		return new GameMove(rs.getInt("move_id"), rs.getInt("game_id"), rs.getInt("move_number"),
				rs.getInt("player_id"), rs.getInt("row_position"), rs.getInt("col_position"),
				rs.getString("letter").charAt(0), rs.getInt("sos_formed"), rs.getInt("points_scored"),
				rs.getTimestamp("move_timestamp").toLocalDateTime());
	}
}