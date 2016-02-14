package tools;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.RescaleOp;

public abstract class ImageTool
{

	public static Image calCulateNightImage(float lightLevel, Image img)
	{
		BufferedImage bi;
		Graphics2D g2d;
		float[] scaleFactors, offsets;
		BufferedImageOp brOp;
	
		scaleFactors = new float[] {lightLevel,lightLevel,lightLevel,1.0f};
		offsets = new float[] {0.0f,0.0f,0.0f,0.0f};
		brOp = new RescaleOp(scaleFactors,offsets,null);
		bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		g2d = (Graphics2D) bi.getGraphics();
		g2d.drawImage(img, 0, 0, null);
		g2d.drawImage(bi, brOp, 0, 0);
		
		return bi;
	}

}
