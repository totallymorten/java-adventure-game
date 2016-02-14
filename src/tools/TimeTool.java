package tools;

public abstract class TimeTool
{
	public static double timeDiff(double startTimeMs)
	{
		return ((System.nanoTime() / 1e6) - startTimeMs);
	}
	
	public static double nanoToMs(long timeNano)
	{
		return (timeNano / 1e6);
	}
	
	public static double getTime()
	{
		return System.nanoTime() / 1e6;
	}
}
