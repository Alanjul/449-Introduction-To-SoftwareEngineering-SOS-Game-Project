package sprint1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class BoardPanel extends JPanel {
	private int gridSize; // create grid size
	private static final int CELL_SIZE = 60; 
	private JPanel topPanel, bottomPanel, gamePanel;
	private JLabel label, currentLabel;
	private JSpinner boardSpinner;
	private JRadioButton simpleGameButton, generalGameButton;
	private JRadioButton blueS, blueO, redS, redO;
	//default mode
	private boolean simpleGameSelected = true;
	private Board board;
	private BoardGameGui parent;
	
	public BoardPanel(BoardGameGui parent,Board board)
	{
		this.parent = parent;
		this.board = board;
		this.gridSize = board.getGridSize();
		//this.setBackground(Color.WHITE);
		//create board layout
		this.setLayout(new BorderLayout());
		
		
		//initialize top panel
		topPanel =createTopPanel();
		this.add(topPanel, BorderLayout.NORTH);
		
		
		//initialize buttom panel
		bottomPanel =createBottomPanel();
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		
		setupBoardPanel();
		add(gamePanel, BorderLayout.CENTER);
		JPanel bluePlayerPanel = createPlayerPanel("Blue player", true);
		this.add(bluePlayerPanel, BorderLayout.WEST);
		
		JPanel redPlayerPanel = createPlayerPanel("Red player", false);
		this.add(redPlayerPanel, BorderLayout.EAST);
		
		
	}

	private JPanel createTopPanel()
	{
		JPanel panel  = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Game Settings"));
		
		//label
		label = new JLabel("SOS");
		label.setFont(new Font("Arial", Font.BOLD, 12));
		panel.add(label);
		
		//Game  mode Button ratio
		ButtonGroup button = new ButtonGroup();
		simpleGameButton = new JRadioButton("Simple Game", true);
		generalGameButton = new JRadioButton("General Game");
		
		button.add(simpleGameButton);
		button.add(generalGameButton);
		
		//add selection listener
		ActionListener buttonListener= e -> {
			simpleGameSelected = simpleGameButton.isSelected();
			resetGame(gridSize);
			
		};
		
		//add action listeners
		simpleGameButton.addActionListener(buttonListener);
		generalGameButton.addActionListener(buttonListener);
		
		panel.add(simpleGameButton);
		panel.add(generalGameButton);
		
		//Board size and spinner
		panel.add(new JLabel("Board Size: "));
		boardSpinner = new JSpinner(new SpinnerNumberModel(gridSize,3, 15, 1));
		//add change listener to board spinner
		boardSpinner.addChangeListener(e ->{
			gridSize = (Integer) boardSpinner.getValue();
			resetGame(gridSize);
		});
		
		//add to panel
		panel.add(boardSpinner);
		
		
		
		return panel;
	}
	
	//rest Game
	public  void resetGame(int size)
	{
		this.gridSize = size;
	   this.board = new Board(size);
	   
	   int newSize = size * CELL_SIZE;
	   gamePanel.setPreferredSize(new Dimension(newSize, newSize));
	   gamePanel.revalidate();// re-layout if needed
	   gamePanel.repaint();
	   parent.resetBoard();//resize the parent panel
	   updateCurrentTurn();
	}
	
	
	public void paintBoard(Graphics g)
	{
		gridSize = board.getGridSize();
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
	}
	
	private void setupBoardPanel() {
	    gamePanel = new JPanel() {
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            paintBoard(g);
	        }

	        @Override
	        public Dimension getPreferredSize() {
	            int size = gridSize * CELL_SIZE;
	            return new Dimension(size, size);
	        }
	    };

	    gamePanel.setBackground(Color.WHITE);
	    
	}
	

	 private JPanel createBottomPanel()
	 {
		 JPanel bottom = new JPanel(new BorderLayout());
		 
		 //spacing
		 bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		 
		 //create header panel
		 
		 //title
		 JCheckBox recordCheck =  new JCheckBox("Record Game");
		 recordCheck.setFont(new Font ("Arial", Font.BOLD,14));
		 JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		 leftPanel.add(recordCheck);
		  bottom.add(leftPanel, BorderLayout.WEST);
	
			
		 
			JPanel rightPanel = new JPanel(new FlowLayout());
			rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
			rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			
			JButton replayGame = new JButton("Replay Game ");
			replayGame.setAlignmentX(Component.CENTER_ALIGNMENT);
			replayGame.addActionListener(e -> resetGame(gridSize));
			rightPanel.add(replayGame);
			
			//create vertical spacing
			rightPanel.add(Box.createVerticalStrut(5));
			
			//center the turn label
			currentLabel = new JLabel("Current Turn: Blue (or Red)");
			currentLabel.setFont(new Font("Arial", Font.BOLD, 14));
			JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			centerPanel.add(currentLabel);
			bottom.add(centerPanel, BorderLayout.CENTER);
			
			JButton gameButton = new JButton("New Game");
			gameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			gameButton.addActionListener(e -> resetGame(gridSize));
			rightPanel.add(gameButton);
			
			//bottom.add(recordCheck);
			//bottom.add(currentLabel);
			//bottom.add(gameButton);
			bottom.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			bottom.add(rightPanel, BorderLayout.EAST);
		 
		 return bottom;
	 }
	 
	
	 
	 
	 
	 private JPanel createPlayerPanel(String player, boolean isBlue)
	 {
		 JPanel panel = new JPanel();
		 panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		 panel.setBorder(BorderFactory.createTitledBorder(player));
		 panel.setPreferredSize(new Dimension(120,200));
		 
		 ButtonGroup button = new ButtonGroup();
		 JRadioButton sTurnButton = new JRadioButton("S", true);
		 JRadioButton oTurnButton = new JRadioButton("O", false);
		 
		 //adding radio button to buttonGroup
		 button.add(sTurnButton);
		 button.add(oTurnButton);
		 if(isBlue)
		 {
			 blueS = sTurnButton;
			 blueO = oTurnButton;
		 }else
		 {
			 redS = sTurnButton;
			 redO=  oTurnButton;
		 }
		 panel.add(Box.createVerticalGlue());
	     panel.add(sTurnButton);
	     panel.add(Box.createRigidArea(new Dimension(0, 10)));
	     panel.add(oTurnButton);
	     panel.add(Box.createVerticalGlue());
		 
		 return panel;
	 }
	 
	 //helper method for current turn label
	 private void updateCurrentTurn()
	 {
		 if(currentLabel != null)
		 {
			 currentLabel.setText("Current Turn: Blue");
		 }
	 }
	 
	 
	 //Getters
	 public boolean isSimpleGameSelected() {
			return simpleGameSelected;
		}
		
		public char getBluePlayerChoice() {
			return blueS.isSelected() ? 'S' : 'O';
		}
		
		public char getRedPlayerChoice() {
			return redS.isSelected() ? 'S' : 'O';
		}
		
		public Board getBoard() {
			return board;
		}
	 
}


