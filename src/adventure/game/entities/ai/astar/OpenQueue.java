package adventure.game.entities.ai.astar;

import java.awt.Point;

import tools.Logger;

public class OpenQueue extends NodeQueue
{
	Point goal;
	
	public OpenQueue(Point goal)
	{
		this.goal = goal;
	}
	
	public void addNode(Node node)
	{
		int i = 0;
		for (Node n : queue)
		{
			if (n.getCostToGoal(goal) > node.getCostToGoal(goal))
				break;
			i++;
		}
		queue.add(i,node);
		Logger.trace("Adding " + node + " to open queue");
		Logger.trace("Open queue now: " + queue);
	}
	
	public Node removeFirst()
	{
		if (queue.isEmpty())
			return null;
		
		return queue.remove(0);
	}
}
