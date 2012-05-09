package biz.neustar.service.metrics.ws.model.validation;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import biz.neustar.service.metrics.ws.model.QueryRequest;

public class QueryRequestValidator implements Validator {
	
	public static final String TS_REGEX = "^-\\d+[smhDM]$";
	public static final String TE_REGEX = "^\\d+[smhDM]$";

	@Override
	public boolean supports(Class<?> clazz) {
		return QueryRequest.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		QueryRequest queryRequest = (QueryRequest)obj;
		// ts is required, but can be empty
		if (queryRequest.getTs() == null) {
			e.rejectValue("ts","ts.null", "ts is required");
		} else if (queryRequest.getTs().trim().length() != 0) {
			// look for the relative pattern first, and then try to parse as timestamp
			if (!Pattern.compile(TS_REGEX).matcher(queryRequest.getTs()).matches()) {
	            try {
	                @SuppressWarnings("unused")
	                Calendar c = DatatypeConverter.parseDateTime(queryRequest.getTs());
	            } catch (IllegalArgumentException iae) {
	            	e.rejectValue("ts", "ts.parsedFailed", "ts parse failure");
	            }
				
			}
		}
		if (queryRequest.getTe() != null) {
			if (!Pattern.compile(TE_REGEX).matcher(queryRequest.getTe()).matches()) {
	            try {
	                @SuppressWarnings("unused")
	                Calendar c = DatatypeConverter.parseDateTime(queryRequest.getTe());
	            } catch (IllegalArgumentException iae) {
	            	e.rejectValue("te", "te.parsedFailed", "te parse failure");
	            }
				
			}
		}
		
		
	}

}
