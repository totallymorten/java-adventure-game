package adventure.game;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import tools.Vector2D;

import adventure.game.entities.Updateable;
import adventure.types.RenderPriority;

public class GameView implements Updateable, Renderable
{
	public int x,y;
	private GameScreen screen;
	private TileMap map;
	
	public GameView(int x, int y, GameScreen screen, TileMap map)
	{
		this.screen = screen;
		this.map = map;
		this.x = getWantedX();
		this.y = getWantedY();
	}
	
	public Point translateRealPosToViewRealPos(Point realPos)
	{
		return new Point(realPos.x - x, realPos.y - y);
	}
	
	int speedFactor = 1;
	Vector2D speed = new Vector2D(0, 0);
	final int diffLowerThreshold = 50;
	final int MAX_SPEED = 500;
	double tenE3 = Math.pow(10, 3);
	
	private double deltax = 0.0;
	private double deltay = 0.0;
	
	public void update(double period)
	{
		try
		{
			double sec = period / tenE3;
			
			speed.x = 0;
			speed.y = 0;
			
			int wantedx = getWantedX();
			int wantedy = getWantedY();
			
			int diffx = wantedx - x;
			int diffy = wantedy - y;
			
			if (Math.abs(diffx) > diffLowerThreshold)
				speed.x = diffx;
			
			if (Math.abs(diffy) > diffLowerThreshold)
				speed.y = diffy;
			
			if (Math.abs(speed.x) > MAX_SPEED)
				speed.x = MAX_SPEED * Math.signum(speed.x);
			
			if (Math.abs(speed.y) > MAX_SPEED)
				speed.y = MAX_SPEED * Math.signum(speed.y);
			
			deltax = speed.x * speedFactor * sec;
			deltay = speed.y * speedFactor * sec;
			
//			if (deltax > 0 && ())
			
			x += deltax;
			y += deltay;
				
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
		
		return wantedx;
	}
	
	private int getWantedY()
	{
		int wantedy = Game.g.player.getRealPoint().y - Math.round(screen.height / 2);
		wantedy = (wantedy < 0) ? 0 : wantedy;
		wantedy = (wantedy + screen.height) > map.mapH*map.tileHeight ? map.mapH*map.tileHeight - screen.height : wantedy;

		return wantedy;
	}
	
	public void render(Graphics g)
	{
		Image img; 

		
		int viewH = 0, viewW = 0;
		int tilex = x < Game.g.tileWidth ? 0 : Math.round(x / Game.g.tileWidth - 1);
		int tiley = y < Game.g.tileHeight ? 0 : Math.round(y / Game.g.tileHeight - 1);
		
		int offsetX = x - tilex*Game.g.tileWidth;
		int offsetY = y - tiley*Game.g.tileHeight;
		
		for (int h = tiley; h <= (tiley + screen.tilesy); h++)
		{
			viewW = 0;
			//   for mapW
			for (int w = tilex; w <= (tilex + screen.tilesx); w++)
			{
				//img = (Game.g.images.get(tiles[h][w]))[(int)(TileMap.selectLightLevel(w, h) * 10)];
				
				img = map.getTileImage(Game.g.map.tiles[h][w], TileMap.selectLightLevel(w, h));
				
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
