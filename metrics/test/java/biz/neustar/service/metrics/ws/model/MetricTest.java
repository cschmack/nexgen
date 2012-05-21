/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;

import com.google.common.collect.Maps;

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
    
    @Test
    public void tesNottEquals() {
        Metric m1 = new Metric();
        Metric m2 = new Metric();
        m1.setFrom("from1");
        m2.setFrom("from2");
        assertFalse(m1.equals(m2));
    }
    
    @Test
    public void testEqualsFilledOut() {
        Metric m1 = new Metric();
        m1.setFrom("from");
        m1.setHost("host");
        m1.setInstance("instance");
        m1.setProcess("proc");
        m1.setResource("resource");
        m1.setSource("source");
        m1.setTimestamp("time");
        m1.setValues(new HashMap<String,Double>());
        
        
        Metric m2 = new Metric();
        m2.setFrom("from");
        m2.setHost("host");
        m2.setInstance("instance");
        m2.setProcess("proc");
        m2.setResource("resource");
        m2.setSource("source");
        m2.setTimestamp("time");
        m2.setValues(new HashMap<String,Double>());
        
        assertEquals(m1, m2);
    }
}
