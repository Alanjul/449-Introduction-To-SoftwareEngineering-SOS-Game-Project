package sprint5.mode.GameMode;

public enum GameState {
	IN_PROGRESS("Game in Progress"),
	BLUE_PLAYER_WINS("Blue Player Wins"),
	RED_PLAYER_WINS("Red player wins"),
	DRAW("Draw!");

	private String description;
	GameState(String description)
	{
		this.description = description;
	}


	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}


	@Override
	public String toString()
	{
		return description;
	}

}
