package sprint5.mode.dao;

import java.sql.*;
import java.util.*;

/**
 * StatsRecordImplementation implements the StatsRecord Interface.
 */
public class StatsRecordImplementation extends AbstractDAO<StatsRecord> implements StatsRecordDao {

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
	protected StatsRecord mapResultSetToEntity(ResultSet rs) throws SQLException {
		return new StatsRecord(rs.getInt("stats_id"), rs.getInt("player_id"), rs.getInt("games_played"),
				rs.getInt("games_won"), rs.getInt("games_lost"), rs.getInt("games_drawn"),
				rs.getInt("total_sos_formed"), rs.getInt("total_points_scored"),
				rs.getTimestamp("last_updated") != null ? rs.getTimestamp("last_updated").toLocalDateTime() : null);
	}

	@Override
	protected String getTableName() {
		return "player_stats";
	}

	@Override
	public Integer create(StatsRecord stats) {
		Integer statsId = executeInsert(SQL_INSERT, stats.getPlayerId());
		stats.setStatsId(statsId);
		return statsId;
	}

	@Override
	public Optional<StatsRecord> findByPlayerId(Integer playerId) {
		List<StatsRecord> results = executeQuery(SQL_SELECT_BY_PLAYER, playerId);
		return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
	}

	@Override
	public boolean update(StatsRecord stats) {
		return executeUpdate(SQL_UPDATE, stats.getGamesPlayed(), stats.getGamesWon(), stats.getGamesLost(),
				stats.getGamesDrawn(), stats.getTotalSosFormed(), stats.getTotalPointsScored(),
				stats.getPlayerId()) > 0;
	}

	@Override
	public boolean delete(Integer playerId) {
		return executeUpdate(SQL_DELETE, playerId) > 0;
	}

	@Override
	public void updateAfterGame(Integer playerId, int won, int lost, int drawn, int sosFormed, int pointsScored) {
		executeUpdate(SQL_UPDATE_AFTER_GAME, won, lost, drawn, sosFormed, pointsScored, playerId);
	}

	@Override
	public List<StatsRecord> getTopPlayersByWins(int limit) {
		return executeQuery(SQL_TOP_BY_WINS, limit);
	}

	@Override
	public List<StatsRecord> getTopPlayersByWinRate(int limit) {
		return executeQuery(SQL_TOP_BY_WIN_RATE, limit);
	}

	@Override
	public List<StatsRecord> getTopPlayersBySOS(int limit) {
		return executeQuery(SQL_TOP_BY_SOS, limit);
	}

	@Override
	public List<StatsRecord> getAllStats() {
		return executeQuery(SQL_ALL_STATS);
	}
}
