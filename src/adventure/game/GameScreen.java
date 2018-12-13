package adventure.game;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import adventure.game.screen.ManBar;
import adventure.types.RenderPriority;

public class GameScreen implements Renderable
{
	RenderPriority renderPriority = RenderPriority.FOREGROUND;
	public int width,height;
	public int tilesx, tilesy;
	private ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	int barCount = 0;
	
	public GameScreen(int width, int height, int tilew, int tileh)
	{
		this.width = width;
		this.height = height;
		
		tilesx = Math.round(width / tilew) + 1;
		tilesy = Math.round(height / tileh) + 1;
	}
	
	public void addRenderable(Renderable renderable)
	{
		renderables.add(renderable);
	}
	
	public void addManBar(ManBar bar)
	{
		int barposx = 5 + barCount * (5 + bar.width); 
		int barposy = height - (bar.height + 30);
		bar.setPos(barposx, barposy);
		addRenderable(bar);
		barCount++;
	}

	@Override
	public void render(Graphics g)
	{
		for (Renderable r : renderables)
		{
			r.render(g);
		}
		
		
		int x = Game.g.screen.width - 250;
		int y = 10;
		g.setColor(Color.white);
		g.setFont(new Font("Courier New", Font.BOLD, 15));
		
		// draw version
		g.drawString(Game.version, x, y);
		
		// draw gravestones
		y += 20;
		g.drawString("Gravestones: "+Game.g.killedGraveStones+"/"+Game.g.totalGraveStones+"", x,y);

		// draw zombies
		y += 20;
		g.drawString("Zombies killed: "+Game.g.killedZombies+"/"+Game.g.totalZombies+"", x,y);
		
		// draw masterzombies
		y += 20;
		g.drawString("Master Zombies killed: "+Game.g.killedMasterZombies+"/"+Game.g.totalMasterZombies+"", x,y);
	}
	
	@Override
	public RenderPriority getRenderPriority()
	{
		return this.renderPriority;
	}
	
	
}
