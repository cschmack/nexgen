/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import biz.neustar.service.metrics.ws.model.Metric;

public class AvgOperation extends StatisticalOperation
{
	private int count;
	private double total;

	public AvgOperation(String valueName) {
		super ("avg", valueName);
		count = 0;
		total = 0.0;
	}

	@Override
	public Double getResult() {
		return total / count;
	}

	@Override
	public void process(Metric metric) {
		Double value = metric.getValues().get(valueName);
		if (value != null) {
			total += value;
			count++;
		}
	}
}
