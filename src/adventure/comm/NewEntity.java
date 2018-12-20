package adventure.comm;

import adventure.game.entities.BaseEntity;

public class NewEntity extends CommandObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8007672766684318938L;
	
	public BaseEntity entity;
	
	
	public NewEntity(BaseEntity e)
	{
		this.entity = e;
	}
	
	@Override
	public void executeCmd()
	{
	}

}
