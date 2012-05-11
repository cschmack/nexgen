/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SimpleTimeZone;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import biz.neustar.service.metrics.operation.Operation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Component
public class MetricsDAO {
	
	// TODO: Figure out a long term solution for this.  For sprint 2, we're just going to brute force a 
	//		 memory solution for the data store.
    private ConcurrentSkipListMap<String, List<Metric>> metricsMap = 
            new ConcurrentSkipListMap<String, List<Metric>>();
    
    private static Splitter KEY_SPLITTER = Splitter.on(":").trimResults();
    
    public void put(Metric metric) {

    	String timestamp = metric.getTimestamp( );
    	
    	List<Metric> metrics = metricsMap.get( timestamp );
    	if( metrics != null )
    	{
    		metrics.add( metric );
    	}
    	else
    	{
    		metrics = new ArrayList<Metric>( );
    		metrics.add( metric );
    		metricsMap.put( timestamp, metrics );
    	}
    }
    
    // for testing..
    protected Map<String, List<Metric>> getMetrics() {
        return metricsMap;
    }
    
    /*
    protected List<Metric> getMetrics( long start, long end, QueryCriteria criteria )
    {
    	List<Metric> metrics = new ArrayList<Metric>( );
    	
        for (Entry<String, List<Metric>> entry : metricsMap.entrySet()) 
        {
	    	long timestamp = Long.parseLong( entry.getKey( ) );
	    	
	    	if( timestamp >= start && timestamp <= end )
	    	{
	    		// The timestamp is in the specified range.  Check to see if to see if the context matches.
	    		
	    	}
        }
        
        return metrics;
    }
    */
    
    /*
    public void apply(Operation<?> op, long start, long end,
            Pattern metricMatch) {
        apply(op, start, end, metricMatch, new HashMap<String, Pattern>());
    }
    */
    
    //public void apply(Operation<?> op, QueryCriteria criteria ) {
    public void apply( QueryCriteria criteria ) {

    	SimpleTimeZone gmtTZ = new SimpleTimeZone(0, "GMT" );
    	Calendar gmtCal = new GregorianCalendar( gmtTZ );
    	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
    	
        for (Entry<String, List<Metric>> entry : metricsMap.entrySet()) 
        {
	    	//long timestamp = Long.parseLong( entry.getKey( ) );
        	try
        	{
	        	gmtCal.setTime( sdf.parse( entry.getKey( ) ) );
	        	long timestamp = gmtCal.getTimeInMillis( ); 
		    	
		    	if( timestamp >= criteria.getTs( ) && timestamp <= criteria.getTe( ) )
		    	{
		    		// The timestamp is in the specified range.  Check to see if there are
		    		// any metrics for this timestamp that match the specified criteria.
		    		for( Metric metric: entry.getValue( ) )
		    		{
		    			if( criteria.matches( metric ) )
		    			{
		    				for( Operation<?> op : criteria.getOperations( ) )
		    					op.apply( metric );
		    			}
		    		}
		    	}
        	}
        	catch( Exception e )
        	{
        		// TODO: Figure out what to do with an invalid timestamp in the metric.  
        		// This should have been validated already.
        	}
        }
        
        /*
        ObjectMapper mapper = new ObjectMapper();
        // lazy version..
        // TODO: do this as a filter pattern
        for (Entry<String, List<Metric>> entry : metricsMap.entrySet()) 
        {

            long timestamp = getTimestamp(entry.getKey());
            String metricName = getMetricName(entry.getKey());
            if (timestamp >= start && timestamp <= end &&
                    isPatternMatch(metricName, metricMatch)) {

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> metricValue = 
                        mapper.readValue(entry.getValue(), Map.class);
                    if (isContextMatch(metricValue, contextMatches)) {
                        op.apply(mapper.readValue(
                                entry.getValue(), Metric.class));
                    }
                	
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        */
    }
    
    protected boolean isContextMatch(Map<String, Object> metricJson, 
            Map<String, Pattern> contextMatches) {
        
        boolean result = true; // assume true unless there is a non-matching filter
        
        // we don't match against values here.
        // yes this is inefficient, but it's POC
        for (Entry<String, Pattern> contextMatch : contextMatches.entrySet()) {
            Object value = null; 
            String key = contextMatch.getKey();
            boolean found = false;
            if (metricJson.containsKey(key)) {
                found = true;
                value = metricJson.get(key);
            } else { // check the user supplied contexts
                @SuppressWarnings("unchecked")
                Map<String, Object> other = (Map<String, Object>) metricJson.get("any");
                if (other != null && other.containsKey(key)) {
                    found = true;
                    value = other.get(key);
                }
            }
            
            if (found) {
                // check for match
                String strValue = "";
                if (value != null) {
                    strValue = value.toString();
                }
            
                if (!isPatternMatch(strValue, contextMatch.getValue())) {
                    result = false;
                    break; // we're done, bail
                }
            } 
        }
        
        return result;
    }
    
    protected boolean isPatternMatch(String value, 
            Pattern match) {

        return match.matcher(value).matches();
    }
    
    protected long getTimestamp(String key) {
        Iterable<String> iter = KEY_SPLITTER.split(key);
        return Long.parseLong(Iterables.get(iter, 0, "0"));
    }
    
    protected String getMetricName(String key) {
        Iterable<String> iter = KEY_SPLITTER.split(key);
        return Iterables.get(iter, 1, "");
    }
}
