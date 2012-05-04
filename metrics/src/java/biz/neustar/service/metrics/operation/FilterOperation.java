/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import biz.neustar.service.metrics.ws.model.Metric;


/**
 * 
 */
public abstract class FilterOperation extends Operation<Void> {
    private Operation<?> nextOperation = null;

    public FilterOperation() {
    }
    
    public FilterOperation(Operation<?> next) {
        nextOperation = next;
    }
    
    @Override
    public Void getResult() {
        return null;
    }
    
    /**
     * This method provides a mechanism to chain operations and filter
     * the metrics that are applied to that chain
     * @param metric the metric to filter or not
     * @return true if the metric should be filtered the next operation should not be applied
     *         otherwise false should be returned
     */
    public abstract boolean filter(Metric metric);
    
    public void process(Metric metric) {
        if (!filter(metric) && nextOperation != null)
            nextOperation.apply(metric);
    }

    public Operation<?> getNextOperation() {
        return nextOperation;
    }
    
    public void setNextOperation(Operation<?> nextOperation) {
        this.nextOperation = nextOperation;
    }
}
