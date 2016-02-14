package adventure.comm;

public class Attack extends CommandObj
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5357718172205602675L;
	
	public int attacker, defender;
	
	public Attack(int attId, int defId)
	{
		this.attacker = attId;
		this.defender = defId;
	}

	@Override
	public void executeCmd()
	{
		// TODO Auto-generated method stub
		
	}

}
