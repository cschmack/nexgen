/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;


public class ServiceException extends WebApplicationException {
    private static final long serialVersionUID = 73245012056851723L;

    private ServiceError serviceError;
    
    public ServiceException(int status, ServiceError serviceError) {
        super(status);
        this.serviceError = serviceError;
    }

    public ServiceException(Status status, ServiceError serviceError) {
        super(status);
        this.serviceError = serviceError;
    }

    public ServiceException(Throwable cause, int status, 
            ServiceError serviceError) {
        
        super(cause, status);
        this.serviceError = serviceError;
    }

    public ServiceException(Throwable cause, Status status, 
            ServiceError serviceError) {
        
        super(cause, status);
        this.serviceError = serviceError;
    }

    
    public int getErrorCode() {
        return serviceError.getCode();
    }
    
    public String getErrorText() {
        return serviceError.getText();
    }
    
    
    @JsonIgnore
    @Override
    public Response getResponse() {
        Response.ResponseBuilder respBuilder = 
                Response.fromResponse(super.getResponse());
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> values = Maps.newHashMap();
            values.put("errorCode", Integer.toString(serviceError.getCode()));
            values.put("errorText", serviceError.getText());
            respBuilder.entity(mapper.writeValueAsString(values));
            // respBuilder.entity(mapper.writeValueAsString(this)); // maybe in the "debug" mode?
        } catch (Exception e) {
            // error mapping serviceError, leave it as is..
        }
        
        return respBuilder.build();
    }
}
