package sprint4.mode;

/**This class creates General mode game*/
public class GeneralGame4Factory  implements GameInterface{

	@Override
	public Game4 createGame(Board board, Player bluePlayer, Player redPlayer)
			{
		     return new GeneralGame4 (board, bluePlayer,redPlayer);
			}
	@Override
	public GameMode4 getGameMode()
	{
		return GameMode4.GENERAL;
	}
}
