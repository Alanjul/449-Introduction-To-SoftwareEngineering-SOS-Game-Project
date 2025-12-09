package sprint5.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import sprint5.controller.*;
import sprint5.mode.computerPlayer.*;

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
		intializeComponents();
		layoutComponents();
		setUpListeners();

	}

	private void intializeComponents() {
		humanButton = new JRadioButton("Human", true);
		// letter selection buttons
		sButton = new JRadioButton("S", true);
		oButton = new JRadioButton("O", false);
		computerButton = new JRadioButton("Computer", true);

		// computer difficulty
		difficultyLabel = new JLabel("Difficulty");
		String[] difficulties = { "Random", "Easy", "Medium", "Hard", "Expert" };
		// create combox for selecting computer mode of play
		difficultyCombo = new JComboBox<>(difficulties);
		difficultyCombo.setSelectedItem("Medium");
		difficultyCombo.setEnabled(false);// disable when a human player is selected

	}

	private void layoutComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), playerName,
				TitledBorder.LEFT, TitledBorder.TOP));
		setPreferredSize(new Dimension(180, getHeight()));

		// Player type panel
		JPanel typePanel = createPlayerTypePanel();

		// Computer panel
		JPanel computerPanel = createDifficultyPanel();

		// add spacing
		add(Box.createVerticalStrut(10));
		add(typePanel);
		add(Box.createVerticalStrut(10));
		add(computerPanel);
		add(Box.createVerticalGlue());// push to top

	}

	// Create player selection panel
	private JPanel createPlayerTypePanel() {

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);

		ButtonGroup group = new ButtonGroup();
		group.add(humanButton);
		group.add(computerButton);
		humanButton.setAlignmentX(Component.LEFT_ALIGNMENT);// align to left
		computerButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		panel.add(humanButton);
		panel.add(Box.createVerticalStrut(10));
		panel.add(createLetterSelection());
		panel.add(Box.createVerticalStrut(10));
		panel.add(computerButton);
		return panel;

	}

	// Create the player type selection panel
	private JPanel createLetterSelection() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
		ButtonGroup letter = new ButtonGroup();
		letter.add(sButton);
		letter.add(oButton);

		sButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		oButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(sButton);
		panel.add(oButton);

		return panel;
	}

	/** Create difficult selection panel */
	private JPanel createDifficultyPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.add(difficultyLabel);
		panel.add(difficultyCombo);
		return panel;
	}

	// setup listeners
	private void setUpListeners() {
		computerButton.addActionListener(e -> {
			difficultyCombo.setEnabled(true);
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
