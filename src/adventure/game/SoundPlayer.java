package adventure.game;

import javax.sound.sampled.Clip;

import adventure.types.Sounds;
import tools.Logger;

public class SoundPlayer implements Runnable
{
	Sounds sound;
	
	public SoundPlayer(Sounds sound)
	{
		this.sound = sound;
	}

	@Override
	public void run()
	{
		Clip soundClip = Game.g.sounds.get(sound);
		try
		{
			soundClip.start();
			Thread.sleep(10000);
			soundClip.stop();
			soundClip.setFramePosition(0);
		}
		catch(InterruptedException e)
		{
			Logger.trace("" + e);
			soundClip.stop();
			soundClip.setFramePosition(0);
		}
	}
	
}
