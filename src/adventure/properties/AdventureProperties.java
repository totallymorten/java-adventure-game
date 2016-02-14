package adventure.properties;

import java.io.IOException;
import java.util.Properties;

public class AdventureProperties 
{
	private Properties props = new Properties();
	
	private static AdventureProperties ap = null;
	
	private AdventureProperties()
	{
		try
		{
			props.load(getClass().getResourceAsStream("/conf/entities.properties"));
			props.load(getClass().getResourceAsStream("/conf/game.properties"));
			props.load(getClass().getResourceAsStream("/conf/world.properties"));
			props.load(getClass().getResourceAsStream("/conf/settings.properties"));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static AdventureProperties inst()
	{
		if (ap == null)
			ap = new AdventureProperties();

		return ap; 
	}
	
	public String getProperty(String property)
	{
		return props.getProperty(property);
	}
	
	public static String getString(String property)
	{
		return AdventureProperties.inst().getProperty(property);
	}
	
	public static int getInt(String property)
	{
		return Integer.parseInt(AdventureProperties.inst().getProperty(property));
	}

	public static float getFloat(String property)
	{
		return Float.parseFloat(AdventureProperties.inst().getProperty(property));
	}

	public static long getLong(String property)
	{
		return Long.parseLong(AdventureProperties.inst().getProperty(property));
	}

	public static double getDouble(String property)
	{
		return Double.parseDouble(AdventureProperties.inst().getProperty(property));
	}
}
