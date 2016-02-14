package adventure.comm;

import adventure.game.Game;

public class GameStats extends CommunicationObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8704989781600042043L;
	
	public GameStats(Game g)
	{
		this.totalGraveStones = g.totalGraveStones;
		this.totalZombies = g.totalZombies;
		this.killedGraveStones = g.killedGraveStones;
		this.killedZombies = g.killedZombies;
		this.totalMasterZombies = g.totalMasterZombies;
		this.killedMasterZombies = g.killedMasterZombies;
	}
	
	public int totalGraveStones = 0;
	public int killedGraveStones = 0;
	public int totalZombies = 0;
	public int killedZombies = 0;
	public int totalMasterZombies = 0;
	public int killedMasterZombies = 0;

}
