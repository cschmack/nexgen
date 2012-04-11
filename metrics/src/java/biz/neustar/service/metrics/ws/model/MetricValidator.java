package biz.neustar.service.metrics.ws.model;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.DatatypeConverter;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MetricValidator implements Validator {

	private ContextProvider contextProvider;
	
	public MetricValidator(ContextProvider contextProvider) {
		this.contextProvider = contextProvider;
	}
	

	@Override
	public boolean supports(Class<?> clazz) {
		return Metric.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors e) {
		Metric metric = (Metric)obj;

		ValidationUtils.rejectIfEmptyOrWhitespace(e, "from", "from.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "host", "host.empty");
		
		if ((metric.getTimestamp() == null) || (metric.getTimestamp().trim().length() == 0)) {
			e.rejectValue("timestamp", "timestamp.blank", "timestamp required");
		} else {
			try {
				@SuppressWarnings("unused")
				Calendar c = DatatypeConverter.parseDateTime(metric.getTimestamp());
			} catch (IllegalArgumentException iae) {
				e.rejectValue("timestamp", "timestamp.parsedFailed", "timestamp parse failure");
			}
		}
		
		Map<String, Object> any = metric.any();
		// check for extra 
		if ((any != null) && (!any.isEmpty())) {
			if (contextProvider == null) {
				e.reject("context.unknown", "no Context Provider");
			} else {
				Set<String> contexts = contextProvider.getContexts(metric.getFrom());
				
				for (String s: any.keySet()) {
					if (!contexts.contains(s)) {
						// since not an actual field, just record an error with specific message
						e.reject("context.unknown", "unknown context: " + s);
					}
				}
			}
		}
	}
}
