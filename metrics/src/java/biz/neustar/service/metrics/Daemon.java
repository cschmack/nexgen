/*
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import biz.neustar.service.common.spring.SpringDaemon;

import com.google.common.base.Preconditions;

/** TBD Fill me in **/

public class Daemon extends SpringDaemon {
    private static final Logger LOGGER = LoggerFactory.getLogger(Daemon.class);
    
    @Override
    protected void afterSpringStarted(
            AnnotationConfigApplicationContext appCtx) throws Exception {
        Server jettyServer = appCtx.getBean(Server.class);
        Preconditions.checkState(jettyServer.isStarted(), "Error in starting jetty");
    }
    
    
    private static volatile Daemon mainDaemon = null;
    static Daemon getMainDaemon() {
        return mainDaemon;
    }
    
    public static void main(String[] args) {
        LOGGER.info("starting metrics service...");
        Daemon daemon = new Daemon();
        mainDaemon = daemon; // for testing purposes.
        State state = daemon.startAndWait();
        Preconditions.checkState(State.RUNNING == state);
        // park..
    }
}

