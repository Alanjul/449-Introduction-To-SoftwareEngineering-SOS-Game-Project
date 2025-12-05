package sprint2Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint2.Board2;
import sprint2.BoardGui;

public class GuiTest {
	private Board2 board;
	@Before
	public void setUp() throws Exception {
		board = new Board2(4);
	}

	@After
	public void tearDown() throws Exception {
		board = null;
	}

	@Test
	public void testEmptyBoard() {
		new BoardGui(board);
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
		new BoardGui(board);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	public void testInvalidBoard() {
		board = new Board2(2);
		new BoardGui(board);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
