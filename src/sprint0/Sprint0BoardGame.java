package sprint0;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

//This is the initial set up of the board game

public class Sprint0BoardGame extends JFrame {
	private BoardGame game;
	private int gridSize; // create grid size
	private static final int CELL_SIZE = 60; //create the cell size
	private JFrame frame = new JFrame(); // create a JFrame instance
	private JPanel panel; // declaration of the panel
	private JPanel topPanel = new JPanel();
	private JPanel westPanel = new JPanel();
	private JPanel eastPanel = new JPanel();
	private JRadioButton blue1, blue2,red1, red2;
	private JPanel bottomPanel = new JPanel();
	private JLabel currentTurn;
	private JButton[][]gameBoard;
	private JTextField textField; //textField
	private JSpinner spinner; //declaration of the spinner
	private JScrollPane scrollPane; //declaration of the scrollpane

	// Create radioButtons
	private JRadioButton radio1 = new JRadioButton();
	private JRadioButton radio2 = new JRadioButton();

	private ButtonGroup group = new ButtonGroup(); //instance of the buttonGroup
	private JLabel label;//declare the JLabel

	private JCheckBox checkBox = new JCheckBox(); //instantiate the checkBox

	// Constructor
	public Sprint0BoardGame(BoardGame game) {
		this.game = game;

		// create frame structure
		frame.setTitle("Welcome to SOS Game");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setPreferredSize(new Dimension(600,600));
		// Create jspinner
		spinner = new JSpinner(new SpinnerNumberModel(game.getGridSize(), 3, 15, 1));

		panel = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				paintBoard(g);

			}

		};
		//set the panel size
		panel.setPreferredSize(new Dimension(gridSize * CELL_SIZE, gridSize * CELL_SIZE));
		scrollPane = new JScrollPane(panel); //use the scrollpane to hold the panel

		// add listener to spinner
		spinner.addChangeListener(e -> {
			int newSize = (int) spinner.getValue();
			panel.setPreferredSize(new Dimension(gridSize * CELL_SIZE, gridSize * CELL_SIZE));

			//reset the board game
			game.setGridSize(newSize);
			game.setBoard(new char[newSize][newSize]);

			// layout the panel again
			updatePanelSize();
			// repaint the panel with new grid
			panel.repaint();
			frame.pack();
			game.printBoardToConsole();

		});

		// create label
		label = new JLabel("SOS");

		// set the button text
		radio1.setText("Simple game");
		radio2.setText("General Game");

		// add the button to group button
		group.add(radio1);
		group.add(radio2);

		// add label to panel
		panel.add(label);

		//check  box
		checkBox.setText("Enable Drawing");
		checkBox.setSelected(true);

		//add to topPanel
		topPanel.add(checkBox);
		topPanel.add(label);
		topPanel.add(radio1);
		topPanel.add(radio2);
		topPanel.add(new JLabel("BoardSize"));
		topPanel.add(spinner);

		//add label to the westPanel
		westPanel.add(new JLabel("Blue Player"));

		//add label
		eastPanel.add(new JLabel("Red Player"));

		//add label to the bottom panel
		bottomPanel.add(new JLabel("Current turn: blue (or red)"));

		// create an image icon
		ImageIcon icon = new ImageIcon("sos.png");

		//add the panels to frame
		frame.add(topPanel, BorderLayout.NORTH);
		frame.add(scrollPane, BorderLayout.CENTER);
		frame.add(westPanel, BorderLayout.WEST);
		frame.add(eastPanel, BorderLayout.EAST);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.setIconImage(icon.getImage());// set the image of the frame
		frame.pack();// resize the window
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	/**paintBoard method to draw the gridlayout of the boardGame
	 * @param Graphics g to draw grid lines
	 * @return none*/
public void paintBoard(Graphics g)
{
	gridSize = game.getGridSize();
			//(int)spinner.getValue();//Get the gridSize
	int panelWidth = gridSize * CELL_SIZE; //Get the panelWidth
	int panelHeight = CELL_SIZE * gridSize; //Get the panelHeight

	// draw vertical lines
	for (int i = 0; i <= gridSize; i++) {
		int x_cordinate = i * CELL_SIZE;
		g.drawLine(x_cordinate, 0, x_cordinate, panelHeight);
	}

	// draw horizontal lines
	for (int i = 0; i <= gridSize; i++) {
		int y_cordinate = i * CELL_SIZE;
		g.drawLine(0, y_cordinate, panelWidth, y_cordinate);
	}
	g.drawLine(panelWidth - 1, 0, panelWidth - 1, panelHeight);
	g.drawLine(0, panelHeight - 1, panelWidth, panelHeight - 1);
}

	public static void main(String[] args) {
		BoardGame game = new BoardGame(8);
		SwingUtilities.invokeLater(() -> new Sprint0BoardGame(game));

	}


	//getters
	public BoardGame getGame() {
		return game;
	}

	public void setGame(BoardGame game) {
		this.game = game;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JSpinner getSpinner() {
		return spinner;
	}

	public void setSpinner(JSpinner spinner) {
		this.spinner = spinner;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	//update the panel size
	private void updatePanelSize()
	{
		int size = game.getGridSize() * CELL_SIZE;//retrieve the size
		panel.setPreferredSize(new Dimension(size, size)); //get the new dimension of the panel
		frame.pack(); //resize the frame
	}

}
