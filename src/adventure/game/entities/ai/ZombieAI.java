package adventure.game.entities.ai;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import adventure.game.Game;
import adventure.game.TileMap;
import adventure.game.World;
import adventure.game.entities.Actor;
import adventure.game.entities.BaseEntity;
import adventure.game.entities.TileImageEntity;
import adventure.game.entities.MasterZombie;
import adventure.game.entities.Player;
import adventure.game.entities.Zombie;
import adventure.game.entities.ai.astar.AStarService;
import adventure.server.AdventureServer;
import adventure.types.DayCycle;
import adventure.types.Direction;
import adventure.types.Sounds;
import tools.Logger;

public class ZombieAI extends AI
{
	private String className = this.getClass().getName();
	
	double ai_delay = 0;
	public double ai_zombie_night_vision = 4;
	public double ai_zombie_day_vision = 10;
	final double ai_change_direction_chance = 0.05;
	final double ai_night_reaction_time = 500;
	
	final int ai_pathfinding_recalc = 4;
	int ai_next_pathfinding = 0;
	
	Actor target = null;
	
	final int ai_cons_pathfinding = 3;
	int ai_next_search = ai_cons_pathfinding;

	ZombieAIState state = ZombieAIState.SEARCHING;
	
	Direction currentDirection;
	
	private Zombie z;

    List<Point> path = null;
	
	public ZombieAI(Zombie z)
	{
		super(z,1000,0.8,200);

		currentDirection = Direction.randomDirection();

		this.z = z;
	}
	

	@Override
	protected void act()
	{
		ai_next_pathfinding--;
		
		if (World.world.currentCycle == DayCycle.NIGHT)
			ai_time_to_act = ai_night_reaction_time;
		
		Logger.trace("ZOMBIE AI: ACT!");
		Logger.trace("ai_next_pathfinding = " + ai_next_pathfinding);
		
		if (target != null && (!target.active || target.isDead()))
		{
			target = null;
			state = ZombieAIState.SEARCHING;
		}
		
		if (state == ZombieAIState.SEARCHING)
		{
			Logger.trace(className + ".act(): Zombie searching");
			
			searchBehavior();
		}
		else if (state == ZombieAIState.PATHFINDING)
		{
			Logger.trace("Zombie pathfinding");
			
			Direction d = nextMovePathFinding(target.getTilePoint());
			
			if (state == ZombieAIState.WAITING_FOR_ASTAR)
			{
				// dont wait too long
				ai_time_to_act = 0;
				return;
			}
			
			// if pathfinding unsuccessfull, fall back to default search behavior
			if (d == null)
			{
				Logger.trace("Pathfinding unsuccessful, falling back to search");
				state = ZombieAIState.SEARCHING;
				ai_time_to_act = 0;
				return;
			}
			
			// move using pathfinding
			if (ai_actor.move(d) != null)
			{
				// if collision is detected, fall back to default search behavior
				path = null;
				state = ZombieAIState.SEARCHING;
				ai_time_to_act = 0; // forcing action next update

				Logger.trace("Collision while pathfinding, falling back to search");
			}
		}
		else if (state == ZombieAIState.WAITING_FOR_ASTAR)
		{
			
			if ((path = checkForAstarResult()) != null)
			{
				// no path was found, not possible - maybe blocked on all sides
				if (path.isEmpty())
				{				
					Logger.trace("path was null or empty, searching instead");
					ai_time_to_act = 0; // forcing action next update
					state = ZombieAIState.SEARCHING;
				}
				else
				{
					state = ZombieAIState.PATHFINDING;	
					ai_time_to_act = 0; // forcing action next update
				}

				ai_next_pathfinding = ai_pathfinding_recalc;

			}
			else
				ai_time_to_act = 0; // forcing update next cycle
		}
	}
	
	private List<Point> checkForAstarResult()
	{
		return AStarService.checkResult(this.ai_actor);
	}
	
