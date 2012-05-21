/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryResponse {
	private long rawDataCount;
	private List<Metric> rawData;
	private Map<String,Map<String,Double>> statistics;

	public QueryResponse( )
	{
		rawDataCount = 0;
		statistics = new HashMap<String, Map<String, Double>>( );
		rawData = null;
	}
	
	public long getRawDataCount() {
		//return this.rawDataCount;
		return this.rawData.size( );
	}
	
	public void setRawDataCount(long rawDataCount) {
		this.rawDataCount = rawDataCount;
	}

	public Map<String, Map<String, Double>> getStatistics( )
	{
		return statistics;
	}

	public void setStatistics( Map<String, Map<String, Double>> statistics )
	{
		this.statistics = statistics;
	}
	
	public void addStatistics( String metricName, String opName, Double value )
	{
		if( statistics.containsKey(  metricName  ) )
		{
			Map<String,Double> map = statistics.get( metricName );
			map.put(  opName, value );
		}
		else
		{
			Map<String,Double> statMap = new HashMap<String,Double>( );
			statMap.put( opName, value );
			statistics.put(  metricName, statMap );
		}
	}

	public List<Metric> getRawData( )
	{
		return rawData;
	}

	public void setRawData( List<Metric> rawData )
	{
		this.rawData = rawData;
		this.rawDataCount = rawData.size( );
	}
}
