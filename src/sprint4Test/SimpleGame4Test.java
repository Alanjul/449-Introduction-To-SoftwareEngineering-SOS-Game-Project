package sprint4Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint5.mode.GameMode.GameMode4;
import sprint5.mode.GameMode.SimpleGame4;
import sprint5.mode.board.Board;
import sprint5.mode.board.Board4;
import sprint5.mode.humanplayer.HumanPlayer;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;



public class SimpleGame4Test {


	private SimpleGame4 game;
	private Board board;
	private Player redPlayer;
	private Player bluePlayer;

	@Before
	public void setUp() throws Exception
	{
		board = new Board4(4);
		bluePlayer = new HumanPlayer('B', "Blue player",'S');
		redPlayer = new HumanPlayer ('R', "Red Player", 'O');
		game = new SimpleGame4(board, bluePlayer, redPlayer);

	}

	@After
	public void tearDown() throws Exception {
		board = null;
		game = null;
		redPlayer = null;
		bluePlayer = null;
	}

	@Test
	public void testInitialization()
	{
		assertEquals(GameMode4.SIMPLE, game.getMode());
		assertEquals('B', game.getCurrentTurn());
		assertFalse(game.isGameOver());//game not over
		//check scores
		assertEquals(0, game.getBlueScore());
		assertEquals(0, game.getRedScore());
	}

	@Test
	public void testSwitchTurns()
	{
		assertEquals('B', game.getCurrentTurn());
		//make move
		game.makeMove(new Move(0, 0 , 'S'));
		//Red turn
		assertEquals('R', game.getCurrentTurn());
		game.makeMove(new Move(1, 1, 'O'));
		//Blue turn
		assertEquals('B', game.getCurrentTurn());
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
				game.makeMove(new Move(i, j, moves[i][j]));
			}
		}
		assertTrue(game.isGameOver());
		assertEquals("Draw!, Board is full with no sos formed", game.generateResult());
	}
	@Test
	public void testWins()
	{
		char [][]moves = { {'S', 'O', 'S', 'O'},
				{'O', 'S', 'O', 'S'},

				{'S', 'O', 'S', 'O'},
				{'O', 'S', 'O', 'S'}
		};
		for (int i = 0; i < moves.length; i++) {
			for (int j = 0; j < moves[i].length; j++) {
				game.makeMove(new Move(i, j, moves[i][j]));
			}
		}
		assertTrue(game.isGameOver());
		assertEquals("Blue player wins", game.generateResult());
		assertEquals(1, game.getBlueScore());
		assertEquals(0, game.getRedScore());
	}
	@Test
	public void testCurrentPlayer()
	{
		assertEquals(bluePlayer, game.getCurrentPlayer());
		assertEquals(redPlayer, game.getOpponent());
		game.makeMove(new Move(0, 0, 'S'));
		assertEquals(redPlayer, game.getCurrentPlayer());
		assertEquals(bluePlayer, game.getOpponent());
	}

	//Testing Sos Lines
	@Test
	public void testSOSLines()
	{
		assertTrue(game.getLineFormed().isEmpty());// check if no sos line formed
		char [][]moves = { {'S', 'O', 'S', 'O'},
				{'S', 'S', 'S', 'S'},

				{'S', 'S', 'S', 'S'},
				{'O', 'S', 'S', 'O'}
		};
		for (int i = 0; i < moves.length; i++) {
			for (int j = 0; j < moves[i].length; j++) {
				game.makeMove(new Move(i, j, moves[i][j]));
			}
		}
		assertEquals(1, game.getLineFormed().size());

	}
}
