package sprint2Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint2.Board2;
import sprint2.BoardPanel2;


public class BoardPanel2Test {
	
	
	private BoardPanel2 boardPanel;
	private Board2 board;
	
	@Before
	public void setUp()
	{
		board = new Board2(5);
		boardPanel = new BoardPanel2(null, board);
	}
	@After
	public void tearDown()
	{
		board = null;
		boardPanel = null;
	}
	@Test
	public void testBoardPanel()
	{
		assertNotNull(boardPanel);
		assertEquals(5, boardPanel.getComponentCount());
	}
	//test boardsize
	@Test
	public void testBoardSize()
	{
		assertEquals(5, board.getSize());
	}

}
