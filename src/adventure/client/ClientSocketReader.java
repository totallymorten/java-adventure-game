package adventure.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketException;

import tools.Logger;
import adventure.comm.CommunicationObj;

public class ClientSocketReader extends Thread
{
	private ObjectInputStream ois;
	private SocketClient sc;
	
	public ClientSocketReader(ObjectInputStream ois, SocketClient sc)
	{
		this.ois = ois;
		this.sc = sc;
	}
	
	@Override
	public void run()
	{		
		Logger.debug("Starting client reader thread");
		while (true)
		{
			CommunicationObj obj;
			try
			{
				obj = (CommunicationObj) this.ois.readObject();
				Logger.trace(this.getClass().getName() + ".run(): Received object ["+obj+"]");
				addMsgToQueue(obj);
			}
			catch (SocketException e)
			{
				Logger.debug(this.getClass().getName() + ".run(): terminating with message: " + e.getMessage());
				System.exit(0);
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void addMsgToQueue(CommunicationObj obj)
	{
		sc.addMsgToQueue(obj);
		Logger.trace(me() + ".addMsgToQueue(): added msg to queue ["+obj+"]");
	}
	
	private String me()
	{
		return this.getClass().getName();
	}
	
}
