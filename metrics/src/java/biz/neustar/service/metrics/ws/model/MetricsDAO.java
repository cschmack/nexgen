/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import biz.neustar.service.metrics.operation.Operation;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

@Component
public class MetricsDAO {
    private ConcurrentSkipListMap<String, String> metricsMap = 
            new ConcurrentSkipListMap<String, String>();
    
    private static Splitter KEY_SPLITTER = Splitter.on(":").trimResults();
    
    public void put(Metric metric) {
        for (Entry<String, Double> entry : metric.getValues().entrySet()) {
            // timestamp:metric
            String key = String.format("%s:%s.%s",  
                    metric.getTimestampMillis(),
                    metric.getFrom(), entry.getKey());
            
            ObjectMapper mapper = new ObjectMapper();
            try {
                String value = mapper.writeValueAsString(metric);
                metricsMap.put(key, value);
///// TODO:
            } catch (JsonGenerationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JsonMappingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    // for testing..
    protected Map<String, String> getMetrics() {
        return metricsMap;
    }
    
    public void apply(Operation op, long start, long end,
            Pattern metricMatch) {
        apply(op, start, end, metricMatch, new HashMap<String, Pattern>());
    }
    
    public void apply(Operation op, long start, long end,
            Pattern metricMatch,
            Map<String, Pattern> contextMatches) {
        
        ObjectMapper mapper = new ObjectMapper();
        // lazy version..
        // TODO: do this as a filter pattern
        for (Entry<String, String> entry : metricsMap.entrySet()) {
            long timestamp = getTimestamp(entry.getKey());
            String metricName = getMetricName(entry.getKey());
            if (timestamp >= start && timestamp <= end &&
                    isPatternMatch(metricName, metricMatch)) {

                try {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> metricValue = 
                        mapper.readValue(entry.getValue(), Map.class);
                    if (isContextMatch(metricValue, contextMatches)) {
                        op.process(mapper.readValue(
                                entry.getValue(), Metric.class));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                
            }
        }
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
