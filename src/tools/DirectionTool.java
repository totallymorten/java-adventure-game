package tools;

import java.awt.Point;

import adventure.types.Direction;

public abstract class DirectionTool
{
	public static Point getPointInDirection(Point fromPoint, Direction dir)
	{
		if (dir == Direction.NORTH)
			return new Point(fromPoint.x, fromPoint.y-1);
		else if (dir == Direction.SOUTH)
			return new Point(fromPoint.x,fromPoint.y+1);
		else if (dir == Direction.EAST)
			return new Point(fromPoint.x+1,fromPoint.y);
		else
			return new Point(fromPoint.x-1,fromPoint.y);		
	}
}
