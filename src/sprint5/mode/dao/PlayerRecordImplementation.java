package sprint5.mode.dao;

import java.sql.*;
import java.util.*;

public class PlayerRecordImplementation implements PlayerRecordDAO {
	private static final String SQL_INSERT = "INSERT INTO players (username, displayname, playertype) VALUES (?, ?, ?)";

	private static final String SQL_SELECT_BY_ID = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players WHERE player_id = ?";

	private static final String SQL_SELECT_BY_USERNAME = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players WHERE username = ?";

	private static final String SQL_SELECT_ALL = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players ORDER BY displayname";

	private static final String SQL_SELECT_HUMANS = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players WHERE playertype = 'HUMAN' ORDER BY displayname";

	private static final String SQL_SELECT_COMPUTERS = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players WHERE playertype = 'COMPUTER' ORDER BY displayname";

	private static final String SQL_SEARCH = "SELECT player_id, username, displayname, playertype, Created_at, last_played "
			+ "FROM players WHERE username LIKE ? OR displayname LIKE ? ORDER BY displayname";

	private static final String SQL_UPDATE = "UPDATE players SET displayname = ?, playertype = ? WHERE player_id = ?";

	private static final String SQL_UPDATE_LAST_PLAYED = "UPDATE players SET last_played = CURRENT_TIMESTAMP WHERE player_id = ?";

	private static final String SQL_DELETE = "DELETE FROM players WHERE player_id = ?";

	private static final String SQL_EXISTS = "SELECT 1 FROM players WHERE player_id = ?";

	@Override
	public Integer create(PlayerRecord player) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, player.getUserName());
			stmt.setString(2, player.getName());
			stmt.setString(3, player.getPlayerType());

			int rowsAffected = stmt.executeUpdate();

			if (rowsAffected == 0) {
				throw new SQLException("Creating player failed");
			}

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					int playerId = keys.getInt(1);
					player.setPlayerId(playerId);
					initializePlayerStats(playerId);
					return playerId;
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error creating player: " + player.getUserName(), e);
		}
		return null;
	}

	private void initializePlayerStats(int playerId) {
		String sql = "INSERT INTO player_stats (player_id) VALUES (?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, playerId);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Error initializing stats", e);
		}
	}

	@Override
	public Optional<PlayerRecord> findById(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {

			stmt.setInt(1, playerId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToPlayer(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error finding player: " + playerId, e);
		}
		return Optional.empty();
	}

	// Find player by userName
	@Override
	public Optional<PlayerRecord> findByUsername(String username) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_USERNAME)) {

			stmt.setString(1, username);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToPlayer(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error finding player: " + username, e);
		}
		return Optional.empty();
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
		List<PlayerRecord> players = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SEARCH)) {

			stmt.setString(1, pattern);
			stmt.setString(2, pattern);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					players.add(mapResultSetToPlayer(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error searching players", e);
		}
		return players;
	}

	@Override
	public boolean exists(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_EXISTS)) {

			stmt.setInt(1, playerId);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error checking exists", e);
		}
	}

	@Override
	public void updateLastPlayed(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_LAST_PLAYED)) {

			stmt.setInt(1, playerId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error updating last played", e);
		}
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
		String sql = "SELECT * FROM player_stats WHERE player_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, playerId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToStats(rs);
				} else {
					return new StatsRecord(playerId);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error getting stats", e);
		}
	}

	private List<PlayerRecord> executeQuery(String sql) {
		List<PlayerRecord> players = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				players.add(mapResultSetToPlayer(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error executing query", e);
		}
		return players;
	}

	@Override
	public boolean update(PlayerRecord player) {
		if (player.getPlayerId() == null) {
			throw new IllegalArgumentException("Cannot update player without ID");
		}

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

			stmt.setString(1, player.getName());
			stmt.setString(2, player.getPlayerType());
			stmt.setInt(3, player.getPlayerId());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error updating player", e);
		}
	}

	@Override
	public boolean delete(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

			stmt.setInt(1, playerId);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error deleting player", e);
		}
	}

	private StatsRecord mapResultSetToStats(ResultSet rs) throws SQLException {
		return new StatsRecord(rs.getInt("stats_id"), rs.getInt("player_id"), rs.getInt("games_played"),
				rs.getInt("games_won"), rs.getInt("games_lost"), rs.getInt("games_drawn"),
				rs.getInt("total_sos_formed"), rs.getInt("total_points_scored"),
				rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null);
	}

	private PlayerRecord mapResultSetToPlayer(ResultSet rs) throws SQLException {
		return new PlayerRecord(rs.getInt("player_id"), rs.getString("username"), rs.getString("displayname"),
				rs.getString("playertype"),
				rs.getTimestamp("Created_at") != null ? rs.getTimestamp("Created_at").toLocalDateTime() : null,
				rs.getTimestamp("last_played") != null ? rs.getTimestamp("last_played").toLocalDateTime() : null);
	}

}
