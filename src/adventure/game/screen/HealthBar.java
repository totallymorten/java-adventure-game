package adventure.game.screen;


import java.awt.Color;

import adventure.game.GameScreen;
import adventure.game.entities.Player;

public class HealthBar extends ManBar
{

	public HealthBar(GameScreen screen, Player man, Color boxColor, Color textColor, Color foreColor, Color backColor)
	{
		super("HEALTH", screen, man, boxColor, textColor, foreColor, backColor);
	}

	@Override
	protected double getCurrentValue()
	{
		return man.health;
	}

	@Override
	protected double getMaxValue()
	{
		return man.maxHealth;
	}

}
