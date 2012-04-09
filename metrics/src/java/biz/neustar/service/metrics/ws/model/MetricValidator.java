package biz.neustar.service.metrics.ws.model;

import java.util.Calendar;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MetricValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Metric.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		Metric metric = (Metric)obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "service", "service.empty");
		
		if ((metric.getTimestamp() == null) || (metric.getTimestamp().trim().length() == 0)) {
			e.rejectValue("timestamp", "timestamp.blank", "timestamp required");
		} else {
			try {
				@SuppressWarnings("unused")
				Calendar c = javax.xml.bind.DatatypeConverter.parseDateTime(metric.getTimestamp());
			} catch (IllegalArgumentException iae) {
				e.rejectValue("timestamp", "timestamp.parsedFailed", "timestamp parse failure");
			}
		}

	}

}