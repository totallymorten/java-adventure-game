package adventure.client;

import adventure.comm.CommandObj;
import adventure.comm.SayCmd;

public abstract class CommandHandler
{
	public static void handleCommand(CommandObj cmd, AdventureClient client)
	{
		cmd.executeCmd();
		
		// special handling for the connect command
		if (cmd instanceof ConnectCmd)
		{
			ConnectCmd concmd = (ConnectCmd) cmd;
			// storing socket connection for later use
			client.setSockClient(concmd.getSockClient());
		}
		
		if (cmd instanceof SayCmd)
		{
			client.sendCmdObj(cmd);
		}
	}
}
