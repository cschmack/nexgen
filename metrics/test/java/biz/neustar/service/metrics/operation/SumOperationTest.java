/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.operation;

import static org.junit.Assert.*;

import org.junit.Test;

import biz.neustar.service.metrics.ws.model.Metric;

public class SumOperationTest {

    @Test
    public void testSum() {
        SumOperation op = new SumOperation("foo");
        Metric metric = new Metric();
        metric.getValues().put("foo", 1.0);
        op.process(metric);
        op.process(metric);
        op.process(metric);
        assertEquals((double) 3.0, op.getResult(), 0.1);
    }

    @Test
    public void testWrongValueSum() {
        SumOperation op = new SumOperation("foo");
        Metric metric = new Metric();
        metric.getValues().put("bar", 1.0);
        op.process(metric);
        op.process(metric);
        op.process(metric);
        assertEquals((double) 0.0, op.getResult(), 0.1);
    }

    @Test
    public void testEmptySum() {
        SumOperation op = new SumOperation("foo");
        assertEquals((double) 0.0, op.getResult(), 0.1);
    }
}
