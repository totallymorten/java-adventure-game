package test;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Test
{

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception
	{
		BufferedImage imGrass = ImageIO.read(new File("img/grass.gif"));
		JFrame frame = new JFrame();
		Dimension d = new Dimension(50,50);
		frame.setPreferredSize(d);
		frame.setSize(d);
		JPanel panel = new JPanel();
		panel.setPreferredSize(d);
		frame.setContentPane(panel);
		
		frame.setVisible(true);
		panel.setVisible(true);

		Graphics g = panel.getGraphics();
		Image img = panel.createImage(50, 50);
		img.getGraphics().drawImage(imGrass,0,0,50,50,null);
		g.drawImage(img, 0, 0,null);
		frame.addNotify();
		panel.setFocusable(true);
		Toolkit.getDefaultToolkit().sync();
		

	}

}
