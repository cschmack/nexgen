/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model.validation;

import java.util.Calendar;

import javax.xml.bind.DatatypeConverter;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TimeValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return String.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        String timestamp = (String) obj;
        
        if ((timestamp == null) || (timestamp.trim().length() == 0)) {
            errors.rejectValue("timestamp", "timestamp.blank", "timestamp required");
        } else {
            try {
                @SuppressWarnings("unused")
                Calendar c = DatatypeConverter.parseDateTime(timestamp);
            } catch (IllegalArgumentException iae) {
                errors.rejectValue("timestamp", "timestamp.parsedFailed", "timestamp parse failure");
            }
        }
    }

}
