package sprint5.mode.move;

public class Move {
	private char letter;
	private int row;
	private int col;

	public Move(int row, int col,char letter)
	{
		if (letter != 'S' && letter != 'O') {
            throw new IllegalArgumentException("Letter must be 'S' or 'O'");
        }
		this.row = row;
		this.col = col;
		this.letter = letter;
	}

	public char getLetter() {
		return letter;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	@Override
	public String toString()
	{
		return String.format("Move(%d, %d, %c)", getRow(), getCol(), getLetter());
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
			return true;
		}
        if (!(obj instanceof Move)) {
			return false;
		}
        Move other = (Move) obj;
        return row == other.row && col == other.col && letter == other.letter;
    }

    @Override
    public int hashCode() {
        return 31 * (31 * row + col) + letter;
    }

}
