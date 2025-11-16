package sprint4.mode;

public class HumanPlayer extends Player {
  private char letter;
	public HumanPlayer(char symbol, String name, char letter) {
		super(symbol, name);
		setLetter(letter);
	}

	@Override
	public Move chooseMove(Board board, Player oponent, Game4 game) {
		return null;// human player moves will be handled by the GUI

	}

	@Override
	public char chooseLetter() {
		return letter;

	}

	/**
	 * @return the letter
	 */
	public char getLetter() {
		return letter;
	}

	/**
	 * Set the player preferred letter
	 * @param letter 
	 */
	public void setLetter(char letter) {
		if(letter != 'S' && letter != 'O')
		{
			throw new IllegalArgumentException("Letter must be S or O");
		}
		this.letter = letter;
	}
	

}
