package adventure.game;

import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.Clip;

import adventure.engine.AdventureGame;
import adventure.game.entities.Actor;
import adventure.game.entities.Animating;
import adventure.game.entities.BaseEntity;
import adventure.game.entities.MasterZombie;
import adventure.game.entities.Player;
import adventure.game.entities.TileImageEntity;
import adventure.game.entities.Updateable;
import adventure.game.entities.Zombie;
import adventure.game.entities.ai.astar.AStarService;
import adventure.game.screen.MessageWindow;
import adventure.properties.AdventureProperties;
import adventure.server.AdventureServer;
import adventure.types.Sounds;
import tools.Logger;


public class Game
{
	public static String MODE_CLIENT = "CLIENT";
	public static String MODE_SERVER = "SERVER";

	public static String version = AdventureProperties.getString("game_version");
	
	public static String MODE = MODE_CLIENT;
	
	private String LOG_PREFIX = this.getClass().getName();
	
	public static Game g = new Game();
	
	public double periodMs;
	public Player player;
	public int tileHeight = AdventureProperties.getInt("game_tileHeight");
	public int tileWidth = AdventureProperties.getInt("game_tileWidth");
	public GameView view;
	public GameScreen screen;
	public TileMap map;
	private MessageWindow msgWind;
	
	public ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	public ArrayList<Renderable> removeRenderables = new ArrayList<Renderable>();
	public ArrayList<Renderable> addRenderables = new ArrayList<Renderable>();

	public ArrayList<Animating> animating = new ArrayList<Animating>();
	public ArrayList<Animating> removeAnimating = new ArrayList<Animating>();
	public ArrayList<Animating> addAnimating = new ArrayList<Animating>();

	public ArrayList<Updateable> updateables = new ArrayList<Updateable>();
	public ArrayList<Updateable> removeUpdateables = new ArrayList<Updateable>();
	public ArrayList<Updateable> addUpdateables = new ArrayList<Updateable>();
	
	public HashMap<Integer, BaseEntity> entities = new HashMap<Integer, BaseEntity>();
	
	public HashMap<Sounds, Clip> sounds = new HashMap<Sounds, Clip>();

	public HashMap<Object, Image> images;
	
	public int totalGraveStones = AdventureProperties.getInt("game_totalGraveStones");
	public int killedGraveStones = 0;
	public int totalZombies = 0;
	public int killedZombies = 0;
	
	
	private Hashtable<String, Player> players = new Hashtable<String, Player>();
	private ArrayList<Player> removePlayers = new ArrayList<Player>();
	
	
	public synchronized Player getPlayer(String id)
	{
		return players.get(id);
	}
	
	public synchronized void addPlayer(String id, Player player)
	{
		players.put(id, player);
		addEntity(player);
	}
	
	public synchronized void renamePlayer(String id, String username)
	{
		players.put(username,players.remove(id));
	}
	
	public synchronized List<Point> getPlayerPositions()
	{
		ArrayList<Point> points = new ArrayList<Point>();
		
		for (Player m : players.values())
		{
			points.add(m.getTilePoint());
		}
		
		return points;
	}
	
	public synchronized List<Actor> getPlayers()
	{
		ArrayList<Actor> as = new ArrayList<Actor>();
		
		for (Actor a : players.values())
		{
			as.add(a);
		}
		
		return as;
	}
	
	public int totalMasterZombies = 0;
	public int maxMasterZombies = AdventureProperties.getInt("game_maxMasterZombies");
	public int killedMasterZombies = 0;
	
	public void handleSpawnMasterZombie()
	{
		if ((totalMasterZombies-killedMasterZombies) >= maxMasterZombies)
			return;
		
		Random r = new Random();
		int x,y;
		double rand;
		
		// creating masterzombie
		do
		{
			rand = r.nextDouble();
			Logger.trace("random is " + rand);
			x = (int)Math.round(rand * 100);
			
			rand = r.nextDouble();
			Logger.trace("random is " + rand);
			y = (int)Math.round(rand * 100);				
		} while (Game.g.map.collision(x, y) != null);
		
		MasterZombie mz = new MasterZombie(x, y, null);
		Logger.info("creating masterzombie at ("+x+","+y+"): " + mz);
		Game.g.addEntity(mz);
		AdventureServer.msgClientNewEntity(mz);
		
		totalMasterZombies++;
	}
	
	
	public synchronized void setMsgWindows(MessageWindow msgWnd)
	{
		this.msgWind = msgWnd;
	}
	
	public void addMessage(String s)
	{
		if (msgWind != null)
		{
			synchronized (msgWind)
			{
				msgWind.addMessage(s);							
			}
		}
	}
	
	protected static HashMap<Sounds, Thread> soundPlayers = new HashMap<Sounds, Thread>();
	
	public static synchronized void playSound(Sounds sound)
	{
		if (!AdventureGame.soundEnabled)
			return;
		
		Logger.trace("Playing sound " + sound);
		
		Thread soundPlayer = soundPlayers.get(sound);
		
		if (soundPlayer != null)
		{
			if (soundPlayer.isAlive())
			{
				soundPlayer.interrupt();
				try
				{
					soundPlayer.join();
				} catch (InterruptedException e)
				{
					Logger.error("playSound(): Error while joining existing Thread");
					e.printStackTrace();
				}
				soundPlayers.remove(soundPlayer);
			}
			else
				soundPlayers.remove(soundPlayer);
		}
		
		
		soundPlayer = new Thread(new SoundPlayer(sound));
		soundPlayers.put(sound, soundPlayer);
		soundPlayer.start();
	}
	
