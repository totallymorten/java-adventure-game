package adventure.comm;

import adventure.types.RequestDataType;

public class RequestData extends CommandObj
{
	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = -8377463820307305056L;
	
	public RequestDataType dataType;
	public String requestInfo;
	
	public RequestData (RequestDataType datatype)
	{
		this.dataType = datatype;
	}
	
	@Override
	public void executeCmd()
	{
	}

	@Override
	public String toString()
	{
		return "RequestData["+dataType+"]";
	}
	
	

}
