package sprint5.mode.board;
import java.util.List;



//Board interfaces defines operations needed by game
public interface Board {
	char getCell(int row, int col);
	boolean makeMove(int row, int col, char letter);

	boolean isEmpty(int row, int col);
	List<Cell> getEmptyCells();
	record Cell(int row, int col) {}//representing board positions
	Board copy();
	int getSize();
	boolean isFull();
	void resetGame();
	int getCountMove();
}
