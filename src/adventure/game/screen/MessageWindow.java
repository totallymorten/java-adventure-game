package adventure.game.screen;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import adventure.game.Game;
import adventure.game.Renderable;
import adventure.game.entities.Updateable;
import adventure.types.RenderPriority;

public class MessageWindow implements Renderable, Updateable
{
	int x=0,y=0;
	int width = Game.g.screen.width;
	int height = 0;
	long countdown = 0;
	long messageLifeMs = 1500;
	int transparancy = 60;
	int textTransparancy = 200;
	int textOffsetX = 5;
	int textOffsetY = 15;
	int currentFadeTransp = textTransparancy;
	int fadeFactor = 3;
	int maxLines = 10;
	
	ArrayList<String> lines = new ArrayList<String>();
	
	@Override
	public RenderPriority getRenderPriority()
	{
		return RenderPriority.FOREGROUND;
	}

	@Override
	public synchronized void render(Graphics g)
	{
		g.setColor(new Color(0,0,255,transparancy));
		g.fillRect(x, y, width, height);
		Color textColor = new Color(255,255,255,textTransparancy);
		Color textFadeColor = new Color(255,255,255,currentFadeTransp);
		g.setColor(textColor);
		
		int i = 0;
		for (String line : lines)
		{
			if (i == 0)
			{
				textFadeColor = new Color(255,255,255,currentFadeTransp);
				g.setColor(textFadeColor);
				g.drawString(line, x+textOffsetX, y+textOffsetY + (i * 20));
				g.setColor(textColor);
			}
			else
				g.drawString(line, x+textOffsetX, y+textOffsetY + (i * 20));
			i++;
			
			if (i >= maxLines) break;
		}
	}

	public synchronized int messageCount()
	{
		return lines.size();
	}
	
	@Override
	public synchronized void update(double ms)
	{
		height = messageCount() >= maxLines ? maxLines*20 : messageCount()*20;
		
		if (countdown <= 0)
			return;
		else
		{
			countdown -= ms;
			
			if (countdown <= 1000 && currentFadeTransp >= fadeFactor)
				currentFadeTransp -= fadeFactor;
		}
		
		if (countdown <= 0)
		{
			removeOldestMessage();
		}
	}
	
	public synchronized void addMessage(String s)
	{
		lines.add(s);
		
		if (countdown <= 0)
			countdown = messageLifeMs;
	}
	
	private synchronized void removeOldestMessage()
	{
		lines.remove(0);
		
		if (lines.size() > 0)
			countdown = messageLifeMs;
		else
			countdown = 0;
		
		currentFadeTransp = textTransparancy;
	}

}
