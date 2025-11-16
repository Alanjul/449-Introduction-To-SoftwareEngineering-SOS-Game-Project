package sprint4Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint4.mode.Board;
import sprint4.mode.Board4;
import sprint4.mode.LineSegment4;



public class Board4Test {
	
	private Board board;
	@Before
	public void setUp() throws Exception {
		
		board = new Board4(4);
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
	//@Test
	//testing for SOS 
	/*public void testCheckSosHorizontal()
	{
		board.makeMove(0, 0, 'S');
		board.makeMove(0, 1, 'O');
		board.makeMove(0, 2, 'S');
		//check SOS
		//assertEquals(1, board.checkSOS(0, 2));
	}*/
	//testing SOS Line formed
	/*@Test
	public void testSosLine()
	{
		
	}*/
	//Testing the horizontal SOS 
	/*@Test
	public  void testCheckSosVertically() {
		
	;
	}*/
	//check for match
	/*@Test
	public void testCheckSosNoMatch()
	{
	   ;
	}*/
	//Test clear method
	/*@Test 
	public void testClearSosLines()
	{
		
	}*/
	
	//testing the line should not be addeded
	/*@Test
	public void testNullSosLine()
	{
		
	}*/


}
