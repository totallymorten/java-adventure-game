package adventure.game;


import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;
import java.util.List;

import tools.ImageTool;
import tools.Logger;
import adventure.comm.CommunicationObj;
import adventure.game.entities.Actor;
import adventure.game.entities.Entity;
import adventure.game.entities.Player;
import adventure.types.Tiles;

public class TileMap extends CommunicationObj
{
	/**
	 * Generated serial ID
	 */
	private static final long serialVersionUID = -5283993525862308743L;
	
	public Tiles[][] tiles;
	HashMap<Tiles,Double> probability;
	public int mapW, mapH;
	int tileWidth = 0;
	int tileHeight = 0;
	

	
	public TileMap(int mapW, int mapH) throws Exception
	{
		tiles = new Tiles[mapH][mapW];
		this.mapW = mapW;
		this.mapH = mapH;
		this.tileWidth = Game.g.tileWidth;
		this.tileHeight = Game.g.tileHeight;
		
		generateProbMap();
		generateMap();
	}
	
	
	private void generateProbMap()
	{
		this.probability = new HashMap<Tiles, Double>()
		{
			{
				//MSM: these values should be in properties file or constants
				put(Tiles.GRASS, new Double(MapProbabilities.PROB_GRASS));
				put(Tiles.FLOWERS, new Double(MapProbabilities.PROB_FLOWERS));
				put(Tiles.WATER, new Double(MapProbabilities.PROB_WATER));
				put(Tiles.TREE, new Double(MapProbabilities.PROB_TREE));
			}
		};

	}
	
	public HashMap<String, Image> tileImages;
	
	public Image getTileImage(Tiles t, float lightLevel)
	{
		Image img = tileImages.get(t.toString() + lightLevel);
		
		if (img == null)
		{
			img = tileImages.get(t.toString() + 1.0f);
			img = ImageTool.calCulateNightImage(lightLevel, img);
			
			// storing light calculated image
			tileImages.put(t.toString() + lightLevel, img);
		}
		
		return img;
	}
	
	public static float selectLightLevel(int w, int h)
	{
		
		if (World.world.lightLevel == 1.0f)
			return 1.0f;
		
		Player man = Game.g.player;
		
		// getting real man position
		int[] manRealPos = {man.tileX, man.tileY};

		if (w > (manRealPos[0] + man.sightLevel) || w < (manRealPos[0] - man.sightLevel)
		 || h > (manRealPos[1] + man.sightLevel) || h < (manRealPos[1] - man.sightLevel))
			return World.world.lightLevel;

		
		
		double[] distFromMan = {Math.abs(w - manRealPos[0]), Math.abs(h - manRealPos[1])};
		int dist = (int)Math.round((Math.sqrt(Math.pow(distFromMan[0],2) + Math.pow(distFromMan[1],2))));

		int sightLightInc = Game.g.player.sightLevel - dist;

		if (sightLightInc < 0)
			return World.world.lightLevel;
		
		float newLight = World.world.lightLevel + (sightLightInc * 0.1f);
		
		if (newLight > 1.0f)
			return 1.0f;
		
		return newLight;
	}
	
	
	private void generateMap() throws Exception
	{
		// for mapH
		for (int h = 0; h < tiles.length; h++)
		{
			//   for mapW
			for (int w = 0; w < tiles[h].length; w++)
			{
				tiles[h][w] = pickTileFromProb(rand());
			}
		}
		
		spreadTile(3,Tiles.WATER,MapProbabilities.WATER_SPREAD);
		spreadTile(3,Tiles.TREE,MapProbabilities.TREE_SPREAD);		
	}
	
	private void spreadTile(int passes, Tiles tileNum, double spreadProb)
	{
		// second pass, spreading water
		for (int i = 1; i <= passes; i++)
		{
			for (int h = 0; h < tiles.length; h++)
			{
				//   for mapW
				for (int w = 0; w < tiles[h].length; w++)
				{
					// if tile is water
					if (tiles[h][w] == tileNum)
					{
						// left
						if (w != 0)
							tiles[h][w-1] = (probRoll(spreadProb)) ? tileNum : tiles[h][w-1];
						// up
						if (h != 0)
							tiles[h-1][w] = (probRoll(spreadProb)) ? tileNum : tiles[h-1][w];
						// right
						if (w < (tiles[h].length - 1))
							tiles[h][w+1] = (probRoll(spreadProb)) ? tileNum : tiles[h][w+1];
						// down
						if (h < (tiles.length - 1))
							tiles[h+1][w] = (probRoll(spreadProb)) ? tileNum : tiles[h+1][w];					
					}
				}
			}
			
		}
		
	}
	
	private boolean probRoll(double prob)
	{
		
		return ((rand() * 100) <= prob);
			
	}
	
	/**
	 * Picks a tile form the tile array based on the random number
	 * generated and the probability array
	 * 
	 * @param rand the random number generated for the tile 
	 * @throws Exception 
	 */
	private Tiles pickTileFromProb(double rand) throws Exception
	{
		double probRand = 100 * rand();
		
		double currentRand = 0;
		for (Tiles tile : probability.keySet())
		{
			currentRand += probability.get(tile);
			if (probRand <= currentRand)
				return tile;
		}
		
		throw new Exception("Error in pickTileFromProb(). No image was chosen");
	}
	
	private double rand()
	{
		return Math.random();
	}

	public synchronized Object collision(int newx, int newy)
	{
		//Logger.trace("TileMap.collision("+newx+","+newy+")");
		
		if (newx < 0 || newx > (mapW - 1)
		||  newy < 0 || newy > (mapH - 1))
		{
			Logger.trace("TileMap.collision(): Collision with edge [mapW:"+Game.g.map.mapW+",mapH:"+Game.g.map.mapH+"]");
			return new Exception("Collision with edge of screen");
		}

		Tiles newt = tiles[newy][newx];
		
		if (newt == Tiles.TREE || newt == Tiles.WATER)
		{
			//Logger.trace("TileMap.collision(): Collision with Tile " + newt);
			return newt;			
		}
		
		List<Entity> entities = Game.g.getEntities();
		
		// checking actor collision
		for (Entity e : entities)
		{
			if (e instanceof Actor)
			{
				Actor a = (Actor) e;
				if (a.collides(newx, newy))
				{
					Logger.trace("collision("+newx+","+newy+") = " + a);
					return a;			
				}
			}
		}
		
		Logger.trace("collision("+newx+","+newy+") = null");
		
		return null;
	}
	
	public static double distance(Point p1, Point p2)
	{
		return Math.sqrt(Math.pow((p1.x - p2.x),2) + Math.pow((p1.y - p2.y),2)); 
	}
}
