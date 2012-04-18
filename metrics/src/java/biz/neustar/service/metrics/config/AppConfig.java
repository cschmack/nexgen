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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.validation.Validator;

import biz.neustar.service.common.cxf.ServletHolderFactory;
import biz.neustar.service.common.cxf.SpringJaxrsServlet;
import biz.neustar.service.common.spring.PropertySourcesUtil;
import biz.neustar.service.metrics.ws.MetricsService;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JsonMappingExceptionMapper;
import com.fasterxml.jackson.jaxrs.json.JsonParseExceptionMapper;
import com.google.common.collect.Maps;

//This 'should' work according to the docs, however it only works if the configEnv properties file is 1st
//@PropertySource({"classpath:defaults/app.properties","classpath:${configEnv:defaults}/app.properties"})
/////

@Configuration("metricsConfig")
@PropertySource({"classpath:defaults/app.properties"}) // by convention this is the default
@ComponentScan("biz.neustar.service")
public class AppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    
    @Value("${app.serverPort}")
    private int serverPort;
    
    @Value("${app.serverMaxThreadPool}")
    private int serverMaxThreadPool;
    
    
    public int getServerPort() {
        return serverPort;
    }
    
    @Bean
    public Validator validator() {
    	return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
    }
    
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer pspc(StandardEnvironment env) {
        PropertySourcesPlaceholderConfigurer propertyConfigurer = new PropertySourcesPlaceholderConfigurer();
        propertyConfigurer.setPropertySources(PropertySourcesUtil.reorderPropertySources(env));
        return propertyConfigurer;
    }
    
    
    /* maybe?
    @Bean
    public PlatformTransactionManager transactionManager() {
        PlatformTransactionManager transMgr = new xxxx
        return transMgr;
    }
    */
    
    @Bean
    public MBeanServerFactoryBean mbeanServer() {
        MBeanServerFactoryBean mbeanFactory = new MBeanServerFactoryBean();
        mbeanFactory.setLocateExistingServerIfPossible(true);
        return mbeanFactory;
    }
    
    @Bean
    public MBeanExporter mbeanExporter() {
        return new AnnotationMBeanExporter();
    }
    
    @Bean
    public AnnotationAwareAspectJAutoProxyCreator aspectJAutoProxy() {
        return new AnnotationAwareAspectJAutoProxyCreator();
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
        // add the jackson json providers
        servlet.addProvider(new JacksonJsonProvider());
        servlet.addProvider(new JsonMappingExceptionMapper());
        servlet.addProvider(new JsonParseExceptionMapper());
        return servlet;
    }
}
