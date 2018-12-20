package adventure.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import adventure.game.entities.Updateable;
import adventure.types.RenderPriority;
import tools.Vector2D;

public class GameView implements Updateable, Renderable
{
	private double x,y;
	public GameScreen screen;
	private TileMap map;
	
	public GameView(int x, int y, GameScreen screen, TileMap map)
	{
		this.screen = screen;
		this.map = map;
		this.x = getWantedX();
		this.y = getWantedY();
	}
	
	public int setX(double newX)
	{
		this.x = newX;
		
		return (int)Math.round(this.x);
	}
	
	public int getXInt()
	{
		return (int)Math.round(this.x);
	}

	public double getX()
	{
		return this.x;
	}

	public int setY(double newY)
	{
		this.y = newY;
		
		return (int)Math.round(this.y);
	}
	
	public int getYInt()
	{
		return (int)Math.round(this.y);
	}

	public double getY()
	{
		return this.y;
	}

	public Point translateRealPosToViewRealPos(Point realPos)
	{
		return new Point(realPos.x - getXInt(), realPos.y - getYInt());
	}
	
	double speedFactor = 2;
	Vector2D speed = new Vector2D(0, 0);
	final double diffLowerThreshold = 0.4;//Game.g.screen.width / 20;
	final double MAX_SPEED = 2000;
	final double MIN_SPEED = 10;
	double tenE3 = Math.pow(10, 3);
	
	private double deltax = 0.0;
	private double deltay = 0.0;

	boolean diffPrinted = false;
	int count = 0;
	public void update(double period)
	{
		try
		{
			double sec = period / tenE3;
			
			speed.x = 0;
			speed.y = 0;
			
			int wantedx = getWantedX();
			int wantedy = getWantedY();
			
			double diffx = (double)wantedx - x;
			double diffy = (double)wantedy - y;
			
			if (Math.abs(diffx) > diffLowerThreshold)
				speed.x = diffx;
			
			if (Math.abs(diffy) > diffLowerThreshold)
				speed.y = diffy;
			
//			if (speed.x == 0 && speed.y == 0)
//			{
//				if (diffPrinted)
//				{
//					System.out.println("count = " + count);
//					System.out.println("##########");
//					count = 0;
//					diffPrinted = false; 
//				}
//				
//				return;
//				
//			}
			
//			if (Math.abs(speed.x + speed.y) > 0)
//			{
//				//System.out.println("");
//			}


			if (Math.abs(speed.x) > MAX_SPEED)
				speed.x = MAX_SPEED * Math.signum(speed.x);
			
			if (Math.abs(speed.y) > MAX_SPEED)
				speed.y = MAX_SPEED * Math.signum(speed.y);

			if (Math.abs(speed.x) < MIN_SPEED && speed.x != 0)
				speed.x = MIN_SPEED * Math.signum(speed.x);
			
			if (Math.abs(speed.y) < MIN_SPEED && speed.y != 0)
				speed.y = MIN_SPEED * Math.signum(speed.y);

			deltax = speed.x * speedFactor * sec;
			deltay = speed.y * speedFactor * sec;

//			if (!diffPrinted)
//			{
//				System.out.println("##########");
//				System.out.println("diffy = " + diffy);
//				System.out.println("deltay = " + deltay);
//				diffPrinted = true;
//			}
//			
//			if (diffPrinted)
//			{
//				System.out.println("diffy = " + diffy);
//				System.out.println("deltay = " + deltay);				
//			}
			
			
			x += deltax;
			y += deltay;
			
//			count++;
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private int getWantedX()
	{
		int wantedx = Game.g.player.getRealPoint().x - Math.round(screen.width / 2);
		wantedx = (wantedx < 0) ? 0 : wantedx;
		wantedx = (wantedx + screen.width) > map.mapW*map.tileWidth ? map.mapW*map.tileWidth - screen.width : wantedx;
		
		return (wantedx - (wantedx % Game.g.map.tileWidth));
	}
	
	private int getWantedY()
	{
		int wantedy = Game.g.player.getRealPoint().y - Math.round(screen.height / 2);
		wantedy = (wantedy < 0) ? 0 : wantedy;
		wantedy = (wantedy + screen.height) > map.mapH*map.tileHeight ? map.mapH*map.tileHeight - screen.height : wantedy;

		return (wantedy - (wantedy % Game.g.map.tileHeight));
	}
	
	public void render(Graphics g)
	{
		Image img; 

		
		int viewH = 0, viewW = 0;
		int tilex = x < Game.g.tileWidth ? 0 : (int)Math.floor(x / Game.g.tileWidth) - 1;
		int tiley = y < Game.g.tileHeight ? 0 : (int)Math.floor(y / Game.g.tileHeight) - 1;
		
//		if ((tilex + screen.tilesx) >= map.mapW)
//			tilex--;
//		
//		if ((tiley + screen.tilesy) >= map.mapH)
//			tiley--;
		
		int offsetX = getXInt() - tilex*Game.g.tileWidth;
		int offsetY = getYInt() - tiley*Game.g.tileHeight;
		
		for (int h = tiley; (h <= (tiley + screen.tilesy)) && (h < Game.g.map.mapH); h++)
		{
			viewW = 0;
			//   for mapW
			for (int w = tilex; (w <= (tilex + screen.tilesx)) && (w < Game.g.map.mapW); w++)
			{
				//img = (Game.g.images.get(tiles[h][w]))[(int)(TileMap.selectLightLevel(w, h) * 10)];
				
				float lightLevel = TileMap.selectLightLevel(w, h);
				
				if (lightLevel <= 0)
				{
					viewW++;
					continue; // don't render totally dark tiles. Will be covered by darkness					
				}
				
				img = map.getTileImage(Game.g.map.tiles[h][w], 1);
				
				g.drawImage(img,viewW*Game.g.map.tileWidth - offsetX,viewH*Game.g.map.tileHeight - offsetY,Game.g.map.tileWidth,Game.g.map.tileHeight, null);

				viewW++;

			}
			
			viewH++;
		}
	}

	@Override
	public RenderPriority getRenderPriority()
	{
		return RenderPriority.BACKGROUND;
	}
}
