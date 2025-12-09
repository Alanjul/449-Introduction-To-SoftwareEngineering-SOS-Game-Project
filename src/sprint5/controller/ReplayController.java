package sprint5.controller;

import sprint5.mode.GameMode.*;
import sprint5.mode.move.*;

public class ReplayController {
	private final GameReplay replay;
	private int currentMoveIndex;
	private boolean isPlaying;
	private int playbackSpeed;

	public ReplayController(GameReplay replay) {
		if (replay == null) {
			throw new IllegalArgumentException("Replay cannot be null");
		}
		this.replay = replay;
		this.currentMoveIndex = -1;
		this.isPlaying = false;
		this.playbackSpeed = 1000;
	}

	public int getRemainingMoves() {
		return replay.getTotalMoves() - currentMoveIndex - 1;
	}

	// Speed preset
	public void setSpeedPreset(String speed) {
		switch (speed.toUpperCase()) {
		case "SLOW":
			playbackSpeed = 2000;
			break;
		case "NORMAL":
			playbackSpeed = 1000;
			break;
		case "FAST":
			playbackSpeed = 500;
			break;
		default:
			throw new IllegalArgumentException("Invalid speed preset: " + speed);
		}
	}

	/**
	 * Get the replay data.
	 */
	public GameReplay getReplay() {
		return replay;
	}

	/**
	 * Get current move index.
	 */
	public int getCurrentMoveIndex() {
		return currentMoveIndex;
	}

	/**
	 * Get current move (null if at start or end).
	 */
	public GameMove getCurrentMove() {
		if (currentMoveIndex < 0 || currentMoveIndex >= replay.getTotalMoves()) {
			return null;
		}
		return replay.getMoves().get(currentMoveIndex);
	}

	/**
	 * Step forward one move
	 * 
	 * @return the move that was played, or null if at end
	 */
	public GameMove stepForward() {
		if (hasNext()) {
			currentMoveIndex++;
			return getCurrentMove();
		}
		return null;
	}

	/**
	 * Step backward one move.
	 * 
	 * @return the move that was undone, or null if at start
	 */
	public GameMove stepBackward() {
		if (hasPrevious()) {
			GameMove move = getCurrentMove();
			currentMoveIndex--;
			return move;
		}
		return null;
	}

	/**
	 * Jump to a specific move
	 * 
	 * @param moveIndex the move index
	 * @return true if successful
	 */
	public boolean jumpToMove(int moveIndex) {
		if (moveIndex >= -1 && moveIndex < replay.getTotalMoves()) {
			currentMoveIndex = moveIndex;
			return true;
		}
		return false;
	}

	/**
	 * Jump to start (before first move).
	 */
	public void jumpToStart() {
		currentMoveIndex = -1;
	}

	/**
	 * Jump to end (after last move).
	 */
	public void jumpToEnd() {
		currentMoveIndex = replay.getTotalMoves() - 1;
	}

	/**
	 * Check if there's a next move.
	 */
	public boolean hasNext() {
		return currentMoveIndex < replay.getTotalMoves() - 1;
	}

	/**
	 * Check if there's a previous move.
	 */
	public boolean hasPrevious() {
		return currentMoveIndex >= 0;
	}

	/**
	 * Get current percentage
	 * 
	 * @return percentage (0-100)
	 */
	public double getProgressPercentage() {
		if (replay.getTotalMoves() == 0) {
			return 100.0;
		}
		return ((currentMoveIndex + 1) * 100.0) / replay.getTotalMoves();
	}

	/**
	 * Get current movNumber
	 * 
	 * @return current move number, or 0 if at start
	 */
	public int getCurrentMoveNumber() {
		return currentMoveIndex + 1;
	}

	/**
	 * Get total moves.
	 */
	public int getTotalMoves() {
		return replay.getTotalMoves();
	}

	/**
	 * Check if at start
	 */
	public boolean isAtStart() {
		return currentMoveIndex < 0;
	}

	/**
	 * Check if at end.
	 */
	public boolean isAtEnd() {
		return currentMoveIndex >= replay.getTotalMoves() - 1;
	}

	/**
	 * Start playing automatically.
	 */
	public void play() {
		isPlaying = true;
	}

	/**
	 * Pause automatic playback.
	 */
	public void pause() {
		isPlaying = false;
	}

	// Toggle between play and pause
	public void togglePlayPause() {
		isPlaying = !isPlaying;
	}

	/**
	 * Check if currently playing.
	 */
	public boolean isPlaying() {
		return isPlaying;
	}

	// Set
	public void setPlaybackSpeed(int milliseconds) {
		if (milliseconds < 100) {
			throw new IllegalArgumentException("Speed must be at least 100ms");
		}
		this.playbackSpeed = milliseconds;
	}

	// Get playback speed
	public int getPlaybackSpeed() {
		return playbackSpeed;
	}

	@Override
	public String toString() {
		return String.format("ReplayController[game=%d, move=%d/%d, playing=%s]", replay.getGameId(),
				getCurrentMoveNumber(), getTotalMoves(), isPlaying);
	}

	public String getStatus() {
		return String.format("Move %d/%d (%.0f%%) - %s", getCurrentMoveNumber(), getTotalMoves(),
				getProgressPercentage(), isPlaying ? "Playing" : "Paused");
	}

}
