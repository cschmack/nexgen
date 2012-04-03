/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.utils;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

public class ToStringUtilTest {

    @Test
    public void testEmptyMapToString() {
        Map<String, Object> map = Maps.newHashMap();
        String out = ToStringUtil.mapToString(map);
        assertEquals("Map{}", out);
    }

    @Test
    public void testMapToString() {
        Map<String, Object> map = Maps.newHashMap();
        map.put("test1", BigInteger.valueOf(8484823429L));
        map.put("test2", "here");
        String out = ToStringUtil.mapToString(map);
        assertEquals("Map{test1=8484823429, test2=here}", out);
    }
    
    @Test
    public void testNullMapToString() {
        String out = ToStringUtil.mapToString(null);
        assertEquals("null", out);
    }
}
