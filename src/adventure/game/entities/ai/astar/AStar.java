package adventure.game.entities.ai.astar;

import java.awt.Point;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import adventure.engine.AdventureGame;
import adventure.game.Game;

import tools.Logger;
import tools.TimeTool;

public abstract class AStar
{
	public static double runTimeLimit = Game.g.periodMs * 0.8;
	public static double costLimit = 6;
	public static double time;
	
	public static List<Point> aStarSearch(Point start, Point end)
	{
		Logger.trace("aStarSearch("+start+","+end+")");
		
		double newCost = 0.0;
		Node bestNode = null;
		
		Node startNode = new Node(null, start);
		OpenQueue open = new OpenQueue(end);
		open.addNode(startNode);
		NodeQueue closed = new NodeQueue();
		
		
		
		time = TimeTool.getTime();
		while (open.size() != 0 && newCost <= costLimit)
		{
			checkTime();
			
			Logger.trace("aStarSearch(): last open.size() loop start: " + TimeTool.timeDiff(time) + " ms ago");
			
			bestNode = open.removeFirst();
			Logger.trace("aStarSearch(): processing next node: " + bestNode);
			
			// if end has been reached
			if (bestNode.point.equals(end))
			{
				ArrayList<Point> path = bestNode.buildPath();
				
				Logger.trace("aStarSearch(): returning " + path);
				return path;
				
			}
			
			else
			{
				ArrayList<Node> neighb = bestNode.getNeighBors();
				for (Node newNode : neighb)
				{
					checkTime();
					
					Logger.trace("aStarSearch(): start processing a neighbor: " + TimeTool.timeDiff(time) + " ms has passed");
					newCost = newNode.getCostFromStart();
					Logger.trace("aStarSearch(): after calculating start cost: " + TimeTool.timeDiff(time) + " ms has passed");
					newNode.getCostToGoal(end);
					Logger.trace("aStarSearch(): after calculating goal cost: " + TimeTool.timeDiff(time) + " ms has passed");

					Logger.trace("aStarSearch(): processing neighbor " + newNode);
					Logger.trace("aStarSearch(): cost is " + newCost);
					Node oldVer;
					
					if ((oldVer = open.findNode(newNode.point)) != null 
					  && oldVer.getCostFromStart() <= newCost)
						continue;
					else if ((oldVer = closed.findNode(newNode.point)) != null 
							  && oldVer.getCostFromStart() <= newCost)
						continue;
					else
					{
						closed.delete(newNode.point);
						open.delete(newNode.point);
						
						open.addNode(newNode);
					}
				}
			} // end else
			closed.addNode(bestNode);
			
		}
		
		ArrayList<Point> path = bestNode.buildPath();
		return path;
		
	} // end aStarSearch()
	
	private static void checkTime()
	{
		synchronized (AStarService.threads)
		{
			if (AdventureGame.cycleTime() > runTimeLimit)
				try
				{
					Logger.trace("AStar search going to sleep. Cycle time = " + AdventureGame.cycleTime());
					AStarService.threads.wait();
					Logger.trace("AStar awake again. Cycletime = " + AdventureGame.cycleTime());
					time = TimeTool.getTime();
				} catch (InterruptedException e)
				{
					Logger.error("Error in AStar.checkTime(): " + e);
					System.exit(1);
				}			
		}
	}
}
