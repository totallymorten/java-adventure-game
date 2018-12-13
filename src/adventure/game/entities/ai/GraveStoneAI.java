package adventure.game.entities.ai;

import adventure.game.Game;
import adventure.game.World;
import adventure.game.entities.Actor;
import adventure.types.DayCycle;
import adventure.types.Direction;
import tools.Logger;

public class GraveStoneAI extends AI
{

	public GraveStoneAI(Actor a)
	{
		super(a,5000,0.25,100);
	}

	@Override
	protected void act()
	{
		Logger.trace("In GraveStoneAI.act()");
		
		if (World.world.currentCycle != DayCycle.NIGHT)
			return;
		
		Direction d = Direction.randomDirection();
		int[] coords = getCoordsInDirection(new int[] {ai_actor.tileX,ai_actor.tileY}, d);
		
		
		if (Game.g.map.collision(coords[0], coords[1]) == null)
		{
			
			Game.g.createZombie(coords[0],coords[1]);
			Game.g.totalZombies++;
		}
	}

}
