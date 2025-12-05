package sprint2Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint2.Board2;
import sprint2.GameMode;


public class Board2Test {
		private Board2 board;

		@Before
		public void setUp() throws Exception {

			board = new Board2(3);
		}

		@After
		public void tearDown() throws Exception {
			board = null;
		}
         //acceptance criteria
		@Test //create a new board
		public void testNewBoard() {
			for (int row = 0; row <3; row++) {
				for (int col = 0; col < 3; col++) {
					assertEquals('\0', board.getCell(row, col));
				}
			}
		}

		//acceptance criteria
		@Test  //checking for overwrites
		public void testRejectsOverwritingMove() {
		    board.makeMove(1, 1, 'S');
		    assertFalse(board.makeMove(1, 1, 'O'));
		}

		//acceptance criteria
		@Test
		public void testInvalidRow() {
			assertEquals('\0', board.getCell(6, -1));
		}

		//acceptance criteria
		@Test
		public void testInvalidColumn() {
			assertEquals('\0', board.getCell(-1, 6));
		}
		@Test
		//testing for switch turn
		public void switchTurnTest()
		{
			assertEquals('S', board.getTurn()); // default
			//switch turn
			board.switchTurn();
			assertEquals('O', board.getTurn());
			board.switchTurn();
			assertEquals('S', board.getTurn());
		}

		//Testing for default game mode
		@Test
		public  void gameModel() {
			assertEquals(GameMode.SIMPLE, board.getGame());
			assertEquals(3, board.getSize());

		}
		//Test for the custome mode
		@Test
		public void customeMode()
		{
			Board2 customMode = new Board2(3, GameMode.GENERAL);
			assertEquals(GameMode.GENERAL, customMode.getGame());
			assertEquals(3, customMode.getSize());
		}
}
