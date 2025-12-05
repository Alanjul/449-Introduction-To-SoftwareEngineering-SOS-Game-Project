package sprint0Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint0.BoardGame;

public class TestBoardGame {
	private BoardGame game;

	@Before
	public void setUp() throws IOException
	{
		//run before the test method
		game = new BoardGame(6);
	}

	@After
	public void tearDown() throws IOException
	{
		//reset  and clean up
		game = null;
	}
	@Test
	public void testTurnInitializedTrue()
	{

		assertTrue( "Turn should be initialized to true", game.getTurn());
	}

	@Test
	public void testBoardSize()
	{
		assertEquals(6, game.getGridSize());
	}
	@Test
	//test the empty board
	public void testPrintBoardToConsole()
	{
		game.printBoardToConsole();
	}
}
