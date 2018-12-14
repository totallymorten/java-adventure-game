package adventure.game.entities;


import adventure.server.AdventureServer;
import adventure.types.RenderPriority;

public class TimedEntity extends Entity
{
	private long entityTime;

	public TimedEntity(int x, int y, Object imgId, long ms, RenderPriority priority)
	{
		super(x, y, imgId, priority);
		
		this.entityTime = ms;
	}

	public TimedEntity(int x, int y, Object imgId, long ms)
	{
		super(x, y, imgId, RenderPriority.EFFECT);
		
		this.entityTime = ms;
	}

	@Override
	public void update(double ms)
	{
		super.update(ms);
		
		this.entityTime -= ms;

		if (this.entityTime <= 0)
		{
			AdventureServer.scheduleRemoveEntity(this);
		}
	}

	@Override
	public String toString() 
	{
		return super.toString() + "[entityTime:"+entityTime+"]";
	}
	
	

}
