package sprint3;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
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

public class BoardPanel3  extends JPanel{
	private BoardGui3 parent;
	private Game game;
	private Board3 board;

	private JRadioButton simpleButton, generalButton;
	private int gridSize;
	private static  final int CELL_SIZE = 60;

	private JPanel bottomPanel, topPanel, gamePanel, layPanel;
	private JLabel label, currentLabel, sosLabel;
	private JSpinner spinner;
	private JRadioButton blues, blueO, reds, redO;
	private JButton newGame;
	private boolean selected = true;
	private JButton[][]cellButtons ; //stores buttons for reference

	public BoardPanel3(BoardGui3 parent, Game game, Board3 board)
	{
		this.parent = parent;
		this.game = game;
		this.board = board;
		this.gridSize = board.getSize();

		//set the layout
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		//create top panel
		topPanel = createTopPanel();

		add(topPanel, BorderLayout.NORTH);
		setUpBoard();
		add(gamePanel, BorderLayout.CENTER);
		bottomPanel = createBottomPanel();
		add(bottomPanel,BorderLayout.SOUTH );

		JPanel bluePanel = createPanelPlayer("Blue player", true);
		add(bluePanel, BorderLayout.WEST);
		JPanel redPlayer =  createPanelPlayer("Red player", false);
		add(redPlayer, BorderLayout.EAST);
	}

	//create the panel for the top

	private JPanel createTopPanel()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		panel.setBorder(BorderFactory.createTitledBorder("Game setting"));
		sosLabel = new JLabel("SOS");
		sosLabel.setFont(new Font("Arial", Font.BOLD, 16));
		panel.add(sosLabel);
		ButtonGroup buttonGroup = new ButtonGroup();
		simpleButton = new JRadioButton("Simple game", true);
		generalButton = new JRadioButton("General game", false);
		buttonGroup.add(simpleButton);
		buttonGroup.add(generalButton);

		//add action listeners
		ActionListener modeListener = e ->
		{
			GameMode3 selectedMode = simpleButton.isSelected() ? GameMode3.SIMPLE : GameMode3.GENERAL;
			applyGameMode(selectedMode);
		};
		simpleButton.addActionListener(modeListener);
		generalButton.addActionListener( modeListener);
		panel.add(simpleButton);
		panel.add(Box.createHorizontalStrut(30));//adds 30px spacings  between simple button and generalButton
		panel.add(generalButton);
		panel.add(Box.createHorizontalStrut(100)); //adds 100px spacing between the generalButton and BoardSize

