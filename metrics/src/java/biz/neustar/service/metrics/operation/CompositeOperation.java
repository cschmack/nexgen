/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import java.util.List;

import com.google.common.collect.Lists;

import biz.neustar.service.metrics.ws.model.Metric;


public class CompositeOperation extends Operation<List<Operation<?>>> {
    private final List<Operation<?>> operations = Lists.newArrayList();
    
    public void add(Operation<?> operation) {
        operations.add(operation);
    }

    @Override
    public List<Operation<?>> getResult() {
        return operations;
    }

    @Override
    public void process(Metric metric) {
        for (Operation<?> op : operations) {
            op.apply(metric);
        }
    }
}
