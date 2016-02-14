package adventure.server;

import java.util.List;

import tools.Logger;
import adventure.comm.CommandObj;
import adventure.comm.NewEntity;
import adventure.comm.PlayerData;
import adventure.comm.RequestData;
import adventure.comm.UpdateWorld;
import adventure.game.Game;
import adventure.game.World;
import adventure.game.entities.Entity;
import adventure.game.entities.Player;
import adventure.types.RequestDataType;

public class RequestDataHandler extends CommandHandler
{

	private String className = this.getClass().getName();
	
	@Override
	public void handleCommand(String clientId, CommandObj commandObj)
	{
		
		RequestData rd = (RequestData) commandObj;
		Logger.trace("RequestDataHandler.handleCommand(): processing command ["+rd.toString()+"]");
		
		if (rd.dataType == RequestDataType.MAP)
		{
			Logger.trace("RequestDataHandler.handleCommand(): adding server message");
			AdventureServer.addServerMessage(new ServerMessage(clientId, ServerDataStorage.s.getMap()));
		}
		else if (rd.dataType == RequestDataType.PLAYER)
		{
			Player player = Game.g.getPlayer(clientId);
			if (player == null)
			{
				Logger.info(className + ".handleCommand(): creating new player for client ["+clientId+"]");
				player = new Player(0, 0, null);				
				Game.g.addPlayer(clientId, player);
				Logger.info(className + ".handleCommand(): entity id ["+player.entityId+"]");
			}
			else
			{
				player.active = true;
				Logger.info(className + ".handleCommand(): found existing player for client ["+clientId+"]");				
				Logger.info(className + ".handleCommand(): ["+player+"]");
				
				Game.g.addEntity(player);
			}
			
			player.username = clientId;
			
			AdventureServer.addServerMessage(new ServerMessage(clientId, new PlayerData(player)));
			AdventureServer.addServerMessage(new ServerMessage(clientId, new NewEntity(player), true));
		}
		else if (rd.dataType == RequestDataType.WORLD)
		{
			AdventureServer.addServerMessage(new ServerMessage(clientId, new UpdateWorld(World.world)));
		}
		else if (rd.dataType == RequestDataType.ALL_ENTITIES)
		{
			List<Entity> entities = Game.g.getEntities();
			
			for (Entity e : entities)
			{
				AdventureServer.addServerMessage(new ServerMessage(clientId, new NewEntity(e)));
			}
		}
		else if (rd.dataType == RequestDataType.ENTITY)
		{
			int entityId = Integer.parseInt(rd.requestInfo);
			Entity e = Game.g.getEntity(entityId);
			
			if (e == null)
				Logger.error(className + "handleCommand(ENTITY): entity with id: ["+entityId+"] not found!!");
			else
				AdventureServer.addServerMessage(new ServerMessage(clientId, new NewEntity(e)));
		}
	}

}
