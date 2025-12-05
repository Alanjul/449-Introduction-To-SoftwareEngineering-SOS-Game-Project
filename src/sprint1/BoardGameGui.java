package sprint1;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BoardGameGui extends JFrame {


	ButtonGroup gameMode;
	private BoardPanel panel;
	private Board1 board;
	public BoardGameGui(Board1 board)
	{
		this.board = board;
		this.setTitle("SOS Game");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panel = new BoardPanel(this, board);
		this.add(panel);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}



	public void resetBoard()
	{
		this.pack();
		this.revalidate();

	}

	public static void main(String[]args)
	{

        SwingUtilities.invokeLater(() -> {
        	Board1 board = new Board1(4);
        	BoardGameGui gui  = new BoardGameGui(board);});
	}


}
