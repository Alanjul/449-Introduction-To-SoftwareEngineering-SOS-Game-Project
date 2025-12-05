package sprint5.mode.GameMode;

import java.time.*;
import java.util.*;

public class GameRecord {
	private Integer gameId;
	private final Integer bluePlayerId;
	private final Integer redPlayerId;
	private final String gameMode;
	private final int boardSize;

	private int blueScore;
	private int redScore;
	private Integer winnerId;
	private String gameStatus;
	private int totalMoves;
	private Long durationSeconds;

	private final LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private final int MIN_BOARD_SIZE = 3;
	private final int MAX_BOARD_SIZE = 15;

	// Constructor for new game
	public GameRecord(Integer bluePlayerId, Integer redPlayerId, String gameMode, int boardSize) {
		this(null, bluePlayerId, redPlayerId, gameMode, boardSize, 0, 0, null, "IN_PROGRESS", 0, null,
				LocalDateTime.now(), null);
	}

	// Full constructor
	public GameRecord(Integer gameId, Integer bluePlayerId, Integer redPlayerId, String gameMode, int boardSize,
			int blueScore, int redScore, Integer winnerId, String gameStatus, int totalMoves, Long durationSeconds,
			LocalDateTime startedAt, LocalDateTime completedAt) {
		validatePlayers(bluePlayerId, redPlayerId);
		validateGameMode(gameMode);
		validateBoardSize(boardSize);
		validateGameStatus(gameStatus);
		validateScores(blueScore, redScore);

		this.gameId = gameId;
		this.bluePlayerId = bluePlayerId;
		this.redPlayerId = redPlayerId;
		this.gameMode = gameMode;
		this.boardSize = boardSize;
		this.blueScore = blueScore;
		this.redScore = redScore;
		this.winnerId = winnerId;
		this.gameStatus = gameStatus != null ? gameStatus : "IN_PROGRESS";
		this.totalMoves = totalMoves;
		this.durationSeconds = durationSeconds;
		this.startedAt = startedAt != null ? startedAt : LocalDateTime.now();
		this.completedAt = completedAt;
	}

	private void validatePlayers(Integer blue, Integer red) {
		if (blue == null || red == null) {
			throw new IllegalArgumentException("Player IDs cannot be null");
		}
		if (blue.equals(red)) {
			throw new IllegalArgumentException("Blue and red players must be different");
		}
	}

	// validate the mode
	private void validateGameMode(String mode) {
		if (mode == null || (!mode.equals("SIMPLE") && !mode.equals("GENERAL"))) {
			throw new IllegalArgumentException("Game mode must be 'SIMPLE' or 'GENERAL'");
		}
	}

	private void validateBoardSize(int size) {
		if (size < MIN_BOARD_SIZE || size > MAX_BOARD_SIZE) {
			throw new IllegalArgumentException(
					String.format("Board size must be %d - %d", MIN_BOARD_SIZE, MAX_BOARD_SIZE));
		}

	}

	private void validateScores(int blue, int red) {
		if (blue < 0 || red < 0) {
			throw new IllegalArgumentException("Scores cannot be negative");
		}
	}

	private void validateGameStatus(String status) {
		if (status != null && !status.equals("IN_PROGRESS") && !status.equals("COMPLETED")
				&& !status.equals("ABANDONED")) {
			throw new IllegalArgumentException("Invalid game status");
		}
	}

	// Getters
	public Integer getGameId() {
		return gameId;
	}

	public Integer getBluePlayerId() {
		return bluePlayerId;
	}

	public Integer getRedPlayerId() {
		return redPlayerId;
	}

	public String getGameMode() {
		return gameMode;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public int getBlueScore() {
		return blueScore;
	}

	public int getRedScore() {
		return redScore;
	}

	public Integer getWinnerId() {
		return winnerId;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public int getTotalMoves() {
		return totalMoves;
	}

	public Long getDurationSeconds() {
		return durationSeconds;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	// setters
	public void setGameId(Integer gameId) {
		if (this.gameId != null)
			throw new IllegalStateException("Game ID already set");
		this.gameId = gameId;
	}

	public void setBlueScore(int blueScore) {
		validateScores(blueScore, this.redScore);
		this.blueScore = blueScore;
	}

	public void setRedScore(int redScore) {
		validateScores(this.blueScore, redScore);
		this.redScore = redScore;
	}

	public void setWinnerId(Integer winnerId) {
		this.winnerId = winnerId;
	}

	public void setGameStatus(String gameStatus) {
		validateGameStatus(gameStatus);
		this.gameStatus = gameStatus;
	}

	public void setTotalMoves(int totalMoves) {
		this.totalMoves = totalMoves;
	}

	public void setDurationSeconds(Long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}

	public void setCompletedAt(LocalDateTime completedAt) {
		this.completedAt = completedAt;
	}

	// complete the game
	public void complete(Integer winnerId, int blueScore, int redScore) {
		this.winnerId = winnerId;
		this.blueScore = blueScore;
		this.redScore = redScore;
		this.gameStatus = "COMPLETED";
		this.completedAt = LocalDateTime.now();

		if (this.startedAt != null) {
			long seconds = java.time.Duration.between(this.startedAt, this.completedAt).getSeconds();
			this.durationSeconds = (Long) seconds;
		}
	}

	// helper
	public boolean isCompleted() {
		return "COMPLETED".equals(gameStatus);
	}

	public boolean isInProgress() {
		return "IN_PROGRESS".equals(gameStatus);
	}

	public boolean isAbandoned() {
		return "ABANDONED".equals(gameStatus);
	}

	public boolean isDraw() {
		return isCompleted() && winnerId == null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof GameRecord))
			return false;
		GameRecord that = (GameRecord) o;
		return Objects.equals(gameId, that.gameId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameId);
	}

	@Override
	public String toString() {
		return String.format("GameRecord[id=%d, mode=%s, status=%s, score=%d-%d]", gameId, gameMode, gameStatus,
				blueScore, redScore);
	}

}
