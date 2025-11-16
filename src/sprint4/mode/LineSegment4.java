package sprint4.mode;

import java.awt.Color;
import java.util.Objects;

public class LineSegment4 {
	
	private Color color;
	private int startRow, endRow, startCol, endCol;
	public LineSegment4(int startRow, int startCol, int endRow, int endCol, Color color) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
        this.color = color;
    }
	public LineSegment4 (LineSegment4 other){
		this.startRow = other. startRow;
		this.endRow = other.endRow;
		this.startCol = other.startCol;
		this.endCol = other.endCol;
		this.color = other.color;
		
	}
	@Override
	public boolean equals(Object obj)
	{
		//check if the are point on same location
		if( this == obj) return true; 
		if(! (obj instanceof LineSegment4)) return false;
		LineSegment4 other = (LineSegment4) obj;
		return startRow == other.startRow && startCol == other.startCol 
				&& endRow == other.endRow &&  endCol == other.endCol;
	}
	
	@Override
	//Create a unique integer from four integer fields using prime multiplier
	public int hashCode()
	{
		return 31 *(31 * (31 *  startRow +startCol) +endRow) + endCol;
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
	@Override
	
	public String toString()
	{
		return "LineSegment4 [(" + startRow + "," + startCol + ") -> (" +
	            endRow + "," + endCol + "), Color=" + color + "]";
	}
	

}
