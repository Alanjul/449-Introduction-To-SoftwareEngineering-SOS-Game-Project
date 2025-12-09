package sprint5.mode.player;

public enum PlayerType {
	HUMAN("HUMAN", "Human Player"), COMPUTER("COMPUTER", "Computer Player");

	private final String code;
	private final String displayName;

	PlayerType(String code, String displayName) {
		this.code = code;
		this.displayName = displayName;
	}

	public String getCode() {
		return code;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getShortName() {
		return this == HUMAN ? "Human" : "Computer";
	}

	public static PlayerType fromCode(String code) {
		for (PlayerType type : values()) {
			if (type.code.equals(code)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid player type code: " + code);
	}
}
