package adventure.client;

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
import adventure.game.entities.Actor.ActorState;
import adventure.game.entities.BaseEntity;
import adventure.game.entities.HealthEntity;
import adventure.types.ActorStat;
import tools.Logger;

public class ServerMsgHandler
{
	private static String LOG_PREFIX;
	
	public static void handleSrvMsg(CommunicationObj msg)
	{
		 LOG_PREFIX = "ServerMsgHandler.handleSrvMsg(): ";
		
		if (msg instanceof UpdateWorld)
		{
			Logger.trace(LOG_PREFIX + "Updating world");
			
			UpdateWorld uw = (UpdateWorld) msg;
			World.world.lightLevel = uw.lightLevel;
			World.world.currentCycle = uw.currentCycle;
		}
		else if (msg instanceof NewEntity)
		{
			NewEntity cmd = (NewEntity) msg;
			BaseEntity e = Game.g.getEntity(cmd.entity.entityId);
			
			Logger.trace("ServerMsgHandler.handleSrvMsg(): received new entity: ["+cmd.entity+"]");
			
			if (e != null)
			{
				Logger.debug("ServerMsgHandler.handleSrvMsg(): entity with id ["+e.entityId+"] already exists! type is ["+e.getClass().getName()+"]");
				return;
			}
			else
			{
				Game.g.client_addEntity(cmd.entity);				
			}
		}
		else if (msg instanceof UpdateEntityPos)
		{
			
			UpdateEntityPos cmd = (UpdateEntityPos) msg;
			BaseEntity e = findEntity(cmd.entityId);
			
			if (e != null)
			{
				Logger.trace(LOG_PREFIX + "updating entity with id ["+cmd.entityId+"] to position ["+cmd.entityPos+"]");
				e.setNewTilePoint(cmd.entityPos);
				
				if (e instanceof Actor)
				{
					((Actor)e).state = ActorState.MOVING;
				}
			}
		}
		else if (msg instanceof UpdateEntityHealth)
		{
			
			UpdateEntityHealth cmd = (UpdateEntityHealth) msg;
			HealthEntity e = (HealthEntity) findEntity(cmd.entityId);
			
			if (e != null)
			{
				Logger.trace(LOG_PREFIX + "updating entity with id ["+cmd.entityId+"] to health ["+cmd.health+"]");
				e.health = cmd.health;				
			}
		}
		else if (msg instanceof GameStats)
		{
			GameStats gs = (GameStats) msg;
			
			Game.g.totalGraveStones = gs.totalGraveStones;
			Game.g.totalZombies = gs.totalZombies;
			Game.g.killedGraveStones = gs.killedGraveStones;
			Game.g.killedZombies = gs.killedZombies;
			Game.g.killedMasterZombies = gs.killedMasterZombies;
			Game.g.totalMasterZombies = gs.totalMasterZombies;
		}
		else if (msg instanceof ActorStats)
		{
			ActorStats as = (ActorStats) msg;
			Actor a = (Actor) findEntity(as.entityId);
			
			if (a != null)
			{
				int i = 0;
				for (ActorStat stat : as.types)
				{
					Logger.trace(LOG_PREFIX + "updating ["+stat+"] for actor with id ["+as.entityId+"]");
					
					switch (stat)
					{
						case HEALTH:
						{
							a.health = as.stats[i];
						}
						break;
					
						case STAMINA:
						{
							a.stamina = as.stats[i];
						}
						break;
					
						case SPEED:
						{
							a.speed = as.stats[i];
						}
						break;

						case EXP_PROG:
						{
							a.experienceProgress = (int) as.stats[i];
						}
						break;
						
						case EXP_TOTAL:
						{
							a.totalExperience = (int) as.stats[i];
						}
						break;

						case LEVEL:
						{
							a.level = (int) as.stats[i];
						}
						break;
						
						case MAX_HEALTH:
						{
							a.maxHealth = as.stats[i];
						}
						break;
						
						case MAX_SPEED:
						{
							a.maxSpeed = as.stats[i];
						}
						break;
						
						case MAX_STAMINA:
						{
							a.maxStamina = as.stats[i];
						}
						break;

						case EXP_REQ_NEXT:
						{
							a.experienceReqNextLevel = (int) as.stats[i];
						}
						break;
					}
					i++;
				}
			}
		}
		else if (msg instanceof PlaySound)
		{
			Game.playSound(((PlaySound)msg).sound);
		}
		else if (msg instanceof RemoveEntity)
		{
			Logger.trace("ServerMsgHandler.handleSrvMsg(): processing message: ["+msg+"]");
			Game.g.removeEntity(((RemoveEntity)msg).entityId);
		}
	}
	
	private static BaseEntity findEntity(int entityId)
	{
		BaseEntity e;
		if ((e = Game.g.getEntity(entityId)) == null)
		{
			Logger.debug("ServerMsgHandler.findEntity(): entity with id ["+entityId+"] not found!");
			// if entity not found, request entity data
			AdventureClient.c.requestEntity(entityId);
			return null;
		}
		
		return e;
	}
}
