package sprint5.mode.dao;

import java.util.*;

public interface StatsRecordDao {
	Integer create(StatsRecord stats);
    Optional<StatsRecord> findByPlayerId(Integer playerId);
    boolean update(StatsRecord stats);
    boolean delete(Integer playerId);
    
    void updateAfterGame(Integer playerId, int gamesWon, int gamesLost, int gamesDrawn, int sosFormed,
			int pointsScored);
    
    List<StatsRecord> getTopPlayersByWins(int limit);
    List<StatsRecord> getTopPlayersByWinRate(int limit);
    List<StatsRecord> getTopPlayersBySOS(int limit);
    List<StatsRecord> getAllStats();
}
