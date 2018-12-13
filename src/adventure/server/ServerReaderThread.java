package adventure.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import adventure.comm.CommandObj;
import adventure.game.Game;
import adventure.game.entities.Player;
import tools.Logger;

public class ServerReaderThread extends Thread
{
	private ObjectInputStream input;
	private String className = this.getClass().getName();
	public String clientId;
	
	public ServerReaderThread(String clientId, ObjectInputStream input)
	{
		this.input = input;
		this.clientId = clientId;
	}
	
	@Override
	public void run()
	{
		Logger.info(className + ".run(): started");
		
		CommandObj object;
		try
		{
			while (true)
			{
				object = null;
				
				Logger.trace(className + ".run(): reading input");
				object = (CommandObj) input.readObject();
				Logger.trace(className + ".run(): received cmd object from client ["+object+"]");
				
				if (object == null)
				{
					Logger.error(className + ".run(): Object received from client was null ["+object+"]");
				}
				
				MasterCommandHandler.handleCommand(clientId, object);
			}
		}
		catch (EOFException e)
		{
			Logger.info(className + ".run(): Client closed connection");
			try
			{
				input.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (IOException e)
		{
			Logger.error(className + ".run(): IOException occurred");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			Logger.error(className + ".run(): ClassNotFoundException occurred");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Logger.error(className + ".run(): Exception occurred");
			e.printStackTrace();
		}
		finally
		{
			Logger.info(className + ".run(): removing thread from reader list");
			AdventureServer.removeClientReader(clientId);
			Player p = Game.g.getPlayer(clientId);
			p.active = false;
			AdventureServer.removeEntity(p);
		}

		Logger.trace(className + ".run(): terminating");

	}
}
