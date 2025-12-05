package sprint5.view;

import java.io.File;
import java.sql.*;

import javax.swing.*;
import sprint5.controller.*;
import sprint5.mode.computerPlayer.*;
import sprint5.mode.mcts.*;

public class GameFrame extends JFrame {

	private SOSGameController controller;
	private MainPanel mainPanel;

	public GameFrame()
	{
		this (new MonteCarloStrategy());
	}
	//Constructor
	public GameFrame(ComputerStrategy strategy)
	{
		super("SOS BoardGame");
		initializeFrame();
		initializeComponents(strategy);
		finalizeFrame();

	}
	//Initialize game
	private void initializeFrame()
	{
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(true);
	}

	//Initialize components
	private void initializeComponents(ComputerStrategy strategy)
	{
		controller = new SOSGameController(this, strategy);
		mainPanel = new MainPanel(controller);
		controller.setMainPanel(mainPanel);
		add(mainPanel);

	}

	// Finalize the set up
	private void finalizeFrame() {
		// Pack and center
		pack();
		setLocationRelativeTo(null);
	}

	public MainPanel getMainPanel()
	{
		return mainPanel;
	}
	public void resetFrame()
	{
		pack();
		revalidate();
		repaint();
	}
	/** Show error during dialog*/
	public void showError(String message)
	{
		JOptionPane.showMessageDialog(this, message,
				"Error", JOptionPane.ERROR_MESSAGE);
	}

	/**Display dialog information*/
	public void showInfo(String message)
	{
		JOptionPane.showMessageDialog(this, message,
				"Info", JOptionPane.INFORMATION_MESSAGE);
	}
	/**ShowGameDialog method gives the player option
	 * @param message store the message**/
	public boolean showGameDialog(String message)
	{
		int result = JOptionPane.showConfirmDialog(this, message
				+"\nDo want to play again?",
				"Game over",
				JOptionPane.YES_NO_OPTION);
		return result == JOptionPane.YES_OPTION;
	}

	public static void main(String[]args) throws SQLException {

		
		//Set the look and feel
		configureLookAndFeel();
		SwingUtilities.invokeLater(() -> {
			GameFrame frame = new GameFrame();
			frame.setVisible(true);
		});

	}
	//Configure the application for feel and look
	private static void configureLookAndFeel()
	{
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
		   JOptionPane.showMessageDialog(null, e.getMessage(),
				   "Could not set look and feel:",
				   JOptionPane.ERROR_MESSAGE);

		}
	}

}
