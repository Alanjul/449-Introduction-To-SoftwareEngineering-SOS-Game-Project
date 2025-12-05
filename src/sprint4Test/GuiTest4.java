package sprint4Test;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.GameMode.SimpleGame4;
import sprint5.mode.board.Board;
import sprint5.mode.board.GameBoard5;
import sprint5.mode.humanplayer.HumanPlayer;
import sprint5.mode.player.Player;
import sprint5.view.GameFrame;




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

			if(gui != null) {
				gui.dispose(); // close after test
			}
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
			board = new GameBoard5(2);
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
