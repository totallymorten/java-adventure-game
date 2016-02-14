package adventure.comm;

import adventure.types.ActorStat;

public class ActorStats extends CommunicationObj
{

	public int entityId;
	public ActorStat[] types;
	public double[] stats;
	
	public ActorStats(int entityId, ActorStat[] types, double[] stats)
	{
		this.entityId = entityId;
		this.types = types;
		this.stats = stats;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -1569486360863123262L;
}
