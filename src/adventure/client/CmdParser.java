package adventure.client;

import java.util.StringTokenizer;

import adventure.comm.CommandObj;
import adventure.comm.SayCmd;
import tools.Logger;

public abstract class CmdParser
{
	public static CommandObj parseCmd(String commandString)
	{
		Logger.info("Parsing command string ["+commandString+"]");
		
		StringTokenizer tok = new StringTokenizer(commandString, " ");
		
		String cmd = tok.nextToken();

		if (cmd.equalsIgnoreCase(CmdTokens.CMD_CONNECT))
		{
			Logger.info("Parsing connect cmd");
			return parseConnectCmd(tok);
		}
		else if (cmd.equalsIgnoreCase(CmdTokens.CMD_SAY))
		{
			return parseSayCmd(tok);
		}
		else
		{
			Logger.error("Error parsing command: ["+commandString+"]");
			return null;
		}
	}

	private static ConnectCmd parseConnectCmd(StringTokenizer tok)
	{
		String ip = tok.nextToken(":");
		ip = ip.trim();
		int port = Integer.parseInt(tok.nextToken());
		ConnectCmd connect = new ConnectCmd(ip, port);
		return connect;
	}
	
	private static SayCmd parseSayCmd(StringTokenizer tok)
	{
		String message = tok.nextToken("\n").trim();
		SayCmd sayCmd = new SayCmd(message);
		return sayCmd;
	}

}
