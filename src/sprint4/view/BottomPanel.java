package sprint4.view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import sprint4.controller.SOSGameController;
/**The Bottom class  displays status and game contros
 * contains turn label, and New Game botton
 * */
public class BottomPanel extends JPanel{

	private SOSGameController controller;
	private JLabel currentLabel;
	private JButton newGameButton;
	
	/**Constructs the BottomPanel with a reference to controller
	 *@param controler the game controller to handle user actions */
	public BottomPanel(SOSGameController controller)
	{
		this.controller = controller;
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		
		//Label to show the current turn
		currentLabel = new JLabel("Current turn: blue (or red)"); //label
		currentLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		add(currentLabel);
		
		//add spacing between label and button
		add(Box.createHorizontalStrut(20));
		
		//New Game Button
		newGameButton = new JButton("New Game");
		newGameButton.setFont(new Font("Arial", Font.PLAIN, 14));
		add(newGameButton);
		
	}
	//Update the current label text
	public void updateCurrentTurn(String text)
	{
		currentLabel.setText(text);
	}
	/*Set new Game listener**/
	public void setNewGameListener(ActionListener listener)
	{
		for(ActionListener al: newGameButton.getActionListeners())
		{
			newGameButton.removeActionListener(al);
		}
		newGameButton.addActionListener(listener);
	}
	/**
	 * @return the newGameButton
	 */
	public JButton getNewGameButton() {
		return newGameButton;
	}	
	
}
