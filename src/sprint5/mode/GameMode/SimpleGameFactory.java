package sprint5.mode.GameMode;

import sprint5.mode.board.*;
import sprint5.mode.player.*;
import sprint5.util.*;


/*SimpleGameFactory creates simple mode game*/
public class SimpleGameFactory implements GameFactory {
	private final PatternDetector patternDetector;
	private final GameOverStrategy gameOverStrategy;
	
	public SimpleGameFactory()
	{
		this.patternDetector = new BoardDetector();
		this.gameOverStrategy = new SimpleGame();
	}
	 public SimpleGameFactory(PatternDetector patternDetector, GameOverStrategy gameOverStrategy) {
	        if (patternDetector == null || gameOverStrategy == null) {
	            throw new IllegalArgumentException("They cannot be null");
	        }
	        this.patternDetector = patternDetector;
	        this.gameOverStrategy = gameOverStrategy;
	    }
	 //Create Simple mode game
	@Override
	public Game4 createGame(Board board, Player bluePlayer, Player redPlayer)
	{
		return new Game4(
	            board,
	            bluePlayer,
	            redPlayer,
	            GameMode4.SIMPLE,
	            patternDetector,
	            gameOverStrategy
	        );
	}
  @Override
  public GameMode4 getGameMode()
  {
	  return GameMode4.SIMPLE;// return simple mode
  }
}
