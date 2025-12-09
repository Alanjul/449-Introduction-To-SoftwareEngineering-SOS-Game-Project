package sprint5.mode.GameMode;

import java.time.*;
import java.util.*;

import sprint5.mode.player.*;

public class GameRecord {
	private Integer gameId;
	private final Integer bluePlayerId;
	private final Integer redPlayerId;
	private final GameMode4 gameMode;
	private final int boardSize;

	private int blueScore;
	private int redScore;
	private Integer winnerId;
	private GameStatus gameStatus;
	private int totalMoves;
	private Long durationSeconds;

	private final LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private static final int MIN_BOARD_SIZE = 3;
	private static final int MAX_BOARD_SIZE = 15;

	// Constructor for new game
	public GameRecord(Builder b) {
		Validator.playerType(b.bluePlayerType);
		Validator.playerType(b.redPlayerType);
		Validator.boardSize(b.boardSize);
		Validator.mode(b.gameMode);
		Validator.scores(b.blueScore, b.redScore);

		this.gameId = b.gameId;
		this.bluePlayerId = b.bluePlayerId;
		this.redPlayerId = b.redPlayerId;

		this.gameMode = b.gameMode;
		this.boardSize = b.boardSize;

		this.blueScore = b.blueScore;
		this.redScore = b.redScore;
		this.winnerId = b.winnerId;

		this.gameStatus = b.gameStatus != null ? b.gameStatus : GameStatus.IN_PROGRESS;

		this.totalMoves = b.totalMoves;
		this.durationSeconds = b.durationSeconds;
		this.startedAt = b.startedAt != null ? b.startedAt : LocalDateTime.now();
		this.completedAt = b.completedAt;
	}

	// Helper class
	public static class Builder {
		private Integer gameId;
		private Integer bluePlayerId;
		private Integer redPlayerId;

		private PlayerType bluePlayerType;
		private PlayerType redPlayerType;

		private GameMode4 gameMode;
		private int boardSize;

		private int blueScore = 0;
		private int redScore = 0;
		private Integer winnerId;
		private GameStatus gameStatus = GameStatus.IN_PROGRESS;

		private int totalMoves = 0;
		private Long durationSeconds;
		private LocalDateTime startedAt;
		private LocalDateTime completedAt;

		public Builder(Integer bluePlayerId, Integer redPlayerId, PlayerType blueType, PlayerType redType,
				GameMode4 mode, int boardSize) {
			this.bluePlayerId = bluePlayerId;
			this.redPlayerId = redPlayerId;
			this.bluePlayerType = blueType;
			this.redPlayerType = redType;
			this.gameMode = mode;
			this.boardSize = boardSize;
		}

		public Builder gameId(Integer id) {
			this.gameId = id;
			return this;
		}

		public Builder startedAt(LocalDateTime t) {
			this.startedAt = t;
			return this;
		}

		public Builder completedAt(LocalDateTime t) {
			this.completedAt = t;
			return this;
		}

		public Builder scores(int blue, int red) {
			this.blueScore = blue;
			this.redScore = red;
			return this;
		}

		public Builder winner(Integer id) {
			this.winnerId = id;
			return this;
		}

		public Builder status(GameStatus s) {
			this.gameStatus = s;
			return this;
		}

		public Builder totalMoves(int m) {
			this.totalMoves = m;
			return this;
		}

		public Builder durationSeconds(Long sec) {
			this.durationSeconds = sec;
			return this;
		}

		public GameRecord build() {
			return new GameRecord(this);
		}
	}

	private static class Validator {
		static void playerType(PlayerType type) {
			if (type == null) {
				throw new IllegalArgumentException("Player type cannot be null");
			}
		}

		static void mode(GameMode4 mode) {
			if (mode == null) {
				throw new IllegalArgumentException("Game mode cannot be null");
			}
		}

		static void boardSize(int size) {
			if (size < MIN_BOARD_SIZE || size > MAX_BOARD_SIZE) {
				throw new IllegalArgumentException(
						String.format("Board size must be %d - %d", MIN_BOARD_SIZE, MAX_BOARD_SIZE));
			}
		}

		static void scores(int blue, int red) {
			if (blue < 0 || red < 0) {
				throw new IllegalArgumentException("Scores cannot be negative");
			}
		}
	}

	public void complete(Integer winnerId, int blueScore, int redScore) {
		Validator.scores(blueScore, redScore);

		this.winnerId = winnerId;
		this.blueScore = blueScore;
		this.redScore = redScore;
		this.gameStatus = GameStatus.COMPLETED;
		this.completedAt = LocalDateTime.now();

		long seconds = Duration.between(startedAt, completedAt).getSeconds();
		this.durationSeconds = seconds;
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

	public GameMode4 getGameMode() {
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

	public GameStatus getGameStatus() {
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

	@Override
	public int hashCode() {
		return Objects.hash(gameId);
	}

	// Status helper
	public boolean isCompleted() {
		return gameStatus == GameStatus.COMPLETED;
	}

	public boolean isInProgress() {
		return gameStatus == GameStatus.IN_PROGRESS;
	}

	public boolean isAbandoned() {
		return gameStatus == GameStatus.ABANDONED;
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
	public String toString() {
		return String.format("GameRecord[id=%d, mode=%s, status=%s, score=%d-%d]", gameId, gameMode, gameStatus,
				blueScore, redScore);
	}
}
