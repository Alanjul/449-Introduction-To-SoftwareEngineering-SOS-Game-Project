package sprint4.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import sprint4.controller.SOSGameController;

public class TopPanel extends JPanel {

	private SOSGameController controller;
	
	//Add labels, radioButton, spinner
	private JLabel sosLabel;
    private JRadioButton simpleButton;
    private JRadioButton generalButton;
    private JLabel boardLabel;
    private JSpinner boardSpinner;
    
    public TopPanel(SOSGameController controller)
    {
    	this.controller = controller;
    	setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	setBorder(BorderFactory.createTitledBorder(
    			BorderFactory.createLineBorder(Color.BLUE),
    			"SOS Game",
    			TitledBorder.LEFT,
    			TitledBorder.TOP));
    	sosLabel = new JLabel("SOS");
    	sosLabel.setFont(new Font("Arial", Font.BOLD, 20));
    	add(sosLabel);
    	
    	//Group for radio buttons
    	ButtonGroup modeGroup = new ButtonGroup();
    	simpleButton = new JRadioButton("Simple game", true);
    	generalButton = new JRadioButton("General Game", false);
    	modeGroup.add(simpleButton);
    	modeGroup.add(generalButton);
    	
    	add(simpleButton);
    	add(Box.createHorizontalStrut(30));//horizontal spacing
    	add(generalButton);
    	add(Box.createHorizontalStrut(100));
    	boardLabel = new JLabel("Board size");
    	boardSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 15, 1));
    	add(boardLabel);
    	add(boardSpinner);
    	
    	
    }

	

	
	/**
	 * @return the simpleButton selected
	 */
	public boolean getSimpleMode() {
		return simpleButton.isSelected();
	}
	
	/**
	 * @return the generalButton selected
	 */
	public boolean getGeneralMode() {
		return generalButton.isSelected();
	}

	/**
	 * @return the generalButton
	 */
	public JRadioButton getGeneralButton() {
		return generalButton;
	}
   
	 //Get the simple button
	public JRadioButton getSimpleButton() {
		return simpleButton;
	}
	/**
	 * @return the boardLabel
	 */
	public JLabel getBoardLabel() {
		return boardLabel;
	}

	/**
	 * @return the boardSpinner
	 */
	 public int getBoardSize() {
	        return (Integer) boardSpinner.getValue();
	    }
	 public JSpinner getBoardSpinner() {
	        return boardSpinner;
	    }
	
	 //Enable and disable button settings
	 public void setSettingsEnabled(boolean enabled) {
	        simpleButton.setEnabled(enabled);
	        generalButton.setEnabled(enabled);
	        boardSpinner.setEnabled(enabled);
	    }
    
}
