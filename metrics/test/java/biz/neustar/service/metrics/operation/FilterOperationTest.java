/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import org.junit.Test;
import org.mockito.Mockito;

import biz.neustar.service.metrics.ws.model.Metric;

public class FilterOperationTest {

    @Test
    public void testFiltering() {
        FilterOperation op = new FilterOperation() {
            @Override
            public boolean filter(Metric metric) {
                return true;
            }
        };
        @SuppressWarnings("rawtypes")
        Operation nextOp = Mockito.mock(Operation.class);
        op.setNextOperation(nextOp);
        op.apply(new Metric());
        Mockito.verifyZeroInteractions(nextOp);
    }
    

    @Test
    public void testNoFiltering() {
        FilterOperation op = new FilterOperation() {
            @Override
            public boolean filter(Metric metric) {
                return false;
            }
        };
        @SuppressWarnings("rawtypes")
        Operation nextOp = Mockito.mock(Operation.class);
        op.setNextOperation(nextOp);
        op.apply(new Metric());
        Mockito.verify(nextOp, Mockito.only()).apply(Mockito.any(Metric.class));
    }
    
}
