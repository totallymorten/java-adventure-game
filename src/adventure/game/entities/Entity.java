package adventure.game.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import tools.ImageTool;

import adventure.comm.CommunicationObj;
import adventure.game.Game;
import adventure.game.Renderable;
import adventure.game.TileMap;
import adventure.types.RenderPriority;



public class Entity extends CommunicationObj implements Renderable, Updateable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1917332278612594127L;
	
	protected RenderPriority renderPriority = RenderPriority.BACKGROUND;
	public int tileX=0,tileY=0,newTileX=0,newTileY=0;
	Image img;
	public int imgW, imgH;
	Object imgId;
	
	public HashMap<Float, Image> bufferedLightLevelImages = new HashMap<Float, Image>();

	public int entityId = this.hashCode();
	
	public Entity(int x, int y, Object imgId)
	{
		this.tileX = x;
		this.tileY = y;
		this.imgId = imgId == null ? this.getClass() : imgId;
	}
	
	public Entity(int x, int y, Object imgId, RenderPriority renderPriority)
	{
		this.tileX = x;
		this.tileY = y;

		this.imgId = imgId == null ? this.getClass() : imgId;
		
		this.renderPriority = renderPriority;
	}

	private void initImage()
	{
		this.img = Game.g.images.get(imgId);

		bufferedLightLevelImages.put(new Float(1.0f), img);
		
		imgW = img.getWidth(null);
		imgH = img.getHeight(null);		
	}

	
	public Point getTilePoint()
	{
		return new Point(tileX,tileY);
	}
	
	public Point getRealPoint()
	{
		return new Point(tileX*Game.g.tileWidth, tileY * Game.g.tileHeight);
	}

	public Point getViewRenderPoint()
	{
		return getViewRealPoint();
	}
	
	public Point getViewRealPoint()
	{
		return Game.g.view.translateRealPosToViewRealPos(getRealPoint());
	}

	public void setNewPoint(Point p)
	{
		this.newTileX = p.x;
		this.newTileY = p.y;
	}
	
	public void setCurrentPoint(Point p)
	{
		this.tileX = p.x;
		this.tileY = p.y;
	}
	
	

	@Override
	public void render(Graphics g)
	{
		if (!Game.g.isVisible(this))
			return;
		
		if (this.img == null)
			initImage();
		
		float lightLevel = TileMap.selectLightLevel(tileX, tileY);
		
		Image bufferedLightLevelImage = this.bufferedLightLevelImages.get(lightLevel);
		
		if (bufferedLightLevelImage == null)
		{
			bufferedLightLevelImage = ImageTool.calCulateNightImage(lightLevel, img);
			this.bufferedLightLevelImages.put(new Float(lightLevel), bufferedLightLevelImage);
		}
		
		Point viewpos = getViewRenderPoint();
		g.drawImage(bufferedLightLevelImage,viewpos.x,viewpos.y,imgW,imgH, null);
	}

	
	
	@Override
	public void update(double ms)
	{
	}

	@Override
	public RenderPriority getRenderPriority()
	{
		return renderPriority;
	}

	@Override
	public String toString()
	{
		return this.getClass().getName() + "[id:"+entityId+",pos:"+getTilePoint()+"]";
	}

}
