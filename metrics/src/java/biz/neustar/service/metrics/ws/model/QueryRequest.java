/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.List;

public class QueryRequest {
    
    private List<String> contexts;
    private String ts;
    private String te;
    private List<String> metrics;
    private List<String> stats;
    private boolean raw = false;
    
    
    
    public List<String> getContexts() {
        return contexts;
    }
    public void setContexts(List<String> contexts) {
        this.contexts = contexts;
    }
    public String getTs() {
        return ts;
    }
    public void setTs(String ts) {
        this.ts = ts;
    }
    public String getTe() {
        return te;
    }
    public void setTe(String te) {
        this.te = te;
    }
    public List<String> getMetrics() {
        return metrics;
    }
    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }
    public List<String> getStats() {
        return stats;
    }
    public void setStats(List<String> stats) {
        this.stats = stats;
    }
    public boolean isRaw() {
        return raw;
    }
    public void setRaw(boolean raw) {
        this.raw = raw;
    }
    
    
}
