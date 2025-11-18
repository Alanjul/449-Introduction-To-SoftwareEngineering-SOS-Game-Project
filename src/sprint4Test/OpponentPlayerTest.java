package sprint4Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint2.GameMode;
import sprint4.mode.*;
import sprint4.mode.ComputerPlayer.LevelsOfDifficulty;

//USing mocking test to test Dependency injection principle

public class OpponentPlayerTest {
	
	
	static class FakeComputerStrategy implements  ComputerStrategy
	{
		private Move moveToReturn;
		private boolean throwException = false;
		private int countCall = 0;
		private String name;
		//Accept a move to return, if the best move is found
		public FakeComputerStrategy(String name, Move moveToReturn)
		{
			this.name = name;
			this.moveToReturn = moveToReturn;
		}
		
		/**
		 * @return the moveToReturn
		 */
		public Move getMoveToReturn() {
			return moveToReturn;
		}

		/**
		 * @return the countCall
		 */
		public int getCountCall() {
			return countCall;
		}

		/**
		 * @param throwException the throwException to set
		 */
		public void setThrowException(boolean throwException) {
			this.throwException = throwException;
		}

		/**
		 * @param countCall the countCall to set
		 */
		public void setCountCall(int countCall) {
			this.countCall = countCall;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		@Override
		public Move findBestMove(Board board, Game4 game, Player computerPlayer, Player opponent,
				ComputerPlayer.LevelsOfDifficulty difficultyLevel)
		{
			countCall++; //tracks number of times called
			if(throwException) throw new RuntimeException("Computer ERROR!");
			return moveToReturn;
		}
		
		/**getStrategyName returns the name of the strategy*/
	@Override
	 public String getStrategyName()
	 {
		 return name;
	 }
	}
	
	static class TestGame4 extends Game4 {
	    public TestGame4(Board b, Player blue, Player red, GameMode4 mode) {
	        super(b, blue, red, mode);
	    }

	   
	    @Override
	    protected boolean shouldSwitchTurns(int sosFormed) {
	        return true;
	    }

	    @Override
	    protected void checkGameOver() {
	    }
	}
	
	static class TestPlayer extends Player {
	    public TestPlayer(char symbol, String name) {
	        super(symbol, name);
	    }

	    @Override
	    public Move chooseMove(Board b, Player o, Game4 g) { return null; }
		
	    @Override
		public  char chooseLetter()
		{
	    	return 'S';//default value
		}
	}
	
	private Board board;
    private TestPlayer opponent;
    private TestGame4 game;
    private FakeComputerStrategy strategy;
    private ComputerPlayer computer;
	//Copied for chatGPT
    @Before 
    public void setUp()throws Exception
    {
    	board = new Board4(4);
    	
    	//oponent player
    	opponent = new TestPlayer('R', "Opponent");
    	
    	//Strategy
    	strategy = new FakeComputerStrategy("Test Computer Move", new Move(0, 0, 'S'));
    	
    	//Computer player
    	computer = new ComputerPlayer('B', ComputerPlayer.LevelsOfDifficulty.MEDIUM, strategy);
    	
    	//Game
    	game = new TestGame4(board, computer, opponent, GameMode4.GENERAL);
    }
	@Test
	public void testComputerPlayerUsesStrategy() {
		Move move = computer.chooseMove(board, opponent, game);
        assertNotNull(move);
        assertEquals(0, move.getRow());
        assertEquals(0, move.getCol());
        assertEquals('S', move.getLetter());
        assertEquals(1, strategy. getCountCall());
	}// created by ChatGPT
	@Test
    public void testEasyDifficultyDoesNotUseStrategy() {
        ComputerPlayer easyPlayer = new ComputerPlayer('B', ComputerPlayer.LevelsOfDifficulty.EASY, strategy);
        Move move = easyPlayer.chooseMove(board, opponent, game);

        // Strategy should not be called for EASY
        assertEquals(0, strategy.getCountCall());
        assertNotNull(move);
	}
	@Test
    public void testRandomDifficultyDoesNotUseStrategy() {
        ComputerPlayer randomPlayer = new ComputerPlayer('B', ComputerPlayer.LevelsOfDifficulty.RANDOM, strategy);
        Move move = randomPlayer.chooseMove(board, opponent, game);

        // Strategy should not be called for RANDOM
        assertEquals(0, strategy.getCountCall());
        assertNotNull(move);
    }
	@Test
    public void testStrategyName() {
        assertEquals("Test Computer Move", strategy.getStrategyName());
    }
	
