/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.spring;

import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.common.util.concurrent.AbstractExecutionThreadService;

public class SpringDaemon extends AbstractExecutionThreadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringDaemon.class);
    private AnnotationConfigApplicationContext appCtx;
    private final CountDownLatch stopLatch = new CountDownLatch(1);
    
    
    public SpringDaemon() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }
    
    /**
     * Override this method if you want do something
     * special after spring has started.
     * As with run(), you must respond to stop requests.
     */
    protected void afterSpringStarted(
            AnnotationConfigApplicationContext appCtx) throws Exception {
        // does nothing
    }
    
    @Override
    protected final void run() throws Exception {
        appCtx = new AnnotationConfigApplicationContext();
        appCtx.scan("biz.neustar.service");
        appCtx.refresh();
        appCtx.registerShutdownHook();
        
        final SpringDaemon daemon = this;
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                if (daemon.isRunning()) {
                    try {
                        LOGGER.debug("JVM is shutting down, kill the daemon");
                        daemon.stopAndWait();
                    } catch (Exception e) {
                        LOGGER.error("Error on shutdown", e);
                    }
                }
            }
        }));

        afterSpringStarted(appCtx);
        
        waitWhileRunning();
    }
    
    @Override
    protected void shutDown() throws Exception {
        LOGGER.info("stop requested, shutting down.");
        appCtx.close();
        
    }
    
    @Override
    protected void triggerShutdown() {
        stopLatch.countDown();
    }
    
    private void waitWhileRunning() {
        LOGGER.debug("Should be started, and now wait if running: {}", isRunning());
        // wait for closing..
        try {
            LOGGER.info("Service Started");
            stopLatch.await();
            LOGGER.debug("Stop signal received");
        } catch (InterruptedException e) {
            LOGGER.error("stop latch interrupted", e);
        }
    }
}

