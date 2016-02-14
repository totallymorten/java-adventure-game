package adventure.comm;

import adventure.types.Sounds;

public class PlaySound extends CommunicationObj
{
	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 4088274440950770642L;
	
	public Sounds sound;
	
	public PlaySound(Sounds sound)
	{
		this.sound = sound;
	}
}
