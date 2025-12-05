package sprint4Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint5.mode.board.Board;
import sprint5.mode.board.GameBoard5;



public class Board4Test {

	private Board board;
	@Before
	public void setUp() throws Exception {

		board = new GameBoard5(4);
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}

	@Test
	//Testing Constructor
	public void testValidConstructor()
	{
		assertNotNull(board);
		assertEquals(4, board.getSize());
		assertEquals(0, board.getCountMove());
		assertFalse(board.isFull());
	}
     //acceptance criteria
	@Test //create a new board
	public void testNewBoard() {
		for (int row = 0; row <4; row++) {
			for (int col = 0; col < 4; col++) {
				assertEquals(Board4.EMPTY, board.getCell(row, col));
				assertTrue("Cell should be empty" ,board.isEmpty(row, col));
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
		assertEquals(Board4.EMPTY, board.getCell(6, -1));
	}

	//acceptance criteria
	@Test
	public void testInvalidColumn() {
		assertEquals(Board4.EMPTY, board.getCell(-1, 6));
	}

	@Test
	//Testing for small grid less than the allowed size of 3
	public void testConstructorSmallGrid()
	{
		try
		{
			new Board4(2);
			fail("Expected Illegal Argument Exception");
		} catch(IllegalArgumentException ex)
		{
			assertEquals("The size must be between 3 and 15", ex.getMessage());
		}
	}
	@Test
	//Testing too big board greater than the allowed size of 15
	public void testConstructorBigBoard()
	{
		try
		{
			new Board4(16);
			fail("Expected Illegal Argument Exception");
		} catch(IllegalArgumentException ex)
		{
			assertEquals("The size must be between 3 and 15", ex.getMessage());
		}
	}
	@Test
	//Testing valid moves
	public void validMove()
	{
		assertTrue(board.makeMove(0, 0, 'S'));
		assertEquals('S', board.getCell(0, 0));
		//count
		assertEquals(1, board.getCountMove());
		assertFalse(board.isEmpty(0, 0));

	}
	//Testing for occupied cell
	@Test
	public void occupiedCells()
	{
		board.makeMove(1, 1, 'S');
		assertFalse(board.makeMove(1, 1, 'O'));//testing for overwrite
		//check if the count is the same after 2 moves
		assertEquals(1, board.getCountMove());
	}
	//Testing invalid  xletter
	@Test
	public  void testInvalidXLetter() {

		try
		{
			board.makeMove(0, 0, 'X');
			fail("Expected Illegal Argument Exception");
		} catch(IllegalArgumentException ex)
		{
			assertEquals("Invalid letter, Only 'S' or 'O' is allowed", ex.getMessage());
		}
	}
	//Testing for invalid y letter
	//Acceptance criteria
	@Test
	public void testChecInvalidLetter()
	{
	   try {
		   board.makeMove(0, 0, 'Y');
		   fail("Expected Illegal Argument Exception");
	   } catch(IllegalArgumentException ex)
	   {
		   assertEquals("Invalid letter, Only 'S' or 'O' is allowed", ex.getMessage());
	   }
	}
	//Test to see if the board is full
	//Acceptance criteria
	@Test
	public void testBoardFull()
	{
		assertFalse(board.isFull());
		for(int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				board.makeMove(i, j, 'S');
			}
		}
		assertTrue(board.isFull());
		assertEquals(16, board.getCountMove());
		assertTrue(board.getEmptyCells().isEmpty());//checking for no empty board

	}

	//Testing changes made to copy to make sure they don't affect the original
	@Test
	public void testBoardCopy()
	{
		board.makeMove(0, 0, 'S');
		board.makeMove(1, 1,  'O');

		Board other = board.copy();

		//Check if states are not changed
		assertEquals(other.getCell(0, 0), board.getCell(0, 0));
		assertEquals(other.getCountMove(), board.getCountMove());
		assertEquals(other.getCell(1, 1), board.getCell(1, 1));
		assertEquals(other.getSize(), board.getSize());

		//Checking if deep copy has been done
		other.makeMove(2, 2, 'O');
	    assertEquals('\u0000', board.getCell(2, 2));//original not changed
	    assertEquals('O', other.getCell(2, 2));
	}


}
