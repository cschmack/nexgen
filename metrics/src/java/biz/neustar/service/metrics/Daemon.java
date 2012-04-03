/*
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import java.util.TimeZone;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.AbstractExecutionThreadService;

/** TBD Fill me in **/

public class Daemon extends AbstractExecutionThreadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Daemon.class);
    private ClassPathXmlApplicationContext appCtx;
    private final CountDownLatch stopLatch = new CountDownLatch(1);
    
    @Override
    protected void run() throws Exception {
        appCtx = new ClassPathXmlApplicationContext("defaults/metrics-context.xml");
        appCtx.registerShutdownHook();
        Server jettyServer = appCtx.getBean(Server.class);
        Preconditions.checkState(jettyServer.isStarted(), "Error in starting jetty");
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
            LOGGER.info("Metrics Service Started");
            stopLatch.await();
            LOGGER.debug("Stop signal received");
        } catch (InterruptedException e) {
            LOGGER.error("stop latch interrupted", e);
        }
    }
    
    public static Daemon createDeamon(String[] args) {
        // just setup some default stuff..
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        
        final Daemon daemon = new Daemon();
        
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
        
        return daemon;
    }
    
    private static volatile Daemon mainDaemon = null;
    static Daemon getMainDaemon() {
        return mainDaemon;
    }
    
    public static void main(String[] args) {
        LOGGER.info("starting metrics service...");
        Daemon daemon = Daemon.createDeamon(args);
        mainDaemon = daemon; // for testing purposes.
        State state = daemon.startAndWait();
        Preconditions.checkState(State.RUNNING == state);
        // park..
    }
}

