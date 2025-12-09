package sprint5.mode.player;

import java.awt.Color;

public enum PlayerColor {
	BLUE('B', "Blue"), RED('R', "Red");

	private final char symbol;
	private final String name;

	PlayerColor(char symbol, String name) {
		this.symbol = symbol;
		this.name = name;
	}

	// Get symbol
	public char getSymbol() {
		return symbol;
	}

	/* @return RED if it is BLUE */
	public PlayerColor getOpposite() {
		return this == BLUE ? RED : BLUE;
	}

	public Color getAwtColor() {
		return this == BLUE ? Color.BLUE : Color.RED;
	}

	// Get the display
	public String getDisplayName() {
		return name;
	}

	// Get PlayerColor
	public static PlayerColor fromSymbol(char symbol) {
		for (PlayerColor color : values()) {
			if (color.symbol == symbol) {
				return color;
			}
		}
		throw new IllegalArgumentException("Invalid player symbol:" + symbol);
	}
}
