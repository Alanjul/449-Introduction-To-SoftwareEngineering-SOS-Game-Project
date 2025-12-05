package sprint5.util;

import java.awt.Color;
import java.util.List;

import sprint5.mode.board.Board;

public interface PatternDetector {

	/**Find all SOS patterns formed by placing a letter at the given position
	 * @param board
	 * @param row the row position
	 * @param col the column position
	 * @param color the color for the line segments
	 * @return list of segment*/
	List<LineSegment4>findPatterns(Board board, int row, int col, Color color);
	
	//Count how many SOS Patterns formed
	int countPatterns(Board board, int row, int col, char letter);
	
	//check if placing letter would form atleast sos pattern
	default boolean formsPattern(Board board, int row, int col, char letter)
	{
		return countPatterns(board, row, col, letter) > 0;
	}
}
