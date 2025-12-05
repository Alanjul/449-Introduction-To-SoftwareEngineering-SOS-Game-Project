package sprint3Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.swing.JButton;
import javax.swing.JRadioButton;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sprint3.Board3;
import sprint3.BoardGui3;
import sprint3.BoardPanel3;
import sprint3.Game;
import sprint3.GeneralGame;
import sprint3.SimpleGame;

public class BoardPanelTest3 {
private BoardGui3 parent;
private Board3 board;
private SimpleGame simpleGame;
private GeneralGame generalGame;
private Game game;
private BoardPanel3 panel;


@Before
public void setUp() throws Exception
{
	System.setProperty("Java.awt.headless", "true"); //run the test in headless mode
	 board = new Board3(4);
	game = new SimpleGame(board) ;
	parent = new BoardGui3(board, game);
	parent.setVisible(false); //don't show the window

	panel = new BoardPanel3(parent, game, board);
}

@After
public void tearDown() throws Exception
{
	board = null;
	game = null;
	if(parent != null)
	{
		parent.dispose();//close the window
	}
	panel = null;
}
//check if the boardpanel is initialized correctly
@Test
public void testPanelInitialization()
{
	assertNotNull(panel);
}

//Acceptance criteria
//Testing the board
@Test
public void testBoardSizeMatchExpectedGrid()
{
	JButton[][] grids = TestingUtils.getField(panel, "cellButtons");
	assertEquals(4, grids.length);
	assertEquals(4, grids[0].length);
	assertNotNull(grids[3][2]); //check if the cell is initialzied

}
//testing if the we can place a letter in cell and disable it
@Test
public void testBoardCellAndPlaceLetter()
{
	JButton[][]cells = TestingUtils.getField(panel, "cellButtons");
	//click to place the letter
	JButton first = cells[0][1];
	first.doClick();
	assertFalse(first.isEnabled());//check if the button is disabled

	//check if the first letter is either S or O
	assertTrue(first.getText().equals("S") || first.getText().equals("O"));
}

//Testing for Overwrite
@Test
public void testOverwrite()
{
	JButton[][]cells = TestingUtils.getField(panel, "cellButtons");
	JButton first = cells[1][2];
	first.doClick();
	String start = first.getText();
	first.doClick();// attempt to overwrite
	assertEquals(start, first.getText());//check if the 2nd click didnot change the letter already placed inside
}

@Test
public void preventMove()
{
	game.setGameOver(true);
	JButton[][]grids = TestingUtils.getField(panel, "cellButtons");
	JButton first = grids[0][0];
	//click the cell
	first.doClick();
	assertEquals("", first.getText()); // no move allowed
}
@Test //copied directly from ChatGPT
public void testSelectGameMode() {
    // Access the radio buttons inside BoardPanel3 using TestingUtils
    JRadioButton simpleButton = TestingUtils.getField(panel, "simpleButton");
    JRadioButton generalButton = TestingUtils.getField(panel, "generalButton");

    // 1. Click SIMPLE mode and verify selection
    simpleButton.doClick();
    assertTrue(simpleButton.isSelected());
    assertFalse(generalButton.isSelected());

    // Create a new simple game and verify the type
    Game simple = new SimpleGame(board);
    assertTrue(simple instanceof SimpleGame);

    // 2. Click GENERAL mode and verify selection
    generalButton.doClick();
    assertTrue(generalButton.isSelected());
    assertFalse(simpleButton.isSelected());

    // Create a new general game and verify the type
    Game general = new GeneralGame(board);
    assertTrue(general instanceof GeneralGame);
}//REF: ChatGPT created testMethod
}
