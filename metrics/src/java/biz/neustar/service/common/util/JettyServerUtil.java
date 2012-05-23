/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.util;

import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import biz.neustar.service.common.cxf.ServletHolderFactory;
import biz.neustar.service.common.cxf.SpringJaxrsServlet;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class JettyServerUtil {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(JettyServerUtil.class);
    
    private JettyServerUtil() {
        // utility class
    }
    
    /**
     * Get the URL for the given Server for the first connector
     * over http.
     * @return an URL of the form:  http://127.0.0.1:9090
     */
    public static String getURL(Server server) {
        Preconditions.checkArgument(server.getConnectors() != null);
        Preconditions.checkArgument(server.getConnectors().length > 0);
        return "http://" + server.getConnectors()[0].getName();
    }
    
    public static ResourceHandler createStaticHandler(String staticAssetPath) {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(staticAssetPath);
        LOGGER.debug("resource base: {} ", resourceHandler.getResourceBase());
        ContextHandler ctxHandler = new ContextHandler();
        ctxHandler.setContextPath("/");
        ctxHandler.setHandler(resourceHandler);
        return resourceHandler;
    }
    
    
    public static ServletContextHandler createServletHandler(
            SpringJaxrsServlet jaxrsServlet, Class<?>... serviceClasses) {
        ServletHolderFactory factory = new ServletHolderFactory();
        return createServletHandler(
                factory.getServletHolder(jaxrsServlet, serviceClasses));
    }
    
    
    public static ServletContextHandler createServletHandler(
            ServletHolder servletHolder) {
        
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(servletHolder, "/*"); // hand everything off to the CXF and let it map the paths
        context.addFilter(gzipFilter(), "/*", 
                EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
        
        return context;
    }
    
    
    public static Server createServer(
            InetSocketAddress serverAddress, int maxThreadPoolSize) {
        Server server = new Server(serverAddress);
        // set defaults for all server instances
        server.setSendServerVersion(false);
        server.setThreadPool(new QueuedThreadPool(maxThreadPoolSize));
        server.setStopAtShutdown(true);
        return server;
    }
    
    
    protected static FilterHolder gzipFilter() {
        FilterHolder filter = new FilterHolder(GzipFilter.class);
        filter.setName("gzipFilter");
        Map<String, String> params = Maps.newHashMap();
        params.put("minGzipSize", "0");
        filter.setInitParameters(params);
        return filter;
    }
}
