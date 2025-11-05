package sprint3Test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import sprint3.Board3;
import sprint3.Game;
import sprint3.SimpleGame;

public class TestingMoves {
	
	private Board3 board;
	private Game game;
	

	@Before
	public void setUp() throws Exception {
		board = new Board3(3);
		game = new SimpleGame(board);
	}

	@After
	public void tearDown() throws Exception {
		board= null;
		game = null;
	}

	// acceptance criterion 2.1
	@Test
	public void testCrossTurnMoveVacantCell() {
		
		game.makeMove(0, 0, 'S');
		assertEquals('S', board.getCell(0, 0));
		assertEquals('R', game.getCurrentTurn());
	}

	// acceptance criterion 2.2
	@Test
	public void testCrossTurnMoveNonVacantCell() {
		game.makeMove(0, 0, 'S'); //S moves at 0, 0
		assertEquals('S', board.getCell(0, 0));
		//Red player s turn
		assertEquals('R', game.getCurrentTurn() );//red turn
		
		//red player playes
		game.makeMove(1, 0, 'O'); // O moves at 1, 0
		assertEquals('O', board.getCell(1, 0));
		assertEquals('B', game.getCurrentTurn() );//Blue player turn
		
		
        //blue attempts to overwrite
		boolean move = board.makeMove(1, 0, 'S');
		//attempting to override
		assertFalse(move);// check if move was made
		assertEquals('O', board.getCell(1, 0));
		assertEquals('B',game.getCurrentTurn()); //Blue player turn but should not switch
		
	}
		

}
