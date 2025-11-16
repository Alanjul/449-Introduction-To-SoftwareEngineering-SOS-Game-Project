package sprint4.mode;
import java.util.List;



//Board interfaces defines operations needed by game
public interface Board {

	//return cell
	char getCell(int row, int col);
	
	//place a letter
	boolean makeMove(int row, int col, char letter);
	
	boolean isEmpty(int row, int col);
	
	//Return a list of empty cell
	List<Cell> getEmptyCells();
	record Cell(int row, int col) {};
	Board copy();
	
	//return size
	int getSize();
	
	//Return true when the board is full
	boolean isFull();
	//Reset
	void resetGame();
	
	//Return number of moves made
	int getCountMove();
}
