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

import com.google.common.collect.ImmutableMap;

import biz.neustar.service.metrics.ws.model.Metric;

public class MinOperationTest {

    @Test
    public void testMin() {
        MinOperation min = new MinOperation("foo");
        Metric m1 = new Metric();
        m1.setValues(ImmutableMap.of("foo", 1.0));
        min.apply(m1);
        
        Metric m2 = new Metric();
        m2.setValues(ImmutableMap.of("foo", 2.0));
        min.apply(m2);
        
        Metric m3 = new Metric();
        m3.setValues(ImmutableMap.of("foo2", 2.0));
        min.apply(m3);
        
        
        assertEquals(1.0, min.getResult(), 0.1);
        assertEquals("foo", min.getValueName());
        assertEquals("min", min.getName());
    }
}
