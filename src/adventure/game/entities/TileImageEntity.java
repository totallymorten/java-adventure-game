package adventure.game.entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.HashMap;

import adventure.game.Game;
import adventure.game.GameView;
import adventure.game.TileMap;
import adventure.types.RenderPriority;



public class TileImageEntity extends BaseEntity implements Updateable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1917332278612594127L;
	
	protected RenderPriority renderPriority = RenderPriority.BACKGROUND;
	Image img;
	public int imgW, imgH;
	Object imgId;
	
	public HashMap<Float, Image> bufferedLightLevelImages = new HashMap<Float, Image>();

	public TileImageEntity(int x, int y, Object imgId)
	{
		this.tileX = x;
		this.tileY = y;
		this.imgId = imgId == null ? this.getClass() : imgId;
	}
	
	public TileImageEntity(int x, int y, Object imgId, RenderPriority renderPriority)
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

	@Override
	public void render(Graphics g, GameView view) {
		if (this.img == null)
			initImage();
		
		float lightLevel = TileMap.selectLightLevel(tileX, tileY);
		
		lightLevel = Math.abs(1 - lightLevel);
		
//		Image bufferedLightLevelImage = this.bufferedLightLevelImages.get(lightLevel);
//		
//		if (bufferedLightLevelImage == null)
//		{
//			bufferedLightLevelImage = ImageTool.calCulateNightImage(lightLevel, img);
//			this.bufferedLightLevelImages.put(new Float(lightLevel), bufferedLightLevelImage);
//		}
		
		Point viewpos = getViewRenderPoint(view);
		g.drawImage(img,viewpos.x,viewpos.y,imgW,imgH, null);
		
//		g.setColor(new Color(0,0,0,Math.round(lightLevel * 255)));
//		g.fillRect(viewpos.x, viewpos.y, imgW, imgH);

		
	}

	@Override
	public void render(Graphics g) {
		
	}

}
