package sprint5Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import sprint5.mode.board.*;

public class GameBoard5Test {
	private Board board;

	@Before
	public void setUp() {
		board = new GameBoard5(5);
	}

	@Test
	public void testBoardInitialization() {
		assertEquals(5, board.getSize());
		assertEquals(0, board.getCountMove());
		assertFalse(board.isFull());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBoardSizeTooSmall() {
		new GameBoard5(2);
	}// to small test

	@Test(expected = IllegalArgumentException.class)
	// Large board
	public void testBoardSizeTooLarge() {
		new GameBoard5(16);
	}

	@Test
	public void testBoardSizeValid() {
	    new GameBoard5(3);  // no exception
	    new GameBoard5(15); // no exception
	}
	@Test
	public void testMakeMoveValidation() {
		assertFalse(board.makeMove(0, 0, 'X'));
		assertFalse(board.makeMove(0, 0, 'A'));
		assertTrue(board.makeMove(0, 0, 'S'));
		assertTrue(board.makeMove(0, 1, 'O'));
	}
	@Test
	public void testIsEmptyBoundsChecking() {
        assertThrows(IndexOutOfBoundsException.class, () -> board.isEmpty(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.isEmpty(0, -1));
        assertThrows(IndexOutOfBoundsException.class, () -> board.isEmpty(5, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> board.isEmpty(0, 5));
    }

	@Test
	public void testMoveCounter() {
        assertEquals(0, board.getCountMove());
        board.makeMove(0, 0, 'S');
        assertEquals(1, board.getCountMove());
        board.makeMove(0, 1, 'O');
        assertEquals(2, board.getCountMove());
        board.makeMove(0, 0, 'O'); // Should fail
        assertEquals(2, board.getCountMove()); 
    }
	@Test 
	public void testBoardFull() {
        Board small = new GameBoard5(3);
        assertFalse(small.isFull());
        
        // Fill the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                small.makeMove(i, j, (i + j) % 2 == 0 ? 'S' : 'O');
            }
        }
        assertTrue(small.isFull());
    }
	@Test
	//Reset game
	public void testResetGame() {
        board.makeMove(0, 0, 'S');
        board.makeMove(1, 1, 'O');
        
        board.resetGame();
        
        assertEquals(0, board.getCountMove());
        assertEquals('\0', board.getCell(0, 0));
        assertEquals('\0', board.getCell(1, 1));
        assertEquals(25, board.getEmptyCells().size());
    }
    
}
