package sprint4Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import sprint4.mode.Board;
import sprint4.mode.Board4;
import sprint4.mode.Game4;
import sprint4.mode.HumanPlayer;
import sprint4.mode.Player;
import sprint4.mode.SimpleGame4;
import sprint4.view.GameFrame;




public class GuiTest4 {
	
	
		private Board board;
		private Game4 game;
		private GameFrame gui;
		private Player bluePlayer;
		private Player redPlayer;
		@Before
		public void setUp() throws Exception {
			board = new Board4(4);
			bluePlayer = new HumanPlayer('B', "Blue player",'S');
			redPlayer = new HumanPlayer ('R', "Red Player", 'O');
			game = new SimpleGame4(board, bluePlayer, redPlayer);
			gui = new GameFrame();
			
		}

		@After
		public void tearDown() throws Exception {
			
			if(gui != null) gui.dispose(); // close after test
			board = null;
			bluePlayer = null;
			redPlayer = null;
			game = null;
			gui = null;
		}

		@Test
		public void testEmptyBoard() {
			gui.setVisible(true);
			assertNotNull("MainPanel should be initialized", gui.getMainPanel());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public void testInvalidBoard() {
			board = new Board4(2);
			gui = new GameFrame();
			gui.setVisible(true);
			assertNotNull("MainPanel should be initialized", gui.getMainPanel());
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

}
