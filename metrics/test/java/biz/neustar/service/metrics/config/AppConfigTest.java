/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.jetty.server.Server;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

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

    
}
