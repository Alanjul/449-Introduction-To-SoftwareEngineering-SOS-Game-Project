package sprint1_test;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint1.Board;
import sprint1.BoardGameGui;


public class GameGuiTest {
	
private BoardGameGui gameGui;
private Board board;
	
	@Before
	public void setUp() throws IOException
	{
		board = new Board(8);
	}
	
	@After
	public void tearDown() throws IOException
	{
		if(gameGui != null)
		{
			gameGui.dispose();//close the window
		}
		board = null;
	}
	
	@Test
	public void testBoardGameGui()
	{
		SwingUtilities.invokeLater(() -> {
			gameGui = new BoardGameGui(board);
			assertNotNull(gameGui);
		});

	}
}
