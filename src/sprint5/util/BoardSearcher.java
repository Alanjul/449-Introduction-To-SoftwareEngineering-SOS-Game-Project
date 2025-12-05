package sprint5.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sprint5.mode.board.Board;
import sprint5.mode.move.Move;

public class BoardSearcher {

	// 8 directional search
	private static final int[][] DIRECTIONS = { 
			{ 1, 0 }, // down {south}
			{ 0, 1 }, // east
			{ -1, 0 }, // up {north}
			{ 0, -1 }, // west

			{ 1, 1 }, // down right //south east
			{ 1, -1 }, // south- west
			{ -1, 1 }, // north east
			{ -1, -1 }, // North west
	};
	// Check 'O' in middle
	private static final int[][] AXES = { { 1, 0 }, { 0, 1 }, { 1, 1 }, { 1, -1 } };

	/*
	 * Static Method FindSOSPatterns finds the sos pattern formed in game search 
	 * @param board
	 * @param row
	 * @param col
	 * @param color
	 * @return SOS pattern formed
	 */

	public static List<LineSegment4> findSOSPatterns(Board board, int row, int col, Color color) {
		char letter = board.getCell(row, col);
		if (letter == '\0') {
			return new ArrayList<>();
		}
		List<LineSegment4> patterns = (letter == 'S') ? findPatternStartingWithS(board, row, col, color)
				: findPatternWithOInMiddle(board, row, col, color);
		return LineDuplicatorRemove.removeDuplicate(patterns);
	}

	// finding SOS patter with s at row, col
	private static List<LineSegment4> findPatternStartingWithS(Board board, int row, int col, Color color) {
		List<LineSegment4> sosPatterns = new ArrayList<>();

		if (board.getCell(row, col) != 'S') {
			return sosPatterns;
		}
		for (int[] direction : DIRECTIONS) {
			SOSPattern pattern = checkDirectionsForPattern(board, row, col, direction);
			if (pattern.isValid()) {
				sosPatterns.add(new LineSegment4(row, col, pattern.endRow, pattern.endCol, color));
			}
		}

		return sosPatterns;
	}

	// Check directions
	private static SOSPattern checkDirectionsForPattern(Board board, int startRow, int startCol, int[] direction) {
		int middleRow = startRow + direction[0];
		int middleCol = startCol + direction[1];
		int endRow = startRow + 2 * direction[0];
		int endCol = startCol + 2 * direction[1];
		if (!isValidPosition(board, middleRow, middleCol) || !isValidPosition(board, endRow, endCol)) {
			return SOSPattern.invalid();
		}
		// check for SOS pattern
		boolean hasPattern = board.getCell(middleRow, middleCol) == 'O' && board.getCell(endRow, endCol) == 'S';
		return hasPattern ? SOSPattern.valid(endRow, endCol) : SOSPattern.invalid();
	}

	// FindPatternWithOInMiddle finds sos pattern where 'O' is at row, col
	// checking for direction along the each axis
	private static List<LineSegment4> findPatternWithOInMiddle(Board board, int row, int col, Color color) {
		List<LineSegment4> sosPattern = new ArrayList<>(8);
		// check 4 directions

		for (int[] dir : AXES) {

			int startRow = row - dir[0];
			int startCol = col - dir[1];
			int endRow = row + dir[0];
			int endCol = col + dir[1];
			// early termination
			if (!isValidPosition(board, startRow, startCol) || !isValidPosition(board, endRow, endCol)) {
				continue;
			}
			// check pattern
			if (board.getCell(startRow, startCol) == 'S' && board.getCell(endRow, endCol) == 'S') {
				sosPattern.add(new LineSegment4(startRow, startCol, endRow, endCol, color));
			}
		}

		return sosPattern;
	}

	/*
	 * count how many SOS are formed used by computer to evaluate moves
	 * @param board
	 * @param row,
	 * @param col
	 * @param letter
	 */
	public static int countSOS(Board board, int row, int col, char letter) {
		if (!board.isEmpty(row, col)) {
			return 0;
		}
		// create copy of the move
		Board testBoard = board.copy();
		testBoard.makeMove(row, col, letter);
		List<LineSegment4> patterns = findSOSPatterns(testBoard, row, col, Color.BLACK);
		return patterns.size();
	}

	/*
	 * formSOS , checking if SOS will be formed by the computer and doesn't modify
	 * the board operations
	 * @param board,
	 * @param row
	 * @param col
	 * @param letter to store the letter
	 */
	public static boolean formSOS(Board board, int row, int col, char letter) {

		return countSOS(board, row, col, letter) > 0;

	}

	// counting total sos patterns formed
	public static int countallSOSFormed(Board board) {
		int count = 0;// track the number of SOS formed
		int size = board.getSize();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				char cell = board.getCell(i, j);
				if (cell == 'S') {
					count += findPatternStartingWithS(board, i, j, Color.BLACK).size();
				}
			}
		}
		return count;
	}

	// Find the best move for the
	public static Move findBestMove(Board board, char letter) {
		List<Board.Cell> emptyCells = board.getEmptyCells();
		Move bestMove = null;
		int maxSOS = 0;

		for (Board.Cell cell : emptyCells) {
			int sosCount = countSOS(board, cell.row(), cell.col(), letter);
			if (sosCount > maxSOS) {
				maxSOS = sosCount;
				bestMove = new Move(cell.row(), cell.col(), letter);
			}
		}

		return bestMove;
	}

	// Helper method
	private static boolean isValidPosition(Board board, int row, int col) {
		int size = board.getSize();
		return row >= 0 && row < size && col >= 0 && col < size;
	}

	// checking for SOS pattern'
	private static class SOSPattern {
		final boolean valid;
		final int endRow;
		final int endCol;

		private SOSPattern(boolean valid, int endRow, int endCol) {
			this.valid = valid;
			this.endRow = endRow;
			this.endCol = endCol;
		}

		static SOSPattern valid(int endRow, int endCol) {
			return new SOSPattern(true, endRow, endCol);
		}

		// invalid
		static SOSPattern invalid() {
			return new SOSPattern(false, -1, -1);
		}

		boolean isValid() {
			return valid;
		}
	}

}
