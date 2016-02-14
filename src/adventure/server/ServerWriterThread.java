package adventure.server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import tools.Logger;
import adventure.comm.CommunicationObj;

public class ServerWriterThread extends Thread
{
	private ObjectOutputStream output;
	private ArrayList<CommunicationObj> commands = new ArrayList<CommunicationObj>();
	private final String className = this.getClass().getName();
	public String clientId;
	
	public ServerWriterThread(String clientId, ObjectOutputStream output)
	{
		this.clientId = clientId;
		this.output = output;
	}
	
	public synchronized void addCommand(CommunicationObj commObj)
	{
		commands.add(commObj);
		
		synchronized (this)
		{
			this.notifyAll();
		}
	}
	
	private synchronized CommunicationObj getCommand()
	{
		if (commands.isEmpty())
		{
			return null;
		}
		
		return commands.remove(0);
	}
	
	
	private void waitForCommand()
	{
		synchronized (this)
		{
			try
			{
				this.wait(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	
	@Override
	public void run()
	{
		Logger.info(className + ".run(): started");
		
		CommunicationObj object;
		try
		{
			while (true)
			{
				while ((object = getCommand()) == null)
				{
					Logger.trace(this.getClass().getName() + ".run(): waiting for command");
					waitForCommand();
					Logger.trace(this.getClass().getName() + ".run(): woke up");
				}
				
				Logger.trace(className + ".run(): sending command: " + object);
				output.writeObject(object);
				output.flush();
				Logger.trace(className + ".run(): command sent");										
			}
		}
		catch (EOFException e)
		{
			Logger.info(className + ".run(): Client closed connection");
			try
			{
				output.close();
			} catch (IOException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (SocketException e)
		{
			Logger.info(className + ".run(): Client closed connection");
		}
		catch (IOException e)
		{
			Logger.error(className + ".run(): IOException occurred");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			Logger.error(className + ".run(): Exception occurred");
			e.printStackTrace();
		}
		finally
		{
			Logger.info(className + ".run(): removing thread from writer list");
			AdventureServer.removeClientWriter(clientId);
		}

		Logger.trace(className + ".run(): terminating");

	}
}
