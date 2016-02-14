package adventure.types;

public enum Direction
{
	NORTH, SOUTH, EAST, WEST;
	
	public static Direction randomDirection()
	{
		double rand = Math.random();
		
		if (rand >= 0 && rand < 0.25)
			return Direction.NORTH;
		else if (rand >= 0.25 && rand < 0.5)
			return Direction.SOUTH;
		else if (rand >= 0.5 && rand < 0.75)
			return Direction.EAST;
		else
			return Direction.WEST;
	}

}
