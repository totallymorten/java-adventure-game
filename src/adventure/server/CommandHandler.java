package adventure.server;

import adventure.comm.CommandObj;



public abstract class CommandHandler
{
	abstract public void handleCommand(String clientId, CommandObj commandObj);
}
