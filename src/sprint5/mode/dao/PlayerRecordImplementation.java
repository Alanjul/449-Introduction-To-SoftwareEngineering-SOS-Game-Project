package sprint5.mode.dao;

import java.sql.*;
import java.util.*;

public class PlayerRecordImplementation extends AbstractDAO<PlayerRecord> implements PlayerRecordDAO {
	private static final String SQL_INSERT = "INSERT INTO players (username, displayname, playertype) VALUES (?, ?, ?)";
	private static final String SQL_SELECT_BY_ID = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players WHERE player_id = ?";
	private static final String SQL_SELECT_BY_USERNAME = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players WHERE username = ?";
	private static final String SQL_SELECT_ALL = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players ORDER BY displayname";
	private static final String SQL_SELECT_HUMANS = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players WHERE playertype = 'HUMAN' ORDER BY displayname";
	private static final String SQL_SELECT_COMPUTERS = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players WHERE playertype = 'COMPUTER' ORDER BY displayname";
	private static final String SQL_SEARCH = "SELECT player_id, username, displayname, playertype, Created_at, last_played FROM players WHERE username LIKE ? OR displayname LIKE ? ORDER BY displayname";
	private static final String SQL_UPDATE = "UPDATE players SET displayname = ?, playertype = ? WHERE player_id = ?";
	private static final String SQL_UPDATE_LAST_PLAYED = "UPDATE players SET last_played = CURRENT_TIMESTAMP WHERE player_id = ?";
	private static final String SQL_DELETE = "DELETE FROM players WHERE player_id = ?";
	private static final String SQL_EXISTS = "SELECT 1 FROM players WHERE player_id = ?";
	private static final String SQL_INIT_STATS = "INSERT INTO player_stats (player_id) VALUES (?)";
	private static final String SQL_GET_STATS = "SELECT * FROM player_stats WHERE player_id = ?";

	@Override
	protected PlayerRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new PlayerRecord(rs.getInt("player_id"), rs.getString("username"), rs.getString("displayname"),
				rs.getString("playertype"),
				rs.getTimestamp("Created_at") != null ? rs.getTimestamp("Created_at").toLocalDateTime() : null,
				rs.getTimestamp("last_played") != null ? rs.getTimestamp("last_played").toLocalDateTime() : null);
	}

	@Override
	protected String getTableName() {
		return "players";
	}

	@Override
	public Integer create(PlayerRecord player) {
		return executeInTransaction(conn -> {
			try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, player.getUserName());
				stmt.setString(2, player.getName());
				stmt.setString(3, player.getPlayerType());

				int rowsAffected = stmt.executeUpdate();
				if (rowsAffected == 0) {
					throw new SQLException("Creating player failed, no rows affected");
				}

				Integer playerId;
				try (ResultSet keys = stmt.getGeneratedKeys()) {
					if (!keys.next()) {
						throw new SQLException("Creating player failed, no ID obtained");
					}
					playerId = keys.getInt(1);
				}

				player.setPlayerId(playerId);
				initializePlayerStats(conn, playerId);
				return playerId;
			}
		});
	}

	private void initializePlayerStats(Connection conn, int playerId) throws SQLException {
		try (PreparedStatement stmt = conn.prepareStatement(SQL_INIT_STATS)) {
			stmt.setInt(1, playerId);
			stmt.executeUpdate();
		}
	}

	@Override
	public Optional<PlayerRecord> findById(Integer playerId) {
		List<PlayerRecord> results = executeQuery(SQL_SELECT_BY_ID, playerId);
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	@Override
	public Optional<PlayerRecord> findByUsername(String username) {
		List<PlayerRecord> results = executeQuery(SQL_SELECT_BY_USERNAME, username);
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	@Override
	public List<PlayerRecord> findAll() {
		return executeQuery(SQL_SELECT_ALL);
	}

	@Override
	public List<PlayerRecord> findAllHumans() {
		return executeQuery(SQL_SELECT_HUMANS);
	}

	@Override
	public List<PlayerRecord> findAllComputers() {
		return executeQuery(SQL_SELECT_COMPUTERS);
	}

	@Override
	public List<PlayerRecord> searchByPattern(String pattern) {
		return executeQuery(SQL_SEARCH, pattern, pattern);
	}

	@Override
	public boolean exists(Integer playerId) {
		List<PlayerRecord> results = executeQuery(SQL_EXISTS, playerId);
		return !results.isEmpty();
	}

	@Override
	public void updateLastPlayed(Integer playerId) {
		executeUpdate(SQL_UPDATE_LAST_PLAYED, playerId);
	}

	@Override
	public PlayerRecord getOrCreate(String username, String displayname, String playertype) {
		Optional<PlayerRecord> existing = findByUsername(username);
		if (existing.isPresent()) {
			return existing.get();
		}

		PlayerRecord newPlayer = new PlayerRecord(username, displayname, playertype);
		create(newPlayer);
		return newPlayer;
	}

	@Override
	public StatsRecord getStats(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_GET_STATS)) {

			stmt.setInt(1, playerId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToStats(rs);
				} else {
					return new StatsRecord(playerId);
				}
			}
		} catch (SQLException e) {
			String msg = String.format("Error getting stats for player: %d", playerId);
			LOGGER.log(java.util.logging.Level.SEVERE, msg, e);
			throw new RuntimeException(msg, e);
		}
	}

	@Override
	public boolean update(PlayerRecord player) {
		if (player.getPlayerId() == null) {
			throw new IllegalArgumentException("Cannot update player without ID");
		}
		return executeUpdate(SQL_UPDATE, player.getName(), player.getPlayerType(), player.getPlayerId()) > 0;
	}

	@Override
	public boolean delete(Integer playerId) {
		return executeUpdate(SQL_DELETE, playerId) > 0;
	}

	private StatsRecord mapResultSetToStats(ResultSet rs) throws SQLException {
		return new StatsRecord(rs.getInt("stats_id"), rs.getInt("player_id"), rs.getInt("games_played"),
				rs.getInt("games_won"), rs.getInt("games_lost"), rs.getInt("games_drawn"),
				rs.getInt("total_sos_formed"), rs.getInt("total_points_scored"),
				rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null);
	}

}
