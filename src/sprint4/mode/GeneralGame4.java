package sprint4.mode;


public class GeneralGame4 extends Game4 {

	
	
	public GeneralGame4(Board board, Player bluePlayer, Player redPlayer)
	{
		super(board,bluePlayer, redPlayer, GameMode4.GENERAL);
		
		
	}
	
	@Override
	protected  boolean shouldSwitchTurns(int sosFormed)
	{
		//switch if only no sos was was formed
		return sosFormed == 0;
	}
			
		
		
	

	// reset the game
	
	
@Override
  protected void checkGameOver()
	{
		if(!board.isFull())
		{
			return ; // Game continues
		}
		
		//Board is full
		if(blueScore > redScore)
		{
			state = GameState.BLUE_PLAYER_WINS;
		}else if(redScore > blueScore)
		{
			state = GameState.RED_PLAYER_WINS;
		} else
		{
			state = GameState.DRAW; /// draw 
		}
		//game over 
		gameOver = true;
	}
	@Override
	protected  String generateResult()
	{
		if(state == GameState.IN_PROGRESS) {
		return String.format("%s's turn in General Mode - Blue score: %d, Red score : %d",
				getCurrentPlayer().getName(), blueScore, redScore);
		}
		return switch(state)
		{
		case BLUE_PLAYER_WINS -> String.format("Blue Player Wins! Final score: Blue Score: %d"
				+ ", Red Score: %d", blueScore, redScore);
		case RED_PLAYER_WINS -> String.format("Red Player Wins! Final score: Blue Score: %d"
				+ ", Red Score: %d", blueScore, redScore);
		case DRAW -> String.format("Draw! Final score: Blue Score: %d"
				+ ", Red Score: %d", blueScore, redScore);
		default   ->  super.generateResult();
		};
		
		
			
		
		
	
	}

}
