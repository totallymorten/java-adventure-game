package adventure.comm;

public class LoginStatus extends CommunicationObj 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9197432429650383920L;
	
	public String status;
	
	public static String STATUS_OK = "OK";
	public static String STATUS_FAILED = "FAILED";
	
	public LoginStatus(String status) 
	{
		this.status = status;
	}

}
