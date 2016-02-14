package adventure.game.entities;

import java.awt.Image;

import adventure.game.entities.ai.ZombieAI;
import adventure.properties.AdventureProperties;

public class MasterZombie extends Zombie
{
	private ZombieAI ai = new ZombieAI(this);

	public MasterZombie(int initx, int inity, Image img)
	{
		super(initx, inity, img);

		this.maxHealth = AdventureProperties.getDouble("mz_maxHealth");
		this.maxStamina = AdventureProperties.getDouble("mz_maxStamina");
		this.maxMana = AdventureProperties.getDouble("mz_maxMana");
		
		this.defence = AdventureProperties.getDouble("mz_defence");
		this.attack = AdventureProperties.getDouble("mz_attack");
		this.staminaGainPrMs = AdventureProperties.getDouble("mz_staminaGainPrMs");
		
		this.xpValue = AdventureProperties.getInt("mz_xpValue");

		ai.ai_zombie_day_vision = AdventureProperties.getDouble("mz_ai_zombie_day_vision");
		ai.ai_zombie_night_vision = AdventureProperties.getDouble("mz_ai_zombie_night_vision");

		// setting all stats = max
		this.resetStats();
		
	}
}
