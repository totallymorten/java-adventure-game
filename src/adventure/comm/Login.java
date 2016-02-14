package adventure.comm;

public class Login extends CommandObj
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4801755886435087984L;
	
	public String username;
	
	public Login(String username)
	{
		this.username = username;
	}

	@Override
	public void executeCmd() 
	{
	}

	@Override
	public String toString() 
	{
		return "Login[username='"+username+"']";
	}
	
	

}
