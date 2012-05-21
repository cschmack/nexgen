/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import com.google.common.base.Function;

import biz.neustar.service.metrics.ws.model.Metric;

public abstract class Operation<T /* result type */> 
        implements Function<Metric, Void> {

	protected String name;
    
    public abstract T getResult();
    
    protected abstract void process(Metric metric);
    
    // implementation for Function
    public Void apply(Metric metric) {
        process(metric);
        return null;
    }

	public String getName( )
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}
}
