package adventure.comm;


public class SayCmd extends CommandObj
{
	public SayCmd(String msg)
	{
		this.message = msg;
	}
	
	public String message;
	
	@Override
	public void executeCmd()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString()
	{
		return "SayCmd(" + message + ")";
	}
	
	

}
