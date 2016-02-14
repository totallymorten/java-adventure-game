package adventure.comm;

import java.awt.Point;

public class UpdateEntityPos extends CommandObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7171428323715957486L;
	
	public int entityId;
	public Point entityPos;
	
	public UpdateEntityPos(int entityId, Point entityPos)
	{
		this.entityId = entityId;
		this.entityPos = entityPos;
	}

	@Override
	public void executeCmd()
	{
		// TODO Auto-generated method stub
		
	}
}
