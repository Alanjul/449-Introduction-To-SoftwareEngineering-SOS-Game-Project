package sprint4Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint5.mode.GameMode.GameMode4;
import sprint5.mode.GameMode.GameState;
import sprint5.mode.GameMode.GeneralGame4;
import sprint5.mode.board.Board;
import sprint5.mode.board.GameBoard5;
import sprint5.mode.humanplayer.HumanPlayer;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;



public class GeneralBoard4Test {

	private GeneralGame4 game;
	private Board board;
	private Player bluePlayer;
	private Player redPlayer;

	@Before
	public void setUp() throws Exception {
		board = new GameBoard5(4);
		bluePlayer = new HumanPlayer('B', "Blue player", 'S');
		redPlayer = new HumanPlayer('R', "Red Player", 'O');
		game = new GeneralGame4(board, bluePlayer, redPlayer);
	}

	@After
	public void tearDown() throws Exception {
		board = null;
		game = null;
		bluePlayer = null;
		redPlayer = null;
	}

	@Test
	public void testInitialGameTest() {
		assertEquals(GameMode4.GENERAL, game.getMode());
		assertEquals(GameState.IN_PROGRESS, game.getState());
		assertEquals('B', game.getCurrentTurn());
		assertFalse(game.isGameOver());// game not over
		// check scores
		assertEquals(0, game.getBlueScore());
		assertEquals(0, game.getRedScore());
	}

	// test full board'
		 @Test
		  public void testGameNotOverBoard()
		  {
			 char [][]moves = {
						{'S', 'O', 'S', 'O'},
						{'O', 'S', 'S', 'S'},
						{'S', 'S', 'O', 'O'}
						};
			  for (int i = 0; i < 3; i++)
			  {
				  for (int j = 0; j < 3; j++)
				  {
					  game.makeMove(new Move(i, j, moves[i][j]));
				  }
			  }
			  assertFalse(game.isGameOver());

		  }

		  @Test
		  public void testResetGame()
		  {
			  //play moves
			  game.makeMove(new Move(0, 0, 'S'));
			  game.makeMove(new Move(0, 1, 'O'));

			  //reset
			  game.resetGame();
			  assertEquals(0, game.getBlueScore());
			  assertEquals(0, game.getRedScore());
			  assertEquals('B', game.getCurrentTurn());
		      assertFalse(game.isGameOver());
		      assertEquals('N', game.determineWinner());

		  }
		  //test for draw
		  @Test
		  public void testDraw()
		  {
			  game.makeMove(new Move(0, 0, 'S'));
			  game.makeMove(new Move(0, 1, 'S'));
			  game.makeMove( new Move(0, 2, 'S'));
			  game.makeMove(new Move(0, 3, 'S'));
			  game.makeMove(new Move(1, 0, 'S'));
			  game.makeMove( new Move(1, 1, 'S'));
			  game.makeMove(new Move(1, 2, 'S'));
			  game.makeMove( new Move(2, 0, 'S'));
			  game.makeMove( new Move(2, 1, 'O'));
			  game.makeMove( new Move(2, 2, 'O'));
			  game.makeMove(new Move(1, 3, 'S'));
			  game.makeMove(new Move(2, 3, 'O'));

			  game.makeMove(new Move(3, 0, 'O'));
			  game.makeMove(new Move(3, 1, 'O'));
			  game.makeMove(new Move(3, 2, 'O'));
			  game.makeMove(new Move(3, 3, 'O'));
			  //game should be full
			  assertTrue(game.isGameOver());
			  //verify draw
			  assertEquals( 0, game.getBlueScore());
			  assertEquals( 0, game.getRedScore());

			  assertEquals(GameState.DRAW, game.getState());
		  }

}
