package sprint2Test;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint2.Board2;


public class TestMoves {
	
	
		private Board2 board;
		

		@Before
		public void setUp() throws Exception {
			board = new Board2(3);
		}

		@After
		public void tearDown() throws Exception {
			board= null;
		}

		// acceptance criterion 2.1
		@Test
		public void testCrossTurnMoveVacantCell() {
			
			board.makeMove(0, 0, 'S');
			assertEquals('S', board.getCell(0, 0));
			assertEquals('O', board.getTurn());
		}

		// acceptance criterion 2.2
		@Test
		public void testCrossTurnMoveNonVacantCell() {
			board.makeMove(0, 0, 'S'); //S moves at 0, 0
			assertEquals('S', board.getCell(0, 0));
			
			//O's turn
			assertEquals('O', board.getTurn() );//O turn
			board.makeMove(1, 0, 'O'); // O moves at 1, 0
			assertEquals('O', board.getCell(1, 0));
			assertEquals('S', board.getTurn() );//s turn
			
			
	        //check for overwrite
			board.makeMove(1, 0, 'S');
			assertEquals('O', board.getCell(1, 0));
			assertEquals('S', board.getTurn());
			
		}


}
