package sprint3Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint3.Board3;
import sprint3.SimpleGame;

public class SimpleClassTest {
	private SimpleGame game;
	private Board3 board;
	
	@Before
	public void setUp() throws Exception
	{
		board = new Board3(4);
		game = new SimpleGame(board);
	}
	
	@After
	public void tearDown() throws Exception {
		board = null;
		game = null;
	}
	
	@Test
	public void testBlueWins()
	{
		game.setWinner('B');
		assertTrue(game.isGameOver());
		assertEquals("Blue Player wins", game.getGameResult());
	}
	
	@Test
	public void testRedWins()
	{
		game.setWinner('R');
		assertTrue(game.isGameOver());
		assertEquals("Red player wins", game.getGameResult());
	}
	@Test
	public void testDraw()
	{
		char [][]moves = {
				{'S', 'S', 'S', 'O'},
				{'S', 'S', 'S', 'S'},
				{'S', 'S', 'O', 'O'},
				{'O', 'O', 'O', 'O'}
		};
		for (int i = 0; i < moves.length; i++)
		{
			for (int j = 0; j < moves[i].length; j++)
			{
				game.makeMove(i, j, moves[i][j]);
			}
		}
		assertTrue(game.isGameOver());
		assertEquals("Draw", game.getGameResult());
	}
	

}
