package sprint1_test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint1.Board1;
import sprint1.BoardPanel;


public class BoardPanelTest {

	private BoardPanel boardPanel;
	private Board1 board;
	
	@Before
	public void setUp()
	{
		board = new Board1(5);
		boardPanel = new BoardPanel(null, board);
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
		assertEquals(5, board.getGridSize());
	}
}
