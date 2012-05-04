/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MetricTest {

    @Test
    public void testToString() {
        Metric m = new Metric();
        String value = m.toString();
        assertNotNull(value);
        assertFalse(value.isEmpty());
    }

    @Test
    public void testEquals() {
        Metric m1 = new Metric();
        Object o = new Object();
        assertFalse(m1.equals(o));
    }
}
