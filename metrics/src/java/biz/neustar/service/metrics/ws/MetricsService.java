/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.Metric;

@Component
@Path("/metrics/v1/")
public class MetricsService {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(MetricsService.class);
    
    @Autowired
    private Validator validator; // see: file:///Users/jdamick/Documents/spring/htmlsingle/spring-framework-reference.html#validation-beanvalidation-overview

    @GET
    @Path("/hello")
    @Produces({MediaType.TEXT_PLAIN})
    public String hello() {
        return "hello world!";
    }
    
    @POST
    @Path("/metrics")
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(List<Metric> metrics) {
        LOGGER.debug("Received: {}", metrics);
        
        validateIteratable(metrics);
        for (Metric metric : metrics) {
            LOGGER.debug("Metric Received: {}", metric);
        }
    }
    
    @POST
    @Path("/config")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createConfig(ContextConfig contextConfig) {
        LOGGER.debug("Received: {}", contextConfig);
        validateItem(contextConfig);
        LOGGER.debug("Validated {}", contextConfig);

    }
    
    protected <T> void validateItem(T item) {
        Set<ConstraintViolation<T>> violations = validator.validate(item);
        if (!violations.isEmpty()) {
            // log the violations
            for (ConstraintViolation<T> violation : violations) {
               LOGGER.info("constraint violation: {}", violation); 
            }
            // at the end or here..
            throw new WebApplicationException(Status.BAD_REQUEST);
        }        
    }
    
    protected <T> void validateIteratable(Iterable<T> iterable) {
        for (T item : iterable) {
            Set<ConstraintViolation<T>> violations = validator.validate(item);
            if (!violations.isEmpty()) {
                // log the violations
                for (ConstraintViolation<T> violation : violations) {
                   LOGGER.info("constraint violation: {}", violation); 
                }
                // at the end or here..
                throw new WebApplicationException(Status.BAD_REQUEST);
            }
        }
    }
    
}
