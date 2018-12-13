package adventure.game.entities.ai.astar;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import adventure.game.entities.Actor;
import adventure.game.entities.Updateable;
import tools.Logger;

public class AStarService implements Updateable
{
	public static int threadCount = Runtime.getRuntime().availableProcessors();
	public static AStarService service = new AStarService();
	
	public static HashMap<Actor, AStarServiceRunner> threads = new HashMap<Actor, AStarServiceRunner>();
	
	
	public static boolean runAstarSearch(Actor a, Point start, Point end)
	{
		if (threads.size() >= threadCount)
		{
			Logger.info("AStar refused. Running AStars "+threads.size()+"/"+threadCount);
			return false; // no room for running more AStars
		}
		
		AStarServiceRunner runner = new AStarServiceRunner();
		runner.start = start;
		runner.end = end;
		
		threads.put(a, runner);
		(new Thread(runner)).start();

		Logger.info("AStar accepted. Running AStars "+threads.size()+"/"+threadCount);
		
		return true;
	}
	
	public static List<Point> checkResult(Actor a)
	{
		if (threads.get(a) == null)
		{
			Logger.error("Actor ["+a+"] not found in AStarService HashMap");
			Logger.error("Map now ["+threads+"]");
			System.exit(1);
		}
		
		
		
		if (threads.get(a).isBusy)
			return null;
		
		return threads.remove(a).returnValue;
	}
	

	@Override
	public void update(double ms)
	{
		synchronized (threads)
		{
			threads.notifyAll();			
		}
	}
}
