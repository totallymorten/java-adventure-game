package adventure.game.entities.ai.astar;

import java.awt.Point;
import java.util.ArrayList;

public class NodeQueue
{
	ArrayList<Node> queue = new ArrayList<Node>();
	
	public int size()
	{
		return queue.size();
	}
	
	public Node findNode(Point nodePoint)
	{
		for (Node n : queue)
		{
			if (n.point.equals(nodePoint))
				return n;
		}
		
		return null;
	}

	public Node delete(Point p)
	{
		Node n = findNode(p);
		
		if (n != null)
			queue.remove(n);
		
		return n;
	}	

	public void addNode(Node n)
	{
		queue.add(n);
	}
}
