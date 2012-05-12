/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import biz.neustar.service.metrics.ws.model.Metric;


public abstract class StatisticalOperation extends Operation<Double> {
	private final String valueName;
	
	public StatisticalOperation(String name, String valueName) {
		this.name = name;
		this.valueName = valueName;
	}
	
	protected abstract void process(Double metricValue);
	
	protected final void process(Metric metric) {
	    Double value = metric.getValues().get(valueName);
        if (value != null) {
            process(value);
        }
	}
	

	public String getValueName() {
		return valueName;
	}
}