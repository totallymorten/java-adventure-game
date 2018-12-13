package test;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SndTest
{

	/**
	 * @param args
	 * @throws Exception 
	 * @throws UnsupportedAudioFileException 
	 */
	public static void main(String[] args) throws UnsupportedAudioFileException, Exception
	{
		AudioInputStream is = AudioSystem.getAudioInputStream(new File("snd/die1.wav"));
		AudioFormat format = is.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		Clip clip = (Clip) AudioSystem.getLine(info);
		clip.open(is);
		is.close();
		clip.start();
		Thread.sleep(3000);

	}

}
