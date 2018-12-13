package adventure.game.screen;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import adventure.game.GameScreen;
import adventure.game.Renderable;
import adventure.game.entities.Player;
import adventure.types.RenderPriority;

public abstract class ManBar implements Renderable
{
	protected RenderPriority renderPriority = RenderPriority.FOREGROUND;
	GameScreen screen;
	Player man;
	public String title;
	int posx=0,posy=0;
	int maxLength = 170;
	public int width = 200;
	public int height = 40;
	Color boxColor, textColor, backColor, foreColor;
	
	public ManBar(String title, GameScreen screen, Player man, Color boxColor, Color textColor, Color foreColor, Color backColor)
	{
		this.screen = screen;
		this.man = man;
		this.title = title;
		this.foreColor = foreColor;
		this.backColor = backColor;
		this.boxColor = boxColor;
		this.textColor = textColor;
	}
	
	public void render(Graphics renderG)
	{
		renderG.setColor(boxColor);
		renderG.fillRect(posx, posy, width, height);
		renderG.setColor(textColor);
		renderG.setFont(new Font("Courier New", Font.BOLD, 15));
		renderG.drawString(title, posx + 5, posy + 15);
		
		// drawing background bar
		renderG.setColor(backColor);
		renderG.fillRect(posx + 10, posy + 20, maxLength, 10);
		
		// drawing yellow bar
		renderG.setColor(foreColor);
		int staminaLength = (int)(Math.round((getCurrentValue() / getMaxValue()) * maxLength));
		renderG.fillRect(posx + 10, posy + 20, staminaLength, 10);

	}
	
	public void setPos(int x, int y)
	{
		this.posx = x;
		this.posy = y;
	}
	
	public RenderPriority getRenderPriority()
	{
		return this.renderPriority;
	}
	
	protected abstract double getCurrentValue();
	protected abstract double getMaxValue();
}
