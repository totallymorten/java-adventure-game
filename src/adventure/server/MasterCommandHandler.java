package adventure.server;

import adventure.comm.Attack;
import adventure.comm.CommandObj;
import adventure.comm.RequestData;
import adventure.comm.UpdateEntityPos;
import tools.Logger;


/**
 * Does the initial distinction and distribution of command handling after a
 * message has been received from the socket interface
 * 
 * @author Morten S. Madsen
 *
 */
public class MasterCommandHandler
{
	public static void handleCommand(String clientId, CommandObj commandObj)
	{
		CommandHandler handler = null;
		
		if (commandObj instanceof RequestData)
			handler = new RequestDataHandler();
		else if (commandObj instanceof UpdateEntityPos)
			handler = new UpdateEntityPosHandler();
		else if (commandObj instanceof Attack)
			handler = new AttackHandler();
		else
			handler = new GeneralCmdHandler();
		
		
		if (handler != null)
		{
			handler.handleCommand(clientId, commandObj);
		}
		else
		{
			Logger.error("handler not identified for command object ["+commandObj+"]");
		}
	}
}
