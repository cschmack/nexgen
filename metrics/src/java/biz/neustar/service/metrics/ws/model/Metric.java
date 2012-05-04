/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.Calendar;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import biz.neustar.service.common.util.ToStringUtil;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Metric {

    private String timestamp;   // required
    private String source;      // required
    private String host;        // required
    // optionals
    private String from;        // optional
    private String process;     // optional
    private String instance;    // optional
    
    private String resource;    // optional
    private Map<String, Double> values = Maps.newHashMap();

    @JsonIgnore
    private Map<String, Object> other = Maps.newHashMap();
    
    public String getTimestamp() {
        return timestamp;
    }
    
    @JsonIgnore
    public long getTimestampMillis() {
        Calendar c = DatatypeConverter.parseDateTime(timestamp);
        return c.getTimeInMillis();
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }    
    
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map<String, Double> getValues() {
        return values;
    }

    public void setValues(Map<String, Double> values) {
        this.values = values;
    }
    
    @JsonAnyGetter
    public Map<String,Object> any() {
    	return other;
    }
    
    @JsonAnySetter
    public void set(String name, Object value) {
    	other.put(name, value);
    }
   
    public String toString() {
        return Objects.toStringHelper(this)
            .add("timestamp", timestamp)
            .add("source", source)
            .add("from", from)
            .add("host", host)
            .add("process", process)
            .add("instance", instance)
            .add("resource", resource)
            .add("values", ToStringUtil.mapToString(values))
            .toString();
    }
    
    public boolean equals(Object m) {
        if (m instanceof Metric) {
            Metric mIn = (Metric) m;
            if (Objects.equal(mIn.from, from) &&
                    Objects.equal(mIn.host, host) &&
                    Objects.equal(mIn.instance, instance) &&
                    Objects.equal(mIn.process, process) &&
                    Objects.equal(mIn.resource, resource) &&
                    Objects.equal(mIn.timestamp, timestamp) &&
                    Objects.equal(mIn.values, values) &&
                    Objects.equal(mIn.other, other)) {
                return true;
            }
        }
        return false;
    }
}
