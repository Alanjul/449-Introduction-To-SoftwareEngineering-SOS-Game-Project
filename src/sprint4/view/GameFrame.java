package sprint4.view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import sprint4.controller.SOSGameController;
import sprint4.mode.*;

public class GameFrame extends JFrame {
	
	private SOSGameController controller;
	private MainPanel mainPanel;
	
	//Constructor
	public GameFrame()
	{
		super("SOS Board Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		//Create controller
		ComputerStrategy computerStrategy = new  MonteCarloStrategy();
		controller = new SOSGameController(this, computerStrategy);
		mainPanel = new MainPanel(controller);
		
		//connect the controller to pannel
		controller.setMainPanel(mainPanel);
		
		//add the panel to frame
		add(mainPanel);
		
		//Pack and center
		pack();
		setLocationRelativeTo(null);
		
	}
	//Constructor
	public GameFrame(ComputerStrategy strategy)
	{
		super("SOS BoardGame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		//Create controller
		ComputerStrategy computerStrategy = new MonteCarloStrategy();
		controller = new SOSGameController(this, computerStrategy);
		mainPanel = new MainPanel(controller);
		
		//connect to mainPanel
		controller.setMainPanel(mainPanel);
		add(mainPanel);
		
		//Pack and center
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
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**Display dialog information*/
	public void showInfo(String message)
	{
		JOptionPane.showMessageDialog(this, message, "Info", JOptionPane.INFORMATION_MESSAGE);
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
	
	public static void main(String[]args) {
		
		//Set the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e)
		{
		   JOptionPane.showMessageDialog(null, e.getMessage(),"Could not set look and feel:",
				   JOptionPane.ERROR_MESSAGE);
			
		}
		SwingUtilities.invokeLater(() -> {
			ComputerStrategy strategy = new MonteCarloStrategy();
			GameFrame frame = new GameFrame();
			frame.setVisible(true);
		});
		
	}

}
