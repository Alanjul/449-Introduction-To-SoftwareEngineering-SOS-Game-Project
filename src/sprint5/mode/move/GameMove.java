package sprint5.mode.move;

import java.time.*;
import java.util.*;

public class GameMove {
	private Integer moveId;
	private final Integer gameId;
	private final int moveNumber;
	private final Integer playerId;
	private final int rowPosition;
	private final int colPosition;
	private final char letter;
	private final int sosFormed;
	private final int pointsScored;
	private final LocalDateTime moveTimestamp;

	/**
	 * Constructor for new move.
	 */
	public GameMove(Integer gameId, int moveNumber, Integer playerId, int rowPosition, int colPosition, char letter,
			int sosFormed, int pointsScored) {
		this(null, gameId, moveNumber, playerId, rowPosition, colPosition, letter, sosFormed, pointsScored,
				LocalDateTime.now());
	}

	/**
	 * Full constructor (for loading from database).
	 */
	public GameMove(Integer moveId, Integer gameId, int moveNumber, Integer playerId, int rowPosition, int colPosition,
			char letter, int sosFormed, int pointsScored, LocalDateTime moveTimestamp) {
		validateGameId(gameId);
		validatePlayerId(playerId);
		validatePosition(rowPosition, colPosition);
		validateLetter(letter);
		validatePoints(sosFormed, pointsScored);

		this.moveId = moveId;
		this.gameId = gameId;
		this.moveNumber = moveNumber;
		this.playerId = playerId;
		this.rowPosition = rowPosition;
		this.colPosition = colPosition;
		this.letter = letter;
		this.sosFormed = sosFormed;
		this.pointsScored = pointsScored;
		this.moveTimestamp = moveTimestamp != null ? moveTimestamp : LocalDateTime.now();
	}

	private void validateGameId(Integer gameId) {
		if (gameId == null)
			throw new IllegalArgumentException("Game ID cannot be null");
	}

	private void validatePlayerId(Integer playerId) {
		if (playerId == null)
			throw new IllegalArgumentException("Player ID cannot be null");
	}

	private void validatePosition(int row, int col) {
		if (row < 0 || col < 0) {
			throw new IllegalArgumentException("Position cannot be negative");
		}
	}

	private void validateLetter(char letter) {
		if (letter != 'S' && letter != 'O') {
			throw new IllegalArgumentException("Letter must be 'S' or 'O'");
		}
	}

	private void validatePoints(int sos, int points) {
		if (sos < 0 || points < 0) {
			throw new IllegalArgumentException("Points cannot be negative");
		}
	}

	// Getters
	public Integer getMoveId() {
		return moveId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public int getMoveNumber() {
		return moveNumber;
	}

	public Integer getPlayerId() {
		return playerId;
	}

	public int getRowPosition() {
		return rowPosition;
	}

	public int getColPosition() {
		return colPosition;
	}

	public char getLetter() {
		return letter;
	}

	public int getSosFormed() {
		return sosFormed;
	}

	public int getPointsScored() {
		return pointsScored;
	}

	public LocalDateTime getMoveTimestamp() {
		return moveTimestamp;
	}

	// Setter
	public void setMoveId(Integer moveId) {
		if (this.moveId != null)
			throw new IllegalStateException("Move ID already set");
		this.moveId = moveId;
	}

	// Helper
	public boolean didScore() {
		return sosFormed > 0 || pointsScored > 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GameMove))
			return false;
		GameMove that = (GameMove) o;
		return Objects.equals(moveId, that.moveId) && Objects.equals(gameId, that.gameId)
				&& moveNumber == that.moveNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(moveId, gameId, moveNumber);
	}

	@Override
	public String toString() {
		return String.format("GameMove[#%d: %c at (%d,%d), SOS=%d, Pts=%d]", moveNumber, letter, rowPosition,
				colPosition, sosFormed, pointsScored);
	}

}
