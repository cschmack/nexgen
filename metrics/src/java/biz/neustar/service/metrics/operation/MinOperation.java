/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;


public class MinOperation extends StatisticalOperation {
	private double min;

	public MinOperation(String valueName) {
		super ("min", valueName);
		min = Double.MAX_VALUE;
	}

	@Override
	public Double getResult() {
		return this.min;
	}

	@Override
	public void process(Double metricValue) {
		if( metricValue < this.min )
			this.min = metricValue;
	}
}
