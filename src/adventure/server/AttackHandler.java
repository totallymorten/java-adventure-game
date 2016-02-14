package adventure.server;

import adventure.comm.Attack;
import adventure.comm.CommandObj;
import adventure.game.Game;

public class AttackHandler extends CommandHandler
{

	@Override
	public void handleCommand(String clientId, CommandObj commandObj)
	{
		Attack att = (Attack) commandObj;
		
		Game.g.handleAttack(att.attacker, att.defender);
	}

}
