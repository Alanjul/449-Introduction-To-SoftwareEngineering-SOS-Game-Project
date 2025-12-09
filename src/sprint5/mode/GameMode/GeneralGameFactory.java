package sprint5.mode.GameMode;

import sprint5.mode.board.Board;
import sprint5.mode.player.Player;
import sprint5.util.*;

/** This class creates General mode game */
public class GeneralGameFactory implements GameFactory {

	private final PatternDetector patternDetector;
	private final GameOverStrategy gameOverStrategy;

	// Constructor
	public GeneralGameFactory() {
		this.patternDetector = new BoardDetector();
		this.gameOverStrategy = new GeneralGame();
	}

	public GeneralGameFactory(PatternDetector patternDetector, GameOverStrategy gameOverStrategy) {
		if (patternDetector == null || gameOverStrategy == null) {
			throw new IllegalArgumentException("They cannot be null");
		}
		this.patternDetector = patternDetector;
		this.gameOverStrategy = gameOverStrategy;
	}

	@Override
	public Game4 createGame(Board board, Player bluePlayer, Player redPlayer) {
		return new Game4(board, bluePlayer, redPlayer, GameMode4.GENERAL, patternDetector, gameOverStrategy);
	}

	@Override
	public GameMode4 getGameMode() {
		return GameMode4.GENERAL;
	}
}
