package adventure.game;

import tools.Logger;
import adventure.game.entities.MasterZombie;
import adventure.game.entities.Updateable;
import adventure.properties.AdventureProperties;
import adventure.server.AdventureServer;
import adventure.types.DayCycle;
import adventure.types.Sounds;

public class World implements Updateable
{
	public float lightLevel = AdventureProperties.getFloat("world_lightLevel");
	
	public static World world = new World();

	private float dayLightLevel = AdventureProperties.getFloat("world_dayLightLevel");
	private float nightLightLevel = AdventureProperties.getFloat("world_nightLightLevel");

	private long dayTimeMs = AdventureProperties.getLong("world_dayTimeMs");
	private long nightTimeMs = AdventureProperties.getLong("world_nightTimeMs");

	private double transitionPercentage = AdventureProperties.getDouble("world_transitionPercentage");
	private int numberOfTransitions = AdventureProperties.getInt("world_numberOfTransitions");
	
	private double lightTransitionStep = (dayLightLevel - nightLightLevel) / numberOfTransitions; 

	private double dayTimeTransitionLimit = dayTimeMs * transitionPercentage;
	private double dayTransitionLength = (dayTimeMs - dayTimeTransitionLimit) / numberOfTransitions;

	private double nightTimeTransitionLimit = nightTimeMs * transitionPercentage;
	private double nightTransitionLength = (nightTimeMs - nightTimeTransitionLimit) / numberOfTransitions;
	
	
	private long currentTime = 0;
	private long totalTime = 0;
	
	public int nightCount = 0;

	public DayCycle currentCycle = DayCycle.DAY;
	
	@Override
	public void update(double ms)
	{
		currentTime += ms;
		totalTime += ms;
		
		if (currentCycle == DayCycle.DAY)
		{
			if (currentTime > dayTimeTransitionLimit)
			{
				currentCycle = DayCycle.EVENING;
				currentTime = 0;
				
				AdventureServer.playSound(Sounds.NIGHT);

				AdventureServer.updateWorld(this);		
			}
			
		}
		else if (currentCycle == DayCycle.EVENING)
		{
			if (currentTime > dayTransitionLength)
			{
				lightLevel -= lightTransitionStep;
				currentTime = 0;

				if (lightLevel <= nightLightLevel)
				{
					lightLevel = nightLightLevel;
					currentCycle = DayCycle.NIGHT;
					
					
					nightCount += 1;

					if (nightCount % 2 == 0)
						Game.g.handleSpawnMasterZombie();
				}
				
				AdventureServer.updateWorld(this);				
			}
		}
		else if (currentCycle == DayCycle.NIGHT)
		{
			if (currentTime > nightTimeTransitionLimit)
			{
				currentCycle = DayCycle.MORNING;
				currentTime = 0;
				
				AdventureServer.playSound(Sounds.ROOSTER);

				AdventureServer.updateWorld(this);
			}			
		}
		else if (currentCycle == DayCycle.MORNING)
		{
			if (currentTime > nightTransitionLength)
			{
				lightLevel += lightTransitionStep;
				currentTime = 0;
				Logger.trace("lightLevel = " + lightLevel);
				
				if (lightLevel >= dayLightLevel)
				{
					lightLevel = dayLightLevel;
					currentCycle = DayCycle.DAY;
				}
				
				AdventureServer.updateWorld(this);
			}			
		}
		
	}
	
}
