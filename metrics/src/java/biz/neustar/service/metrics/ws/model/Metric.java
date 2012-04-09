/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.Map;

import biz.neustar.service.metrics.utils.ToStringUtil;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;

public class Metric {
	// ISO 8601 e.g. "2012-04-04T12:33:59.173"
    private String timestamp;

    @JsonIgnore
    private Map<String, Object> other = Maps.newHashMap();
    
    private String service;
    
    private String customer; 
    private String resource;
    
    private Map<String, String> values = Maps.newHashMap();

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
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
            .add("service", service)
            .add("customer", customer)
            .add("resource", resource)
            .add("values", ToStringUtil.mapToString(values))
            .toString();
    }
}
