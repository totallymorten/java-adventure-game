package adventure.server;

import adventure.comm.CommunicationObj;

public class ServerMessage
{
	public static String ALL_CLIENTS = "ALL_CLIENTS";
	public static String EXCEPT_CLIENT = "EXCEPT_CLIENT";
	
	public String client_id;
	public String except;
	public CommunicationObj commObj;
	
	public ServerMessage(String clientId, CommunicationObj commObj)
	{
		this.client_id = clientId;
		this.commObj = commObj;
	}
	
	public ServerMessage(String clientId, CommunicationObj commObj, boolean except)
	{
		if (except)
		{
			this.except = clientId;
		}
		else
			this.client_id = clientId;
		
		this.commObj = commObj;
	}

	@Override
	public String toString() 
	{
		return "ServerMessage["+this.commObj+"]"; 
	}
	
	
	
}
