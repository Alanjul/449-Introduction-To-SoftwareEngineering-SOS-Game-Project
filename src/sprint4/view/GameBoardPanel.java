package sprint4.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import sprint3.LineSegment;
import sprint4.controller.SOSGameController;
import sprint4.mode.Board;
import sprint4.mode.Board4;
import sprint4.mode.LineSegment4;

public class GameBoardPanel extends JPanel{
	private SOSGameController  controller;
	private int gridSize;
	private static  final int CELL_SIZE = 60;
	private JButton[][] cellButtons;
	private JPanel gridPanel;
	private JPanel overLayPanel;
	private List<LineSegment4>sosLinesFormed;
	private Board board;
	
	//Constructor
	public GameBoardPanel(SOSGameController controller)
	{
		this.controller = controller;
		this.gridSize = 5;
		
		//set the layout for absolute position
		setLayout(null);
		setBackground(Color.WHITE);
		
		//create the board
		createBoard(gridSize);
	}
	
	//Create Game board
	public void createBoard(int size)
	{
		this.gridSize = size;
		
		//remove all components
		removeAll();
		
		//set size
		setPreferredSize(new Dimension(size * CELL_SIZE, size * CELL_SIZE));
		
		//Create a grid panel
		gridPanel = new JPanel(new GridLayout(size, size));
		//set bounds with x, y, width, and height
		gridPanel.setBounds(0, 0, size * CELL_SIZE, size * CELL_SIZE);
		
		cellButtons = new JButton[size][size];
		
		//loop through the board as create buttons
		for (int row = 0; row < size; row++)
		{
			for(int col = 0; col < size; col++)
			{
				JButton cellButton = createCellButton(row, col);
				cellButtons[row][col] = cellButton;
				gridPanel.add(cellButton);
			}
		}
		overLayPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawSosLines(g);
            }
        };
        overLayPanel.setBounds(0, 0, size * CELL_SIZE, size * CELL_SIZE);
        overLayPanel.setOpaque(false);
        
        // Add panels (overlay on top)
        add(gridPanel);
        add(overLayPanel);
        
        // Set z-order
        setComponentZOrder(overLayPanel, 0); // Top
        setComponentZOrder(gridPanel, 1);    // Bottom
        
        revalidate();
        repaint();
	}
	
	//Create cell Button
	private JButton  createCellButton(int row, int col)
	{
		JButton button = new JButton("");
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setOpaque(true);
		button.setBackground(Color.WHITE);
		button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		button.setFont(new Font("Arial", Font.BOLD, 20));
		
		button.addActionListener( e ->
		{
			if(controller != null)
			{
				controller.handleCellClick(row, col, button);
			}
		
		});
		return button; 
	}
	
	/***/
	public void updateBoard(Board board2)
	{
		for (int row = 0; row < gridSize; row++)
		{
			for(int col = 0; col < gridSize; col++)
			{
				char cell = board2.getCell(row, col);
				JButton button = cellButtons[row][col];
				if(cell == '\0')
				{
					button.setText("");
					button.setEnabled(true);
					button.setForeground(Color.BLACK);
				}else
				{
					button.setText(String.valueOf(cell));//get the text at that cell
					button.setEnabled(false);
				}
			}
		}
		revalidate();
		repaint();
	}
	/**Update SOS Lines*/
	public void updateSosLines(List<LineSegment4>lines)
	{
		this.sosLinesFormed = lines;
		if(overLayPanel != null)
		{
			overLayPanel.repaint();
		}
	}
	
	//Draw SOS
	private void drawSosLines(Graphics g)
	{
		//no lines formed
		if(sosLinesFormed == null || sosLinesFormed.isEmpty())
		{
			return ;
		}
		Graphics2D  g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//set the thickness of the line
		g2d.setStroke(new BasicStroke(3.0f)); 
		
		//draw the line
		for (LineSegment4 line : sosLinesFormed)
		{
			g2d.setColor(line.getColor());
			
			//Calculate the pixels coordinates for  line endpoint
			int y1 = line.getStartRow() * CELL_SIZE  + CELL_SIZE /2;
			int x1 = line.getStartCol() * CELL_SIZE + CELL_SIZE/2;
			int y2 = line.getEndRow() * CELL_SIZE + CELL_SIZE /2;
			int x2 = line.getEndCol() * CELL_SIZE + CELL_SIZE/2;
			
			g2d.drawLine(x1, y1, x2, y2);
		}
	}
	//Clear the board
	public void clearBoard()
	{
		for (int row = 0; row < gridSize; row++)
		{
			for (int col = 0; col < gridSize; col++) {
				cellButtons[row][col].setText("");
				//enable the buttons
				cellButtons[row][col].setEnabled(true);
				//Set the foreground color
				cellButtons[row][col].setForeground(Color.BLACK);
			}
		}
		sosLinesFormed = null;
		if(overLayPanel != null)
		{
			overLayPanel.repaint();//remove previous drawn lines
		}
	}
	//Update specific cell
	public void updateCell(int row, int col, char letter, Color color)
	{
		if(row >= 0 && row < gridSize && col >= 0 &&  col < gridSize) {
			JButton button = cellButtons[row][col];
			button.setText(String.valueOf(letter));//get the letter
			//Set foreground color
			button.setForeground(color);
			//Disable the button after to prevent overwriting
			button.setEnabled(false);
			
		}
	}
	//Get the cell buttons
	public JButton getCellButton(int row, int col)
	{
		if(row >= 0 && row < gridSize && col >= 0 &&  col < gridSize)
		{
			return cellButtons[row][col];
		}
		return null;
	}

	/**
	 * @return the gridSize
	 */
	public int getGridSize() {
		return gridSize;
	}

	
	
}
