package adventure.client;

import java.awt.Point;

import tools.Logger;
import adventure.comm.Attack;
import adventure.comm.CommandObj;
import adventure.comm.CommunicationObj;
import adventure.comm.Login;
import adventure.comm.LoginStatus;
import adventure.comm.PlayerData;
import adventure.comm.PlayerMove;
import adventure.comm.RequestData;
import adventure.comm.UpdateEntityPos;
import adventure.game.TileMap;
import adventure.game.entities.Actor;
import adventure.game.entities.Entity;
import adventure.game.entities.Player;
import adventure.types.Direction;
import adventure.types.RequestDataType;

public class AdventureClient
{
	SocketClient sockClient = null;
	final String version = "0.0.1";
	public static AdventureClient c = new AdventureClient();
	
	private void handleUserInput(String input)
	{
		CommandObj cmd = CmdParser.parseCmd(input);
		
		if (cmd != null)
			CommandHandler.handleCommand(cmd, this);
	}

	public SocketClient getSockClient()
	{
		return sockClient;
	}

	public void setSockClient(SocketClient sockClient)
	{
		this.sockClient = sockClient;
	}
	
	public void startConsoleClient()
	{
		String input;
		Console.println("Adventure client v" + version + " started");
		input = Console.readln();
		
		while (!(input.equalsIgnoreCase("exit")))
		{
			handleUserInput(input);
			input = Console.readln();
		}
		
		Console.println("Terminating Adventure client...");
		sockClient.shutdown();
	}
	
	public void sendCmdObj(CommandObj cmd)
	{
		sockClient.sendCmdObj(cmd);
	}
	
	public void updateEntityPos(Entity e, Point pos)
	{
		Logger.trace(this.getClass().getName() + ".updateEntityPos(): Updating entity position");
		sockClient.sendCmdObj(new UpdateEntityPos(e.entityId, pos));
	}
	
	public void attack(Actor attacker, Actor defender)
	{
		Logger.trace(this.getClass().getName() + ".attack(): sending attack command");
		sockClient.sendCmdObj(new Attack(attacker.entityId, defender.entityId));
	}
	
	public void connect(String ip, int port)
	{
		CommandObj obj = new ConnectCmd(ip, port);
		handleCommand(obj);
	}
	
	int pollDelayMs = 200;
	
	public void login(String username)
	{
		Login loginCmd = new Login(username);
		sendCmdObj(loginCmd);
		
		CommunicationObj obj = waitForCommType(LoginStatus.class);
		
		Logger.info("Login with username ["+username+"] OK");
	}
	
	private CommunicationObj waitForCommType(Class someType)
	{
		CommunicationObj obj;
		do
		{
			obj = sockClient.getMsgTypeFromQueue(someType);
			
			if (obj == null || obj.getClass() != someType)
			{
				try
				{
					Thread.sleep(pollDelayMs);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				

				obj = null;				
			}
		}
		while(obj == null);
		
		return obj;
	}
	
	public TileMap getTileMapFromServer()
	{
		CommunicationObj obj;
		
		RequestData rd = new RequestData(RequestDataType.MAP);
		sendCmdObj(rd);
		Logger.debug(this.getClass().getName() + ".getTileMapFromServer(): sent request for map to server");

		obj = waitForCommType(TileMap.class);
		
		Logger.debug(this.getClass().getName() + ".getTileMapFromServer(): got map from server");
		
		return (TileMap) obj;
		
	}
	
	public Player getPlayerFromServer()
	{
		CommunicationObj cmd;
		
		RequestData rd = new RequestData(RequestDataType.PLAYER);
		sendCmdObj(rd);
		Logger.debug(this.getClass().getName() + ".getPlayerFromServer(): sent request for player to server");
		
		cmd = waitForCommType(PlayerData.class);
		
		Logger.debug(this.getClass().getName() + ".getTileMapFromServer(): got player from server");
		
		return ((PlayerData)cmd).player;
	}
	
	public void requestWorldUpdate()
	{
		sockClient.sendCmdObj(new RequestData(RequestDataType.WORLD));
	}
	
	public void requestAllEntities()
	{
		RequestData rd = new RequestData(RequestDataType.ALL_ENTITIES);
		Logger.debug(this.getClass().getName() + ".requestAllEntities(): Requesting all entities ["+rd+"]");
		sockClient.sendCmdObj(rd);
	}
	
	public void requestEntity(int entityId)
	{
		RequestData rd = new RequestData(RequestDataType.ENTITY);
		rd.requestInfo = String.valueOf(entityId);
		Logger.debug(this.getClass().getName() + ".requestEntity(): Requesting entity with id ["+entityId+"]");
		sockClient.sendCmdObj(rd);
	}

	public void shutdown()
	{
		if (sockClient != null)
			sockClient.shutdown();
	}
	
	public CommunicationObj getMessage()
	{
		if (sockClient == null)
			return null;
		
		return sockClient.getMsgFromQueue();
	}
	
	private void handleCommand(CommandObj comObj)
	{
		CommandHandler.handleCommand(comObj, this);
	}
	
// Experimental !!!
//	public static void main(String[] args)
//	{
//		AdventureClient client = new AdventureClient();
//		client.startConsoleClient();
//	}

	public void playerMove(Player player, Direction dir)
	{
		sockClient.sendCmdObj(new PlayerMove(player.entityId, dir));
	}
}
