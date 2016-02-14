package adventure.comm;

public class RemoveEntity extends CommunicationObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6716118271031983629L;
	
	public int entityId;
	
	public RemoveEntity(int entityId)
	{
		this.entityId = entityId;
	}

	@Override
	public String toString() 
	{
		return "RemoveEntity[entityId:"+entityId+"]";
	}
	
	
}
