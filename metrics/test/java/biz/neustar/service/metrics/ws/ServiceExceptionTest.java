/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import static org.junit.Assert.*;

import javax.ws.rs.core.Response;


import org.junit.Test;

public class ServiceExceptionTest {

    @Test
    public void testServiceExceptionJson() {
        ServiceError err = ServiceError.TOO_MANY_POINTS;
        assertTrue(err.toString().length() > 0);
        ServiceException ex = new ServiceException(509, err);
        
        assertEquals(err.getCode(), ex.getErrorCode());
        assertEquals(err.getText(), ex.getErrorText());
        
        Response resp = ex.getResponse();
        assertNotNull(resp);
        assertEquals(509, resp.getStatus());
        assertEquals("{\"errorText\":\"Too many metric points\",\"errorCode\":\"101\"}",
                resp.getEntity());
    }
}
