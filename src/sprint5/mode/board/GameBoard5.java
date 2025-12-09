package sprint5.mode.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameBoard5 implements Board {

	private char[][] grid;
	private int size;
	public static final char EMPTY = '\u0000';
	private static final int MIN_BOARD_SIZE = 3;
	private static final int MAX_BOARD_SIZE = 15;
	private int countMove;

	public GameBoard5(int size) {
		validateBoardSize(size);
		this.size = size; // initialize game size
		grid = new char[size][size];
		this.countMove = 0;
		initializeGrid();
	}

	/* Private method to validate the move within the board */
	private void validateBoardSize(int size) {
		if ((size < MIN_BOARD_SIZE) || size > MAX_BOARD_SIZE) {
			throw new IllegalArgumentException(String.format("The size must be between %d and %d , got: %d",
					MIN_BOARD_SIZE, MAX_BOARD_SIZE, size));
		}
	}

	/**
	 * GetCell returns the grid cell
	 *
	 * @param row, column
	 * @return char grid[row][colum]
	 */
	@Override
	public char getCell(int row, int column) {

		if (row < 0 || row >= size || column < 0 || column >= size) {
			return EMPTY;
		}
		return grid[row][column];
	}

	// validate letter
	private boolean isValidLetter(char letter) {
		return letter == 'S' || letter == 'O';
	}

	// making a move
	@Override
	public boolean makeMove(int row, int col, char letter) {
		checkValidRowsAndColumns(row, col);

		if (!isValidLetter(letter) || (grid[row][col] != EMPTY)) {
			return false; // cell is already occupied

		}

		grid[row][col] = letter;
		countMove++;

		return true;

	} // end of make move method

	// initialize the grid
	private void initializeGrid() {
		for (int i = 0; i < size; i++) {
			Arrays.fill(grid[i], EMPTY);// clear the grid
		}

	}// end of the method grid

	// reset the game
	@Override
	public void resetGame() {
		initializeGrid();
		countMove = 0; // set to zero

	}

//Find if the grid is full
	@Override
	public boolean isFull() {
		return countMove == size * size;
	}

	// helper method to check for valid row and col
	private void checkValidRowsAndColumns(int row, int col) {
		if (row < 0 || row >= size || col < 0 || col >= size) {
			throw new IndexOutOfBoundsException(
					String.format("Invalid position (%d, %d) valid indices are 0 to %d", row, col, (size - 1)));
		}
	}

	// check for empty board
	@Override
	public boolean isEmpty(int row, int col) {
		checkValidRowsAndColumns(row, col);
		return grid[row][col] == EMPTY;
	}

	/*
	 * GetEmptyCell used by computer player to make a move returns a list of int[]
	 * arrays where the computer can place the letter
	 */
	@Override
	public List<Cell> getEmptyCells() {
		List<Cell> empty = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (grid[i][j] == EMPTY) {
					empty.add(new Cell(i, j)); // add row and column coordinates to list
				}
			}
		}
		return empty; // return the empty grid for computer to make a move
	}

	/*
	 * The Board4 copy that is used by computer algorithm without modify the game
	 * board
	 */
	private GameBoard5(GameBoard5 other) {
		this.size = other.size;
		this.grid = new char[size][size];
		this.countMove = other.countMove;
		for (int i = 0; i < size; i++) {
			System.arraycopy(other.grid[i], 0, this.grid[i], 0, size);
		}
	}

	// return deep copy of the board
	@Override
	public GameBoard5 copy() {
		return new GameBoard5(this);
	}

	@Override
	public int getCountMove() {
		return countMove;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Board (").append(size).append("x").append(size).append("):\n");

		for (int i = 0; i < size; i++) {
			sb.append(i).append(i < 10 ? "  " : " ");

		}
		sb.append("\n");
		// print rows
		for (int i = 0; i < size; i++) {
			sb.append(i).append(i < 10 ? "  " : " ");
			for (int j = 0; j < size; j++) {
				char cell = grid[i][j];
				sb.append(cell == EMPTY ? '.' : cell).append(' ');
			}
			sb.append('\n');
		}
		return sb.toString();
	}

}
