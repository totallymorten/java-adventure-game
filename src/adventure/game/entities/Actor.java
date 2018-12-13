package adventure.game.entities;


import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import adventure.game.Game;
import adventure.properties.AdventureProperties;
import adventure.server.AdventureServer;
import adventure.types.ActorStat;
import adventure.types.Direction;
import adventure.types.RenderPriority;
import tools.DirectionTool;

public abstract class Actor extends HealthEntity implements Animating
{
	public ArrayList<Class> friends = new ArrayList<Class>();
	
	public double stamina;
	public double maxStamina;
	public double mana;
	public double maxMana;

	public int level = AdventureProperties.getInt("actor_level");
	public double moveStaminaCost = AdventureProperties.getDouble("actor_moveStaminaCost");
	public double moveSpeedCost = AdventureProperties.getDouble("actor_moveSpeedCost");
	public double staminaGainPrMs = AdventureProperties.getDouble("actor_staminaGainPrMs");
	public double manaGainPrMs = AdventureProperties.getDouble("actor_manaGainPrMs");
	public double attackStaminaCost = AdventureProperties.getDouble("actor_attackStaminaCost"); 
	public double attackSpeedCost = AdventureProperties.getDouble("actor_attackSpeedCost");
	public int sightLevel = AdventureProperties.getInt("actor_sightLevel");
	public double maxSpeed = AdventureProperties.getDouble("actor_maxSpeed"); 
	public double speed = maxSpeed;
	public double speedGainPrMs = AdventureProperties.getDouble("actor_speedGainPrMs");
	public double percentGainPrLevel = AdventureProperties.getDouble("actor_percentGainPrLevel");
	public int experienceProgress = 0;
	public int totalExperience = 0;
	public int experienceReqNextLevel = AdventureProperties.getInt("actor_experienceReqNextLevel");
	public double experienceReqGain = AdventureProperties.getDouble("actor_experienceReqGain");
	
	// can be 0-100.
	public double moveProgress = 0;
	
	// movement speed measured in tiles / sec (std. 0.5)
	public double moveSpeed = 5.0;
	
	public boolean active = true;
	
	public double statsUpdateInterval = 500; //ms
	public double statsUpdateCount = 0;
	
	public Actor(int initx, int inity, Object imgId)
	{
		super(initx, inity, imgId, RenderPriority.ACTOR);
		
		this.newTileX = this.tileX;
		this.newTileY = this.tileY;

		resetStats();
	}
	
	public enum ActorState {STANDING, MOVING};
	
	public ActorState state = ActorState.STANDING;
	
	public void gainXp(int gainedExperience)
	{
		this.experienceProgress += gainedExperience;
		this.totalExperience += gainedExperience;
		
		if (this instanceof Player)
		{
			AdventureServer.sendActorStats(this.entityId, 
					   new ActorStat[] {ActorStat.EXP_PROG, ActorStat.EXP_TOTAL, ActorStat.LEVEL, ActorStat.MAX_HEALTH, ActorStat.MAX_SPEED, ActorStat.MAX_STAMINA, ActorStat.EXP_REQ_NEXT}, 
					   new double[] {experienceProgress, totalExperience, level, maxHealth, maxSpeed, maxStamina, experienceReqNextLevel});	
			
		}

		while (this.experienceProgress >= experienceReqNextLevel)
		{
			experienceProgress -= experienceReqNextLevel;
			experienceReqNextLevel *= experienceReqGain;
			levelUp();
		}
	}

	
	public void addLevels(int level)
	{
		for (int i = 1; i <= level; i++)
			this.levelUp();
		
	}
	
	public void resetStats()
	{
		stamina = maxStamina;
		health = maxHealth;
		speed = maxSpeed;
		mana = maxMana;
	}
	
	public void levelUp()
	{
		this.level++;
		
		maxStamina *= percentGainPrLevel;
		staminaGainPrMs *= percentGainPrLevel;
		maxHealth *= percentGainPrLevel;
		healthGainPrMs *= percentGainPrLevel;
		maxSpeed *= percentGainPrLevel;
		speedGainPrMs *= percentGainPrLevel;
		attack *= percentGainPrLevel;
		defence *= percentGainPrLevel;

		if (this instanceof Player)
		{
			AdventureServer.sendActorStats(this.entityId, 
					   new ActorStat[] {ActorStat.EXP_PROG, ActorStat.EXP_TOTAL, ActorStat.LEVEL, ActorStat.MAX_HEALTH, ActorStat.MAX_SPEED, ActorStat.MAX_STAMINA, ActorStat.EXP_REQ_NEXT}, 
					   new double[] {experienceProgress, totalExperience, level, maxHealth, maxSpeed, maxStamina, experienceReqNextLevel});	
			
		}
	}
	
