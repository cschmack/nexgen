package biz.neustar.service.metrics.ws.model;

public class ContextKVPair
{
	private String contextKey;
	private String contextValue;

	public ContextKVPair( String s )
	{
		int idx = s.indexOf( "{" );
		if( idx > 0 )
		{
			// TODO: Add more validation here.
			contextKey = s.substring( 0, idx );
			contextValue = s.substring( idx + 1, s.length( ) - 1 );
		}
	}

	public String getContextKey( )
	{
		return contextKey;
	}

	public String getContextValue( )
	{
		return contextValue;
	}
}
