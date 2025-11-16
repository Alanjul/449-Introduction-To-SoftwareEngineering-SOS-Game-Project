package sprint4.mode;



public class SimpleGame4  extends Game4{
	
	 
	public SimpleGame4(Board board, Player bluePlayer, Player redPlayer)
	{
		super(board, bluePlayer, redPlayer, GameMode4.SIMPLE);
	}
	
	@Override
	protected boolean shouldSwitchTurns(int sosFormed)
	{
		return true; // always switches in simple game
	}
		
	@Override 
	protected void checkGameOver()
	{
		//check if sos if formed
		if(blueScore > 0)
		{
			state = GameState.BLUE_PLAYER_WINS;
			gameOver = true;
			return;
		}
		
		if(redScore > 0)
		{
			state = GameState.RED_PLAYER_WINS;
			gameOver = true;
			return;
		}
		//check if the board is full with SOS formed
		if(board.isFull())
		{
			state = GameState.DRAW;
			gameOver = true;
			return;
		}
	}
	
	
	@Override
	protected String generateResult() {
		if (state == GameState.IN_PROGRESS) {
			return getCurrentPlayer().getName() + " -Game in progress in Simple mode";
		}

		switch (state) {
		case BLUE_PLAYER_WINS:
			return "Blue player wins";
		case RED_PLAYER_WINS:
			return "Red player wins";
		case DRAW:
			return "Draw!, Board is full with no sos formed";
		default:
			return super.generateResult();
		}
		
	}

}
