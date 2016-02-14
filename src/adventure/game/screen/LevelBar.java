package adventure.game.screen;

import java.awt.Color;

import adventure.game.GameScreen;
import adventure.game.entities.Player;

public class LevelBar extends ManBar
{

	public LevelBar(GameScreen screen, Player man, Color boxColor, Color textColor, Color foreColor, Color backColor)
	{
		super("Level " + man.level, screen, man, boxColor, textColor, foreColor, backColor);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected double getCurrentValue()
	{
		this.title = "Level " + man.level;
		return man.experienceProgress;
	}

	@Override
	protected double getMaxValue()
	{
		return man.experienceReqNextLevel;
	}

}
