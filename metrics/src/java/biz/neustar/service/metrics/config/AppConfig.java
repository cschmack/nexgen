/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.config;

import java.net.InetSocketAddress;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import biz.neustar.service.metrics.cxf.ServletHolderFactory;
import biz.neustar.service.metrics.cxf.SpringJaxrsServlet;
import biz.neustar.service.metrics.ws.MetricsService;


@Configuration("metricsConfig")
@ImportResource("classpath:defaults/properties-config.xml")
public class AppConfig {

    @Value("${app.serverPort}")
    private int serverPort;
    
    @Value("${app.serverMaxThreadPool}")
    private int serverMaxThreadPool;
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server getServer() {
        
        Server server = new Server(new InetSocketAddress(serverPort));
        server.setSendServerVersion(false);
        server.setThreadPool(new QueuedThreadPool(serverMaxThreadPool));
        
        ServletContextHandler context =
                new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(servletHolder(), "/*"); // hand everything off to the CXF and let it map the paths
        context.addFilter(gzipFilter(), "/*", EnumSet.of(DispatcherType.ASYNC, DispatcherType.REQUEST));
        server.setHandler(context);
        server.setStopAtShutdown(true);
        return server;
    }
    
    @Bean
    public FilterHolder gzipFilter() {
        FilterHolder filter = new FilterHolder(GzipFilter.class);
        filter.setName("gzipFilter");
        Map<String, String> params = Maps.newHashMap();
        params.put("minGzipSize", "0");
        filter.setInitParameters(params);
        return filter;
    }
    
    @Bean
    public ServletHolder servletHolder() {
        ServletHolderFactory servletHolder = new ServletHolderFactory();
        return servletHolder.getServletHolder(springJaxrsServlet(),
                MetricsService.class);
    }
    
    @Bean
    public SpringJaxrsServlet springJaxrsServlet() {
        return new SpringJaxrsServlet();
    }
}
