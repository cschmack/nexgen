/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;


public class MaxOperation extends StatisticalOperation {
	private double max;

	public MaxOperation(String valueName) {
		super ("max", valueName);
		max = Double.MIN_VALUE;
	}

	@Override
	public Double getResult() {
		return this.max;
	}

	@Override
	public void process(Double metricValue) {
		if( metricValue > this.max )
			this.max = metricValue;
	}
}
