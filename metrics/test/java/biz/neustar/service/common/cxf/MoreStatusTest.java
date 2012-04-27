/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.cxf;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Test;

public class MoreStatusTest {

    @Test
    public void testMoreStatus() {
        assertTrue(MoreStatus.values().length > 1);
        
        MoreStatus status1 = MoreStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;
        assertEquals(Response.Status.Family.CLIENT_ERROR, status1.getFamily());
        assertEquals(431, status1.getStatusCode());
        assertTrue(status1.getReasonPhrase().length() > 0);
        
        MoreStatus status2 = MoreStatus.valueOf("NETWORK_AUTH_REQUIRED");
        assertEquals(Response.Status.Family.SERVER_ERROR, status2.getFamily());
        assertEquals(511, status2.getStatusCode());
        assertTrue(status2.getReasonPhrase().length() > 0);
    }

    
}
