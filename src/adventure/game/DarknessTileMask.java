package adventure.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import adventure.types.RenderPriority;

public class DarknessTileMask implements Renderable {

	public DarknessTileMask() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RenderPriority getRenderPriority() {
		return RenderPriority.FOREGROUND;
	}

	@Override
	public void render(Graphics g) {
		Image img; 

		
		int viewH = 0, viewW = 0;
		int tilex = Game.g.view.getXInt() < Game.g.tileWidth ? 0 : (int)Math.floor(Game.g.view.getXInt() / Game.g.tileWidth) - 1;
		int tiley = Game.g.view.getYInt() < Game.g.tileHeight ? 0 : (int)Math.floor(Game.g.view.getYInt() / Game.g.tileHeight) - 1;
		
//		if ((tilex + screen.tilesx) >= map.mapW)
//			tilex--;
//		
//		if ((tiley + screen.tilesy) >= map.mapH)
//			tiley--;
		
		int offsetX = Game.g.view.getXInt() - tilex*Game.g.tileWidth;
		int offsetY = Game.g.view.getYInt() - tiley*Game.g.tileHeight;
		
		for (int h = tiley; (h <= (tiley + Game.g.view.screen.tilesy)) && (h < Game.g.map.mapH); h++)
		{
			viewW = 0;
			//   for mapW
			for (int w = tilex; (w <= (tilex + Game.g.view.screen.tilesx)) && (w < Game.g.map.mapW); w++)
			{
				//img = (Game.g.images.get(tiles[h][w]))[(int)(TileMap.selectLightLevel(w, h) * 10)];
				
				float lightLevel = TileMap.selectLightLevel(w, h);
				
				lightLevel = Math.abs(1 - lightLevel);
				
				
				g.setColor(new Color(0,0,0,Math.round(lightLevel * 255)));
				g.fillRect(viewW*Game.g.map.tileWidth - offsetX, viewH*Game.g.map.tileHeight - offsetY, Game.g.map.tileWidth, Game.g.map.tileHeight);

				
				viewW++;
				
			}
			
			viewH++;
		}
	}



}
