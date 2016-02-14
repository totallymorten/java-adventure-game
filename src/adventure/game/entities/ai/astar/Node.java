package adventure.game.entities.ai.astar;

import java.awt.Point;
import java.util.ArrayList;

import adventure.game.Game;
import adventure.game.TileMap;
import adventure.game.entities.Actor;
import adventure.game.entities.Player;

public class Node
{
	public Node prevNode, nextNode;
	double costToGoal = -1, costFromStart = -1;
	public Point point;
	
	public Node (Node prevNode, Point point)
	{
		this.prevNode = prevNode;
		this.point = point;
	}
	
	public double getCostToGoal(Point goal)
	{
		if (costToGoal == -1 && goal != null)
			costToGoal = TileMap.distance(point, goal);
			
		return costToGoal;
	}
	
	public double getCostFromStart()
	{
		if (prevNode == null)
			return 0;
		else
			return 1 + prevNode.getCostFromStart();
	}
	
	public ArrayList<Point> buildPath()
	{
		return buildPath(new ArrayList<Point>());
	}
	
	public ArrayList<Point> buildPath(ArrayList<Point> path)
	{
		path.add(0,point);
		
		// while has prev node getPoint and add to array + prev.buildPath()
		if (prevNode != null)
			return prevNode.buildPath(path);
		else
		{
			path.remove(0);
			return path;			
		}
	}
	
	public ArrayList<Node> getNeighBors()
	{
		ArrayList<Node> nodes = new ArrayList<Node>();
		Point newPoint;
		Object collidee;
		
		newPoint = new Point(point.x + 1, point.y);
		if (checkNewPoint(newPoint))
			nodes.add(new Node(this,newPoint));
		
		newPoint = new Point(point.x - 1, point.y);
		if (checkNewPoint(newPoint))
			nodes.add(new Node(this,newPoint));

		newPoint = new Point(point.x, point.y + 1);
		if (checkNewPoint(newPoint))
			nodes.add(new Node(this,newPoint));

		newPoint = new Point(point.x, point.y - 1);
		if (checkNewPoint(newPoint))
			nodes.add(new Node(this,newPoint));

		return nodes;
	}

	private boolean checkNewPoint(Point newPoint)
	{
		Object collidee = Game.g.map.collision(newPoint.x, newPoint.y);
		return (collidee == null || collidee instanceof Player);
	}
	
	public boolean equals(Node otherNode)
	{
		return (point.equals(otherNode.point));
	}

	@Override
	public String toString()
	{
		return "Node("+point.x+","+point.y+","+getCostFromStart()+","+getCostToGoal(null)+")";
	}
	
	

}
