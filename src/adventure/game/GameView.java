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
		this.x = x;
		this.x = y;
		this.screen = screen;
		this.map = map;
	}
	
	public Point translateRealPosToViewRealPos(Point realPos)
	{
		return new Point(realPos.x - x, realPos.y - y);
	}
	
	int speedFactor = 1;
	Vector2D speed = new Vector2D(0, 0);
	int diffThreshold = 50;
	double tenE3 = Math.pow(10, 3);
	
	public void update(double period)
	{
		try
		{
			double sec = period / tenE3;
			
			speed.x = 0;
			speed.y = 0;
			
			int wantedx = Game.g.player.getRealPoint().x - Math.round(screen.width / 2);
			wantedx = (wantedx < 0) ? 0 : wantedx;
			wantedx = (wantedx + screen.width) > map.mapW*map.tileWidth ? map.mapW*map.tileWidth - screen.width : wantedx;
			
			int wantedy = Game.g.player.getRealPoint().y - Math.round(screen.height / 2);
			wantedy = (wantedy < 0) ? 0 : wantedy;
			wantedy = (wantedy + screen.height) > map.mapH*map.tileHeight ? map.mapH*map.tileHeight - screen.height : wantedy;
			
			int diffx = wantedx - x;
			int diffy = wantedy - y;
			
			if (diffx > diffThreshold || diffx < -diffThreshold)
				speed.x = diffx;
			
			if (diffy > diffThreshold || diffy < -diffThreshold)
				speed.y = diffy;
			
			x += speed.x * speedFactor * sec;
			y += speed.y * speedFactor * sec;
				
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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
