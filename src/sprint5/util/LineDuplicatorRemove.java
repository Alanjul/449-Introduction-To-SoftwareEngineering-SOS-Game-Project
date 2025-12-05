package sprint5.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineDuplicatorRemove {

	
	

	//Removing duplicate SOS pattern
	public static List<LineSegment4>removeDuplicate(List<LineSegment4>patterns)
	{
		List<LineSegment4>uniqueLine = new ArrayList<>();
		Set<String>visited = new HashSet<>();
		for(LineSegment4 line : patterns)
		{
			
			String key = createNormalizedKey(line);
			
			if(visited.add(key))
			{
				uniqueLine.add(line);
			}
		}
		return uniqueLine;
	}
	//Create normalized key
	private static String createNormalizedKey(LineSegment4 segment)
	{
		int row1 = segment.getStartRow(), col1 = segment.getStartCol();
		int row2 = segment.getEndRow(), col2 = segment.getEndCol();
		if(row1 < row2 || (row1 == row2 && col1 < col2))
		{
			return formatKey(row1, col1, row2, col2);
		}else
		{
			return formatKey(row2, col2, row1, col1);
		}
	}
	
	//Format Coordinates
	private static String formatKey(int row1, int col1, int row2, int col2)
	{
		return row1 + "," + col1 + "-" + row2 + "," + col2;
	}
}
