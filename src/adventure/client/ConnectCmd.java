package adventure.client;

import adventure.comm.CommandObj;

public class ConnectCmd extends CommandObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8445329073477589051L;
	private String ip;
	private int port;
	private SocketClient sockClient = null;
	
	public ConnectCmd(String ip, int port)
	{
		this.ip = ip;
		this.port = port;
	}
	
	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	@Override
	public void executeCmd()
	{
		this.sockClient =  new SocketClient(ip, port);
		this.sockClient.connectObject();
	}

	public SocketClient getSockClient()
	{
		return sockClient;
	}

	public void setSockClient(SocketClient sockClient)
	{
		this.sockClient = sockClient;
	}

}