	@Test
    public void testComputerHandlesNullMove() {
        
        strategy = new FakeComputerStrategy("Test Computer Move", null);
        computer = new ComputerPlayer('B', ComputerPlayer.LevelsOfDifficulty.MEDIUM, strategy);

        Move move = computer.chooseMove(board, opponent, game);
        assertNull(move);
        assertEquals(1, strategy.getCountCall());
    }
	@Test
	//Testing other difficulties
	public void testOtherDifficulties()
	{
		ComputerPlayer.LevelsOfDifficulty[] advanced = {
				LevelsOfDifficulty.MEDIUM,
				LevelsOfDifficulty.HARD,
				LevelsOfDifficulty.EXPERT
		};
		for (LevelsOfDifficulty level : advanced)
		{
			strategy.setCountCall(0);//reset count
			ComputerPlayer player = new ComputerPlayer('B', level, strategy);
			Move move = player.chooseMove(board, player, game);
			assertNotNull("Level " + level + " return "+ move);
			assertEquals("Level " + level + "  Strategy", 1, strategy.getCountCall());
		}
	}
	
	//Testing valid letters
	@Test
	public void testComputerChoosesValidLetter() {
	    for (int i = 0; i < 5; i++) {
	        char letter = computer.chooseLetter();
	        assertTrue("Letter must be S or O", 
	                  letter == 'S' || letter == 'O');
	    }
	}
	
	//Testing computer player
	@Test
	public void testComputerPlayerIsPlayer() {
	    
	    Player player = computer;  
	    
	    Move move = player.chooseMove(board, opponent, game);
	    assertNotNull(move);
	    
	    // Verify it's still a ComputerPlayer
	    assertTrue(player instanceof ComputerPlayer);
	}
	//Test legal move made by computer
	@Test
	public void testComputerMakesLegalMove() {
	    Move move = computer.chooseMove(board, opponent, game);
	    assertTrue(board.isEmpty(move.getRow(), move.getCol()));
	}
	
	//Testing that a computer never picks occupied cell
	@Test
	public void testComputerAvoidsOccupiedCells() {
	    board.makeMove(1,1,'S'); // occupy a spot
	    
	    Move move = computer.chooseMove(board, opponent, game);
	    
	    assertFalse(move.getRow() == 1 && move.getCol() == 1);
	}
	@Test
	public void testComputerHandlesFullBoard() {
	    int size = board.getSize();

	    for (int r = 0; r < size; r++)
	        for (int c = 0; c < size; c++)
	            board.makeMove(r, c, 'S');

	    Move m = computer.chooseMove(board, opponent, game);

	    assertNull("No legal move expected on full board", m);
	}
	//Computer opponent remain Computer
	@Test
	public void testComputerIsStillComputerPlayer() {
	    Player p = computer;
	    Move m = p.chooseMove(board, opponent, game);

	    assertTrue(p instanceof ComputerPlayer);
	}
	
	
	//Making sure the move is within the bounds
	@Test
	public void testComputerMoveWithinBounds() {
	    Move m = computer.chooseMove(board, opponent, game);

	    assertTrue(m.getRow() >= 0 && m.getRow() < board.getSize());
	    assertTrue(m.getCol() >= 0 && m.getCol() < board.getSize());
	}
}
