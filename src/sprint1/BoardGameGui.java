package sprint1;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class BoardGameGui extends JFrame {

	
	ButtonGroup gameMode;
	private BoardPanel panel;
	private Board board;
	public BoardGameGui(Board board)
	{
		this.board = board;
		this.setTitle("SOS Game");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        	Board board = new Board(4);
        	BoardGameGui gui  = new BoardGameGui(board);});
	}
	

}
