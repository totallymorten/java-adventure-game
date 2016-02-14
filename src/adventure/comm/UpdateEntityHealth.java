package adventure.comm;

public class UpdateEntityHealth extends CommunicationObj
{
	public UpdateEntityHealth(int entityId, double health)
	{
		this.entityId = entityId;
		this.health = health;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = -5184147296650668191L;
	
	public int entityId;
	public double health;
	
	
}
