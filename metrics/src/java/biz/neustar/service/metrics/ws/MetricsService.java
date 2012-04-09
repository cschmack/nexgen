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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.utils.ValidationUtil;
import biz.neustar.service.metrics.ws.model.ContextConfig;
import biz.neustar.service.metrics.ws.model.ContextConfigValidator;
import biz.neustar.service.metrics.ws.model.Metric;
import biz.neustar.service.metrics.ws.model.MetricValidator;

@Component
@Path("/metrics/v1/")
public class MetricsService {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(MetricsService.class);
    
    @Autowired
    private ValidationUtil validationUtil;
    
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
        
        validationUtil.validate(metrics, 
                MetricValidator.getDefaultInstance());
        for (Metric metric : metrics) {
            LOGGER.debug("Metric Received: {}", metric);
        }
    }
    
    @POST
    @Path("/config")
    @Consumes({MediaType.APPLICATION_JSON})
    public void createConfig(ContextConfig contextConfig) {
        LOGGER.debug("Received: {}", contextConfig);
        validationUtil.validate(contextConfig, 
                ContextConfigValidator.getDefaultInstance());
        LOGGER.debug("Validated {}", contextConfig);

    }
    
}
