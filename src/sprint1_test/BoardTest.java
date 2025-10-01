package sprint1_test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint1.Board;


public class BoardTest {
	private Board board;
	
	@Before 
	public void setUp()
	{
		board = new Board(5);
	}

	@After
	public void tearDown()
	{
		board = null;
	}
	
	@Test
	public void testBoardInitialization()
	{
		assertEquals(5, board.getGridSize());
		//check if all cells are empty
		for (int row = 0; row < board.getGridSize(); row++)
		{
			for (int col = 0; col < board.getGridSize(); col++)
			{
				assertEquals('\0', board.getCell(row, col));
			}
		}
	}
	//test the get and set cell
	@Test
	public void testSetCellAndGetCell()
	{
		board.setCell(1, 1, 'S');
		assertEquals('S', board.getCell(1, 1));
		board.setCell(0, 2, 'O');
        assertEquals('O', board.getCell(0, 2));
		
	}
	//test for valid cell
	@Test
	public void testValidCell()
	{
		assertEquals('-', board.getCell(-1, 0)); //test for row < 0
		assertEquals('-', board.getCell(0, -1)); //column less than 0
		assertEquals('-', board.getCell(0, 5));//column greater or equal to size
		assertEquals('-', board.getCell(5, 0)); // row greater or equal to size
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
}
