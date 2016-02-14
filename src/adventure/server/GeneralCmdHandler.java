package adventure.server;

import tools.Logger;
import adventure.comm.CommandObj;
import adventure.comm.Login;
import adventure.comm.LoginStatus;
import adventure.comm.PlayerMove;
import adventure.game.Game;

public class GeneralCmdHandler extends CommandHandler
{

	@Override
	public void handleCommand(String clientId, CommandObj commandObj)
	{
		if (commandObj instanceof PlayerMove)
		{
			PlayerMove move = (PlayerMove) commandObj;
			AdventureServer.handlePlayerMove(move.entityId, move.dir);
		}
		else if (commandObj instanceof Login)
		{
			Login login = (Login)commandObj;
			LoginStatus status;
			
			if (AdventureServer.getClientWriter(login.username) == null)
			{
				AdventureServer.renameClientThreads(clientId, login.username);
				clientId = login.username;
				status = new LoginStatus(LoginStatus.STATUS_OK);
			}
			else
			{
				Logger.error(this.getClass().getName() + ".handleCommand(): login failed");
				status = new LoginStatus(LoginStatus.STATUS_FAILED);
			}
			
			AdventureServer.addServerMessage(new ServerMessage(clientId, status));
		}
	}

}
