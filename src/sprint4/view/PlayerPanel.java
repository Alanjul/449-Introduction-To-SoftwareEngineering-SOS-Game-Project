package sprint4.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import sprint4.controller.SOSGameController;
import sprint4.mode.ComputerPlayer;

public class PlayerPanel extends JPanel {

	private SOSGameController controller;
	private String playerName;
	private boolean isBlue;

	private JRadioButton humanButton;
	private JRadioButton computerButton;
	private JRadioButton sButton;
	private JRadioButton oButton;

	// combo
	private JComboBox<String> difficultyCombo;
	private JLabel difficultyLabel;

	/** Constructor */
	public PlayerPanel(String playerName, boolean isBlue, SOSGameController controller) {
		this.controller = controller;
		this.playerName = playerName;
		this.isBlue = isBlue;

		// arrange the components vertically
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), playerName,
				TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(180, getHeight()));
		createComponents();
	}

	private void createComponents() {
		// Player type
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.Y_AXIS));

		ButtonGroup typeGroup = new ButtonGroup();
		humanButton = new JRadioButton("Human", true);
		computerButton = new JRadioButton("Computer", true);
		typeGroup.add(humanButton);
		typeGroup.add(computerButton);
		// alignment for player
		humanButton.setAlignmentX(Component.LEFT_ALIGNMENT);// align to left
		computerButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Set letter button group
		ButtonGroup letter = new ButtonGroup();
		sButton = new JRadioButton("S", true);
		oButton = new JRadioButton("O", false);
		letter.add(sButton);
		letter.add(oButton);

		// Panel for letters
		JPanel subPanel = new JPanel();
		subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
		subPanel.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

		// Alignment for buttons
		sButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		oButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		// Add button to subPanel
		subPanel.add(sButton);
		subPanel.add(oButton);

		// Add to the panel
		typePanel.add(humanButton);
		typePanel.add(subPanel);
		typePanel.add(Box.createRigidArea(new Dimension(0, 5))); // add 5 pixels of space vertically between humanButton
																	// and computerButton
		typePanel.add(computerButton);

		// create spacings
		add(Box.createVerticalStrut(10));
		add(typePanel);
		add(Box.createVerticalStrut(15));

		// difficulty level for computer
		JPanel difficultyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		difficultyLabel = new JLabel("Difficulty");
		String[] difficulties = { "Random", "Easy", "Medium", "Hard", "Expert" };

		// create combox for selecting computer mode of play
		difficultyCombo = new JComboBox<>(difficulties);
		difficultyCombo.setSelectedItem("Medium");
		difficultyCombo.setEnabled(false);

		// add the label to panel
		difficultyPanel.add(difficultyLabel);
		difficultyPanel.add(difficultyCombo);
		add(difficultyPanel);
		add(Box.createVerticalStrut(15));

		// add(letterPanel);
		add(Box.createVerticalGlue()); // Add the spacing dynamically

		// adding listeners
		computerButton.addActionListener(e -> {
			difficultyCombo.setEnabled(true);
			// Disable letter buttons because computer will select automatically
			sButton.setEnabled(false);
			oButton.setEnabled(false);
		});

		humanButton.addActionListener(e -> {
			difficultyCombo.setEnabled(false);
			sButton.setEnabled(true);
			oButton.setEnabled(true);
		});

	}

	/**
	 * @return the humanButton
	 */
	public boolean isHuman() {
		return humanButton.isSelected();
	}

	/**
	 * @return the computerButton
	 */
	public boolean isComputer() {
		return computerButton.isSelected();
	}

	/** return the selected letter */
	public char getSelectedLetter() {
		return sButton.isSelected() ? 'S' : 'O';
	}

	/**
	 * @return the difficultyLabel
	 */
	public String getDifficultyLabel() {
		return (String) difficultyCombo.getSelectedItem();
	}

	public ComputerPlayer.LevelsOfDifficulty getDifficultyEnum() {
		String diff = getDifficultyLabel();
		return switch (diff) {
		case "Random" -> ComputerPlayer.LevelsOfDifficulty.RANDOM;
		case "Easy" -> ComputerPlayer.LevelsOfDifficulty.EASY;
		case "Medium" -> ComputerPlayer.LevelsOfDifficulty.MEDIUM;
		case "Hard" -> ComputerPlayer.LevelsOfDifficulty.HARD;
		case "Expert" -> ComputerPlayer.LevelsOfDifficulty.EXPERT;
		default -> ComputerPlayer.LevelsOfDifficulty.MEDIUM;
		};
	}

	/** set the letter selection */
	public void setLetterSelection(char letter) {
		if (letter == 'S') {
			sButton.setSelected(true);
		} else {
			oButton.setSelected(true);
		}
	}

	// Enable and disable the panel
	public void setPlayerSettings(boolean enabled) {
		humanButton.setEnabled(enabled);
		computerButton.setEnabled(enabled);
		difficultyCombo.setEnabled(enabled && computerButton.isSelected());
		sButton.setEnabled(enabled && humanButton.isSelected());
		oButton.setEnabled(enabled && humanButton.isSelected());
	}

}
