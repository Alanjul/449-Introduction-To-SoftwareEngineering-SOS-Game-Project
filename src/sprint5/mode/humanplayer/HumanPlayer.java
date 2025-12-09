package sprint5.mode.humanplayer;

import sprint5.mode.GameMode.Game4;
import sprint5.mode.board.Board;
import sprint5.mode.move.Move;
import sprint5.mode.player.Player;

public class HumanPlayer extends Player {
	private char preferredLetter;

	public HumanPlayer(char symbol, String name, char preferredLetter) {
		super(symbol, name);
		setPreferredLetter(preferredLetter);
	}

	@Override
	public Move chooseMove(Board board, Player oponent, Game4 game) {
		return null;// human player moves will be handled by the GUI

	}

	@Override
	public char chooseLetter() {
		return preferredLetter;

	}

	/**
	 * @return the letter
	 */
	public char getPreferredLetter() {
		return preferredLetter;
	}

	/**
	 * Set the player preferred letter
	 * 
	 * @param letter
	 */
	public void setPreferredLetter(char letter) {
		if (letter != 'S' && letter != 'O') {
			throw new IllegalArgumentException("Letter must be S or O");
		}
		this.preferredLetter = letter;
	}

}