		label = new JLabel("Board size");
		//Create a spinner with a innitial value of 3 to 15 with a step of 1
		panel.add(label);
		spinner = new JSpinner( new SpinnerNumberModel(gridSize,3, 15, 1));
		spinner.addChangeListener(e -> {
			gridSize = (Integer) spinner.getValue();
			resetGame(gridSize);
		});
		panel.add(spinner);
		return panel;
	}

	private void applyGameMode(GameMode3 mode)
	{
		if(mode == GameMode3.SIMPLE)
		{
			game  = new SimpleGame(board);
		}else
		{
			game = new GeneralGame(board);
		}
		parent.setGame(game); // set the game
		resetGame(gridSize); //reset the grid
	}

	private JPanel createBottomPanel()
	{
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		currentLabel = new JLabel ("Current Turn: Blue or Red: ");
		currentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		panel.add(currentLabel);

		//add spacings between the current label and the next word
		panel.add(Box.createHorizontalStrut(20));

		newGame = new JButton("New Game");
		newGame.addActionListener(e ->
			resetGame(gridSize));

		panel.add(newGame);
		return panel;
	}

	private JPanel createPanelPlayer(String player, boolean isBlue)
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder(player));
		panel.setPreferredSize(new Dimension(200, 100));

		ButtonGroup group = new ButtonGroup();
		JRadioButton sTurn = new JRadioButton("S");//, true);
		JRadioButton oTurn = new JRadioButton("O");//, false);
		group.add(sTurn);
		group.add(oTurn);

		if(isBlue) //blue player turn
		{
			sTurn.setSelected(true);
			blues = sTurn;
			blueO = oTurn;
		}else //red player turn
		{
			sTurn.setSelected(true);
			reds = sTurn;
			redO = oTurn;
		}
		panel.add(Box.createVerticalGlue()); //add vertical empty spacing
		panel.add(sTurn);
		//add spacing  button two vertical radio buttons
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(oTurn);
		panel.add(Box.createVerticalGlue());

		return panel;
	}
	private void resetGame(int size)
	{


		if (gamePanel != null) {
			remove(gamePanel);
		}
		board = new Board3(size);

		//game mode
		GameMode3 mode = simpleButton.isSelected() ? GameMode3.SIMPLE : GameMode3.GENERAL;

		if(mode == GameMode3.SIMPLE)
		{
			game = new SimpleGame(board);
		}else
		{
			game = new GeneralGame(board);
		}

		parent.setGame(game);
		setUpBoard();
		add(gamePanel, BorderLayout.CENTER);

		//repaint
		revalidate();
		repaint();
		parent.resetBoard();
		currentLabel.setText("New " +  mode + " started! Current Turn: Blue");

	}

	//create the board layout
	private void setUpBoard() {
		int n = gridSize;
		//main container for the grid
		JPanel container = new JPanel(null);
		container.setPreferredSize(new Dimension(n * CELL_SIZE , n * CELL_SIZE));
		JPanel gridPanel  = new JPanel(new GridLayout(n, n));

		gridPanel.setBounds(0, 0, n * CELL_SIZE, n * CELL_SIZE);

		cellButtons = new JButton[n][n];

		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				char currentLetter = board.getCell(i, j);
				JButton cellButton = new JButton(currentLetter == '\0'?"": String.valueOf(currentLetter));

				cellButton.setFocusPainted(false);
				cellButton.setContentAreaFilled(false);
				cellButton.setOpaque(true);
				cellButton.setBackground(Color.WHITE);
				cellButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				cellButton.setFont(new Font("Arial", Font.BOLD, 20));

				int row = i, col= j;
				cellButton.addActionListener(e ->  handleClicks(row, col, cellButton));

				cellButtons[i][j] = cellButton; //store the buttons for reference
				gridPanel.add(cellButton);
			}
		}
		 layPanel = new JPanel()
				{
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawSosLines(g);
			}
		};
		layPanel.setBounds(0, 0, n * CELL_SIZE, n * CELL_SIZE);
		layPanel.setOpaque(false);
		container.add(gridPanel);
		container.add(layPanel);

		container.setComponentZOrder(layPanel, 0);  // 0 = topmost
		container.setComponentZOrder(gridPanel, 1); // 1 = below layPanel
		this.gamePanel = container;
	}// end of the setUpBoard method

	//draw SOS lines
	private void drawSosLines(Graphics g)
	{
		Graphics2D  g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//set the thickness of the line
		g2d.setStroke(new BasicStroke(3.0f)); // set the thickness of the line draw

		//draw the line
		for (LineSegment line : board.getSosLineFormed())
		{
			g2d.setColor(line.getColor());

			//Calculate the pixels coordinates for  line endpoinst
			int y1 = line.getStartRow() * CELL_SIZE  + CELL_SIZE /2;
			int x1 = line.getStartCol() * CELL_SIZE + CELL_SIZE/2;
			int y2 = line.getEndRow() * CELL_SIZE + CELL_SIZE /2;
			int x2 = line.getEndCol() * CELL_SIZE + CELL_SIZE/2;

			g2d.drawLine(x1, y1, x2, y2);
		}
	}
	//handle clicks
	private void handleClicks(int row, int col, JButton btn )
	{
		//check if the game is over
		if(game.gameOver) {
			currentLabel.setText("Game is over! Click the New Game Button to start over");
		return;
		}
		//check if the btn is occupied
		if(!btn.getText().equals(""))
		{
			currentLabel.setText("The Cell is occupied and can't be Overwritten");
			return;
		}

		//get the letter of the current player
		char currentPlayer = game.getCurrentTurn();
		//determine player color
		Color playerColor = currentPlayer == 'B' ? Color.BLUE : Color.RED;

		//select the current player
		char selected = getSelectedLetter(currentPlayer);

		//make move
		game.makeMove(row, col, selected);
		//update buttons
		btn.setText(String.valueOf(selected));
		//set the fonts of the selected player
		btn.setFont(new Font("Arial", Font.BOLD, 20));
		btn.setForeground(playerColor);

		btn.setEnabled(false); //prevent further click

		detectAndDrawSOSLine(row, col, playerColor);
		if (layPanel != null) {
	        layPanel.revalidate();
	        layPanel.repaint();
	        gamePanel.repaint();
	    }
		 displayTheCurrentLabel();
	}

	//get the current player
	private char getCurrentPlayer()
	{
		char current = game.getCurrentTurn();
		return current;
	}


	//draw sosLine
	private void detectAndDrawSOSLine(int row, int col, Color playerColor)
	{
		//get the current cell
		char currentCellLetter = board.getCell(row, col);
		if(currentCellLetter == '\0') {
			return;
		}
		//check for directions
		int[][]directions = {
				{0,1}, //right
				{1,0},// down
				{-1,0},//up
				{0, -1},//left
				{1,1}, //down right
				{-1,1},//up right
				{1,-1}, //down left
				{-1,-1} // up left
		};
		//placed S to check for SOS formation
		if(currentCellLetter == 'S')
		{
			// computing the cell  and next one
			for (int [] dir : directions)
			{
				int row1 = row + dir[0];
				int col1 = col + dir[1];
				int row2 = row + 2* dir[0];
				int col2 = col + 2 * dir[1];

				if (isValidCell(row1, col1) && isValidCell(row2, col2))
				{
					if(board.getCell(row1, col1) == 'O' && board.getCell(row2, col2) == 'S'){
						//draw the line from S to S to form a SOS
						board.addSosLine(new LineSegment(row, col, row2, col2, playerColor));
					}
				}
			}// end of for statement
			//placed O  to check for  SOS formation
		} else if (currentCellLetter == 'O')
		{
			for (int []axis : directions)
			{
				int row1 = row - axis[0];
				int col1 = col - axis[1];
				int row2 = row + axis[0];
				int col2 = col + axis[1];

				//check if there are valid
				if(isValidCell(row1, col1) &&  isValidCell(row2, col2))
				{
					char start = board.getCell(row1, col1);
	                char end = board.getCell(row2, col2);

					if (board.getCell(row1, col1) == 'S' && board.getCell(row2, col2) == 'S')
					{
						LineSegment line = new LineSegment(row1, col1, row2, col2, playerColor);
						board.addSosLine(line);
					}
				}
			}

		}

	}//end of detectAndDrawSOSLine method
	//check if the cells are valid
	private boolean isValidCell(int row,  int col)
	{
		return row >= 0 && row < gridSize && col >= 0 && col < gridSize;
	} //end of isValidCellMethod

	//updateing the turn
	private void displayTheCurrentLabel()
	{
		if (game.isGameOver())
		{
			currentLabel.setText(game.getGameResult());
			return ;
		}

		//current turn
		char turn = game.getCurrentTurn();
		String player = (turn == 'B') ? "Blue" : "Red";
			//diclare a winner
			if( game.getMode()== GameMode3.GENERAL && game instanceof  GeneralGame general) //check if it general game mode
			{
				//display the score
				currentLabel.setText("Turn: " + player + " Score - Blue"+ general.getBlueScore()
				+ ", Red " + general.getRedScore());
			}else
			{
				currentLabel.setText("Current Turn: " + player);
		}
	}
	//helper method to select the letter based on radio buttons
	private char getSelectedLetter(char currentPlayer)
	{
		if (currentPlayer == 'B')
		{
			return blues.isSelected() ? 'S' : 'O';
		}else
		{
			return reds.isSelected() ? 'O' : 'S';
		}
	}
} //end of class
