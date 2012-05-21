/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.google.common.util.concurrent.Service.State;

public class DaemonTest {
    
    @Test
    public void testStartup() throws InterruptedException {
        Daemon.main(new String[] {});
        Daemon daemon = Daemon.getMainDaemon();
        while (daemon == null) {
            daemon = Daemon.getMainDaemon();
            Thread.yield();
        }
        assertNotNull(daemon);
        daemon.startAndWait();
        
        TimeUnit.SECONDS.sleep(1);
        assertTrue(daemon.isRunning());
        daemon.stopAndWait();
        assertFalse(daemon.isRunning());
    }
    
    @Test
    public void testInterruption() {
        final AtomicReference<Thread> threadRef = new AtomicReference<Thread>();
        Daemon daemon = new Daemon() {
            @Override
            protected void run() throws Exception {
                threadRef.set(Thread.currentThread());
                super.run();
            }
        };
        
        Thread.yield();
        State state = daemon.startAndWait();
        Thread.yield();
        assertTrue(State.RUNNING == state);
        assertTrue(daemon.isRunning());
        try {
            while (threadRef.get() == null) {
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (InterruptedException e) {
            fail();
        }
        assertNotNull(threadRef.get());
        
        threadRef.get().interrupt();
        Thread.yield();
        try {
            while (threadRef.get().isAlive()) {
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (InterruptedException e) {
            fail();
        }
    }
}
