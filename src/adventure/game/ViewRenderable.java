package adventure.game;

import java.awt.Graphics;
import java.awt.Point;

public interface ViewRenderable extends Renderable {
	public void render(Graphics g, GameView view);
	
	public Point getTilePoint();
	public Point getRealPoint();
	public Point getViewRenderPoint(GameView view);
	public Point getViewRealPoint(GameView view);
	
	public void setNewTilePoint(Point p);
	public void setCurrentTilePoint(Point p);


}
