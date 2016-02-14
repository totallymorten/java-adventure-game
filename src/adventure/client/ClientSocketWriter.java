package adventure.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import tools.Logger;
import adventure.comm.CommunicationObj;

public class ClientSocketWriter extends Thread
{
	private ObjectOutputStream ous;
	private ArrayList<CommunicationObj> internalMessageQueue = new ArrayList<CommunicationObj>();
	private String className = this.getClass().getName();
	
	public ClientSocketWriter(ObjectOutputStream ous)
	{
		this.ous = ous;
	}
	
	@Override
	public void run()
	{
		Logger.debug("Starting client writer thread");
		CommunicationObj obj;
		while (true)
		{
			try
			{
				while ((obj = getMessageFromQueue()) == null)
				{
					synchronized(this)
					{
						wait(1000);
					}					
				}

				ous.writeObject(obj);
				ous.flush();
				Logger.trace(className + ".run(): sent object ["+obj+"]");				
			}
			catch (SocketException e)
			{
				Logger.debug(className + ".readInput(): terminating with message: " + e.getMessage());
				System.exit(0);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public synchronized void addMsgToQueue(CommunicationObj obj)
	{
		this.internalMessageQueue.add(obj);
		
		synchronized(this)
		{
			notifyAll();
		}
	}
	
	private synchronized CommunicationObj getMessageFromQueue()
	{
		if (this.internalMessageQueue.isEmpty()) return null;
		
		return internalMessageQueue.remove(0);
	}
	
}