	double tenE3 = Math.pow(10, 3);
	
	@Override
	public void updateAnimation(double ms)
	{
		if (state == ActorState.MOVING)
		{
			moveProgress += moveSpeed * (ms / tenE3);

			if (moveProgress >= 1)
			{
				moveProgress = 0;
				
				tileX = newTileX;
				tileY = newTileY;
				
				state = ActorState.STANDING;
			}
			
		}
		
	}


	@Override
	public void update(double ms)
	{
		if (state == ActorState.MOVING)
		{
			moveProgress += moveSpeed * (ms / Math.pow(10, 3));

			if (moveProgress >= 1)
			{
				moveProgress = 0;
				
				tileX = newTileX;
				tileY = newTileY;
				
				state = ActorState.STANDING;
			}
			
		}

		speed = increaseStatPrMs(speed, maxSpeed, speedGainPrMs, ms);
		
		// if stamina max then heal
		if (stamina == maxStamina)
			health = increaseStatPrMs(health, maxHealth, healthGainPrMs, ms);
		else
			stamina = increaseStatPrMs(stamina, maxStamina, staminaGainPrMs, ms);
		
		mana = increaseStatPrMs(mana, maxMana, manaGainPrMs, ms);
		
		
		if (this instanceof Player)
		{
			statsUpdateCount += ms;
			if (statsUpdateCount >= statsUpdateInterval)
			{
				statsUpdateCount = 0;
				AdventureServer.sendActorStats(this.entityId, 
						   new ActorStat[] {ActorStat.HEALTH, ActorStat.STAMINA, ActorStat.SPEED}, 
						   new double[] {health,stamina,speed});	
			}			
		}
	}

	public static double increaseStatPrMs(double stat, double maxstat, double statGainPrMs, double ms)
	{
		if (stat < maxstat)
		{
			stat += (ms*statGainPrMs);
			if (stat > maxstat)
				stat = maxstat;
		}
		
		return stat;
	}
	
	public void render(Graphics g)
	{
		if (!Game.g.isVisible(this))
			return;		

		super.render(g);
		
	}
	
	
	
	@Override
	public Point getViewRenderPoint()
	{
		Point p = super.getViewRenderPoint();
		
		if (state == ActorState.MOVING)
		{
			int deltax = (int)((newTileX - tileX) * moveProgress * Game.g.tileWidth);
			int deltay = (int)((newTileY - tileY) * moveProgress * Game.g.tileHeight);
			
			p.x += deltax; 
			p.y += deltay;
			
		}
		
		return p;

	}


	public Object move(Direction dir)
	{
		// only move if currently standing still
		if (state == ActorState.STANDING)
		{
			Point move = DirectionTool.getPointInDirection(getTilePoint(), dir);
			Object collidee;
			if ((collidee = Game.g.map.collision(move.x, move.y)) != null)
			{
				return collidee;
			}
			
			if (drainStaminaAndSpeed(moveStaminaCost, moveSpeedCost))
			{
				this.newTileX = move.x;
				this.newTileY = move.y;
				
				state = ActorState.MOVING;
				
				AdventureServer.updateEntityPos(this.entityId, new Point(newTileX,newTileY));
			}
			
		}
		
		return null;
	}
	
	public void attack(HealthEntity target)
	{
		// if target is a friend, then do not attack
		if (friends.contains(target.getClass()))
			return;
		
		if (drainStaminaAndSpeed(attackStaminaCost, attackSpeedCost))
			if (target.attacked(this))
				this.gainXp(target.xpValue);
	}
	
	public boolean drainStaminaAndSpeed(double neededStamina, double neededSpeed)
	{
		if (stamina >= neededStamina && speed >= neededSpeed)
		{
			stamina -= neededStamina;
			speed -= neededSpeed;
			
			return true;
		}
		return false;
	}

	public boolean collides(int otherx, int othery)
	{
		return (otherx == tileX && othery == tileY);
	}
	

	@Override
	public RenderPriority getRenderPriority()
	{
		return renderPriority;
	}
}
