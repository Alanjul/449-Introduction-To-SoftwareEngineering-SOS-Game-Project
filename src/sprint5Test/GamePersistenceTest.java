package sprint5Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint5.mode.GameMode.GameStatus;
import sprint5.mode.dao.DaoFactory;
import sprint5.mode.player.*;
import sprint5.util.*;
import sprint5.util.GamePersistenceService.*;

public class GamePersistenceTest {
	private GamePersistenceService persistence;
	private DaoFactory.DAOBundle daos;

	@Before
	public void setUp() {
		daos = DaoFactory.createAllDAOs();
		persistence = new GamePersistenceService(daos.getPlayerDAO(), daos.getGameDAO(), daos.getStatsDAO());
	}

	@Test
	public void testStartGameSession() {
		GameSession session = persistence.startGameSession("TestBlue", "TestRed", "SIMPLE", 5, true, false);
		assertNotNull(session);
		assertNotNull(session.getGameId());
		assertNotNull(session.getBluePlayerId());
		assertNotNull(session.getRedPlayerId());
		daos.getGameDAO().delete(session.getGameId());
		daos.getPlayerDAO().delete(session.getBluePlayerId());
		daos.getPlayerDAO().delete(session.getRedPlayerId());
	}

	@Test
	public void testTransactionIntegrity() {

		GameSession session = persistence.startGameSession("TransactionTest1", "TransactionTest2", "SIMPLE", 5, true,
				true);
		var bluePlayer = daos.getPlayerDAO().findById(session.getBluePlayerId());
		assertTrue(bluePlayer.isPresent());

		var blueStats = daos.getStatsDAO().findByPlayerId(session.getBluePlayerId());
		assertTrue("Stats should exist for player", blueStats.isPresent());
		daos.getGameDAO().delete(session.getGameId());
		daos.getPlayerDAO().delete(session.getBluePlayerId());
		daos.getPlayerDAO().delete(session.getRedPlayerId());
	}

	@Test
	public void testRecordMove() {
		GameSession session = persistence.startGameSession("MoveTest1", "MoveTest2", "SIMPLE", 5, true, false);
		persistence.recordMove(session.getGameId(), 1, session.getBluePlayerId(), 0, 0, 'S', 0, 0);

		var moves = daos.getGameDAO().getGameMoves(session.getGameId());
		assertEquals(1, moves.size());
		assertEquals('S', moves.get(0).getLetter());
		daos.getGameDAO().delete(session.getGameId());
		daos.getPlayerDAO().delete(session.getBluePlayerId());
		daos.getPlayerDAO().delete(session.getRedPlayerId());
	}

	@Test
	public void testCompleteGame() {
		
		String blueName = "CompleteTest1_" + System.currentTimeMillis();
		String redName = "CompleteTest2_" + System.currentTimeMillis();
		GameSession session = persistence.startGameSession(blueName, redName, "GENERAL", 8, true, false);
		PlayerStats blueStats = new PlayerStats(blueName);
		blueStats.recordSOSFormed(3);
		blueStats.recordPointsScored(3);
		PlayerStats redStats = new PlayerStats(redName);
		redStats.recordSOSFormed(1);
		redStats.recordPointsScored(1);
		persistence.completeGame(session, Winner.BLUE, 3, 1, blueStats, redStats);

		// Verify is complete
		var game = daos.getGameDAO().findById(session.getGameId());
		assertTrue(game.isPresent());
		assertEquals(GameStatus.COMPLETED, game.get().getGameStatus());

		// Update
		var updatedBlueStats = daos.getStatsDAO().findByPlayerId(session.getBluePlayerId());
		assertTrue(updatedBlueStats.isPresent());
		assertEquals(3, updatedBlueStats.get().getTotalSosFormed());
		assertEquals(3, updatedBlueStats.get().getTotalSosFormed());
		
		//Verify red
		var updatedRedStats = daos.getStatsDAO().findByPlayerId(session.getRedPlayerId());
	    assertTrue(updatedRedStats.isPresent());
	    assertEquals(1, updatedRedStats.get().getTotalSosFormed());
	    assertEquals(1, updatedRedStats.get().getTotalSosFormed());

		// clean
		daos.getGameDAO().delete(session.getGameId());
		daos.getPlayerDAO().delete(session.getBluePlayerId());
		daos.getPlayerDAO().delete(session.getRedPlayerId());
	}

}
