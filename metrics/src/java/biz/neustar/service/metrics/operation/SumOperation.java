/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import biz.neustar.service.metrics.ws.model.Metric;

public class SumOperation extends StatisticalOperation {
    private double sum;

    public SumOperation(String valueName) {
    	super("sum", valueName);
    }
    
    @Override
    public Double getResult() {
        return sum;
    }

    @Override
    public void process(Metric metric) {
        Double value = metric.getValues().get(valueName);
        if (value != null) {
            sum += value;
        }
    }
}
