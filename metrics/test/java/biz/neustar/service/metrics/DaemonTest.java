/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import static org.junit.Assert.*;

import org.junit.Test;


public class DaemonTest {

    @Test
    public void testCreate() {
        Daemon daemon = new Daemon();
        assertNotNull(daemon);
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testStartup() {
        Daemon.main(new String[] {});
    }
}
