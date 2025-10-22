package sprint1;



public class Board1{
	private char[][] grid;
	private char turn = 'S';
	private int size;//initialize the  board size

	public Board1(int size) {
		this.size = size;
		grid = new char [size][size];
		 turn = 'S';
	}

	/**GetCell returns the grid cell
	 * @param row, column
	 * @return char grid[row][colum] or  '-' if invalid numbers are provided*/
	public char getCell(int row, int column) {
		if (row >= 0 && row < size && column >= 0 && column < size)
			return grid[row][column];
		else 
			return '-';
	}

	//returns the turn
	public char getTurn() {
		return turn;
	}
	public int getGridSize()
	{
		return grid.length;
	}
	 public void setCell(int row, int col, char value) {
		 if(row >= 0 && row < size && col >= 0 && col < size)
	        grid[row][col] = value;
	    }
	
//switch other player 
	 public void switchTurn()
	 {
		 turn = (turn == 'S') ? 'O' : 'S';
	 }

	 
}
