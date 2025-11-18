package sprint4.Util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sprint4.mode.Board;
import sprint4.mode.Board4;
import sprint4.mode.LineSegment4;
import sprint4.mode.Move;

public class BoardSearcher {
	
	
	private static final int  [][] DIRECTIONS = {
			{ 1, 0 }, // down {south}
			{ 0, 1 }, // east
			{ -1, 0 }, // up {north}
			{ 0, -1 }, // west

			{ 1, 1 }, // down right //south east
			{ 1, -1 }, // south- west
			{ -1, 1 }, // north east
			{ -1, -1 }, // North west
	};
	//Helper method
	private static boolean checkValidRowsAndColumns(Board board, int row, int col)
	{
		 int size = board.getSize();
		 return row >= 0 && row < size && col >= 0 && col < size;
	}
	
	/*Static Method FindSOSPatterns finds the sos pattern formed in game search
	 * @param board
	 * @param row
	 * @param col
	 * @param color
	 * @return SOS pattern formed*/
	
	public static List<LineSegment4>findSOSPatterns(Board board , int row, int col, Color color)
	{
		char letter = board.getCell(row, col);
		if(letter == '\0')
		{
			return new ArrayList<>();
		}
		List<LineSegment4>pattern =  (letter == 'S') ? findPatternStartingWithS(board, row, col, color): 
			findPatternWithOInMiddle(board, row, col, color);
		return removeDuplicate(pattern);
	}
	//finding SOS patter with s at row, col
	private static List<LineSegment4>findPatternStartingWithS(Board board, int row, int col, Color color)
	{
		List<LineSegment4> sosPattern = new ArrayList<>(8);//start checking  8 direction
		
		if(board.getCell(row, col) != 'S')
		{
			return sosPattern;
		}
		for (int []dir : DIRECTIONS)
		{
			//checking SOS formed
			int middleRow = row + dir[0];
			int middleCol = col + dir[1]; 
			int endRow = row + 2 * dir[0]; 
			int endCol = col + 2 * dir[1];
			
			//checking for valid position
			if(!checkValidRowsAndColumns(board, middleRow, middleCol) || !checkValidRowsAndColumns(board, endRow, endCol))
			{
				//continue
				continue;
			}
			//check pattern
			if(board.getCell(middleRow, middleCol) == 'O' && board.getCell(endRow, endCol) == 'S')
			{
				sosPattern.add(new LineSegment4(row, col, endRow, endCol, color));
			}
			
		}
		
		return sosPattern;
	}
	
	//FindPatternWithOInMiddle finds sos pattern where 'O' is at row, col
	//checking for direction along the each axis
	private static List<LineSegment4>findPatternWithOInMiddle(Board board, int row, int col, Color color)
	{
		List<LineSegment4> sosPattern = new ArrayList<>(8);
		//check 4 directions
		int [][]axes = {
				{1,0},
				{0,1},
				{1,1},
				{1,-1}
		};
		for (int []dir : axes) {
			
			int startRow = row - dir[0];
			int startCol = col - dir[1];
			int endRow = row + dir[0];
			int endCol = col + dir[1];
			// early termination
			if (!checkValidRowsAndColumns(board, startRow, startCol)
					|| !checkValidRowsAndColumns(board, endRow, endCol)) {
				continue;
			}
			// check pattern
			if (board.getCell(startRow, startCol) == 'S' && board.getCell(endRow, endCol) == 'S') {
				sosPattern.add(new LineSegment4(startRow, startCol, endRow, endCol, color));
			}
		}

		return sosPattern;
	}

	/*count how many SOS are formed 
	 * used by computer to evaluate moves
	 * @param board
	 * @param row,
	 * @param col
	 * @param letter*/
	public static int countSOS(Board board, int row, int col, char letter)
	{
		if(!board.isEmpty(row, col))
		{
			return 0;
		}
		//create copy of the move
		Board testBoard = board.copy();
		testBoard.makeMove(row, col, letter);
		List<LineSegment4>patterns = findSOSPatterns(testBoard, row, col, Color.BLACK);
		return patterns.size();
	}
	/*
	 * formSOS , checking if SOS will be formed by the computer and doesn't modify
	 * the board operations
	 * 
	 * @param board,
	 * 
	 * @param row
	 * 
	 * @param col
	 * 
	 * @param letter to store the letter
	 */
	public static boolean formSOS(Board board, int row, int col, char letter) {

		return  countSOS(board, row, col, letter) > 0;
		
	}

	
	//counting total sos patterns formed
	public static int countallSOSFormed(Board board)
	{
		int count = 0;//track the number of SOS formed
		int size = board.getSize();
		for(int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				char cell = board.getCell(i, j);
				if(cell == 'S')
				{
					count += findPatternStartingWithS(board, i, j,Color.BLACK).size();
				}
			}
		}
		return count;
	}

	
	
	//Used by computer to find the bestMove
	public static Move findBestMove (Board board, char letter)
	{
		List<Board.Cell>emptyCells = board.getEmptyCells();
		if(emptyCells.isEmpty())
		{
			return null;
		}
		int maxSOS = -1;
		Move bestMove =  null;
		
		for (Board.Cell cell  : emptyCells)
		{
			int row = cell.row();
			int col = cell.col();
			//count o
			int sosCount = countSOS(board, row, col, letter);
			
			
			if(sosCount > maxSOS)
			{
				maxSOS = sosCount;
				bestMove = new Move(row, col, letter);
			}
			
		}
		//pick the first valid move, if all moves produces the same score(0)
		if(bestMove  == null)
		{
			Board.Cell firstMove  = emptyCells.get(0);
			bestMove = new Move(firstMove.row(), firstMove.col(), letter);
		}
		
		return bestMove;
	}
	
	//Removing duplicate SOS pattern
	private static List<LineSegment4>removeDuplicate(List<LineSegment4>patterns)
	{
		List<LineSegment4>uniqueLine = new ArrayList<>();
		Set<String>visited = new HashSet<>();
		for(LineSegment4 line : patterns)
		{
			int row1 = line.getStartRow(), col1 = line.getStartCol();
			int row2 = line.getEndRow(), col2 = line.getEndCol();
			String smaller;
			if(row1 < row2 || (row1 == row2 && col1 < col2))
			{
				smaller = row1 + "," + col1 + "-" + row2 + "," + col2;
			}else
			{
				smaller = row2 + "," + col2 + "-" + row1 + "," + col1;
			}
			if(visited.add(smaller))
			{
				uniqueLine.add(line);
			}
		}
		return uniqueLine;
	}

}
