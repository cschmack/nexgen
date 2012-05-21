/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class ContextConfigTest {

    @Test
    public void testToString() {
        ContextConfig config = new ContextConfig();
        config.setDescription("desc");
        assertTrue(config.toString().length() > 0);
    }

}