	public boolean isVisible(ViewRenderable e)
	{
		int diffx = e.getRealPoint().x - view.getXInt();
		int diffy = e.getRealPoint().y - view.getYInt();
		
		// check for view visibility (else, don't render)
		if (diffx < -g.map.tileWidth || diffx > (screen.width)
		|| diffy < -g.map.tileHeight || diffy > (screen.height))
		return false;

		if (TileMap.selectLightLevel(e.getTilePoint().x, e.getTilePoint().y) <= 0.0f)
			return false;

		return true;
	}

	public boolean isTileVisible(int tilex, int tiley)
	{
		int diffx = tilex*Game.g.tileWidth - view.getXInt();
		int diffy = tiley*Game.g.tileHeight - view.getYInt();
		
		// check for view visibility (else, don't render)
		if (diffx < -g.map.tileWidth || diffx > (screen.width)
		|| diffy < -g.map.tileHeight || diffy > (screen.height))
		return false;

		if (TileMap.selectLightLevel(tilex, tiley) <= 0.0f)
			return false;

		return true;
	}

	public synchronized void scheduleRemoveRenderable(Renderable r)
	{
		removeRenderables.add(r);
	}

	public synchronized void addRenderable(Renderable r)
	{
		addRenderables.add(r);
	}

	public synchronized void clearRenderableArrays()
	{
		removeRenderables.clear();
		addRenderables.clear();
	}
	
	public synchronized void scheduleRemoveAnimating(Animating a)
	{
		removeAnimating.add(a);
	}

	public synchronized void addAnimating(Animating a)
	{
		addAnimating.add(a);
	}

	public synchronized void clearAnimatingArrays()
	{
		removeAnimating.clear();
		addAnimating.clear();
	}


	public synchronized void scheduleRemoveUpdateable(Updateable u)
	{
		removeUpdateables.add(u);
	}
	
	public synchronized void scheduleRemovePlayer(Player p)
	{
		removePlayers.add(p);
	}
	
	public synchronized void addUpdateable(Updateable u)
	{
		addUpdateables.add(u);
	}
	
	public synchronized void handleUpdateables()
	{
		updateables.removeAll(removeUpdateables);
		updateables.addAll(Game.g.addUpdateables);

		clearUpdateableArrays();
	}

	public synchronized void handlePlayers()
	{
		for (Player p : removePlayers)
		{
			players.remove(p.clientId);
		}
		
		removePlayers.clear();
	}
	
	public synchronized void removeEntity(TileImageEntity e)
	{
		removeEntity(e.entityId);
	}
	
	public synchronized void removeEntity(int entityId)
	{
		Logger.trace(LOG_PREFIX + ".removeEntity(): removing entity [entityId:"+entityId+"] from entities map");

		BaseEntity e = entities.remove(new Integer(entityId));
		
		
		if (e == null)
		{
			Logger.error(LOG_PREFIX + ".removeEntity(): Unable to remove entity with id: " + entityId);
			return;
		}
		
		
		Logger.trace(LOG_PREFIX + ".removeEntity(): adding entity [entityId:"+entityId+"] to remove arrays");
		
		if (e instanceof Renderable)
			scheduleRemoveRenderable(e);
		
		if (e instanceof Updateable)
			scheduleRemoveUpdateable((Updateable) e);
		AStarService.threads.remove(e);
	}

	public synchronized void addEntity(BaseEntity e)
	{
		if (entities.get(e.entityId) != null)
			Logger.error("Game.addEntity(): Entity with id ["+e.entityId+"] already exists");
		else
		{
			if (e instanceof Updateable)
				addUpdateable((Updateable) e);
			
			entities.put(new Integer(e.entityId), e);
		}
	}
	
	public synchronized void client_addEntity(BaseEntity e)
	{
		if (entities.get(e.entityId) != null)
			Logger.error("Game.client_addEntity(): Entity with id ["+e.entityId+"] already exists");
		else
		{
//			if (e instanceof BloodAnimation)
//				System.out.println("");
			
			addRenderable(e);
			
			if (e instanceof Animating)
				addAnimating((Animating)e);
			entities.put(new Integer(e.entityId), e);
		}
	}

	public synchronized BaseEntity getEntity(int entityId)
	{
		return entities.get(new Integer(entityId));
	}
	
	public synchronized List<BaseEntity> getEntities()
	{
		ArrayList<BaseEntity> entityList = new ArrayList<BaseEntity>();
		
		for (BaseEntity e : entities.values())
		{
			entityList.add(e);
		}
		
		return entityList;
	}

	public synchronized void clearUpdateableArrays()
	{
		removeUpdateables.clear();
		addUpdateables.clear();
	}

	public void createZombie(int i, int j)
	{
		TileImageEntity e = new Zombie(i,j,null);
		addEntity(e);
		AdventureServer.msgClientNewEntity(e);
	}

	public synchronized void handleAttack(int attacker, int defender)
	{
		Actor att, def;
		att = (Actor) entities.get(attacker); def = (Actor) entities.get(defender);
		
		att.attack(def);
	}
}
