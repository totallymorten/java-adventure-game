package adventure.game.screen;

import java.awt.Color;

import adventure.game.GameScreen;
import adventure.game.entities.Player;


public class StaminaBar extends ManBar
{

	public StaminaBar(GameScreen screen, Player man, Color boxColor, Color textColor, Color foreColor, Color backColor)
	{
		super("STAMINA", screen, man, boxColor, textColor, foreColor, backColor);
	}

	@Override
	protected double getCurrentValue()
	{
		return man.stamina;
	}

	@Override
	protected double getMaxValue()
	{
		return man.maxStamina;
	}

}
