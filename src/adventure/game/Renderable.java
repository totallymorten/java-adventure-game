package adventure.game;

import java.awt.Graphics;

import adventure.types.RenderPriority;

public interface Renderable
{
	public RenderPriority getRenderPriority();
	public void render(Graphics g);
}
