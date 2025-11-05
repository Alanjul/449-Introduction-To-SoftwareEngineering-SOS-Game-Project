package sprint3Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint3.Board3;
import sprint3.BoardGui3;
import sprint3.Game;
import sprint3.SimpleGame;



public class GUiTest3 {
		private Board3 board;
		private Game game;
		private BoardGui3 gui;
		@Before
		public void setUp() throws Exception {
			board = new Board3(4);
			game = new SimpleGame(board);
			gui = new BoardGui3(board, game);
			
		}

		@After
		public void tearDown() throws Exception {
			
			if(gui != null) gui.dispose(); // close after test
			board = null;
			game = null;
		}

		@Test
		public void testEmptyBoard() {
			
			
			new BoardGui3(board,game); 
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		@Test
		public void testNonEmptyBoard() {
			board.makeMove(0, 0, 'S');
			board.makeMove(1, 1, 'O');		
			new BoardGui3(board,game); 
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		}
		public void testInvalidBoard() {
			board = new Board3(2);
			new BoardGui3(board, game); 
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

}
