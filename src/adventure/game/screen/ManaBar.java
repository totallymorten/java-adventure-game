package adventure.game.screen;


import java.awt.Color;

import adventure.game.GameScreen;
import adventure.game.entities.Player;

public class ManaBar extends ManBar
{

	public ManaBar(GameScreen screen, Player man, Color boxColor,
			Color textColor, Color foreColor, Color backColor)
	{
		super("MANA", screen, man, boxColor, textColor, foreColor, backColor);
	}

	@Override
	protected double getCurrentValue()
	{
		return man.mana;
	}

	@Override
	protected double getMaxValue()
	{
		return man.maxMana;
	}

}
