package adventure.game.entities;

import java.awt.Point;

import adventure.comm.CommunicationObj;
import adventure.game.Game;
import adventure.game.GameView;
import adventure.game.ViewRenderable;
import adventure.types.RenderPriority;

public abstract class BaseEntity extends CommunicationObj implements ViewRenderable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6736638066925555498L;

	protected RenderPriority renderPriority = RenderPriority.BACKGROUND;
	public int tileX=0,tileY=0,newTileX=0,newTileY=0;

	
	public int entityId = this.hashCode();

	
	@Override
	public RenderPriority getRenderPriority() {
		return renderPriority;
	}

	@Override
	public Point getTilePoint()
	{
		return new Point(tileX,tileY);
	}
	
	@Override
	public Point getRealPoint()
	{
		return new Point(tileX*Game.g.tileWidth, tileY * Game.g.tileHeight);
	}

	@Override
	public Point getViewRenderPoint(GameView view)
	{
		return getViewRealPoint(view);
	}
	
	@Override
	public Point getViewRealPoint(GameView view)
	{
		return view.translateRealPosToViewRealPos(getRealPoint());
	}

	@Override
	public void setNewTilePoint(Point p)
	{
		this.newTileX = p.x;
		this.newTileY = p.y;
	}
	
	@Override
	public void setCurrentTilePoint(Point p)
	{
		this.tileX = p.x;
		this.tileY = p.y;
	}


}
