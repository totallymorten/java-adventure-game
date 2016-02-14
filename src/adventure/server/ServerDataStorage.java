package adventure.server;

import tools.Logger;
import adventure.game.Game;
import adventure.game.TileMap;

public class ServerDataStorage
{
	public static ServerDataStorage s = new ServerDataStorage();
	
	public synchronized TileMap getMap()
	{
		return Game.g.map;
	}
}
