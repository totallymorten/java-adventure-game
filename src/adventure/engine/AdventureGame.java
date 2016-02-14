package adventure.engine;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import tools.Logger;
import tools.TimeTool;
import adventure.client.AdventureClient;
import adventure.client.ServerMsgHandler;
import adventure.comm.CommunicationObj;
import adventure.game.Entities;
import adventure.game.Game;
import adventure.game.GameScreen;
import adventure.game.GameView;
import adventure.game.LoginScreen;
import adventure.game.Renderable;
import adventure.game.TileMap;
import adventure.game.entities.Actor.ActorState;
import adventure.game.entities.Animating;
import adventure.game.entities.Entity;
import adventure.game.entities.GraveStone;
import adventure.game.entities.MasterZombie;
import adventure.game.entities.Player;
import adventure.game.entities.Updateable;
import adventure.game.entities.Zombie;
import adventure.game.screen.HealthBar;
import adventure.game.screen.LevelBar;
import adventure.game.screen.MessageWindow;
import adventure.game.screen.SpeedBar;
import adventure.game.screen.StaminaBar;
import adventure.properties.AdventureProperties;
import adventure.types.Direction;
import adventure.types.RenderPriority;
import adventure.types.Sounds;
import adventure.types.Tiles;
import engine.JavaEngine;
import engine.Keys;

public class AdventureGame extends JavaEngine
{
	public static int DEFAULT_FPS = AdventureProperties.getInt("default_fps");
	
	private static final long serialVersionUID = 1L;
	public static boolean soundEnabled = true;
	
	public static boolean DEBUG_MODE = false;
	
	String className = this.getClass().getName();
	
	private String serverAddress = "localhost";
	private int serverPort = 5555;
	
	public static final int WIDTH = AdventureProperties.getInt("screen_width");
	public static final int HEIGHT = AdventureProperties.getInt("screen_height");
	
	private AdventureClient client = AdventureClient.c; 
	
	int grassW,grassH;
	TileMap map;
	GameView view;
	GameScreen screen;
	
	String username;
	
	private enum GameState {LOGIN, GAME, PAUSED};
	
	private GameState state = GameState.LOGIN;
	
