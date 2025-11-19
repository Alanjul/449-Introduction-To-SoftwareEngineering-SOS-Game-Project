package sprint4Test;
import sprint4.mode.*;
import sprint4.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BoardSearcherSOSPatternTest {
	private Board board;

	
	@Before
	public void setUp()throws Exception
	{
		board = new Board4(4);
	}
	@After
	public void tearDow() throws Exception
	{
		board = null;
	}
	
	@Test
	//Detecting horizontal SOS
	public void testHorizontalSOS()
	{
		char [][]moves = {
				{'S', 'O', 'S', 'S'},
				{'S', 'S', 'S', 'S'},
				{'S', 'S', 'O', 'O'},
				{'O', 'O', 'O', 'O'}
		};
		for (int i = 0; i < moves.length; i++)
		{
			for (int j = 0; j < moves[i].length; j++)
			{
				board.makeMove(i, j, moves[i][j]);
				
			}
		}
		List<LineSegment4>lines = BoardSearcher.findSOSPatterns(board, 0, 2, Color.BLUE);
		assertEquals(1, lines.size());
		
		LineSegment4 line = lines.get(0);
		assertEquals(0, line.getStartRow());
		assertEquals(2, line.getStartCol());
		List<Integer>cols = Arrays.asList(line.getStartCol(), line.getEndCol());
		assertTrue(cols.contains(0));
		assertTrue(cols.contains(2));
	}
	
	@Test
	//Detecting horizontal SOS
	public void testVerticalSOS()
	{
		char [][]moves = {
				{'S', 'S', 'S', 'S'},
				{'O', 'S', 'S', 'S'},
				{'S', 'S', 'O', 'O'},
				{'O', 'O', 'O', 'O'}
		};
		for (int i = 0; i < moves.length; i++)
		{
			for (int j = 0; j < moves[i].length; j++)
			{
				board.makeMove(i, j, moves[i][j]);
				
			}
		}
		List<LineSegment4>lines = BoardSearcher.findSOSPatterns(board, 2, 0, Color.BLUE);
		
		
		assertEquals(1, lines.size());
		LineSegment4 line = lines.get(0);
		System.out.println(line.getStartCol());
		assertEquals(2, line.getStartRow());
		assertEquals(0, line.getStartCol());
		
		assertEquals(0, line.getEndCol());
		assertEquals(0, line.getEndRow());
	}
	
	//Detecting diagonal movement
	@Test
	public void testDiagonalSOSFormed() {
		char[][] moves = { 
				{ 'S', 'S', 'S', 'S' },
				{ 'S', 'O', 'S', 'S' }, 
				{ 'S', 'S', 'S', 'O' },
				{ 'O', 'O', 'O', 'O' } };
		for (int i = 0; i < moves.length; i++) {
			for (int j = 0; j < moves[i].length; j++) {
				board.makeMove(i, j, moves[i][j]);

			}
		}
		List<LineSegment4> lines = BoardSearcher.findSOSPatterns(board, 2, 2, Color.BLUE);

		assertEquals(1, lines.size());
		LineSegment4 line = lines.get(0);
		System.out.println(line.getStartCol());
		assertEquals(2, line.getStartRow());
		assertEquals(2, line.getStartCol());

		assertEquals(0, line.getEndCol());
		assertEquals(0, line.getEndRow());
	}

}
