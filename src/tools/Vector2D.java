package tools;

public class Vector2D
{
	public double x,y;
	
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString()
	{
		return String.format("Vector2D[%10.2f,%10.2f]", x,y);
	}
	
	

}
