/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws;

import java.util.List;

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
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.ContextConfigValidator;
import biz.neustar.service.metrics.ws.model.Metric;
import biz.neustar.service.metrics.ws.model.MetricValidator;

@Component
@Path("/metrics/v1/")
public class MetricsService {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(MetricsService.class);
    
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
        
        validateIteratable(metrics, new MetricValidator(), "metric");
        for (Metric metric : metrics) {
            LOGGER.debug("Metric Received: {}", metric);
        }
    }
    
    @POST
    @Path("/config")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createConfig(ContextConfig contextConfig) {
        LOGGER.debug("Received: {}", contextConfig);
        validateItem(contextConfig, new ContextConfigValidator(), "ContextConfig");
        LOGGER.debug("Validated {}", contextConfig);

    }
    
//    protected <T> void validateItem(T item) {
//        Set<ConstraintViolation<T>> violations = validator.validate(item);
//        if (!violations.isEmpty()) {
//            // log the violations
//            for (ConstraintViolation<T> violation : violations) {
//               LOGGER.info("constraint violation: {}", violation); 
//            }
//            // at the end or here..
//            throw new WebApplicationException(Status.BAD_REQUEST);
//        }        
//    }
//    
//    protected <T> void validateIteratable(Iterable<T> iterable) {
//        for (T item : iterable) {
//            Set<ConstraintViolation<T>> violations = validator.validate(item);
//            if (!violations.isEmpty()) {
//                // log the violations
//                for (ConstraintViolation<T> violation : violations) {
//                   LOGGER.info("constraint violation: {}", violation); 
//                }
//                // at the end or here..
//                throw new WebApplicationException(Status.BAD_REQUEST);
//            }
//        }
//    }
//    
    protected <T> void validateItem(T item, Validator validator, String name) {
//    	Errors result = new BeanPropertyBindingResult(item, name);
//    	ValidationUtils.invokeValidator(validator, item, result);
    	DataBinder binder = new DataBinder(item, name);
    	binder.setValidator(validator);
    	binder.validate();
    	Errors result = binder.getBindingResult();
    	
        if (result.hasErrors()) {
        	for (ObjectError oe : result.getAllErrors()) {
        		LOGGER.info("constraint violation {}", oe.toString());
        	}
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }
    
    protected <T> void validateIteratable(Iterable<T> iterable, Validator validator, String name) {
    	// TODO this will stop at first error, should we collect all errors before fail?
        for (T item : iterable) {
        	validateItem(item, validator, name);
        }
    }
    
}
