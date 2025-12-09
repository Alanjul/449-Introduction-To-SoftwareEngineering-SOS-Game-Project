package sprint5Test;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sprint5.mode.computerPlayer.*;
import sprint5.mode.humanplayer.*;
import sprint5.mode.player.*;

public class PlayerTest {
	
	@Test
	public void testPlayerSymbolValid() {
	    // Should not throw
	    new HumanPlayer('B', "Blue", 'S');
	    new HumanPlayer('R', "Red", 'O');
	}
	@Test(expected = IllegalArgumentException.class)
	public void testPlayerSymbolInvalid() {
	    new HumanPlayer('X', "Invalid", 'S');
	}
	@Test
	public void testPlayerScore() {
	    Player player = new HumanPlayer('B', "Blue", 'S');

	    // Initially zero
	    assertEquals(0, player.getScore());

	    // Add scores
	    player.addScore(5);
	    assertEquals(5, player.getScore());

	    player.addScore(3);
	    assertEquals(8, player.getScore());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testPlayerScoreNegative() {
	    Player player = new HumanPlayer('B', "Blue", 'S');
	    player.addScore(-1);
	}
	
	@Test
	public void testScoreReset() {
        Player player = new HumanPlayer('B', "Blue", 'S');
        player.addScore(10);
        player.resetScore();
        assertEquals(0, player.getScore());
    }
@Test
public void testPlayerTypeIdentification() {
    Player human = new HumanPlayer('B', "Blue", 'S');
    Player computer = new ComputerPlayer('R', 
        ComputerPlayer.LevelsOfDifficulty.MEDIUM, null);
    
    assertTrue(human.isHumanPlayer());
    assertFalse(human.isComputer());
    
    assertFalse(computer.isHumanPlayer());
    assertTrue(computer.isComputer());
}
}
