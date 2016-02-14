package adventure.game.entities.ai.astar;

import java.awt.Point;
import java.util.List;

public class AStarServiceRunner implements Runnable
{
	public boolean isBusy = true;
	
	public Point start, end;
	public List<Point> returnValue;

	
	@Override
	public void run()
	{
		returnValue = AStar.aStarSearch(start, end);
		isBusy = false;
	}

}
