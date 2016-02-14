package adventure.comm;

import adventure.game.World;
import adventure.types.DayCycle;

public class UpdateWorld extends CommunicationObj
{
	public float lightLevel;
	public DayCycle currentCycle;
	
	public UpdateWorld(World world)
	{
		this.lightLevel = world.lightLevel;
		this.currentCycle = world.currentCycle;
	}

	@Override
	public String toString() 
	{
		return "UpdateWorld[lightLevel="+lightLevel+",currentCycle="+currentCycle+"]";
	}
	
	
}
