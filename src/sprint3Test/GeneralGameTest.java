package sprint3Test;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint3.Board3;
import sprint3.GeneralGame;

public class GeneralGameTest {
  private GeneralGame game;
  private Board3 board;
  
  @Before
  public void setUp()throws Exception
  {
	board = new Board3(3);
	game = new GeneralGame(board);
  }
  @After
  public void tearDown() throws Exception
  {
	  board = null;
	  game = null;
  }
  
  @Test
  public void testInitialGameTest()
  {
	  assertEquals("Initial turn should be Blue",'B', game.getCurrentTurn());
	  assertEquals("Initial blue score should be O", 0, game.getBlueScore());
	  assertEquals("Initial red score should be 0", 0, game.getRedScore());
	  assertFalse("Game should not initially be over", game.isGameOver());
	  //no winner
	  assertEquals("No Winner at the start", '\0', game.getWinner());
  }
  //test full board'
  @Test
  public void testGameOverWhenBoardFull()
  {
	  for (int i = 0; i < 3; i++)
	  {
		  for (int j = 0; j < 3; j++)
		  {
			  game.makeMove(i, j, 'S');
		  }
	  }
	  //check for game over
	  game.makeMove(2, 2, 'O');
	  assertTrue("Game should be over when the game is filled", game.isGameOver());
  }
  
  @Test
  public void testResetGame()
  {
	  //play moves
	  game.makeMove(0, 0, 'S');
	  game.makeMove(0, 1, 'O');
	  
	  //reset
	  game.reset();
	  assertEquals(0, game.getBlueScore());
	  assertEquals(0, game.getRedScore());
	  assertEquals('B', game.getCurrentTurn());
      assertFalse(game.isGameOver());
      assertEquals('\0', game.getWinner());
	  
  }
  //test for draw
  @Test
  public void testDraw()
  {
	  game.makeMove(0, 0, 'S');
	  game.makeMove(0, 1, 'S');
	  game.makeMove(0, 2, 'O');
	  game.makeMove(1, 0, 'O');
	  game.makeMove(1, 1, 'S');
	  game.makeMove(1, 2, 'S');
	  game.makeMove(2, 0, 'O');
	  game.makeMove(2, 1, 'O');
	  game.makeMove(2, 2, 'S');
	  
	  //game should be full
	  assertTrue("Game should be over when full", game.isGameOver());
	  //verify draw
	  assertEquals("Bule Score should be 0 in a draw", 0, game.getBlueScore());
	  assertEquals("Bule Score should be 0 in a draw", 0, game.getRedScore());
	  assertEquals("Winner should be null in draw", '\0', game.getWinner());
	  
	  //check message
	  String answer = game.getGameResult();
	  assertTrue("Result message should be draw", answer.toLowerCase().contains("draw"));
  }
}
