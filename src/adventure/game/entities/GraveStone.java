package adventure.game.entities;

import java.awt.Image;

import adventure.game.entities.ai.GraveStoneAI;
import adventure.properties.AdventureProperties;

public class GraveStone extends Actor
{

	private GraveStoneAI ai = new GraveStoneAI(this);
	
	public GraveStone(int initx, int inity, Object imgId)
	{
		super(initx, inity, imgId);

		this.maxHealth = AdventureProperties.getDouble("gs_maxHealth");
		this.maxStamina = AdventureProperties.getDouble("gs_maxStamina");
		this.maxMana = AdventureProperties.getDouble("gs_maxMana");
		
		this.defence = AdventureProperties.getDouble("gs_defence");
		this.attack = AdventureProperties.getDouble("gs_attack");
		
		this.xpValue = AdventureProperties.getInt("gs_xpValue");

		// setting all stats = max
		this.resetStats();
	}

	@Override
	public void update(double ms)
	{
		//super.update(ms); - NOT NEEDED. Not a moving actor
		
		ai.update(ms);
	}
	
	

}
