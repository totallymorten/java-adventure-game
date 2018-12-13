package adventure.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import adventure.comm.CommunicationObj;
import tools.Logger;

public class SocketServer extends Thread
{
	private ServerSocket serverSocket;
	private int port;
	private ArrayList<ServerReaderThread> serverReaderThreads = new ArrayList<ServerReaderThread>();
	private ArrayList<ServerWriterThread> serverWriteThreads = new ArrayList<ServerWriterThread>();
	private ObjectOutputStream oout;
	private ObjectInputStream oin;
	//private ClientUpdateThread clientUpdateThread;
	
	
	public SocketServer(int port)
	{
		try 
		{
			this.serverSocket = new ServerSocket(port);
			this.port = port;
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startListening()
	{
		this.start();
	}
	
	public void sendObj(CommunicationObj comObj)
	{
		try
		{
			oout.writeObject(comObj);
			oout.flush();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		try 
		{
			ServerReaderThread readerThread;
			ServerWriterThread writerThread;
			(new ServerMessageThread()).start();
			String clientId;
			while (true)
			{
				Logger.info("Server listening on socket: ["+port+"]");
				Socket connection = this.serverSocket.accept();
				Logger.info("Connected with [" + connection.getInetAddress() + "]");
				
				clientId = connection.getInetAddress().toString();
				
				Logger.trace("Creating output stream");
				oout = new ObjectOutputStream(new BufferedOutputStream(connection.getOutputStream()));
//				oout = new ObjectOutputStream(connection.getOutputStream());
				oout.flush();
				Logger.trace("Output stream created successfully");

				Logger.trace("Creating input stream");
				oin = new ObjectInputStream(new BufferedInputStream(connection.getInputStream()));
//				oin = new ObjectInputStream(connection.getInputStream());
				Logger.trace("Input stream created successfully");

				// creating reader thread
				readerThread = new ServerReaderThread(clientId, oin);
				//serverReaderThreads.add(readerThread);
				AdventureServer.registerClientReader(clientId, readerThread);
				readerThread.start();
				
				writerThread = new ServerWriterThread(clientId, oout);
				//serverWriteThreads.add(writerThread);
				AdventureServer.registerClientWriter(clientId, writerThread);
				writerThread.start();
				
				Logger.info("Object communication successfully initiated for client [" + clientId + "]");
				
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
/*
	public ClientUpdateThread getClientUpdateThread()
	{
		return clientUpdateThread;
	}
	*/
}
