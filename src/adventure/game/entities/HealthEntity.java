package adventure.game.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import adventure.game.Entities;
import adventure.game.Game;
import adventure.game.GameView;
import adventure.game.animations.BloodAnimation;
import adventure.properties.AdventureProperties;
import adventure.server.AdventureServer;
import adventure.types.Direction;
import adventure.types.RenderPriority;
import adventure.types.Sounds;
import tools.DirectionTool;

public class HealthEntity extends TileImageEntity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7528657901665689133L;
	
	public double attack = AdventureProperties.getDouble("he_attack");
	public double defence = AdventureProperties.getDouble("he_defence");
	public double health, maxHealth;
	public double healthGainPrMs = AdventureProperties.getDouble("he_healthGainPrMs");
	public int xpValue;

	public HealthEntity(int x, int y, Object imgId, RenderPriority priority)
	{
		super(x, y, imgId, priority);

	}

	@Override
	public void render(Graphics g, GameView view)
	{
		super.render(g, view);
		
		// only render if actor is damaged
		if (health == maxHealth)
			return;
		
		Point viewPos = getViewRenderPoint(view);
		
		// drawing health background bar
		g.setColor(new Color(130,4,9));
		g.fillRect(viewPos.x, viewPos.y + imgH + 5, imgW, 5);
		
		// drawing yellow bar
		g.setColor(Color.red);
		int barLength = (int)(Math.round((health / maxHealth) * imgW));
		g.fillRect(viewPos.x, viewPos.y + imgH + 5, barLength, 5);
	}
	
	/**
	 * Method is called when this actor is attacked by 'attacker'
	 * 
	 * @param attacker
	 * @return true if this actor is killed. Else false.
	 */
	public boolean attacked(Actor attacker)
	{
		double randomAttack = Math.random();
		double attack = attacker.attack * randomAttack;
		
		double randomDefence = Math.random();
		double defence = this.defence * randomDefence;
		
		if (attack > defence)
		{

			AdventureServer.playRandomSound(new Sounds[] {Sounds.HIT3,Sounds.HIT4,Sounds.HIT5,Sounds.HIT6,Sounds.HIT7,Sounds.HIT8});
			
			TimedEntity te = new TimedEntity(tileX,tileY,Entities.BLOOD_HIT1, 200);
			Game.g.addEntity(te);
			AdventureServer.msgClientNewEntity(te);				
			
			Direction attackDirection = DirectionTool.getDirectionFromEntities(attacker, this);
			
			// adding blood spray animation (particles)
			// NOT adding to array because it's only a client (graphical) thing
			BloodAnimation ba = new BloodAnimation(tileX, tileY, attackDirection);
			AdventureServer.msgClientNewEntity(ba);

			this.health -= (attack - defence);
			
			if (this.health <= 0)
			{
				// removing this from the game
				AdventureServer.scheduleRemoveEntity(this);

				// handle remove player
				if (this instanceof Player)
					AdventureServer.scheduleRemovePlayer((Player)this);

				if (this instanceof GraveStone)
					Game.g.killedGraveStones++;
				else if (this instanceof MasterZombie)
					Game.g.killedMasterZombies++;
				else if (this instanceof Zombie)
					Game.g.killedZombies++;
				
				// adding pool of blood instead
				TileImageEntity blood = new TileImageEntity(tileX,tileY,Entities.BLOOD1);
				Game.g.addEntity(blood);
				AdventureServer.msgClientNewEntity(blood);

				// playing die sound
				AdventureServer.playSound(Sounds.DIE1);
				
				return true; // defender (this) was killed
			}
			
			// Send entity health update to clients
			AdventureServer.updateEntityHealth(this);
		}
		// hit is blocked
		else
		{
			AdventureServer.playSound(Sounds.BLOCK);
			TimedEntity te = new TimedEntity(tileX,tileY,Entities.BLOCK, 200);
			Game.g.addEntity(te);
			AdventureServer.msgClientNewEntity(te);
		}
		return false; // defender was not killed
		
	}

	public boolean isDead()
	{
		return health <= 0;
	}

}
