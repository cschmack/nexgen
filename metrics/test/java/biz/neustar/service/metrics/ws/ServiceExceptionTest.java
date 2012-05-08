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
import javax.ws.rs.core.Response.Status;


import org.junit.Test;

import biz.neustar.service.common.cxf.MoreStatus;

public class ServiceExceptionTest {

    @Test
    public void testServiceExceptionJson() {
        ServiceError err = ServiceError.TOO_MANY_POINTS;
        assertTrue(err.toString().length() > 0);
        ServiceException ex = 
                new ServiceException(MoreStatus.TOO_MANY_REQUESTS, err);
        
        assertEquals(err.getCode(), ex.getErrorCode());
        assertEquals(err.getText(), ex.getErrorText());
        
        Response resp = ex.getResponse();
        assertNotNull(resp);
        assertEquals(429, resp.getStatus());
        assertEquals("{\"errorText\":\"Too many metric points\",\"errorCode\":\"101\"}",
                resp.getEntity());
    }
    
    @Test
    public void testServiceExceptionCtors() {
        ServiceException srvcEx1 = new ServiceException(Status.BAD_REQUEST, ServiceError.TOO_LARGE);
        assertNotNull(srvcEx1);
        
        ServiceException srvcEx2 = new ServiceException(
                new Throwable(), 
                Status.BAD_REQUEST, ServiceError.TOO_LARGE);
        assertNotNull(srvcEx2);
        
        ServiceException srvcEx3 = new ServiceException(
                new Throwable(), MoreStatus.NETWORK_AUTH_REQUIRED,
                ServiceError.TOO_LARGE);
        assertNotNull(srvcEx3);
    }
}
