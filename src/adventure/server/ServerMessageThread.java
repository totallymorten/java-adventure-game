package adventure.server;

import tools.Logger;

public class ServerMessageThread extends Thread
{

	@Override
	public void run()
	{
		ServerMessage msg;
		while (true)
		{		
			Logger.trace(this.getClass().getName() + ": Looking for message");

			msg = AdventureServer.getServerMessage();
			
			if (msg == null)
			{
				AdventureServer.waitForMessage();
				Logger.trace(this.getClass().getName() + ": woke up!!");
			}
			else
			{
				Logger.trace(this.getClass().getName() + ": processing message ["+msg+"]");
				
				if (msg.client_id == ServerMessage.ALL_CLIENTS)
				{
					Logger.trace(this.getClass().getName() + ": sending message ["+msg+"] to ALL clients");
					AdventureServer.sendAllClientMsg(msg.commObj);
				}
				else if (msg.except != null)
				{
					Logger.trace(this.getClass().getName() + ": sending message ["+msg+"] to ALL except client ["+msg.except+"]");
					AdventureServer.sendAllClientMsgExcept(msg.commObj, msg.except);				
				}
				else
				{
					Logger.trace(this.getClass().getName() + ": sending message ["+msg+"] to client ["+msg.client_id+"]");
					AdventureServer.sendClientMsg(msg.client_id, msg.commObj);
				}
				
			}				
		}
	}
	
}
