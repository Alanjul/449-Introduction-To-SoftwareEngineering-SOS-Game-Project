package sprint5.mode.dao;

import java.sql.*;
import java.util.*;

/**
 * StatsRecordImplementation implements the StatsRecord Interface.
 */
public class StatsRecordImplementation implements StatsRecordDao {

	private static final String SQL_INSERT = "INSERT INTO player_stats (player_id) VALUES (?)";

	private static final String SQL_SELECT_BY_PLAYER = "SELECT * FROM player_stats WHERE player_id = ?";

	private static final String SQL_UPDATE = "UPDATE player_stats SET games_played = ?, games_won = ?, games_lost = ?, "
			+ "games_drawn = ?, total_sos_formed = ?, total_points_scored = ? WHERE player_id = ?";

	private static final String SQL_DELETE = "DELETE FROM player_stats WHERE player_id = ?";

	private static final String SQL_UPDATE_AFTER_GAME = "UPDATE player_stats SET " + "games_played = games_played + 1, "
			+ "games_won = games_won + ?, " + "games_lost = games_lost + ?, " + "games_drawn = games_drawn + ?, "
			+ "total_sos_formed = total_sos_formed + ?, " + "total_points_scored = total_points_scored + ? "
			+ "WHERE player_id = ?";

	private static final String SQL_TOP_BY_WINS = "SELECT * FROM player_stats ORDER BY games_won DESC LIMIT ?";

	private static final String SQL_TOP_BY_WIN_RATE = "SELECT * FROM player_stats WHERE games_played >= 10 "
			+ "ORDER BY (games_won * 1.0 / games_played) DESC LIMIT ?";

	private static final String SQL_TOP_BY_SOS = "SELECT * FROM player_stats ORDER BY total_sos_formed DESC LIMIT ?";

	private static final String SQL_ALL_STATS = "SELECT * FROM player_stats ORDER BY games_played DESC";

	@Override
	public Integer create(StatsRecord stats) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, stats.getPlayerId());
			stmt.executeUpdate();

			try (ResultSet keys = stmt.getGeneratedKeys()) {
				if (keys.next()) {
					int statsId = keys.getInt(1);
					stats.setStatsId(statsId);
					return statsId;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error creating stats", e);
		}
		return null;
	}

	@Override
	public Optional<StatsRecord> findByPlayerId(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_PLAYER)) {

			stmt.setInt(1, playerId);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return Optional.of(mapResultSetToStats(rs));
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error finding stats", e);
		}
		return Optional.empty();
	}

	@Override
	public boolean update(StatsRecord stats) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

			stmt.setInt(1, stats.getGamesPlayed());
			stmt.setInt(2, stats.getGamesWon());
			stmt.setInt(3, stats.getGamesLost());
			stmt.setInt(4, stats.getGamesDrawn());
			stmt.setInt(5, stats.getTotalSosFormed());
			stmt.setInt(6, stats.getTotalPointsScored());
			stmt.setInt(7, stats.getPlayerId());

			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error updating stats", e);
		}
	}

	@Override
	public boolean delete(Integer playerId) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

			stmt.setInt(1, playerId);
			return stmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new RuntimeException("Error deleting stats", e);
		}
	}

	@Override
	public void updateAfterGame(Integer playerId, int won, int lost, int drawn, int sosFormed,
			int pointsScored) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_AFTER_GAME)) {

			stmt.setInt(1, won);
			stmt.setInt(2, lost );
			stmt.setInt(3, drawn);
			stmt.setInt(4, sosFormed);
			stmt.setInt(5, pointsScored);
			stmt.setInt(6, playerId);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error updating stats after game", e);
		}
	}

	@Override
	public List<StatsRecord> getTopPlayersByWins(int limit) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_TOP_BY_WINS)) {

			stmt.setInt(1, limit);
			return executeStatsQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting top players by wins", e);
		}
	}

	@Override
	public List<StatsRecord> getTopPlayersByWinRate(int limit) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_TOP_BY_WIN_RATE)) {

			stmt.setInt(1, limit);
			return executeStatsQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting top players by win rate", e);
		}
	}

	@Override
	public List<StatsRecord> getTopPlayersBySOS(int limit) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL_TOP_BY_SOS)) {

			stmt.setInt(1, limit);
			return executeStatsQuery(stmt);

		} catch (SQLException e) {
			throw new RuntimeException("Error getting top players by SOS", e);
		}
	}

	@Override
	public List<StatsRecord> getAllStats() {
		List<StatsRecord> statsList = new ArrayList<>();

		try (Connection conn = DBConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(SQL_ALL_STATS)) {

			while (rs.next()) {
				statsList.add(mapResultSetToStats(rs));
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error getting all stats", e);
		}
		return statsList;
	}

	private List<StatsRecord> executeStatsQuery(PreparedStatement stmt) throws SQLException {
		List<StatsRecord> statsList = new ArrayList<>();

		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				statsList.add(mapResultSetToStats(rs));
			}
		}
		return statsList;
	}

	private StatsRecord mapResultSetToStats(ResultSet rs) throws SQLException {
		return new StatsRecord(rs.getInt("stats_id"),
				rs.getInt("player_id"),
				rs.getInt("games_played"),
				rs.getInt("games_won"),
				rs.getInt("games_lost"),
				rs.getInt("games_drawn"),
				rs.getInt("total_sos_formed"),
				rs.getInt("total_points_scored"),
				rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null);
	}
}
