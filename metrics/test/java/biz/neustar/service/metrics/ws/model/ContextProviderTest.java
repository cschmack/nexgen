/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class ContextProviderTest {

    @Test
    public void testContextProvider() {
        ContextProvider provider = new ContextProvider();
        ContextConfig contextConfig = new ContextConfig();
        contextConfig.setName("something");
        contextConfig.setContexts(Sets.newHashSet("foo"));
        provider.setContexts(contextConfig);
        
        Set<String> ctx = provider.getContexts("something");
        assertNotNull(ctx);
        assertEquals(1, ctx.size());
        assertEquals("foo", ctx.iterator().next());
        
        Set<String> ctx2 = provider.getContexts("nothing");
        assertNotNull(ctx2);
        assertEquals(0, ctx2.size());       
    }

}
