package sprint3Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint3.Board3;
import sprint3.LineSegment;

public class Board3Test {
	private Board3 board;
	@Before
	public void setUp() throws Exception {

		board = new Board3(4);
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
		assertFalse(board.isFull());
	}
     //acceptance criteria
	@Test //create a new board
	public void testNewBoard() {
		for (int row = 0; row <4; row++) {
			for (int col = 0; col < 4; col++) {
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
	//testing for SOS
	public void testCheckSosHorizontal()
	{
		board.makeMove(0, 0, 'S');
		board.makeMove(0, 1, 'O');
		board.makeMove(0, 2, 'S');
		//check SOS
		assertEquals(1, board.checkSOS(0, 2));
	}
	//testing SOS Line formed
	@Test
	public void testSosLine()
	{
		LineSegment seg = new LineSegment(0, 0, 0, 2, Color.BLUE);
		board.addSosLine(seg);
		assertEquals(1, board.getSOSLineSize());
	}
	//Testing the horizontal SOS
	@Test
	public  void testCheckSosVertically() {

		board.makeMove(1, 1, 'S');
		board.makeMove(2, 1, 'O');
		board.makeMove(3, 1, 'S');
		//check SOS
		assertEquals(1, board.checkSOS(3, 1));
	}
	//check for match
	@Test
	public void testCheckSosNoMatch()
	{
	    board.makeMove(1, 1, 'S');
	    board.makeMove(1, 2, 'S');
	    assertEquals(0, board.checkSOS(1, 1));
	}
	//Test clear method
	@Test
	public void testClearSosLines()
	{
		board.addSosLine(new LineSegment(0, 0, 0, 2, Color.BLUE));
		board.clearSosLines();//clear the line
		//get the size
		assertEquals(0, board.getSOSLineSize());
	}

	//testing the line should not be addeded
	@Test
	public void testNullSosLine()
	{
		board.addSosLine(null);
		assertEquals(0, board.getSOSLineSize()); //don't add
	}

}
