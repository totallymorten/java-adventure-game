package tools;

import java.awt.Point;

import adventure.game.entities.BaseEntity;
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
	
	public static Direction getDirectionFromEntities(BaseEntity fromEntity, BaseEntity toEntity)
	{
		Point fromPoint = fromEntity.getTilePoint();
		Point toPoint = toEntity.getTilePoint();
		
		if (fromPoint.x < toPoint.x)
			return Direction.EAST;
		else if (fromPoint.x > toPoint.x)
			return Direction.WEST;
		else if (fromPoint.y < toPoint.y)
			return Direction.SOUTH;
		else if (fromPoint.y > toPoint.y)
			return Direction.NORTH;
		
		return null; // same point
	}
}