	public AdventureGame(double fps, String username, String serverAddress, String serverPort)
	{
		super(WIDTH, HEIGHT, fps);
		this.username = username;
		this.serverAddress = serverAddress;
		this.serverPort = Integer.parseInt(serverPort);
		
		Game.g.periodMs = 1.0 / fps * Math.pow(10, 3);
		
		setBackground(Color.WHITE);
		
		try
		{
			// initializing game data
			//initGameData();
			initLoginScreen();
			
		} catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}		
	}
	
	public static void main(String[] args)
	{
		int fps = DEFAULT_FPS;
	
		if (args == null || args.length == 0)
			args = new String[] {"cruiser", "localhost", "5555"};
		
		(new Thread(new AdventureGame(fps, args[0], args[1], args[2]))).start();
	}
	
	public static double cycleTimeStart = 0;

	
	public static double cycleTime()
	{
		return TimeTool.timeDiff(cycleTimeStart);
	}

	
	private Clip loadSound(String fileName)
	{
		if (!AdventureGame.soundEnabled)
			return null;
		
		try
		{
			Logger.trace("loadSound("+fileName+")");
			
			AudioInputStream is = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
			AudioFormat format = is.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			Clip clip = (Clip) AudioSystem.getLine(info);
			clip.open(is);
			is.close();
			return clip;			
		}
		catch (Exception e)
		{
			AdventureGame.soundEnabled = false;
			
			Logger.error("loadSound(): " + e);
		}
		
		return null;
	}
	
	private Image loadImage(String name)
	{
		try
		{
			Logger.trace("loadImage("+name+")");
			return ImageIO.read(getClass().getResource(name));
		}
		catch (Exception e)
		{
			Logger.error("loadImage(): " + e);
			System.exit(1);
		}
		
		return null;
	}
	
	LoginScreen loginScreen;
	
	private void initLoginScreen()
	{
		loginScreen = new LoginScreen();
		Game.g.addRenderable(loginScreen);		
	}
	
	private void initGameData() throws Exception
	{
		// creating screen
		screen = new GameScreen(WIDTH, HEIGHT, Game.g.tileWidth, Game.g.tileHeight);
		Game.g.screen = screen;
		// message window
		MessageWindow msgWind = new MessageWindow(); 
		screen.addRenderable(msgWind);
		Game.g.setMsgWindows(msgWind);
		Game.g.addUpdateable(msgWind);
		Game.g.addRenderable(screen);
		
		// LOADING SOUNDS
		Game.g.sounds = new HashMap<Sounds, Clip>()
		{
			{
				put(Sounds.HIT3, loadSound("/snd/hit3.wav"));
				put(Sounds.HIT4, loadSound("/snd/hit4.wav"));
				put(Sounds.HIT5, loadSound("/snd/hit5.wav"));
				put(Sounds.HIT6, loadSound("/snd/hit6.wav"));
				put(Sounds.HIT7, loadSound("/snd/hit7.wav"));
				put(Sounds.HIT8, loadSound("/snd/hit8.wav"));
				put(Sounds.TELEPORT, loadSound("/snd/teleport.wav"));
				put(Sounds.DIE1, loadSound("/snd/die1.wav"));
				put(Sounds.BLOCK, loadSound("/snd/block1.wav"));
				put(Sounds.NIGHT, loadSound("/snd/night.wav"));
				put(Sounds.ROOSTER, loadSound("/snd/rooster.wav"));
				put(Sounds.MASTERZOMBIE, loadSound("/snd/masterzombie.wav"));
			}
		};
		
		// LOADING IMAGES
		

		Game.g.images = new HashMap<Object, Image>()
		{
			{
				Image imBlood1 = loadImage("/img/blood1.gif");
				Image imBloodHit1 = loadImage("/img/blood-hit1.gif");
				Image imBlock = loadImage("/img/block.gif");
				Image imMan = loadImage("/img/eric.gif");
				Image imZombie = loadImage("/img/zombie.gif");
				Image imGraveStone = loadImage("/img/gravestone.gif");
				Image imMasterZombie = loadImage("/img/masterzombie.gif");

				put(Entities.BLOOD1, imBlood1);
				put(Entities.BLOOD_HIT1, imBloodHit1);
				put(Entities.BLOCK, imBlock);
				put(Player.class, imMan);
				put(Zombie.class, imZombie);
				put(GraveStone.class, imGraveStone);
				put(MasterZombie.class, imMasterZombie);
			}
		};
		
		client.connect(serverAddress, serverPort);
		client.login(username);
		map = client.getTileMapFromServer();
		
		Player man = client.getPlayerFromServer();

		Logger.info(className + ".iniGameData(): got man from server with position ["+man.getTilePoint()+"]");
		
		view = new GameView(0,0,screen,map);
		screen.addManBar(new StaminaBar(screen,man, Color.gray, Color.white, Color.yellow, Color.white));
		screen.addManBar(new HealthBar(screen,man, Color.gray, Color.white, Color.red, Color.white));
		//screen.addManBar(new ManaBar(screen,man, Color.gray, Color.white, Color.blue, Color.white));
		screen.addManBar(new SpeedBar(screen,man, Color.gray, Color.white, Color.green, Color.white));
		screen.addManBar(new LevelBar(screen,man, Color.gray, Color.white, Color.cyan, Color.white));

		map.tileImages = new HashMap<String, Image>() 
		{
			{
				Float lightLevel = new Float(1.0f);
				
				Image imMan,imGrass, imGrassFlower, imWater, imTree;

				// loading images
				imGrass = ImageIO.read(getClass().getResource("/img/grass.gif"));
				imGrassFlower = ImageIO.read(getClass().getResource("/img/grass-flowers.gif"));
				imWater = ImageIO.read(getClass().getResource("/img/water.gif"));
				imTree = ImageIO.read(getClass().getResource("/img/tree.gif"));

				put(Tiles.GRASS + lightLevel.toString(),imGrass); 
				put(Tiles.FLOWERS + lightLevel.toString(),imGrassFlower);
				put(Tiles.WATER + lightLevel.toString(),imWater);
				put(Tiles.TREE + lightLevel.toString(),imTree);
			}
		};
		
		client.requestWorldUpdate();
		client.requestAllEntities();
		
		Game.g.player = man;
		Game.g.view = view;
		Game.g.map = map;
		
		Game.g.addRenderable(view);
		
		Game.g.client_addEntity(man);
		
		Game.g.addUpdateable(view);
	}

	@Override
	protected void handleKeys()
	{
		if (state == GameState.GAME)
		{
			handleGameKeys();
		}
		else if (state == GameState.LOGIN)
		{
			if (Keys.pull(KeyEvent.VK_ENTER))
			{
				try
				{
					state = GameState.PAUSED;

					// maing sure an update is not in progress
					Thread.sleep(1000);
					
					username = loginScreen.username.toString();
					serverAddress = loginScreen.server.toString();
					serverPort = Integer.parseInt(loginScreen.port.toString());
					
					Game.g.renderables.clear();

					initGameData();
					
					state = GameState.GAME;
					
				} 
				catch (Exception e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else
				handleLoginKeys();
		}
		
		
		// TODO Auto-generated method stub
		super.handleKeys();
	}

	public void handleLoginKeys()
	{
		loginScreen.handleKeyEvent();
	}
	
	public void handleGameKeys()
	{
		Direction dir = null;
		
		if (Keys.pull(KeyEvent.VK_RIGHT))
		{
			dir = Direction.EAST;
		}
		else if (Keys.pull(KeyEvent.VK_LEFT))
		{
			dir = Direction.WEST;
		}
		else if (Keys.pull(KeyEvent.VK_UP))
		{
			dir = Direction.NORTH;
		}
		else if (Keys.pull(KeyEvent.VK_DOWN))
		{
			dir = Direction.SOUTH;
		}
		else
			return;
		
		if (dir != null && Game.g.player.state != ActorState.MOVING)
			client_handlePlayerMove(Game.g.player, dir);
		

	}

	private void handleDebugMode()
	{
		if (AdventureGame.DEBUG_MODE)
		{
			boolean found = false;
			for (Entity e : Game.g.entities.values())
			{
				found = false;
				
				for (Renderable r : Game.g.renderables)
				{
					if (e == r)
					{
						found = true;
						break;
					}
				}
				
				if (!found)
				{
					Logger.error("Entity ["+e+"] not found in renderables array");
				}
				
			}
		}
	}
	
	
	
	@Override
	public void preExit()
	{
		if (client != null)
			client.shutdown();
		showExitStats();
	}

	private void showExitStats()
	{
		Logger.info("Entities in entity list:");
		for (Entity e : Game.g.entities.values())
		{
			Logger.info(e.toString());
		}
		Logger.info("Entities in renderable list:");
		for (Renderable r : Game.g.renderables)
		{
			Logger.info(r.toString());
		}
	}

	private void handleUpdateables()
	{
		Game.g.updateables.removeAll(Game.g.removeUpdateables);
		Game.g.updateables.addAll(Game.g.addUpdateables);

		Game.g.clearUpdateableArrays();
	}
	
	private void handleRenderables()
	{
		for (Renderable r : Game.g.addRenderables)
		{
			searchAndAddRenderable(r);
		}

		Game.g.renderables.removeAll(Game.g.removeRenderables);

		
		Game.g.clearRenderableArrays();
	}
	
	private void handleAnimating()
	{
		Game.g.animating.removeAll(Game.g.removeAnimating);
		Game.g.animating.addAll(Game.g.addAnimating);

		Game.g.clearAnimatingArrays();
	}
	
	private void searchAndAddRenderable(Renderable r)
	{
		int i = 0;
		for (Renderable r1 : Game.g.renderables)
		{
			if (RenderPriority.leq(r1.getRenderPriority(), r.getRenderPriority()))
			{
				i++;
			}
			else
			{
				// insert renderables in front of current element
				break;
			}
			
		}
		Game.g.renderables.add(i,r);
	}
	

	private void updateUpdateables(double period)
	{
		for (Updateable u : Game.g.updateables)
		{
			u.update(period);
		}
	}
	
	private void updateAnimating(double period)
	{
		for (Animating a : Game.g.animating)
		{
			a.updateAnimation(period);
		}
	}

	private void handleServerMessages()
	{
		CommunicationObj obj;
		while ((obj = client.getMessage()) != null)
		{
			ServerMsgHandler.handleSrvMsg(obj);
		}
	}
	
	private void client_handlePlayerMove(Player p, Direction dir)
	{
		client.playerMove(p, dir);
	}

	@Override
	public void render(Graphics2D g)
	{
		for (Renderable r : Game.g.renderables)
		{
			r.render(g);
		}
	}

	double tenE6 = Math.pow(10, 6);
	
	@Override
	public void update(double ns)
	{
		double ms = ns / tenE6;
		updateUpdateables(ms);
		updateAnimating(ms);
	}

	@Override
	public void handlePreCycle()
	{
		cycleTimeStart = TimeTool.getTime();
		handleServerMessages();
		handleRenderables();
		handleAnimating();
		handleUpdateables();
	}

	@Override
	public void init()
	{
		// TODO Auto-generated method stub
		
	}
	
}
