package sprint0;

import javax.swing.JSpinner;

public class BoardGame {
	private char [][]board;
	private boolean turn = true;
	private int gridSize;
	
	//constructor
	public BoardGame(int gridSize)
	{
		this.gridSize = gridSize;
		board = new char[gridSize][gridSize];
		
	}
	//get the row and column
	public int getCell(int row, int column)
	{
		return board[row][column];
	}

	//return the turn
	public boolean getTurn()
	{
		return turn;
	}
	
	//setters and getters
	public char[][] getBoard() {
		return board;
	}
	public void setBoard(char[][] board) {
		this.board = board;
	}
	public int getGridSize() {
		return gridSize;
	}
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	public void setTurn(boolean turn) {
		this.turn = turn;
	}
	
	//display the board to the console
	public void printBoardToConsole()
	{
		for (int i = 0; i < gridSize; i++)
		{
			System.out.println("-".repeat(20));
			for (int j = 0; j < gridSize; j++)
			{
				System.out.print(board[i][j] + " ");
				
				if(j < gridSize - 1)
				{
				  System.out.print("|");	//add pipe between cells
				}
				
			}
			System.out.println();
			
		}
		System.out.println("-".repeat(20));
	}
	
}
