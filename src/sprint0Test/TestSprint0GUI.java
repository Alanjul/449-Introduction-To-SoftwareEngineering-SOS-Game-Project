package sprint0Test;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint0.BoardGame;
import sprint0.Sprint0BoardGame;

public class TestSprint0GUI {
	private BoardGame game;
	
	@Before
	public void setUp() throws IOException
	{
		game = new BoardGame(8);
	}
	
	@After
	public void tearDown() throws IOException
	{
		game = null;
	}
	
	@Test
	public void testBoardGameGui()
	{
		new Sprint0BoardGame(game);
		try
		{
			Thread.sleep(3000);//sleep for 3 seconds
		}catch (InterruptedException e)
		{
			e.getMessage();
		}
	}

}
