/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;


public class AvgOperation extends StatisticalOperation {
	private int count;
	private double total;

	public AvgOperation(String valueName) {
		super ("avg", valueName);
	}

	@Override
	public Double getResult() {
		return total / count;
	}

	@Override
	public void process(Double metricValue) {
		total += metricValue;
		count++;
	}
}
