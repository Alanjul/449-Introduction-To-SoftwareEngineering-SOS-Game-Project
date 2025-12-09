package sprint5.view;

import java.awt.*;

import java.awt.event.*;
import javax.swing.*;

import sprint5.controller.SOSGameController;

/**
 * The Bottom class displays status and game contros contains turn label, and
 * New Game bottom
 */
public class BottomPanel extends JPanel {

	private SOSGameController controller;
	private JLabel currentTurnLabel;
	private JButton newGameButton;
	private JCheckBox recordGameCheckBox;
	private JButton replayButton;

	/**
	 * Constructs the BottomPanel with a reference to controller
	 * 
	 * @param controler the game controller to handle user actions
	 */
	public BottomPanel(SOSGameController controller) {
		this.controller = controller;
		initializeComponent();
		layoutComponents();

	}

	private void initializeComponent() {
		recordGameCheckBox = new JCheckBox("Record game");
		recordGameCheckBox.setFont(new Font("Arial", Font.PLAIN, 14));

		// current turn label
		currentTurnLabel = new JLabel("Current turn: blue (or red)");
		currentTurnLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		// replay button
		replayButton = new JButton("Replay");
		replayButton.setFont(new Font("Arial", Font.PLAIN, 14));
		replayButton.setEnabled(false);// disable until game is recorded

		// new Button
		newGameButton = new JButton("New Game");
		newGameButton.setFont(new Font("Arial", Font.PLAIN, 14));

	}

	private void layoutComponents() {
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		add(recordGameCheckBox);
		add(Box.createHorizontalStrut(30));
		add(currentTurnLabel);
		add(Box.createHorizontalStrut(30));

		// Button
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(replayButton);
		buttonPanel.add(Box.createVerticalStrut(5));
		buttonPanel.add(newGameButton);
		add(buttonPanel);
	}

	// Update the current label text
	public void updateCurrentTurn(String text) {
		currentTurnLabel.setText(text);
	}

	/* Set new Game listener **/
	public void setNewGameListener(ActionListener listener) {
		for (ActionListener al : newGameButton.getActionListeners()) {
			newGameButton.removeActionListener(al);
		}
		newGameButton.addActionListener(listener);
	}

	// set Listener for record game
	public void setRecordGameListener(ActionListener listener) {
		for (ActionListener al : recordGameCheckBox.getActionListeners()) {
			recordGameCheckBox.removeActionListener(al);// remove duplicates
		}
		recordGameCheckBox.addActionListener(listener);
	}

	// set Listener for replay game
	public void setReplayGameListener(ActionListener listener) {
		for (ActionListener al : replayButton.getActionListeners()) {
			replayButton.removeActionListener(al);// remove duplicates
		}
		replayButton.addActionListener(listener);
	}

	/*
	 * enable record return true if record is checked
	 */
	public boolean isRecordGameEnabled() {
		return recordGameCheckBox.isSelected();
	}

	/**
	 * @return the controller
	 */
	public SOSGameController getController() {
		return controller;
	}

	/**
	 * @return the currentTurnLabel
	 */
	public JLabel getCurrentTurnLabel() {
		return currentTurnLabel;
	}

	/**
	 * @return the replayButton
	 */
	public JButton getReplayButton() {
		return replayButton;
	}

	/**
	 * @param recordGameCheckBox the recordGameCheckBox to set
	 */
	public void setRecordGameCheckBox(boolean selected) {
		recordGameCheckBox.setSelected(selected);
	}

	/**
	 * @param enabled to enable if true, false to disable
	 */
	public void setReplayButton(boolean enabled) {
		this.replayButton.setEnabled(enabled);
	}

}
