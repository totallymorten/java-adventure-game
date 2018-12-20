package adventure.server;

import adventure.comm.CommandObj;
import adventure.comm.UpdateEntityPos;
import adventure.game.Game;
import adventure.game.entities.BaseEntity;
import tools.Logger;

public class UpdateEntityPosHandler extends CommandHandler
{

	private String className = this.getClass().getName();
	
	@Override
	public void handleCommand(String clientId, CommandObj commandObj)
	{
		
		UpdateEntityPos cmd = (UpdateEntityPos) commandObj;
		
		Logger.trace(className + "handleCommand(): updating entity with id ["+cmd.entityId+"] to position ["+cmd.entityPos+"]");
		
		BaseEntity e = Game.g.getEntity(cmd.entityId);
		e.setNewTilePoint(cmd.entityPos);
	}

}
