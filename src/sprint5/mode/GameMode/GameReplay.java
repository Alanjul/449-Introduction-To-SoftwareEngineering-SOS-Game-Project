package sprint5.mode.GameMode;

import java.time.*;
import java.util.*;

import sprint5.mode.dao.*;
import sprint5.mode.move.*;
import sprint5.mode.player.*;

public class GameReplay {
	private final GameRecord game;
	private final PlayerRecord bluePlayer;
	private final PlayerRecord redPlayer;
	private final List<GameMove> moves;

	public GameReplay(GameRecord game, PlayerRecord bluePlayer, PlayerRecord redPlayer, List<GameMove> moves) {
		if (game == null)
			throw new IllegalArgumentException("Game cannot be null");
		if (bluePlayer == null)
			throw new IllegalArgumentException("Blue player cannot be null");
		if (redPlayer == null)
			throw new IllegalArgumentException("Red player cannot be null");
		if (moves == null)
			throw new IllegalArgumentException("Moves cannot be null");

		this.game = game;
		this.bluePlayer = bluePlayer;
		this.redPlayer = redPlayer;
		this.moves = new ArrayList<>(moves);

		// Sort moves by move number
		this.moves.sort((m1, m2) -> Integer.compare(m1.getMoveNumber(), m2.getMoveNumber()));
	}

	// Getters
	public Integer getGameId() {
		return game.getGameId();
	}

	public String getGameMode() {
		return game.getGameMode().name();
	}

	public GameMode4 getGameModeEnum() {
		return game.getGameMode();
	}

	public int getBoardSize() {
		return game.getBoardSize();
	}

	public int getBlueScore() {
		return game.getBlueScore();
	}

	public int getRedScore() {
		return game.getRedScore();
	}

	public Integer getWinnerId() {
		return game.getWinnerId();
	}

	public String getGameStatus() {
		return game.getGameStatus().name();
	}

	public GameStatus getGameStatusEnum() {
		return game.getGameStatus();
	}

	public LocalDateTime getCreatedAt() {
		return game.getStartedAt();
	}

	public LocalDateTime getCompletedAt() {
		return game.getCompletedAt();
	}

	public Long getDurationSeconds() {
		return game.getDurationSeconds();
	}

	public boolean isCompleted() {
		return game.isCompleted();
	}

	public PlayerRecord getBluePlayer() {
		return bluePlayer;
	}

	public PlayerRecord getRedPlayer() {
		return redPlayer;
	}

	public String getBluePlayerName() {
		return bluePlayer.getName();
	}

	public String getRedPlayerName() {
		return redPlayer.getName();
	}

	public Integer getBluePlayerId() {
		return bluePlayer.getPlayerId();
	}

	public Integer getRedPlayerId() {
		return redPlayer.getPlayerId();
	}

	/**
	 * Get all moves
	 */
	public List<GameMove> getMoves() {
		return Collections.unmodifiableList(moves);
	}

	// Check if there are any moves
	public boolean hasMoves() {
		return !moves.isEmpty();
	}

	// Get a specific move by index
	public GameMove getMove(int index) {
		return moves.get(index);
	}

	public boolean isBlueHuman() {
		return getBluePlayerType() == PlayerType.HUMAN;
	}

	public boolean isRedHuman() {
		return getRedPlayerType() == PlayerType.HUMAN;
	}

	/**
	 * Get total number of moves.
	 */
	public int getTotalMoves() {
		return moves.size();
	}

	// Get winner name
	public String getWinnerName() {
		if (game.getWinnerId() == null) {
			return "Draw";
		}

		if (game.getWinnerId().equals(bluePlayer.getPlayerId())) {
			return bluePlayer.getName();
		} else if (game.getWinnerId().equals(redPlayer.getPlayerId())) {
			return redPlayer.getName();
		}

		return "Unknown";
	}

	/**
	 * Get Winner symbol
	 */
	public char getWinnerSymbol() {
		if (game.getWinnerId() == null) {
			return 'D';
		}

		if (game.getWinnerId().equals(bluePlayer.getPlayerId())) {
			return 'B';
		} else if (game.getWinnerId().equals(redPlayer.getPlayerId())) {
			return 'R';
		}

		return 'D';
	}

	// Check draw
	public boolean isDraw() {
		return game.getWinnerId() == null;
	}

	/**
	 * Check blue Win
	 */
	public boolean didBlueWin() {
		return game.getWinnerId() != null && game.getWinnerId().equals(bluePlayer.getPlayerId());
	}

	// Check red
	public boolean didRedWin() {
		return game.getWinnerId() != null && game.getWinnerId().equals(redPlayer.getPlayerId());
	}

	// Summary
	public String getSummary() {
		return String.format("Game #%d: %s vs %s | %s Mode | Board: %dx%d | Score: %d-%d | Winner: %s | Moves: %d",
				getGameId(), getBluePlayerName(), getRedPlayerName(), getGameMode(), getBoardSize(), getBoardSize(),
				getBlueScore(), getRedScore(), getWinnerName(), getTotalMoves());
	}

	// Get information for the game
	public String getDetailedInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("** Game Details **\n");
		sb.append(String.format("Game ID: %d\n", getGameId()));
		sb.append(String.format("Mode: %s\n", getGameMode()));
		sb.append(String.format("Board Size: %dx%d\n", getBoardSize(), getBoardSize()));
		sb.append(String.format("\nBlue Player: %s (%s)\n", getBluePlayerName(), isBlueHuman() ? "Human" : "Computer"));
		sb.append(String.format("Red Player: %s (%s)\n", getRedPlayerName(), isRedHuman() ? "Human" : "Computer"));
		sb.append(String.format("\nScore: Blue %d - Red %d\n", getBlueScore(), getRedScore()));
		sb.append(String.format("Winner: %s\n", getWinnerName()));
		sb.append(String.format("\nTotal Moves: %d\n", getTotalMoves()));
		sb.append(String.format("Duration: %d seconds\n", getDurationSeconds()));
		sb.append(String.format("Created: %s\n", getCreatedAt()));
		sb.append(String.format("Completed: %s\n", getCompletedAt()));

		return sb.toString();
	}

	public String getMatchDescription() {
		return String.format("%s vs %s", getBluePlayerName(), getRedPlayerName());
	}

	public String getScoreDisplay() {
		return String.format("%d - %d", getBlueScore(), getRedScore());
	}

	public boolean isValid() {

		if (game == null || bluePlayer == null || redPlayer == null) {
			return false;
		}

		if (!game.isCompleted()) {
			return false;
		}

		if (!game.getBluePlayerId().equals(bluePlayer.getPlayerId())
				|| !game.getRedPlayerId().equals(redPlayer.getPlayerId())) {
			return false;
		}

		// Check moves are sequential
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).getMoveNumber() != i + 1) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return getSummary();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof GameReplay))
			return false;

		GameReplay other = (GameReplay) obj;
		return game.getGameId().equals(other.game.getGameId());
	}

	@Override
	public int hashCode() {
		return game.getGameId().hashCode();
	}

	public PlayerType getBluePlayerType() {
		return PlayerType.fromCode(bluePlayer.getPlayerType());
	}

	public PlayerType getRedPlayerType() {
		return PlayerType.fromCode(redPlayer.getPlayerType());
	}

	public String getRedPlayerDisplay() {
		return redPlayer.getName() + " (" + getRedPlayerType().getShortName() + ")";
	}

	public String getBluePlayerDisplay() {
		return bluePlayer.getName() + " (" + getBluePlayerType().getShortName() + ")";
	}
}