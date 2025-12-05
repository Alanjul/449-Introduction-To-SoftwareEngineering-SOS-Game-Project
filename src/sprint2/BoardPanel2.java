package sprint2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class BoardPanel2 extends JPanel {
	private static Board2 board;
	private int gridSize; // create grid size
	private static final int CELL_SIZE = 60;
	private JPanel topPanel, bottomPanel, gamePanel;
	private JLabel label, currentLabel;
	private JSpinner boardSpinner;
	private JRadioButton simpleGameButton, generalGameButton;
	private JRadioButton blueS, blueO, redS, redO;

	// default mode
	private boolean simpleGameSelected = true;
	private BoardGui parent;
	public BoardPanel2(BoardGui parent, Board2 board) {
		BoardPanel2.board = board;
		this.parent = parent;

		this.gridSize = board.getSize(); // initialize gridsize

		// set layout
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		topPanel = createTopPanel();
		add(topPanel, BorderLayout.NORTH);
		setUpBoard();
		add(gamePanel, BorderLayout.CENTER);

		//blue player side
		JPanel bluePanel =  createPlayerPanel("Blue Player", true);
		add(bluePanel, BorderLayout.WEST);
		//red player panel
		JPanel redPanel = createPlayerPanel("Red Player", false);
        add(redPanel, BorderLayout.EAST);
		bottomPanel = createBottomPanel();
		add(bottomPanel, BorderLayout.SOUTH);

	}

	//
	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Game Settings"));
		// label
		label = new JLabel("SOS Game");
		label.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(label);

		// Game mode Button ratio
		ButtonGroup modelGroup = new ButtonGroup();
		simpleGameButton = new JRadioButton("Simple Game", true);
		generalGameButton = new JRadioButton("General Game");

		modelGroup.add(simpleGameButton);
		modelGroup.add(generalGameButton);
		// add selection listener
		ActionListener buttonListener = e -> {
			simpleGameSelected = simpleGameButton.isSelected();

			GameMode mode = simpleGameSelected ? GameMode.SIMPLE : GameMode.GENERAL;
			board.setGame(mode);

			resetGame(gridSize);

		};
		// add action listeners
		simpleGameButton.addActionListener(buttonListener);
		generalGameButton.addActionListener(buttonListener);

		panel.add(simpleGameButton);
		panel.add(generalGameButton);

		// Board size and spinner
		panel.add(new JLabel("Board Size: "));
		boardSpinner = new JSpinner(new SpinnerNumberModel(gridSize, 3, 15, 1));
		// add change listener to board spinner
		boardSpinner.addChangeListener(e -> {
			gridSize = (Integer) boardSpinner.getValue();
			resetGame(gridSize);
		});
		panel.add(boardSpinner);
		return panel;
	}

	public void resetGame(int size) {
		this.gridSize = size;
		BoardPanel2.board = new Board2(size, simpleGameSelected ? GameMode.SIMPLE : GameMode.GENERAL);
		if (gamePanel != null) {
	        remove(gamePanel);
		}
	    setUpBoard(); //rebuild the panle

	    //add the grid back to layout
	    add(gamePanel, BorderLayout.CENTER);

	    //update the layout
	    revalidate();
	    repaint();
	    parent.resetBoard();
	    updateCurrentLetter();
	    currentLabel.setText("New game started: " + gridSize + "x" + gridSize);
	}

	// create a buttom panel

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		currentLabel = new JLabel("Current turn: blue (or red");
		panel.add(currentLabel);
		return panel;
	}

	//create player panel
	private JPanel createPlayerPanel(String player, boolean isBlue) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(player));
		panel.setPreferredSize(new Dimension(120, 200));

		ButtonGroup button = new ButtonGroup();
		JRadioButton sTurnButton = new JRadioButton("S", true);
		JRadioButton oTurnButton = new JRadioButton("O", false);

		// adding radio button to buttonGroup
		button.add(sTurnButton);
		button.add(oTurnButton);
		if (isBlue) {
			blueS = sTurnButton;
			blueO = oTurnButton;
		} else {
			redS = sTurnButton;
			redO = oTurnButton;
		}
		panel.add(Box.createVerticalGlue());
		panel.add(sTurnButton);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(oTurnButton);
		panel.add(Box.createVerticalGlue());

		return panel;
	}

	// private method
	private void setUpBoard() {
		int n = gridSize;
		gamePanel = new JPanel(new GridLayout(n, n)); // create a gridlayout
		gamePanel.setPreferredSize(new Dimension(n * CELL_SIZE, n * CELL_SIZE));

		// create the board layout
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {

				//get the current current
				 char current = board.getCell(i, j);
				JButton cellButton = new JButton(current== '\0'?"": String.valueOf(current));

				cellButton.setFocusPainted(false);
				cellButton.setContentAreaFilled(false);
				cellButton.setOpaque(true);
				cellButton.setBackground(Color.WHITE);
				cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				cellButton.setFont(new Font("Arial", Font.BOLD, 20));


				final int row = i, col = j;
				cellButton.addActionListener(e -> handleClick(row, col, cellButton));//add action Listener
				gamePanel.add(cellButton);
			}
		}
	}

	// handle clicks
	private void handleClick(int row, int col, JButton btn) {
		char selected = getCurrentPlayerLetter(); // get the current letter selected
		if (btn.getText().equals("")) {

			btn.setText(String.valueOf(selected));
	        btn.setFont(new Font("Arial", Font.BOLD, 22));
	        if (board.getTurn() == 'S') { // Blue player's turn
	            btn.setForeground(Color.BLUE);
	            currentLabel.setText("Red's turn (O)");
	        } else { // Red player's turn
	            btn.setForeground(Color.RED); // red text

	            currentLabel.setText("Blue's turn (S)");

	        }
	        board.makeMove(row, col, selected);
	        btn.setEnabled(false);
		}else
		{
			currentLabel.setText("Invalid move — cell already used!");
		}
		updateCurrentLetter();

	}

	// Selecting the Letter's turn
	private char getCurrentPlayerLetter() {
		char currentLetter = board.getTurn();
		if (currentLetter == 'S') {
			return (blueO != null) && (blueO.isSelected()) ? 'O' : 'S';
		} else {
			return (redO != null) && (blueO.isSelected()) ? 'S' : 'O';
		}
	} // end of getCurrentPlayerLetter

	// update the game
	private void updateCurrentLetter() {
		char current = board.getTurn();
		currentLabel.setText("Current turn: blue (or red)" + (current == 'S' ? " Blue's turn" : " Red's turn"));
	}





}
