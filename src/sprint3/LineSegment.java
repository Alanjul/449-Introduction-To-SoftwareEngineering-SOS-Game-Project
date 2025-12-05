package sprint3;

import java.awt.Color;

public class LineSegment {
	private Color color;
	private int startRow, endRow, startCol, endCol;
	public LineSegment (int startRow,  int startCol,int endRow, int endCol, Color color){
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		this.color = color;

	}
	public Color getColor() {
		return color;
	}
	public int getStartRow() {
		return startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public int getStartCol() {
		return startCol;
	}
	public int getEndCol() {
		return endCol;
	}



}
