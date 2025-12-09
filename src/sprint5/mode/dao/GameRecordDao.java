package sprint5.mode.dao;

import java.time.*;
import java.util.*;

import sprint5.mode.GameMode.*;
import sprint5.mode.move.*;


public interface GameRecordDao {
	Integer create(GameRecord game);

	Optional<GameRecord> findById(Integer gameId);

	List<GameRecord> findAll();

	boolean update(GameRecord game);

	boolean delete(Integer gameId);

	Integer recordMove(GameMove move);

	List<GameMove> getGameMoves(Integer gameId);

	void completeGame(Integer gameId, Integer winnerId, int blueScore, int redScore);

	List<GameRecord> getRecentGames(int limit);

	List<GameRecord> getPlayerGames(Integer playerId);

	List<GameRecord> getGamesBetweenPlayers(Integer player1Id, Integer player2Id);

	List<GameRecord> getGamesByMode(String gameMode);

	List<GameRecord> getGamesByStatus(String gameStatus);

	List<GameRecord> getIncompleteGames();

	List<GameRecord> getGamesByDateRange(LocalDateTime start, LocalDateTime end);

	int getTotalGameCount();

	int getGameCountByMode(String gameMode);

}
