package adventure.server;

import java.util.Random;

import tools.Logger;
import tools.TimeTool;
import adventure.game.Game;
import adventure.game.TileMap;
import adventure.game.World;
import adventure.game.entities.GraveStone;
import adventure.game.entities.MasterZombie;
import adventure.game.entities.Updateable;
import adventure.game.entities.ai.astar.AStarService;

public class GameThread extends Thread
{
	double cycleTimeStart;
	long FPS = 30;
	double period = 1000 / FPS;
	
	double gameStatUpdateDelay = 1000; // ms
	double gameStatUpdate = 0;

	@Override
	public void run()
	{
		boolean running = true;
		int cycleCount = 0;
		double avgCycleTime = 0;
		
		initGameData();
		
		while (running)
		{
			cycleCount++;
			cycleTimeStart = TimeTool.getTime();
			gameUpdate(period);
			gameStatUpdate(period);
			
			avgCycleTime += cycleTime();
			if (cycleCount >= 60)
			{
				avgCycleTime = avgCycleTime / cycleCount;
				if (avgCycleTime > period)
					Logger.info("ALERT: avg cycle: " + avgCycleTime + " ms [period = "+period+"]");
				
				cycleCount = 0;
				avgCycleTime = 0;
			}
			
			try
			{
				//log("sleeping for " + period + " ms");
				if (cycleTime() < period)
					Thread.sleep((long)(period - cycleTime()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		Logger.trace("exiting run()");
		System.exit(0);
	}
	
	private void gameStatUpdate(double ms)
	{
		gameStatUpdate -= ms;
		
		if (gameStatUpdate <= 0)
		{
			AdventureServer.sendGameStats();
			gameStatUpdate = gameStatUpdateDelay;
		}
	}

	private void gameUpdate(double ms)
	{
		handleUpdateables();
		
		for (Updateable upd : Game.g.updateables)
		{
			upd.update(ms);
		}

	}

	private void handleUpdateables()
	{
		Game.g.updateables.removeAll(Game.g.removeUpdateables);
		Game.g.updateables.addAll(Game.g.addUpdateables);

		Game.g.clearUpdateableArrays();
	}
	
	private void initGameData()
	{
		try
		{
			Game.g.map = new TileMap(100, 100);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Random r = new Random();
		double rand;
		int x,y;

		// creating gravestones
		for (int i = 1; i <= Game.g.totalGraveStones; i++)
		{
			do
			{
				rand = r.nextDouble();
				Logger.trace("random is " + rand);
				x = (int)Math.round(rand * 100);
				
				rand = r.nextDouble();
				Logger.trace("random is " + rand);
				y = (int)Math.round(rand * 100);				
			} while (Game.g.map.collision(x, y) != null);
			
			GraveStone gravestone = new GraveStone(x,y,null);
			Logger.info("creating gravestone at ("+x+","+y+"): " + gravestone);
			Game.g.addEntity(gravestone);
		}
		
		Game.g.addUpdateable(World.world);
		Game.g.addUpdateable(AStarService.service);

	}
	
	public double cycleTime()
	{
		return TimeTool.timeDiff(cycleTimeStart);
	}
	
}
