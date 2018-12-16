package adventure.comm;

import adventure.exception.AdventureException;

public abstract class CommandObj extends CommunicationObj
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4007336914844815986L;

	abstract public void executeCmd() throws AdventureException;
}
