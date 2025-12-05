package sprint5.util;

import sprint5.mode.player.PlayerColor;

public enum Winner {
BLUE(PlayerColor.BLUE, "Blue Player"),
RED(PlayerColor.RED, "Red Player"),
DRAW(null, "Draw"),
NONE(null, "No Winner");
	
private final PlayerColor playerColor;
private String description;
Winner(PlayerColor playerColor, String description)
{
	this.playerColor  = playerColor;
	this.description = description;
}
public PlayerColor getPlayerColor()
{
	return playerColor;
}
/**
 * @return the description
 */
public String getDescription() {
	return description;
}

//check for winner
public boolean hasWinner()
{
	return this == BLUE || this == RED;
}
//Draw
public boolean isDraw()
{
	return this == DRAW;
}
public boolean isProgress()
{
	return this == NONE;
}
//Create winner
public static Winner fromPlayerColor(PlayerColor color) {
    return color == PlayerColor.BLUE ? BLUE : RED;
}
//Determine winner from the score
public static Winner fromScores(int blueScore, int redScore, boolean gameOver) {
    if (!gameOver) {
        return NONE;
    }
    
    if (blueScore > redScore) {
        return BLUE;
    } else if (redScore > blueScore) {
        return RED;
    } else {
        return DRAW;
    }
}
@Override
public String toString() {
    return description;
}
}
