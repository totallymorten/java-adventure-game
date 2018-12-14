package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import adventure.game.Game;

public abstract class Logger
{
	private static LogLevel level = LogLevel.INFO;
	private static boolean fileLogging = false;
	private static String fileName = "adventure.log";
	private static File f;
	private static FileWriter w;
	private static BufferedWriter bw;
	private static Calendar cal = Calendar.getInstance();
	
	static 
	{
		if (fileLogging)
		{
			f = new File(fileName);
			f.delete();
			try
			{
				f.createNewFile();
				w = new FileWriter(f);
				bw = new BufferedWriter(w);
			} catch (IOException e)
			{
				println("Error when creating log file: " + e);
				System.exit(1);
			}
			
		}
	}
	
	
	public static synchronized void trace(String s)
	{
		if (Logger.level == LogLevel.TRACE)
		{
			s = "[TRACE] " + s;
			println(s);
			fileLog(s);			
		}
	}
	
	public static synchronized void debug(String s)
	{
		if (Logger.level == LogLevel.TRACE
		||  Logger.level == LogLevel.DEBUG)
		{
			s = "[DEBUG] " + s;
			println(s);
			fileLog(s);			
		}
	}

	public static synchronized void info(String s)
	{
		if (Logger.level == LogLevel.TRACE
		||  Logger.level == LogLevel.DEBUG
		||  Logger.level == LogLevel.INFO)
		{
			s = "[INFO] " + s;
			println(s);
			fileLog(s);
			
			if (Game.MODE == Game.MODE_CLIENT)
				Game.g.addMessage(s);
		}
	}
	
	public static synchronized void special(String s)
	{
		s = "[SPECIAL] " + s;
		println(s);
		fileLog(s);
	}

	public static synchronized void error(String s)
	{
		s = "[ERROR] " + s;
		println(s);
		fileLog(s);
		
		if (Game.MODE == Game.MODE_CLIENT)
			Game.g.addMessage(s);

	}
	
	private static void println(String s)
	{
		System.out.println(getTimeStamp() + " " + s);
		System.out.flush();
	}
	
	private static String getTimeStamp()
	{
		cal.setTimeInMillis(System.currentTimeMillis());

		return cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DAY_OF_MONTH) 
		  + " "
		  + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND) + "." + cal.get(Calendar.MILLISECOND);

		
		
		
		//return "";
	}
	
	private static synchronized void fileLog(String s)
	{
		if (!fileLogging) return;
		
		try
		{
			bw.write(getTimeStamp() + " " + s + "\n");
			bw.flush();
			
		} catch (IOException e)
		{
			System.out.println("Error writing to log file: " + e);
			System.exit(1);
		}
	}
}
