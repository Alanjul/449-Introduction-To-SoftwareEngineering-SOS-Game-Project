package sprint2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class BoardGui extends JFrame {
	private BoardPanel2 panel;
	private Board2 board;
	private JLabel label ;
	
	public BoardGui(Board2 board)
	{
		setTitle("Welcome to SOS game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.board = board;
		panel = new BoardPanel2(this, board);
		add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void resetBoard()
	{
		this.pack();
		this.revalidate();
	}
	
}
