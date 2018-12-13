package adventure.game.entities.ai;

import java.util.ArrayList;

import adventure.comm.CommunicationObj;
import adventure.game.entities.Actor;
import adventure.game.entities.Updateable;
import adventure.types.Direction;
import tools.Logger;

public abstract class AI extends CommunicationObj implements Updateable
{
	private String className = this.getClass().getName();
	
	double ai_time_to_act = 0;
	double ai_delay = 0;
	Direction currentDirection = Direction.randomDirection();
	
	double ai_reaction_time; // ms between zombie actions
	double ai_reaction_chance; // chance that an action will occur
	double ai_reaction_delay; // the delay of an action does not occur

	Actor ai_actor;

	
	public AI(Actor a, double reaction_time, double reaction_chance, double reaction_delay)
	{
		this.ai_actor = a;
		
		this.ai_reaction_time = reaction_time;
		this.ai_reaction_chance = reaction_chance;
		this.ai_reaction_delay = reaction_delay;
	}
	
	@Override
	public void update(double ms)
	{
		
		if (ai_time_to_act > 0)
		{
			ai_time_to_act -= ms;
			return;
		}
		
		if (ai_delay > 0)
		{
			ai_delay -= ms;
			return;
		}
		
		double rand = Math.random();
		if (rand <= ai_reaction_chance)
		{

			ai_time_to_act = ai_reaction_time;
			act();
		}
		else
		{
			// delay a reaction
			ai_delay = ai_reaction_delay;
			return;
		}

	}
	
	protected abstract void act();
	
	protected int[] getCoordsInDirection(int[] currentCoords, Direction d)
	{
		if (d == Direction.NORTH)
			return new int[]{currentCoords[0], currentCoords[1]-1};
		else if (d == Direction.SOUTH)
			return new int[]{currentCoords[0], currentCoords[1]+1};
		else if (d == Direction.EAST)
			return new int[]{currentCoords[0]+1, currentCoords[1]};
		else
			return new int[]{currentCoords[0]-1, currentCoords[1]};
	}
	
	/**
	 * Selects a walking direction based on the target
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	protected ArrayList<Direction> directionOfTarget(int x, int y)
	{
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		
		if (x > ai_actor.tileX)
			dirs.add(Direction.EAST);
		else if (x < ai_actor.tileX)
			dirs.add(Direction.WEST);
		
		if (y > ai_actor.tileY)
			dirs.add(Direction.SOUTH);
		else if (y < ai_actor.tileY)
			dirs.add(Direction.NORTH);
		
		Logger.trace("directionOfTarget("+x+","+y+") = " + dirs);
		
		return dirs;
	}
	
	protected boolean checkForActorInReach(Actor other)
	{
		return ((other.tileX == ai_actor.tileX + 1 || other.tileX == ai_actor.tileX - 1) && (other.tileY == ai_actor.tileY))
		    || ((other.tileY == ai_actor.tileY + 1 || other.tileY == ai_actor.tileY - 1) && (other.tileX == ai_actor.tileX));		
	}


}
