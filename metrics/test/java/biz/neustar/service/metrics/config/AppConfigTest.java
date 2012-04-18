/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.config;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jetty.server.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AppConfig.class, loader=AnnotationConfigContextLoader.class)
@DirtiesContext
public class AppConfigTest {

    @Autowired
    ApplicationContext appCtx;
    
    @Test
    public void startTest() {
        Server jettyServer = appCtx.getBean(Server.class);
        assertNotNull(jettyServer);
        
        AppConfig appConfig = appCtx.getBean(AppConfig.class);
        assertEquals(9090, appConfig.getServerPort());
    }

    private static final String CONFIG_ENV_OVERRIDE = "mockProperties";
    protected StandardEnvironment getTestStandardEnv() {
        return new StandardEnvironment() {
            protected void customizePropertySources(MutablePropertySources propertySources) {
                super.customizePropertySources(propertySources);
                    //ResourcePropertySource scndSrc = new ResourcePropertySource(CONFIG_ENV_OVERRIDE, 
                    //        new ByteArrayResource("configEnv".getBytes(), CONFIG_ENV_OVERRIDE));
                MockPropertySource mockSrc = new MockPropertySource().withProperty("configEnv", "");
                propertySources.addFirst(mockSrc);                
            }
        };
    }
    
    @Test
    public void testConsistency() {
        StandardEnvironment env = getTestStandardEnv();
        
        final int startSize = env.getPropertySources().size();
        
        Iterator<PropertySource<?>> iter1 = env.getPropertySources().iterator();
        assertEquals(CONFIG_ENV_OVERRIDE, 
                iter1.next().getName());
        
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter1.next().getName());
        
        PropertySources srcs = AppConfig.reorderPropertySources(env);
        Iterator<PropertySource<?>> iter = srcs.iterator();
        assertTrue(iter.hasNext());
        
        assertEquals(CONFIG_ENV_OVERRIDE, iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
        
        assertEquals(startSize, Iterators.size(srcs.iterator()));
    }
    
    @Test
    public void testReordering() throws IOException {
        StandardEnvironment env = getTestStandardEnv();
        final int initialSize = env.getPropertySources().size();
        
        ResourcePropertySource firstSrc = new ResourcePropertySource("first", 
                new ByteArrayResource("first".getBytes() ,"first"));
        ResourcePropertySource scndSrc = new ResourcePropertySource("second", 
                new ByteArrayResource("second".getBytes(), "second"));
        
        env.getPropertySources().addFirst(firstSrc);
        env.getPropertySources().addLast(scndSrc);
        
        final int startSize = env.getPropertySources().size();
        assertEquals(initialSize + 2, startSize);
        
        PropertySources srcs = AppConfig.reorderPropertySources(env);
        Iterator<PropertySource<?>> iter = srcs.iterator();
        
        assertEquals(startSize, Iterators.size(srcs.iterator()));
        assertTrue(iter.hasNext());

        assertEquals("first", iter.next().getName());
        assertEquals("second", iter.next().getName());
        assertEquals(CONFIG_ENV_OVERRIDE, iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
    }
    
    @Test
    public void testReordering1First() throws IOException {
        StandardEnvironment env = getTestStandardEnv();
        final int initialSize = env.getPropertySources().size();
        
        ResourcePropertySource firstSrc = new ResourcePropertySource("first", 
                new ByteArrayResource("first".getBytes() ,"first"));
        
        env.getPropertySources().addFirst(firstSrc);
        
        final int startSize = env.getPropertySources().size();
        assertEquals(initialSize + 1, startSize);
        
        PropertySources srcs = AppConfig.reorderPropertySources(env);
        Iterator<PropertySource<?>> iter = srcs.iterator();
        
        assertEquals(startSize, Iterators.size(srcs.iterator()));
        assertTrue(iter.hasNext());

        assertEquals("first", iter.next().getName());
        assertEquals(CONFIG_ENV_OVERRIDE, iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
    }
    
    @Test
    public void testReordering1Last() throws IOException {
        StandardEnvironment env = getTestStandardEnv();
        final int initialSize = env.getPropertySources().size();
        
        ResourcePropertySource firstSrc = new ResourcePropertySource("first", 
                new ByteArrayResource("first".getBytes() ,"first"));
        
        env.getPropertySources().addLast(firstSrc);
        
        final int startSize = env.getPropertySources().size();
        assertEquals(initialSize + 1, startSize);
        
        PropertySources srcs = AppConfig.reorderPropertySources(env);
        Iterator<PropertySource<?>> iter = srcs.iterator();
        
        assertEquals(startSize, Iterators.size(srcs.iterator()));
        assertTrue(iter.hasNext());

        assertEquals("first", iter.next().getName());
        assertEquals(CONFIG_ENV_OVERRIDE, iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
    }
    
    @Test
    public void testReorderingAndConfigEnv() throws IOException {
        StandardEnvironment env = new StandardEnvironment();
        
        ResourcePropertySource firstSrc = new ResourcePropertySource("first", 
                new ByteArrayResource("configEnv=defaults".getBytes() ,"first"));
        ResourcePropertySource scndSrc = new ResourcePropertySource("second", 
                new ByteArrayResource("second".getBytes(), "second"));
        
        env.getPropertySources().addFirst(firstSrc);
        env.getPropertySources().addLast(scndSrc);
        
        final int startSize = env.getPropertySources().size();
        assertEquals(4, startSize);
        
        PropertySources srcs = AppConfig.reorderPropertySources(env);
        Iterator<PropertySource<?>> iter = srcs.iterator();
        
        // +1 for the new one
        assertEquals(startSize + 1, Iterators.size(srcs.iterator()));
        assertTrue(iter.hasNext());

        assertEquals("class path resource [defaults/app.properties]", iter.next().getName());
        assertEquals("first", iter.next().getName());
        assertEquals("second", iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_PROPERTIES_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
        assertEquals(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, 
                iter.next().getName());
    }
    
}
