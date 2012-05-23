/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.config;

import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
import org.springframework.jmx.support.MBeanServerFactoryBean;
import org.springframework.validation.Validator;

import biz.neustar.service.common.cxf.JacksonProviders;
import biz.neustar.service.common.cxf.SpringJaxrsServlet;
import biz.neustar.service.common.util.JettyServerUtil;
import biz.neustar.service.metrics.ws.MetricsService;

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
    
    @Value("${app.staticPath}")
    private String staticPath;
    
    public int getServerPort() {
        return serverPort;
    }
    
    @Bean
    public Validator validator() {
    	return new org.springframework.validation.beanvalidation.LocalValidatorFactoryBean();
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
    
    @Bean
    public String staticPath() {
        StringBuilder path = new StringBuilder();
        String appHome = System.getenv("APP_HOME");
        if (appHome == null) {
            path.append('.');
        } else {
            path.append(appHome);
        }
        if (!staticPath.startsWith("/")) {
            path.append("/");
        }
        path.append(staticPath);
        return path.toString().replace("//", "/");
    }
    
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server getServer() {
        LOGGER.debug("initializing server");
        Server server = JettyServerUtil.createServer(
                new InetSocketAddress(serverPort), 
                serverMaxThreadPool);
                
        // servlet context
        ServletContextHandler context = 
                JettyServerUtil.createServletHandler(
                        springJaxrsServlet(), MetricsService.class);
        
        // set of handlers
        HandlerList handlers = new HandlerList();
        handlers.addHandler(
                JettyServerUtil.createStaticHandler(staticPath()));
        handlers.addHandler(context);
        
        server.setHandler(handlers);
        
        return server;
    }
    
    
    @Bean
    public SpringJaxrsServlet springJaxrsServlet() {
        SpringJaxrsServlet servlet = new SpringJaxrsServlet();
        // add the jackson json providers
        JacksonProviders.registerJsonProvider(servlet);
        return servlet;
    }
}
