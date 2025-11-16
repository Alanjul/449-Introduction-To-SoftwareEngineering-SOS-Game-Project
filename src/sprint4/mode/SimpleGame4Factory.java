package sprint4.mode;

public class SimpleGame4Factory implements GameInterface {
	@Override
	public Game4 createGame(Board board, Player bluePlayer, Player redPlayer)
	{
		return new SimpleGame4(board, bluePlayer, redPlayer);
	}
  @Override 
  public GameMode4 getGameMode()
  {
	  return GameMode4.SIMPLE;// return simple mode
  }
}
