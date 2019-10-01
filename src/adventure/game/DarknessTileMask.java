package adventure.game;

import java.awt.Color;
import java.awt.Graphics;

import adventure.types.RenderPriority;

public class DarknessTileMask implements Renderable {

	
	private float[][] darknessMap = new float[Game.g.map.mapH][Game.g.map.mapW];
	
	public DarknessTileMask() {
		
		// initializing float map
		
		for (int h = 0; h < Game.g.map.mapH; h++)
		{
			for (int w = 0; w < Game.g.map.mapW; w++)
			{
				darknessMap[h][w] = 0.0f;
			}
		}
	}

	@Override
	public RenderPriority getRenderPriority() {
		return RenderPriority.FOREGROUND;
	}

	@Override
	public void render(Graphics g) {

		
		int viewH = 0, viewW = 0;
		int tilex = Game.g.view.getXInt() < Game.g.tileWidth ? 0 : (int)Math.floor(Game.g.view.getXInt() / Game.g.tileWidth) - 1;
		int tiley = Game.g.view.getYInt() < Game.g.tileHeight ? 0 : (int)Math.floor(Game.g.view.getYInt() / Game.g.tileHeight) - 1;
		
		
		int offsetX = Game.g.view.getXInt() - tilex*Game.g.tileWidth;
		int offsetY = Game.g.view.getYInt() - tiley*Game.g.tileHeight;
		
		
		// handling world lighting
		float worldTargetDarknessLevel = Math.abs(1 - World.world.lightLevel);
		float worldDarknessDiff = worldTargetDarknessLevel - World.world.currentDarknessLevel;
		
		if (worldDarknessDiff < 0.01f)
			World.world.currentDarknessLevel = worldTargetDarknessLevel;
		else
			World.world.currentDarknessLevel += (0.003f * Math.signum(worldDarknessDiff));
			
		
		for (int h = tiley; (h <= (tiley + Game.g.view.screen.tilesy)) && (h < Game.g.map.mapH); h++)
		{
			viewW = 0;
			//   for mapW
			for (int w = tilex; (w <= (tilex + Game.g.view.screen.tilesx)) && (w < Game.g.map.mapW); w++)
			{
				//img = (Game.g.images.get(tiles[h][w]))[(int)(TileMap.selectLightLevel(w, h) * 10)];
				
				float lightLevel = TileMap.selectLightLevel(w, h);
				
				float targetDarknessLevel = Math.abs(1 - lightLevel);
				
				if (targetDarknessLevel == worldTargetDarknessLevel)
				{
					darknessMap[h][w] = worldTargetDarknessLevel;					
				}
				else
				{
					float darknessDiff = targetDarknessLevel - darknessMap[h][w];
					
					if (Math.abs(darknessDiff) < 0.01f)
					{
						darknessMap[h][w] = targetDarknessLevel;
					}
					else
					{
						darknessMap[h][w] += (0.003f * Math.signum(darknessDiff));
					}
					
					
				}

				g.setColor(new Color(0,0,0,darknessMap[h][w]));
				g.fillRect(viewW*Game.g.map.tileWidth - offsetX, viewH*Game.g.map.tileHeight - offsetY, Game.g.map.tileWidth, Game.g.map.tileHeight);

				viewW++;
				
			}
			
			viewH++;
		}
	}



}
