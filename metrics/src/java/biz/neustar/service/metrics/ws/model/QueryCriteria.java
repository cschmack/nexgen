/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.Map.Entry;

import biz.neustar.service.metrics.operation.Operation;

import com.google.common.collect.Maps;

public class QueryCriteria
{
	private long ts;
	private long te;

	// TODO: The reserved metric tags should be changed to an enum or something
	// so that they can be
	// accessed programmatically.

	// These are the reserved Metric
	private String source; // required
	private String host; // required
	private String from; // optional
	private String process; // optional
	private String instance; // optional
	private String resource; // optional
	private Map<String, String> other; // These are the custom context key/value pairs
	
	// Define the Patterns
	private Pattern sourcePattern; // required
	private Pattern hostPattern; // required
	private Pattern fromPattern; // optional
	private Pattern processPattern; // optional
	private Pattern instancePattern; // optional
	private Pattern resourcePattern; // optional
	private Map<String, Pattern> otherPatterns; // These are the custom context key/value pairs
	
	private List<Operation<?>> operations;
	
	public QueryCriteria( )
	{
		ts = 0;
		te = 0;
		source = null;
		host = null;
		from = null;
		process = null;
		instance = null;
		resource = null;
		other = Maps.newHashMap( );
		otherPatterns = Maps.newHashMap( );
		operations = new ArrayList<Operation<?>>( );
	}
	
	public QueryCriteria( long start, long end )
	{
		ts = start;
		te = end;
		source = null;
		host = null;
		from = null;
		process = null;
		instance = null;
		resource = null;
		other = Maps.newHashMap( );
		otherPatterns = Maps.newHashMap( );
		operations = new ArrayList<Operation<?>>( );
	}

	public void setTs( long ts )
	{
		this.ts = ts;
	}
	
	public long getTs( )
	{
		return this.ts;
	}

	public void setTe( long te )
	{
		this.te = te;
	}
	
	public long getTe( )
	{
		return this.te;
	}

	public void setSource( String source )
	{
		this.source = source;
		this.sourcePattern = Pattern.compile( source );
	}
	
	public String getSource( )
	{
		return this.source;
	}

	public void setHost( String host )
	{
		this.host = host;
		this.hostPattern = Pattern.compile( host );
	}
	
	public String getHost( )
	{
		return this.host;
	}

	public void setFrom( String from )
	{
		this.from = from;
		this.fromPattern = Pattern.compile( from );
	}
	
	public String getFrom( )
	{
		return this.from;
	}

	public void setProcess( String process )
	{
		this.process = process;
		this.processPattern = Pattern.compile( process );
	}
	
	public String getProcess( )
	{
		return this.process;
	}

	public void setInstance( String instance )
	{
		this.instance = instance;
		this.instancePattern = Pattern.compile( instance );
	}
	
	public String getInstance( )
	{
		return this.instance;
	}

	public void setResource( String resource )
	{
		this.resource = resource;
		this.resourcePattern = Pattern.compile( resource  );
	}
	
	public String getResource( )
	{
		return this.resource;
	}
	
	public void addOther( String contextKey, String contextValue )
	{
		this.other.put(  contextKey, contextValue );
		this.otherPatterns.put(  contextKey, Pattern.compile( contextValue ) );
	}
	
	public void addOperation( Operation<?> op )
	{
		this.operations.add( op );
	}
	
	public List<Operation<?>> getOperations( )
	{
		return this.operations;
	}
	
	public Map<String, String> getOther( )
	{
		return this.other;
	}

    protected boolean isPatternMatch(String value, 
            Pattern match) {

        return match.matcher(value).matches();
    }

	public boolean matches( Metric metric )
	{
		boolean isMatch = true;

		if( source != null )
			isMatch = source.equals( metric.getSource( ) ) || 
				isPatternMatch( metric.getSource( ), sourcePattern );
		
		if( isMatch && host != null )
			isMatch = host.equals( metric.getHost( ) ) || 
				isPatternMatch( metric.getHost( ), hostPattern );
		
		if( isMatch && from != null )
			isMatch = from.equals( metric.getFrom( ) ) ||
				isPatternMatch( metric.getFrom( ), fromPattern );
		
		if( isMatch && process != null )
			isMatch = process.equals( metric.getProcess( ) ) || 
				isPatternMatch( metric.getProcess( ), processPattern );
		
		if( isMatch && instance != null )
			isMatch = instance.equals( metric.getInstance( ) ) ||
				isPatternMatch( metric.getInstance( ), instancePattern );
		
		
		if( isMatch && resource != null )
			isMatch = resource.equals( metric.getResource( ) ) || 
				isPatternMatch( metric.getResource( ), resourcePattern );
		
		if( isMatch && other != null && other.size( ) > 0 )
		{
			for( Entry<String, String> entry : other.entrySet() ) 
			{
				String ctxKey = entry.getKey( );
				if( !other.containsKey( ctxKey ) )
				{
					isMatch = false;
					break;
				}
				else
				{
					String ctxValue = entry.getValue( ).toString( );
					String metricCtxValue = metric.any( ).get( ctxKey ).toString( );
					isMatch = metricCtxValue.equals(  ctxValue ) ||
							isPatternMatch( metricCtxValue, otherPatterns.get( ctxKey ) );
					
					if( !isMatch )
						break;
				}
			}
		}
		
		return isMatch;
	}
}
