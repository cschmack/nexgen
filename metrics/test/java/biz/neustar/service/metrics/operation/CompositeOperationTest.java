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

public class CompositeOperationTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void testCompositeOperations() {
        CompositeOperation c = new CompositeOperation();
        Operation op1 = Mockito.mock(Operation.class);
        Operation op2 = Mockito.mock(Operation.class);
        c.add(op1);
        c.add(op2);
        c.apply(new Metric());
        Mockito.verify(op1, Mockito.only()).apply(Mockito.any(Metric.class));
        Mockito.verify(op2, Mockito.only()).apply(Mockito.any(Metric.class));
    }
}
