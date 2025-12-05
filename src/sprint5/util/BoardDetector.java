package sprint5.util;

import java.awt.Color;
import java.util.List;

import sprint5.mode.board.Board;

/**BoardDetector class implements PatternDetector using BoardSearcher*/
public class BoardDetector  implements PatternDetector{
	
	/*Find all SOS patterns formed at the given position*/
	@Override
	public List<LineSegment4>findPatterns(Board board, int row, int col, Color color)
	{
		return BoardSearcher.findSOSPatterns(board, row, col, color);
	}

	@Override
	public int countPatterns(Board board, int row, int col, char letter)
	{
		return BoardSearcher.countSOS(board, row, col, letter);
	}
	@Override //check if the pattern would form SOS
    public boolean formsPattern(Board board, int row, int col, char letter) {
        return BoardSearcher.formSOS(board, row, col, letter);
    }
}
