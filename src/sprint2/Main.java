package sprint2;

import javax.swing.SwingUtilities;


public class Main {

	public static void main(String[] args) {
		
		SwingUtilities.invokeLater(()-> {
			Board2 board = new Board2(3);
			BoardGui gui = new BoardGui(board);
		});

	}

}
