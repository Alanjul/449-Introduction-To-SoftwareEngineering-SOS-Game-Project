package sprint5.view;

import java.awt.*;
import javax.swing.*;


import sprint5.controller.SOSGameController;

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
    	initializeComponents();
    	layoutComponents();

    }
    private void initializeComponents() {
    	sosLabel = new JLabel("SOS");
    	sosLabel.setFont(new Font("Arial", Font.BOLD, 20));

    	//Game mode radio Buttons
    	ButtonGroup modeGroup = new ButtonGroup();
    	simpleButton = new JRadioButton("Simple game", true);
    	generalButton = new JRadioButton("General Game", false);
    	modeGroup.add(simpleButton);
    	modeGroup.add(generalButton);

    	//Board size components
    	boardLabel = new JLabel("Board size");
    	boardSpinner = new JSpinner(new SpinnerNumberModel(5, 3, 15, 1));
    }

	//Layout Components
    private void layoutComponents()
    {
    	setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    	setBackground(Color.WHITE);
    	setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    	//add componnets
    	add(sosLabel);
    	add(simpleButton);
    	add(Box.createHorizontalStrut(15));
    	add(generalButton);
    	add(Box.createHorizontalStrut(40));
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
