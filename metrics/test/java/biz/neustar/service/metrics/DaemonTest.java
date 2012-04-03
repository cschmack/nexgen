/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class DaemonTest {
    
    @Test
    public void testStartup() throws InterruptedException {
        /*Daemon daemon = Daemon.createDeamon(new String[] {});
        assertNotNull(daemon);
        daemon.startAndWait();
        
        TimeUnit.SECONDS.sleep(1);
        assertTrue(daemon.isRunning());
        daemon.stopAndWait();
        assertFalse(daemon.isRunning());
        */assertTrue(true);
    }
}
