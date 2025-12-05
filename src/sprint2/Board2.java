package sprint2;

public class Board2 {

	private char[][] grid;
	private char turn = 'S';
	private GameMode game;
	private int size;

	public Board2(int size) {
		this(size, GameMode.SIMPLE);
	}

	public Board2(int size, GameMode game) {
		if ((size < 3) || size > 15) {
			throw new IllegalArgumentException("The size must be between 3 and 15");
		}
		this.size = size; // initialize game size
		grid = new char[size][size];
		this.game = game; // initialize the game mode
		// reset the game
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
			return false ;

		}

		grid[row][col] = letter;
		switchTurn();
		return true;

	} // end of make move method

	private void resetGame() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				grid[i][j] = '\u0000'; // clear the grid
			}
		}
	}

	// helper method to check for valid row and col
	private boolean checkValidRowsAndColumns(int row, int col) {
		return row >= 0 && row < size && col >= 0 && col < size;
	}

	// switch turns
	public void switchTurn() {
		turn = (turn == 'S') ? 'O' : 'S';
	}

	// setters and getters
	public char[][] getGrid() {
		return grid;
	}

	public void setGrid(char[][] grid) {
		this.grid = grid;
	}

	public char getTurn() {
		return turn;
	}

	public void setTurn(char turn) {
		this.turn = turn;
	}

	public GameMode getGame() {
		return game;
	}

	public void setGame(GameMode game) {
		this.game = game;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
