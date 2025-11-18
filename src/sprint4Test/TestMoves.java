package sprint4Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;



import sprint4.mode.*;


public class TestMoves {
	
	private Board board;
	private SimpleGame4 game;
	private Player bluePlayer;
	private Player redPlayer;
	

	@Before
	public void setUp() throws Exception {
		board = new Board4(5);
		bluePlayer = new HumanPlayer('B', "Blue player",'S');
		redPlayer = new HumanPlayer ('R', "Red Player", 'O');
		game = new SimpleGame4(board, bluePlayer, redPlayer);
	}

	@After
	public void tearDown() throws Exception {
		redPlayer= null;
		game = null;
	}

	// acceptance criterion 2.1
	@Test
	public void testCrossTurnMoveVacantCell() {
		
		game.makeMove(new Move(0, 0, 'S'));
		assertEquals('S', board.getCell(0, 0));
		assertEquals('R', game.getCurrentTurn());
	}

	// acceptance criterion 2.2
	@Test
	public void testCrossTurnMoveNonVacantCell() {
		game.makeMove(new Move(0, 0, 'S')); //S moves at 0, 0
		assertEquals('S', board.getCell(0, 0));
		//Red player s turn
		assertEquals('R', game.getCurrentTurn() );//red turn
		
		//red player playes
		game.makeMove(new Move(1, 0, 'O')); // O moves at 1, 0
		assertEquals('O', board.getCell(1, 0));
		assertEquals('B', game.getCurrentTurn() );//Blue player turn
		
		
        //blue attempts to overwrite
		MoveResult move = game.makeMove(new Move(1, 0, 'S'));
		//attempting to override
		assertFalse(move.isSuccess());// check if move was made
		assertEquals('O', board.getCell(1, 0));
		assertEquals('B',game.getCurrentTurn()); //Blue player turn but should not switch
		
	}

}
