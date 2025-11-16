package sprint3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board3 {
	
	private char[][] grid;
	private  int size;
	private List<LineSegment>sosLineFormed ; // stores the SOS line formed

	public Board3(int size) {
		if ((size < 3) || size > 15) {
			throw new IllegalArgumentException("The size must be between 3 and 15");
		}
		this.size = size; // initialize game size
		grid = new char[size][size];
		sosLineFormed = new ArrayList<>();
		resetGame();
	}

	/**
	 * GetCell returns the grid cell
	 * 
	 * @param row, column
	 * @return char grid[row][colum]
	 */
	public char getCell(int row, int column) {
		
		if (!(checkValidRowsAndColumns(row, column))) {
			return '\0';
		}
		return grid[row][column];
	}

	// making a move
	public boolean makeMove(int row, int col, char letter) {
		if (!checkValidRowsAndColumns(row, col)) {
			throw new IllegalArgumentException(
					"Move is out of bounds. Row and column must be between 0 and " + (size - 1) + ".");
		}
		if (letter != 'S' && letter != 'O') {
			throw new IllegalArgumentException("Invalid letter, Only 'S' or 'O' is allowed");
		}

		if (grid[row][col] != '\u0000') {
			return false ; //cell is already occupied

		}

		grid[row][col] = letter;
		
		return true;

	} // end of make move method

	public void resetGame() {
		for (int i = 0; i < size; i++) {
			Arrays.fill(grid[i], '\u0000');//clear the grid
		}
		sosLineFormed.clear();//removes all the lines formed
		
	}

//find if the grid is full
	public boolean isFull()
	{
		for (int i = 0; i < size; i++)
		{
			for(int j = 0; j < size; j++)
			{
				//check if empty
				if(grid[i][j] == '\u0000') return false;
			}
		}
		return true;//grid is full
	}
	
	//check if SOS is created
	public int checkSOS( int row, int col)
	{
		int count = 0; // counts the number of SOS formed
		// store the letters
		char letter = grid[row][col];
		// check if the grid has no letter
		if (letter == '\u0000')
			return 0;
		// define directions
		int[][] directions = { 
				{ 1, 0 }, // down {south}
				{ 0, 1 }, // east
				{ -1, 0 }, // up {north}
				{ 0, -1 }, // west

				{ 1, 1 }, // down right //south east
				{ 1, -1 }, // south- west
				{ -1, 1 }, // north east
				{ -1, -1 }, // North west
		};

		// check SOS is formed S begins and ends
		if (letter == 'S') {
			for (int[] dir : directions) {
				int row1 = row + dir[0];// move one row down
				int col1 = col + dir[1];// one step in the same column
				int row2 = row + 2 * dir[0]; // move two steps / two rows down
				int col2 = col+ 2 * dir[1]; // tow steps in the same columns
				if (checkValidRowsAndColumns(row1, col1) && checkValidRowsAndColumns(row2, col2)) {
					if (grid[row1][col1] == 'O' && grid[row2][col2] == 'S') {
						count++;
						}
				}
			} // end of for statement

		} else if (letter == 'O') {
			
			for (int[] axis : directions) {
				int row1 = row - axis[0];
				int col1 = col - axis[1];
				int row2 = row + axis[0];
				int col2 = col + axis[1];
				if (checkValidRowsAndColumns(row1, col1) && checkValidRowsAndColumns(row2, col2)) {
					if (grid[row1][col1] == 'S' && grid[row2][col2] == 'S') {
						count++;
					}
				}

			}
		}// end of if else statement
	return count;

	}//end of the methoth
	
	// helper method to check for valid row and col
	private  boolean checkValidRowsAndColumns(int row, int col) {
		return row >= 0 && row < size && col >= 0 && col < size;
	}


	public int getSize() {
		return size;
	}

	
	//returns all sos
	public List<LineSegment> getSosLineFormed() {
		return sosLineFormed;
	}

	public void setSosLineFormed(List<LineSegment> sosLineFormed) {
		this.sosLineFormed = sosLineFormed;
	}
	
	//add sos
	public void addSosLine(LineSegment line)
	{
		if (line != null)
		{
			sosLineFormed.add(line);
		}
	}
	
	//clear the sos formed once down playing the game
	public void clearSosLines()
	{
		sosLineFormed.clear();
	}
	
	public  int getSOSLineSize()
	{
		return sosLineFormed.size();
	}


}
