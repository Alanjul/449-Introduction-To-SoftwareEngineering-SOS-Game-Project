package sprint5.mode.player;

public class TurnManager {
	private PlayerColor currentTurn;

	public TurnManager() {
		this.currentTurn = PlayerColor.BLUE;
	}

	public TurnManager(PlayerColor startingPlayer) {
		if (startingPlayer == null) {
			throw new IllegalArgumentException("Starting player cannot be null");
		}
		this.currentTurn = startingPlayer;
	}

	// Get the current player color
	public PlayerColor getCurrentTurn() {
		return currentTurn;
	}

	// Get the opposite player colr
	public PlayerColor getOpponentTurn() {
		return currentTurn.getOpposite();
	}

	// switch to the other player's turn
	public void switchTurn() {
		currentTurn = currentTurn.getOpposite();
	}

	// Checks
	public boolean isBluesTurn() {
		return currentTurn == PlayerColor.BLUE;
	}

	// red turn
	public boolean isRedsTurn() {
		return currentTurn == PlayerColor.RED;
	}
	// Reset turn manager to initial state
	public void reset() {
		this.currentTurn = PlayerColor.BLUE;
	}

	public void reset(PlayerColor startingPlayer) {
		if (startingPlayer == null) {
			throw new IllegalArgumentException("Starting player cannot be null");
		}
		this.currentTurn = startingPlayer;
	}
	@Override
	public String toString() {
		return currentTurn.getDisplayName() + "'s turn";
	}

}
