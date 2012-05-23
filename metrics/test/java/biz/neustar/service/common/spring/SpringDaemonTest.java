/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.util.concurrent.Service.State;

public class SpringDaemonTest {
    
    @Test
    public void testStartup() throws InterruptedException {
        SpringDaemon daemon = new SpringDaemon();
        daemon.startAndWait();
        Thread.yield();

        TimeUnit.SECONDS.sleep(1);
        
        assertTrue(daemon.isRunning());
        daemon.stopAndWait();
        assertFalse(daemon.isRunning());
    }
    
    @Test
    public void testStopDuringStartedCallback() {
        final AtomicReference<Thread> threadRef = new AtomicReference<Thread>();
        final SpringDaemon daemon = new SpringDaemon() {
            @Override
            protected void afterSpringStarted(
                    AnnotationConfigApplicationContext appCtx) throws Exception {
                threadRef.set(Thread.currentThread());
                this.triggerShutdown();
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
        try {
            while (threadRef.get().isAlive()) {
                TimeUnit.MILLISECONDS.sleep(50);
            }
        } catch (InterruptedException e) {
            fail();
        }
    }
    
    @Test
    public void testInterruption() {
        final AtomicReference<Thread> threadRef = new AtomicReference<Thread>();
        SpringDaemon daemon = new SpringDaemon() {
            @Override
            protected void afterSpringStarted(
                    AnnotationConfigApplicationContext appCtx) throws Exception {
                threadRef.set(Thread.currentThread());
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
