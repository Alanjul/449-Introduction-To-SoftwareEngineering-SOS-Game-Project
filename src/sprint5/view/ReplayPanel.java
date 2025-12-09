package sprint5.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.List;
import sprint5.controller.*;
import sprint5.mode.GameMode.*;
import sprint5.mode.board.*;
import sprint5.mode.move.*;
import sprint5.util.*;

public class ReplayPanel extends JPanel {
	private final ReplayController controller;
	private final Board board;
	private final GameBoardPanel boardPanel;
	private Timer playTimer;

	private final PatternDetector patternDetector;
	private final List<LineSegment4> allLines;
	// UI Components
	private JButton playPauseButton;
	private JButton stepBackButton;
	private JButton stepForwardButton;
	private JButton jumpStartButton;
	private JButton jumpEndButton;
	private JButton exitButton;
	private JSlider progressSlider;
	private JLabel statusLabel;
	private JLabel moveInfoLabel;
	private JLabel gameInfoLabel;
	private JComboBox<String> speedComboBox;

	public ReplayPanel(GameReplay replay) {
		this.controller = new ReplayController(replay);
		this.controller.setSpeedPreset("NORMAL");
		this.board = new GameBoard5(replay.getBoardSize());
		this.boardPanel = new GameBoardPanel(null);
		this.boardPanel.createBoard(replay.getBoardSize());
		this.patternDetector = new BoardDetector();
		this.allLines = new ArrayList<>();

		setLayout(new BorderLayout(10, 10));
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		initComponents();
		setupTimer();
		refreshReplayUI();
		addHierarchyListener(e -> {
			if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
				if (!isShowing()) {
					cleanup();
				}
			}
		});
	}

	private void cleanup() {
		if (playTimer != null && playTimer.isRunning()) {
			playTimer.stop();
			playTimer = null;
		}
		allLines.clear();
	}

	private void initComponents() {
		add(createGameInfoPanel(), BorderLayout.NORTH);
		add(boardPanel, BorderLayout.CENTER);
		add(createControlsPanel(), BorderLayout.SOUTH);
	}

	// Update playback speed
	private void updateSpeed() {
		String selected = (String) speedComboBox.getSelectedItem();

		if (selected.contains("Slow"))
			controller.setSpeedPreset("SLOW");
		else if (selected.contains("Normal"))
			controller.setSpeedPreset("NORMAL");
		else if (selected.contains("Fast"))
			controller.setSpeedPreset("FAST");

		playTimer.setDelay(controller.getPlaybackSpeed());
	}

	private void stepForward() {
		if (!controller.hasNext())
			return;

		controller.stepForward(); // FIRST advance
		GameMove move = controller.getCurrentMove();

		applyMove(move);
		refreshReplayUI();
	}

	private void stepBackward() {
		if (!controller.hasPrevious())
			return;

		controller.stepBackward();
		replayAllMoves();
		refreshReplayUI();
	}

	private void jumpToStart() {
		controller.jumpToStart();
		replayAllMoves();
		refreshReplayUI();
	}

	private void togglePlayPause() {
		controller.togglePlayPause();

		if (controller.isPlaying())
			playTimer.start();
		else
			playTimer.stop();

		updatePlayPauseButton();
	}

	private void jumpToEnd() {
		controller.jumpToEnd();
		replayAllMoves();
		refreshReplayUI();
	}

	private void jumpToMove(int index) {
		controller.jumpToMove(index);
		replayAllMoves();
		refreshReplayUI();
	}

	// REF @ChatGPT
	private JPanel createGameInfoPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Game Information"));

		GameReplay replay = controller.getReplay();

		gameInfoLabel = new JLabel(
				String.format("<html><b>Game #%d:</b> %s (Blue) vs %s (Red) | Mode: %s | Board: %dx%d</html>",
						replay.getGameId(), replay.getBluePlayerName(), replay.getRedPlayerName(), replay.getGameMode(),
						replay.getBoardSize(), replay.getBoardSize()));

		statusLabel = new JLabel("Ready to replay");

		panel.add(gameInfoLabel);
		panel.add(statusLabel);

		return panel;
	}

	private JPanel createControlsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBorder(BorderFactory.createTitledBorder("Playback Controls"));

		panel.add(createButtonsPanel());
		panel.add(Box.createVerticalStrut(10));
		panel.add(createProgressPanel());
		panel.add(Box.createVerticalStrut(10));
		panel.add(createMoveInfoPanel());

		return panel;
	}

	// Provided by CHATGPT
	private JPanel createButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

		jumpStartButton = new JButton("⏮");
		jumpStartButton.setToolTipText("Jump to start");
		jumpStartButton.addActionListener(e -> jumpToStart());

		stepBackButton = new JButton("◀");
		stepBackButton.setToolTipText("Step backward");
		stepBackButton.addActionListener(e -> stepBackward());

		playPauseButton = new JButton("▶");
		playPauseButton.setToolTipText("Play/Pause");
		playPauseButton.addActionListener(e -> togglePlayPause());

		stepForwardButton = new JButton("⏵");
		stepForwardButton.setToolTipText("Step forward");
		stepForwardButton.addActionListener(e -> stepForward());

		jumpEndButton = new JButton("⏭");
		jumpEndButton.setToolTipText("Jump to end");
		jumpEndButton.addActionListener(e -> jumpToEnd());

		// Exit button
		exitButton = new JButton("Exit Replay");
		exitButton.setToolTipText("Close replay and return to main board");
		exitButton.addActionListener(e -> closeReplay());

		JLabel speedLabel = new JLabel("Speed:");
		String[] speeds = { "Slow (2s)", "Normal (1s)", "Fast (0.5s)" };
		speedComboBox = new JComboBox<>(speeds);
		speedComboBox.setSelectedIndex(1);
		speedComboBox.addActionListener(e -> updateSpeed());

		panel.add(jumpStartButton);
		panel.add(stepBackButton);
		panel.add(playPauseButton);
		panel.add(stepForwardButton);
		panel.add(jumpEndButton);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(speedLabel);
		panel.add(speedComboBox);
		panel.add(Box.createHorizontalStrut(20));
		panel.add(exitButton);

		return panel;
	}

	private JPanel createProgressPanel() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));

		JLabel progressLabel = new JLabel("Progress:");

		int totalMoves = controller.getTotalMoves();
		progressSlider = new JSlider(0, totalMoves, 0);
		progressSlider.setMajorTickSpacing(Math.max(1, totalMoves / 10));
		progressSlider.setMinorTickSpacing(1);
		progressSlider.setPaintTicks(true);
		progressSlider.setPaintLabels(true);

		progressSlider.addChangeListener(e -> {
			if (!progressSlider.getValueIsAdjusting()) {
				jumpToMove(progressSlider.getValue());
			}
		});

		panel.add(progressLabel, BorderLayout.WEST);
		panel.add(progressSlider, BorderLayout.CENTER);

		return panel;
	}

	private JPanel createMoveInfoPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		moveInfoLabel = new JLabel("Move: 0 / " + controller.getTotalMoves());
		panel.add(moveInfoLabel);
		return panel;
	}

	private void setupTimer() {
		playTimer = new Timer(controller.getPlaybackSpeed(), e -> {
			if (controller.hasNext()) {
				stepForward();
			} else {
				controller.pause();
				updatePlayPauseButton();
			}
		});
	}

	private void applyMove(GameMove move) {
		if (move == null)
			return;

		Color color = move.getPlayerId().equals(controller.getReplay().getBluePlayerId()) ? Color.BLUE : Color.RED;
		board.makeMove(move.getRowPosition(), move.getColPosition(), move.getLetter());
		List<LineSegment4> newLines = patternDetector.findPatterns(board, move.getRowPosition(), move.getColPosition(),
				color);
		allLines.addAll(newLines);

		boardPanel.updateCell(move.getRowPosition(), move.getColPosition(), move.getLetter(), color);
		boardPanel.updateBoard(board);
		boardPanel.updateSosLines(allLines);
	}

	private void replayAllMoves() {
		board.resetGame();
		boardPanel.clearBoard();
		allLines.clear();

		GameReplay replay = controller.getReplay();
		int currentIndex = controller.getCurrentMoveIndex();

		for (int i = 0; i <= currentIndex && i < replay.getTotalMoves(); i++) {
			GameMove move = replay.getMove(i);
			Color color = move.getPlayerId().equals(replay.getBluePlayerId()) ? Color.BLUE : Color.RED;
			board.makeMove(move.getRowPosition(), move.getColPosition(), move.getLetter());

			List<LineSegment4> newLines = patternDetector.findPatterns(board, move.getRowPosition(),
					move.getColPosition(), color);

			allLines.addAll(newLines);
			boardPanel.updateCell(move.getRowPosition(), move.getColPosition(), move.getLetter(), color);
		}

		boardPanel.updateBoard(board);
		boardPanel.updateSosLines(allLines);
	}

	private void refreshReplayUI() {
		progressSlider.setValue(controller.getCurrentMoveIndex());

		moveInfoLabel.setText(String.format("Move: %d / %d (%.0f%%)", controller.getCurrentMoveIndex(),
				controller.getTotalMoves(), controller.getProgressPercentage()));

		if (controller.getCurrentMoveIndex() == 0) {
			statusLabel.setText("At start");
		} else if (controller.isAtEnd()) {
			GameReplay replay = controller.getReplay();
			String winnerText;
			if (replay.isDraw()) {
				winnerText = "DRAW";
			} else {
				winnerText = replay.getWinnerName() + " WINS!";
			}
			statusLabel.setText(String.format("Replay completed - %s (Blue: %d, Red: %d)", winnerText,
					replay.getBlueScore(), replay.getRedScore()));
		} else {
			GameMove move = controller.getCurrentMove();
			if (move != null) {
				String player = move.getPlayerId().equals(controller.getReplay().getBluePlayerId()) ? "Blue" : "Red";

				statusLabel.setText(String.format("Move %d: %s played %c at (%d, %d) - SOS: %d",
						controller.getCurrentMoveNumber(), player, move.getLetter(), move.getRowPosition(),
						move.getColPosition(), move.getSosFormed()));
			}
		}

		jumpStartButton.setEnabled(!controller.isAtStart());
		stepBackButton.setEnabled(controller.hasPrevious());
		stepForwardButton.setEnabled(controller.hasNext());
		jumpEndButton.setEnabled(!controller.isAtEnd());

		updatePlayPauseButton();
	}

	private void updatePlayPauseButton() {
		if (controller.isPlaying()) {
			playPauseButton.setText("⏸");
			playPauseButton.setToolTipText("Pause");
		} else {
			playPauseButton.setText("▶");
			playPauseButton.setToolTipText("Play");
		}
	}

	private void closeReplay() {
		if (playTimer != null && playTimer.isRunning()) {
			playTimer.stop();
		}
		Window window = SwingUtilities.getWindowAncestor(this);
		if (window != null) {
			window.dispose();
		}
	}

	public void dispose() {
		if (playTimer != null)
			playTimer.stop();
		allLines.clear();
	}
}