	private Direction nextMovePathFinding(Point destination)
	{
		Point pathPoint = null;
		
		// generating new path
		if (ai_next_pathfinding <= 0)
		{
			if (ai_next_search <= 0)
			{
				// time to try and search again.
				ai_next_search = ai_cons_pathfinding;
				return null;
			}
			
			Logger.trace("calculating new pathfinding path");
			calcPath(destination);
			return null;
		}

		// using existing path
		else if (path != null && !path.isEmpty())
		{
			pathPoint = path.remove(0);
			Logger.trace("got next pathfinding move");
		}
		else
		{
			return null;
		}
		
		// if last move, reset behavior
		if (path.isEmpty())
			state = ZombieAIState.SEARCHING;

		return directionOfTarget(pathPoint.x, pathPoint.y).get(0);
	}
	
	private void calcPath(Point destination)
	{
		ai_next_search--;
		
		if (AStarService.runAstarSearch(this.ai_actor, ai_actor.getTilePoint(), destination))
		{
			Logger.trace("zombie ["+this.ai_actor+"] successfully registered for search");
			state = ZombieAIState.WAITING_FOR_ASTAR;
		}
		else
		{
			Logger.trace("Zombie not able to register for pathfinding. Searching instead");
			ai_time_to_act = 0; // forcing action next update
			state = ZombieAIState.SEARCHING;			
		}
	}

	private boolean handleActor(Actor tracked)
	{
		// if is enemy??
		if (!(tracked instanceof Player))
			return false;
		
		Logger.trace(className + ".act(): checking actor proximity ["+tracked+"]");

		if (checkForActorInReach(tracked))
		{
			target = tracked;
			Logger.trace(className + ".handleActor(): ZOMBIE AI: ATTACKING!");
			z.attack(tracked);
			return true;
		}
		// check if actor is in view
		else if (checkForActorInView(tracked))
		{
			if (target == null && z instanceof MasterZombie)
				AdventureServer.playSound(Sounds.MASTERZOMBIE);
				
			target = tracked;
			
			
			Logger.trace(className + ".handleActor(): Zombie tracking target ["+target+"]");
			ArrayList<Direction> dirs = directionOfTarget(tracked.tileX, tracked.tileY);
			
			for (Direction d : dirs)
			{
				if (ai_actor.move(d) == null)
					return true;
			}

			// going to pathfinding state. Can see man, but cannot reach him
			state = ZombieAIState.PATHFINDING;
			
			return true;
		}		
		
		Logger.trace(className + ".handleActor(): not tracking actor");
		
		return false;
	}
	
	private void searchForTarget()
	{
		
		// now do something
		List<BaseEntity> entities = Game.g.getEntities();
		
		Logger.trace(className + ".searchForTarget(): checking ["+entities.size()+"] players");

		for (BaseEntity e : entities)
		{
			if (e instanceof Actor && handleActor((Actor)e))
				break;
		}
		
	}
	
	private void searchBehavior()
	{
		Logger.trace(className + ".searchBehavior(): target is ["+target+"]");
		
		if (target != null)
		{
			if (!handleActor(target))
				target = null;			
		}
		else
			searchForTarget();
		
		// change direction?
		if (target == null)
		{
			if (Math.random() < ai_change_direction_chance)
			{
				Logger.trace("Zombie changing direction");
				currentDirection = Direction.randomDirection();
			}
			// moving in dedicated direction & change direction if blocked
			else
			{
				if (ai_actor.move(currentDirection) != null)
					currentDirection = Direction.randomDirection(); 
			}						
		}
		
	}
	
	private boolean checkForActorInView(Actor a)
	{
		double dist = TileMap.distance(z.getTilePoint(), a.getTilePoint());
		
		Logger.trace("distance to player = " + dist);
		
		if (World.world.currentCycle == DayCycle.NIGHT && dist <= ai_zombie_night_vision)
			return true;
		else
			return (dist <= ai_zombie_day_vision);
	}

	
}
