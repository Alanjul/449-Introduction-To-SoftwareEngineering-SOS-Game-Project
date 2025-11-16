package sprint4.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import sprint4.controller.SOSGameController;

public class MainPanel extends JPanel {

	private SOSGameController controller;
	
	//sub-Panels
	private TopPanel topPanel;
	private PlayerPanel bluePlayerPanel;
	private PlayerPanel redPlayerPanel;
	private GameBoardPanel boardPanel;
	private BottomPanel bottomPanel;
	
	/**Constructor to setup the panel*/
	public MainPanel(SOSGameController controller)
	{
		this.controller = controller;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		//Create panels
		topPanel = new TopPanel(controller);
		bluePlayerPanel = new PlayerPanel("Blue Player", true, controller);
		boardPanel = new GameBoardPanel(controller);
		redPlayerPanel = new PlayerPanel("Red player", false, controller);
		bottomPanel = new BottomPanel(controller);
		
		//add panels
		add(topPanel, BorderLayout.NORTH);
        add(bluePlayerPanel, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(redPlayerPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
		
		
	}
	
	
	//Reset panels
	
	/**
	 * @return the topPanel
	 */
	public TopPanel getTopPanel() {
		return topPanel;
	}


	/**
	 * @return the bluePlayerPanel
	 */
	public PlayerPanel getBluePlayerPanel() {
		return bluePlayerPanel;
	}


	/**
	 * @return the redPlayerPanel
	 */
	public PlayerPanel getRedPlayerPanel() {
		return redPlayerPanel;
	}


	/**
	 * @return the boardPanel
	 */
	public GameBoardPanel getBoardPanel() {
		return boardPanel;
	}


	/**
	 * @return the bottomPanel
	 */
	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}

	public void resetPanels(int boardSize)
	{
		boardPanel.createBoard(boardSize);
		revalidate();
		repaint();
	}
}
