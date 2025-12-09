package sprint5Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sprint5.controller.*;
import sprint5.mode.GameMode.*;
import sprint5.mode.dao.*;
import sprint5.mode.move.*;
import sprint5.mode.player.*;

public class replayControllerTest {
	private ReplayController controller;
	private PlayerRecord blue;
    private PlayerRecord red;
	private GameReplay replay;
	private List<GameMove> moves;

	@Before
	public void setUp() {
		// Creates players
		 blue = new PlayerRecord(1, "dan123", "Dan", PlayerType.HUMAN.getCode(), null, null);
		 red = new PlayerRecord(2, "boot3", "Boot", PlayerType.COMPUTER.getCode(), null, null);

		// Creates moves
		moves = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			moves.add(new GameMove(1, // gameId
					i, // moveNumber
					i % 2 == 1 ? blue.getPlayerId() : red.getPlayerId(), i - 1, // rowPosition
					i - 1, // colPosition
					i % 2 == 1 ? 'S' : 'O', 0, 0));
		}
		GameRecord game = new GameRecord.Builder(blue.getPlayerId(), red.getPlayerId(), PlayerType.HUMAN,
				PlayerType.COMPUTER, GameMode4.SIMPLE, 5).gameId(1).scores(3, 2).winner(blue.getPlayerId())
				.status(GameStatus.COMPLETED).totalMoves(moves.size()).durationSeconds(120L)
				.startedAt(LocalDateTime.now().minusMinutes(2)).completedAt(LocalDateTime.now()).build();
		replay = new GameReplay(game, blue, red, moves);
		controller = new ReplayController(replay);
	}

	@Test

	public void testInitialState() {
		assertEquals(-1, controller.getCurrentMoveIndex());
		assertTrue(controller.isAtStart());
		assertFalse(controller.isAtEnd());
		assertEquals(5, controller.getTotalMoves());
		assertEquals(1000, controller.getPlaybackSpeed());
		assertFalse(controller.isPlaying());
	}
	
	@Test
   public void testStepForwardAndBacKWard() {
		
		//Step through the moves
		for (int i = 0; i < moves.size(); i++) {
        GameMove move = controller.stepForward();
        assertEquals(i, controller.getCurrentMoveIndex());
        assertEquals(moves.get(i), move);
		}
		assertFalse(controller.hasNext());
		assertTrue(controller.isAtEnd());
		
		//Backward test
		for (int i = moves.size() - 1; i >= 0; i--) {
            GameMove move = controller.stepBackward();
            assertEquals(i - 1, controller.getCurrentMoveIndex());
            assertEquals(moves.get(i), move);
        }

        assertFalse(controller.hasPrevious());
        assertTrue(controller.isAtStart());
    }
	@Test
    public void testJumpToStartEnd() {
        controller.jumpToEnd();
        assertEquals(moves.size() - 1, controller.getCurrentMoveIndex());
        assertTrue(controller.isAtEnd());

        controller.jumpToStart();
        assertEquals(-1, controller.getCurrentMoveIndex());
        assertTrue(controller.isAtStart());
    }
	 @Test
	    public void testToStringStatus() {
	        String status = controller.getStatus();
	        assertTrue(status.contains("Move 0/5"));
	        assertTrue(status.contains("Paused"));
	    }
	 @Test
	    public void testJumpToMove() {
	        assertTrue(controller.jumpToMove(2));
	        assertEquals(2, controller.getCurrentMoveIndex());
	        assertEquals(moves.get(2), controller.getCurrentMove());

	        assertFalse(controller.jumpToMove(-2));
	        assertFalse(controller.jumpToMove(10)); 
	    }
	 @Test
	    public void testPlayPauseToggle() {
	        controller.play();
	        assertTrue(controller.isPlaying());

	        controller.pause();
	        assertFalse(controller.isPlaying());

	        controller.togglePlayPause();
	        assertTrue(controller.isPlaying());

	        controller.togglePlayPause();
	        assertFalse(controller.isPlaying());
	    }
	 @Test
	    public void testSpeedPresets() {
	        controller.setSpeedPreset("SLOW");
	        assertEquals(2000, controller.getPlaybackSpeed());

	        controller.setSpeedPreset("NORMAL");
	        assertEquals(1000, controller.getPlaybackSpeed());

	        controller.setSpeedPreset("FAST");
	        assertEquals(500, controller.getPlaybackSpeed());
	    }


}
