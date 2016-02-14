package adventure.comm;

import adventure.game.entities.Player;

public class PlayerData extends CommandObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8007672766684318938L;
	
	public Player player;
	
	
	public PlayerData(Player p)
	{
		this.player = p;
	}
	
	@Override
	public void executeCmd()
	{
	}

}
