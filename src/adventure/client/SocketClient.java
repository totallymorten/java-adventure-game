package adventure.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import tools.Logger;
import adventure.comm.CommunicationObj;

public class SocketClient 
{
	private Socket socket;
	private String ip;
	private int port;
	private ObjectOutputStream ous;
	private ObjectInputStream ois;
	
	
	private ClientSocketReader reader;
	private ClientSocketWriter writer;
	
	private ArrayList<CommunicationObj> incomingMessageQueue = new ArrayList<CommunicationObj>();
	
	public SocketClient(String ip,int port)
	{
		this.ip = ip;
		this.port = port;
	}

/*
	public void connectText()
	{
		try 
		{
			Logger.info("Trying connection to: ["+ip+":"+port+"]");
			socket = new Socket(this.ip,this.port);
			Logger.info("Connection accepted");
			
			InputStreamReader isr = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(isr);
			Logger.info("Text connection established");
			
			String msg;
			System.out.print("> ");
			PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
			while ((msg = br.readLine()) != null)
			{
				Logger.info("Client got input: ["+msg+"]");
				
				pw.println(msg);
				System.out.print("> ");
			}
			socket.shutdownOutput();
			socket.close();
			
		} 
		catch (UnknownHostException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
*/
	public void connectObject()
	{
		try 
		{
			
			
			Logger.info("Trying connection to: ["+ip+":"+port+"]");
			socket = new Socket(this.ip,this.port);
			Logger.info("Connection accepted");
			
			
			// Get both outputstream and inputstream and create threads to handling each (like the server)
			Logger.debug("Creating output stream");
			this.ous = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
//			this.ous = new ObjectOutputStream(socket.getOutputStream());
			ous.flush();
			Logger.debug("Output stream successfully created");

			Logger.debug("Creating input stream");
			this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
//			this.ois = new ObjectInputStream(socket.getInputStream());
			Logger.debug("Input stream successfully created");
			
			Logger.debug("object connection established");
			
			this.reader = new ClientSocketReader(this.ois, this);
			this.reader.start();
			
			this.writer = new ClientSocketWriter(this.ous);
			this.writer.start();
		} 
		catch (UnknownHostException e) 
		{
			Logger.error("Error, unknown host");
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			Logger.error("Error, IOException occurred");
			e.printStackTrace();
		}
		
	}

	public void sendCmdObj(CommunicationObj command)
	{
		if (command == null)
		{
			Logger.error(this.getClass().getName() + ".sendCmdObj(): command was null!!!!");
			return;
		}
		
		Logger.trace("Client got object input: ["+command+"]");	
		writer.addMsgToQueue(command);
	}

	public void shutdown()
	{
		try
		{
			Logger.info(this.getClass().getName() + ".shutdown(): shutting down socket connection");
			socket.shutdownOutput();
			socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public synchronized CommunicationObj getMsgFromQueue()
	{
		if (this.incomingMessageQueue.isEmpty()) return null;
		
		return this.incomingMessageQueue.remove(0);
	}
	
	public synchronized CommunicationObj getMsgTypeFromQueue(Class commType)
	{
		if (this.incomingMessageQueue.isEmpty()) return null;
		
		CommunicationObj obj;
		do
		{
			obj = this.incomingMessageQueue.remove(0);
			Logger.trace(this.getClass().getName() + ".getMsgTypeFromQueue(): removed msg from queue ["+obj+"]. Queue size now ["+incomingMessageQueue.size()+"]");		
			
			if (obj.getClass() == commType)
				return obj;
		}
		while (!incomingMessageQueue.isEmpty());
		
		return null;
	}

	public synchronized void addMsgToQueue(CommunicationObj obj)
	{
		incomingMessageQueue.add(obj);
		Logger.trace(this.getClass().getName() + ".addMsgToQueue(): added msg to queue ["+obj+"]. Queue size now ["+incomingMessageQueue.size()+"]");		
	}
	
}
