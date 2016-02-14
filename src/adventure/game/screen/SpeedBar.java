package adventure.game.screen;

import java.awt.Color;

import adventure.game.GameScreen;
import adventure.game.entities.Player;

public class SpeedBar extends ManBar
{

	public SpeedBar(GameScreen screen, Player man, Color boxColor,
			Color textColor, Color foreColor, Color backColor)
	{
		super("SPEED", screen, man, boxColor, textColor, foreColor, backColor);
	}

	@Override
	protected double getCurrentValue()
	{
		return man.speed;
	}

	@Override
	protected double getMaxValue()
	{
		return man.maxSpeed;
	}

}
