package adventure.server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import tools.Logger;
import adventure.comm.ActorStats;
import adventure.comm.CommunicationObj;
import adventure.comm.GameStats;
import adventure.comm.NewEntity;
import adventure.comm.PlaySound;
import adventure.comm.RemoveEntity;
import adventure.comm.UpdateEntityHealth;
import adventure.comm.UpdateEntityPos;
import adventure.comm.UpdateWorld;
import adventure.game.Game;
import adventure.game.World;
import adventure.game.entities.Actor;
import adventure.game.entities.Entity;
import adventure.game.entities.HealthEntity;
import adventure.game.entities.Player;
import adventure.types.ActorStat;
import adventure.types.Direction;
import adventure.types.Sounds;
import adventure.types.Tiles;


public class AdventureServer
{
	public static final String versionString = "An Adventure Game Server v0.3.0";
	
	private SocketServer sockServer;
	
	private static ArrayList<ServerMessage> serverMessageQueue = new ArrayList<ServerMessage>();
	private static HashMap<String, ServerWriterThread> clientWriters = new HashMap<String, ServerWriterThread>();
	private static HashMap<String, ServerReaderThread> clientReaders = new HashMap<String, ServerReaderThread>();
	private GameThread gameThread;
	
	private static String LOG_PREFIX = "AdventureServer.";
	
	public static synchronized void sendGameStats()
	{
		GameStats gs = new GameStats(Game.g);
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, gs));
	}
	
	public static synchronized void registerClientWriter(String clientId, ServerWriterThread writer)
	{
		clientWriters.put(clientId, writer);
	}
	
	public static synchronized void registerClientReader(String clientId, ServerReaderThread reader)
	{
		clientReaders.put(clientId, reader);
	}

	public static synchronized void removeClientWriter(String clientId)
	{
		Logger.info(LOG_PREFIX + "removeClientWriter(): size before move ["+clientWriters.size()+"]");
		clientWriters.remove(clientId);
		Logger.info(LOG_PREFIX + "removeClientWriter(): size after move ["+clientWriters.size()+"]");
	}
	
	public static synchronized void removeClientReader(String clientId)
	{
		clientReaders.remove(clientId);
	}

	public static synchronized ServerWriterThread getClientWriter(String clientId)
	{
		return clientWriters.get(clientId);
	}

	public static synchronized void renameClientThreads(String clientId, String username)
	{
		ServerWriterThread writer = clientWriters.remove(clientId);
		ServerReaderThread reader = clientReaders.remove(clientId);
		reader.clientId = username;
		writer.clientId = username;
		
		clientWriters.put(username, writer);
		clientReaders.put(username, reader);
	}

	public static synchronized void sendClientMsg(String clientId, CommunicationObj obj)
	{
		clientWriters.get(clientId).addCommand(obj);
	}
	
	public static synchronized void sendAllClientMsg(CommunicationObj obj)
	{
		for (ServerWriterThread writer: clientWriters.values())
		{
			writer.addCommand(obj);
		}
	}

	public static synchronized void sendAllClientMsgExcept(CommunicationObj obj, String exceptClientId)
	{
		for (String key : clientWriters.keySet())
		{
			if (key != exceptClientId)
				clientWriters.get(key).addCommand(obj);
		}
	}

	public static synchronized void updateWorld(World world)
	{
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new UpdateWorld(world)));
	}
	
	public static synchronized void playSound(Sounds sound)
	{
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new PlaySound(sound)));
	}
	
	public static synchronized void playRandomSound(Sounds[] sounds)
	{
		double rand = Math.random();
		int position = (int) Math.round(rand * (sounds.length - 1));
		playSound(sounds[position]);
	}

	public static synchronized void removeEntity(Entity e)
	{
		Logger.info(LOG_PREFIX + "removeEntity(): removing entity: " + e);
		Game.g.removeEntity(e);
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new RemoveEntity(e.entityId)));
	}
	
	public static synchronized void newEntity(Entity e)
	{
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new NewEntity(e)));
	}
	
	public static synchronized void updateEntityPos(int entityId, Point pos)
	{
		Logger.trace("AdventureServer.updateEntityPos(): sending entity ["+entityId+"] update pos ["+pos+"]");
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new UpdateEntityPos(entityId, pos)));
	}
	
	public static synchronized void updateEntityHealth(HealthEntity e)
	{
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new UpdateEntityHealth(e.entityId, e.health)));
	}

	public static synchronized void addServerMessage(ServerMessage msg)
	{
		Logger.trace("AdventureServer.addServerMessage(): adding server message ["+msg+"]");
		
		serverMessageQueue.add(msg);
		
		Logger.trace("AdventureServer.addServerMessage(): message queue size ["+serverMessageQueue.size()+"]");

		synchronized(serverMessageQueue)
		{
			AdventureServer.serverMessageQueue.notifyAll();			
		}
	}
	
	public static void waitForMessage()
	{
		synchronized(serverMessageQueue)
		{
			try
			{
				AdventureServer.serverMessageQueue.wait(1000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}						
	}
	
	public static synchronized ServerMessage getServerMessage()
	{
		if (serverMessageQueue.isEmpty())
		{
			return null;
		}
		
		return serverMessageQueue.remove(0);
	}
	
	public static synchronized void handlePlayerMove(int entityId, Direction dir)
	{
		Object collidee;
		
		Player player = (Player) Game.g.getEntity(entityId);
		Logger.info("AdventureServer.handlePlayerMove(): handling move ["+dir+"]. pos ["+player.getTilePoint()+"]");
		if ((collidee = player.move(dir)) != null)
		{
			// handle tile collision
			if (collidee instanceof Tiles)
			{
				// MSM: handle special tile collision
			}
			// handle actor collision
			else if (collidee instanceof Actor)
			{
				Actor target = (Actor) collidee;
				
				player.attack(target);
			}
		}
		Logger.info("AdventureServer.handlePlayerMove(): moved to pos ["+player.getTilePoint()+"]");
	}

	
	public void runServer()
	{
		Logger.info("Starting " + AdventureServer.versionString);
		gameThread = new GameThread();
		gameThread.start();
		sockServer = new SocketServer(5555);
		sockServer.startListening();
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Game.MODE = Game.MODE_SERVER;
		
		AdventureServer server = new AdventureServer();
		server.runServer();
	}

	public static void sendActorStats(int entityId, ActorStat[] types, double[] stats)
	{
		addServerMessage(new ServerMessage(ServerMessage.ALL_CLIENTS, new ActorStats(entityId, types, stats)));		
	}

}
