package adventure.game.entities;

import java.awt.Image;

import adventure.game.entities.ai.ZombieAI;
import adventure.properties.AdventureProperties;

public class Zombie extends Actor
{
	private ZombieAI ai = new ZombieAI(this);

	public Zombie(int initx, int inity, Image img)
	{
		super(initx, inity, img);
		
		friends.add(Zombie.class);
		
		this.maxHealth = AdventureProperties.getDouble("z_maxHealth");
		this.maxStamina = AdventureProperties.getDouble("z_maxStamina");
		this.maxMana = AdventureProperties.getDouble("z_maxMana");
		
		this.defence = AdventureProperties.getDouble("z_defence");
		this.attack = AdventureProperties.getDouble("z_attack");
		this.staminaGainPrMs = AdventureProperties.getDouble("z_staminaGainPrMs");
		
		this.xpValue = AdventureProperties.getInt("z_xpValue");

		// setting all stats = max
		this.resetStats();
	}


	@Override
	public void update(double ms)
	{
		super.update(ms);
		ai.update(ms);		
	}

}
