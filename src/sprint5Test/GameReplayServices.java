package sprint5Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sprint5.mode.GameMode.*;
import sprint5.mode.dao.*;
import sprint5.mode.move.GameMove;
import sprint5.mode.player.PlayerType;

public class GameReplayServices {
	private GameReplayService service;
	private DaoFactory.DAOBundle daos;
	private Integer testGameId;
	private List<GameMove> moves;
	private Integer blueId;
	private Integer redId;

	@Before
	public void setUpTestData() {
		daos = DaoFactory.createAllDAOs();
		service = new GameReplayService(daos.getGameDAO(), daos.getPlayerDAO());

		try { // Create test Games
			daos.getPlayerDAO().findByUsername("replay_blue")
					.ifPresent(p -> daos.getPlayerDAO().delete(p.getPlayerId()));
			daos.getPlayerDAO().findByUsername("replay_red")
					.ifPresent(p -> daos.getPlayerDAO().delete(p.getPlayerId()));
			PlayerRecord blue = new PlayerRecord("replay_blue", "Replay Blue", "HUMAN");
			PlayerRecord red = new PlayerRecord("replay_red", "Replay Red", "COMPUTER");

			blueId = daos.getPlayerDAO().create(blue);
			redId = daos.getPlayerDAO().create(red);

			// Create game record
			GameRecord game = new GameRecord.Builder(blueId, redId, PlayerType.HUMAN, PlayerType.COMPUTER,
					GameMode4.SIMPLE, 5).scores(3, 2).status(GameStatus.COMPLETED).totalMoves(5).durationSeconds(120L)
					.startedAt(LocalDateTime.now().minusMinutes(2)).completedAt(LocalDateTime.now()).build();

			testGameId = daos.getGameDAO().create(game);
			moves = new ArrayList<>();
			for (int i = 1; i <= 5; i++) {
				Integer playerId = (i % 2 == 1) ? blueId : redId;
				char letter = (i % 2 == 1) ? 'S' : 'O';
				GameMove move = new GameMove(testGameId, i, playerId, i - 1, i - 1, letter, 0, 0);
				moves.add(move);
				daos.getGameDAO().recordMove(move);
			}
			daos.getGameDAO().completeGame(testGameId, blueId, 3, 2);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Setup failed: " + e.getMessage());
		}

	}

	@After
	public void cleanUp() {
		if (testGameId != null) {
			try {
				daos.getGameDAO().delete(testGameId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Test

	public void testLoadReplay() {
		Optional<GameReplay> replay = service.loadReplay(testGameId);

		assertTrue(replay.isPresent());
		assertEquals(testGameId, replay.get().getGameId());
		assertEquals("SIMPLE", replay.get().getGameMode());
		assertEquals(5, replay.get().getTotalMoves());
	}

	@Test
	public void testLoadNonExistentReplay() {
		Optional<GameReplay> replay = service.loadReplay(99999);
		assertFalse(replay.isPresent());
	}

	@Test
	public void testGetAllReplays() {
		var replays = service.getAllReplays();
		assertNotNull(replays);
		assertTrue(replays.size() > 0);
	}

}
