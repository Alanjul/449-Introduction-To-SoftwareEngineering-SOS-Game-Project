package sprint3Test;

import java.lang.reflect.Field;

/**TestingUtils to test non-public fields*/
public class TestingUtils {

	//Helper to retrieve private methods during testing
	@SuppressWarnings("unchecked") //don't show warning
	public static <T> T getField(Object obj, String fieldName)
	{
		try
		{
			Field f = obj.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return (T) f.get(obj);//return the field
		}catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
