package sprint3;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class BoardGui3 extends JFrame{
	private  Game game;
	private Board3 board;
	private BoardPanel3 panel;

	public BoardGui3(Board3 board, Game game)
	{
		this.game = game;
		this.board = board;
		panel =  new BoardPanel3(this, game, board);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void resetBoard()
	{
		this.pack();
		this.revalidate();
		this.repaint();
	}

	public Game getGame() {
		return game;
	}



	public void setGame(Game game) {
		this.game = game;
	}



	public Board3 getBoard() {
		return board;
	}



	public void setBoard(Board3 board) {
		this.board = board;
	}



	public BoardPanel3 getPanel() {
		return panel;
	}



	public void setPanel(BoardPanel3 panel) {
		this.panel = panel;
	}



	public static void main(String[] args) {

		SwingUtilities.invokeLater( () ->
		{
			Board3 board = new Board3(5);
			Game game = new SimpleGame(board);

		new BoardGui3(board, game);
		});

	}

}
