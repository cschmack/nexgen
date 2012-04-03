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
import javax.validation.Validator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.GzipFilter;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import biz.neustar.service.metrics.cxf.ServletHolderFactory;
import biz.neustar.service.metrics.cxf.SpringJaxrsServlet;
import biz.neustar.service.metrics.ws.MetricsService;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.common.collect.Maps;


@Configuration("metricsConfig")
@ImportResource("classpath:defaults/properties-config.xml")
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    
    @Value("${app.serverPort}")
    private int serverPort;
    
    @Value("${app.serverMaxThreadPool}")
    private int serverMaxThreadPool;
    
    
    @Bean
    public Validator validator() {
        return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server getServer() {
        LOGGER.debug("initializing server");
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
        SpringJaxrsServlet servlet = new SpringJaxrsServlet();
        servlet.addProvider(new JacksonJsonProvider());
        return servlet;
    }
}
