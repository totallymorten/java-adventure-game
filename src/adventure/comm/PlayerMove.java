package adventure.comm;

import adventure.types.Direction;

public class PlayerMove extends CommandObj
{
	private static final long serialVersionUID = 2331931844587789379L;
	
	public int entityId;
	public Direction dir;
	
	public PlayerMove(int entityId, Direction dir)
	{
		this.entityId = entityId;
		this.dir = dir;
	}

	@Override
	public void executeCmd()
	{
		// TODO Auto-generated method stub
		
	}

}
