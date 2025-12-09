package sprint5.mode.GameMode;

public enum GameStatus {
	IN_PROGRESS("IN_PROGRESS", "Game in Progress"), 
	COMPLETED("COMPLETED", "Game Completed"),
	ABANDONED("ABANDONED", "Game Abandoned");

	private final String code;
	private final String displayName;

	GameStatus(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}

	public String getCode() {
		return code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static GameStatus fromCode(String code) {
		for (GameStatus status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		throw new IllegalArgumentException("Invalid game status code: " + code);
	}

}
