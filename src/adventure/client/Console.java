package adventure.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import tools.Logger;

public class Console
{
	public static void println(String s)
	{
		Logger.info(s);
		System.out.println(s);
	}
	
	public static String readln()
	{
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		
		System.out.print("> ");
		try
		{
			String input = br.readLine();
			Logger.info("Client read ["+input+"] from console");
			return input;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
