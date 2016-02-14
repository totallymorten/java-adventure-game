package adventure.comm;

import adventure.game.entities.Entity;

public class NewEntity extends CommandObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8007672766684318938L;
	
	public Entity entity;
	
	
	public NewEntity(Entity e)
	{
		this.entity = e;
	}
	
	@Override
	public void executeCmd()
	{
	}

}
