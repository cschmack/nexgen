/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.common.spring;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

@Component
public class ValidationUtil {
    private static final Logger LOGGER = 
            LoggerFactory.getLogger(ValidationUtil.class);

    public <T> void validate(T item, Validator validator) {
        DataBinder binder = new DataBinder(item, item.getClass().getName());
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

    public <T> void validate(Iterable<T> iterable, 
            Validator validator) {
        // TODO this will stop at first error, should we collect all errors before fail?
        for (T item : iterable) {
            validate(item, validator);
        }
    }
}
