/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.util;

import static org.junit.Assert.*;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.junit.Test;
import org.mockito.Mockito;

public class JettyServerUtilTest {

    
    @Test
    public void testServerLocation() {
        Server server = new Server();
        Connector conn = new SelectChannelConnector();
        conn.setPort(9910);
        conn.setHost("127.0.0.1");
        server.addConnector(conn);
        String loc = JettyServerUtil.getURL(server);
        assertEquals("http://127.0.0.1:9910", loc);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testServerNoLocation() {
        Server server = new Server();
        String loc = JettyServerUtil.getURL(server);
        assertEquals("http://127.0.0.1:9910", loc);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testServerNoLocationEmptyArray() {
        Server server = Mockito.mock(Server.class);
        Mockito.when(server.getConnectors()).thenReturn(new Connector[0]);
        String loc = JettyServerUtil.getURL(server);
        assertEquals("http://127.0.0.1:9910", loc);
    }
}
