package sprint4.mode;

import java.util.List;

public class MoveResult {
	private boolean success;
	private List<LineSegment4>sosFormed;
	private  boolean gameEnded; //goal state
	private String message;
	public MoveResult(String message, boolean success, List<LineSegment4>sosFormed )
	{
		this.message = message;
		this.sosFormed = sosFormed;
		this.success = success;
		
	
	}
	public boolean isSuccess() {
		return success;
	}
	
	public List<LineSegment4> getSosFormed() {
		return sosFormed;
	}
	
	
	
	public String getMessage() {
		return message;
	}
	
	public int getSosCount()
	{
		return sosFormed != null ? sosFormed.size() : 0;
	}

}
